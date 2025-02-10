package top.zephyrs.xflow.service;

import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import top.zephyrs.xflow.data.ConfigDAO;
import top.zephyrs.xflow.data.ConfigPublishDAO;
import top.zephyrs.xflow.data.keys.XFlowKeySnowflake;
import top.zephyrs.xflow.entity.Query;
import top.zephyrs.xflow.entity.config.*;
import top.zephyrs.xflow.enums.EdgeTypeEnum;
import top.zephyrs.xflow.exceptions.ConditionExecuteFailedException;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigService {

    private final ConfigDAO configMapper;

    private final ConfigPublishDAO publishMapper;
    private final XFlowKeySnowflake snowflake;

    public ConfigService(ConfigDAO configMapper,
                         ConfigPublishDAO publishMapper, XFlowKeySnowflake snowflake) {
        this.configMapper = configMapper;
        this.publishMapper = publishMapper;
        this.snowflake = snowflake;
    }

    public Config getConfigInfoById(Long configId) {
        return configMapper.selectById(configId);
    }

    public List<ConfigVO> getByQuery(Query query) {
        return configMapper.selectVOByQuery(query);
    }

    /**
     * 查询流程当前发布的配置信息
     *
     * @param configCode 流程定义编码
     * @return 当前生效的流程定义
     */
    public ConfigPublish getPublishByConfigCode(String configCode) {
        return publishMapper.getActivePublishByConfigCode(configCode);
    }

    public ConfigPublish getPublishByTaskId(Long taskId) {
        return publishMapper.getByTaskId(taskId);
    }

    /**
     * 查询符合条件的后续节点
     *
     * @param publish 发布的流程信息
     * @param nodeId  当前节点ID
     * @param data    附加数据，用于表达式判定。如果后续节点无表达式判定，可传空
     * @return 后续节点集合
     */
    public List<ConfigNode> getNextNodes(ConfigPublish publish, String nodeId, EdgeTypeEnum type, Map<String, Object> data) {
        Config config = publish.getConfig();
        //筛选出符合条件的后续节点
        Set<String> nextTargets = publish.getConfig().getEdges().stream()
                .filter(edge -> edge.getSource().equals(nodeId) && edge.getType() == type)
                .map(ConfigEdge::getTarget)
                .collect(Collectors.toSet());
        List<ConfigNode> allNextNodes = config.getNodes().stream().filter(node -> nextTargets.contains(node.getId())).collect(Collectors.toList());
        List<ConfigNode> nextNodes = new ArrayList<>();
        for (ConfigNode nextLink : allNextNodes) {
            String condition = nextLink.getData().getConditional();
            //如果不存在条件判断，则认为符合条件
            if (StringUtils.isEmpty(condition)) {
                nextNodes.add(nextLink);
            }
            // 如果存在条件判断，则进行表达式判定
            else {
                try {
                    if ((Boolean) AviatorEvaluator.execute(condition, data)) {
                        nextNodes.add(nextLink);
                    }
                } catch (RuntimeException e) {
                    throw new ConditionExecuteFailedException("failed to execute condition expression :" + condition + ", data: " + JSONUtils.toJson(data));
                }
            }
        }
        return nextNodes;
    }

    /**
     * 保存流程配置信息
     *
     * @param config 流程配置信息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveConfig(Config config) {
        //校验编码是否存在
        Config exists = configMapper.selectByCode(config.getConfigCode());
        if (exists != null) {
            if (config.getConfigId() == null || !exists.getConfigId().equals(config.getConfigId())) {
                throw new FlowConfigurationIncorrectException("flow config code already exists!");
            }
        }
        if (config.getConfigId() == null) {
            config.setConfigId(snowflake.nextId());
            configMapper.insert(config);
        } else {
            exists = configMapper.selectById(config.getConfigId());
            if (exists == null) {
                config.setConfigId(snowflake.nextId());
                configMapper.insert(config);
            } else {
                configMapper.updateById(config);
            }
        }
    }

    /**
     * 发布流程配置信息
     *
     * @param configId 流程配置ID
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void publish(Long configId) {
        Config info = configMapper.selectById(configId);
        publishMapper.disActiveByConfigId(configId);
        Integer lastVersion = publishMapper.selectMaxVersionByConfigId(configId);
        Integer version = lastVersion == null ? 1 : lastVersion + 1;
        ConfigPublish publish = new ConfigPublish();
        publish.setConfigId(configId);
        publish.setVersion(String.valueOf(version));
        publish.setConfig(info);
        publish.setActive(true);
        publish.setPublishId(snowflake.nextId());
        publishMapper.insert(publish);
    }


}
