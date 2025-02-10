package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.config.ConfigPublish;

import java.util.List;

public interface ConfigPublishDAO {

    int insert(ConfigPublish configPublish);

    /**
     * 查询当前已发布的全部流程
     * @return 全部已发布流程
     */
    List<ConfigPublish> getActivePublish();

    int disActiveByConfigId(Long configId);

    Integer selectMaxVersionByConfigId(Long configId);

    /**
     * 查询当前已发布的流程
     * @param configId 流程定义ID
     * @return 已发布流程
     */
    ConfigPublish getActivePublishByConfigId(Long configId);

    /**
     * 查询当前已发布的流程
     * @param configCode 流程定义编码
     * @return 已发布流程
     */
    ConfigPublish getActivePublishByConfigCode(String configCode);

    /**
     * 查询流程使用的配置信息
     * @param flowId
     * @return
     */
    ConfigPublish getByFlowId(Long flowId);

    ConfigPublish getByCurrentId(Long currentId);

    ConfigPublish getByTaskId(Long taskId);
}
