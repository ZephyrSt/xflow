package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.config.ConfigPublish;

import java.util.List;

public interface ConfigPublishDAO {

    int insert(ConfigPublish configPublish);

    int disActiveByConfigId(Long configId);

    Integer selectMaxVersionByConfigId(Long configId);

    /**
     * 查询当前已发布的流程
     * @param configCode 流程定义编码
     * @return 已发布流程
     */
    ConfigPublish getActivePublishByConfigCode(String configCode);

    List<ConfigPublish> getByConfigId(Long configId);

    /**
     * 查询流程使用的配置信息
     * @param flowId
     * @return
     */
    ConfigPublish getByFlowId(Long flowId);

    ConfigPublish getByCurrentId(Long currentId);

    ConfigPublish getByTaskId(Long taskId);
}
