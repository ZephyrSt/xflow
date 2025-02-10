package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.zephyrs.xflow.data.ConfigPublishDAO;
import top.zephyrs.xflow.entity.config.ConfigPublish;

import java.util.List;

@Mapper
public interface ConfigPublishMapper extends ConfigPublishDAO {


    @Insert("insert into x_flow_config_publish(publish_id, version, is_active, config, config_id, create_time) " +
            "values (publish_id, version, is_active, config, config_id, create_time)")
    @Override
    int insert(ConfigPublish configPublish);

    /**
     * 查询当前已发布的全部流程
     * @return 全部已发布流程
     */
    @Select("select * from x_flow_config_publish t where is_active = 1")
    @Override
    List<ConfigPublish> getActivePublish();

    @Update("update x_flow_config_publish set is_active=false where config_id = #{configId}")
    @Override
    int disActiveByConfigId(Long configId);

    @Select("select max(version) from x_flow_config_publish t where config_id = #{configId}")
    @Override
    Integer selectMaxVersionByConfigId(Long configId);

    /**
     * 查询当前已发布的流程
     * @param configId 流程定义ID
     * @return 已发布流程
     */
    @Select("select t.* from x_flow_config_publish t inner join x_flow_config f on t.config_id = f.config_id " +
            "where t.is_active = 1 and f.config_id=#{configId}")
    @Override
    ConfigPublish getActivePublishByConfigId(Long configId);

    /**
     * 查询当前已发布的流程
     * @param configCode 流程定义编码
     * @return 已发布流程
     */
    @Select("select t.* from x_flow_config_publish t inner join x_flow_config f on t.config_id = f.config_id " +
            "where t.is_active = 1 and f.config_code=#{configCode}")
    @Override
    ConfigPublish getActivePublishByConfigCode(String configCode);

    /**
     * 查询流程使用的配置信息
     * @param flowId
     * @return
     */
    @Select("select t.* from x_flow_config_publish t inner join x_flow f on f.publish_id = t.publish_id " +
            "where f.flow_id=#{flowId}")
    @Override
    ConfigPublish getByFlowId(Long flowId);

    @Select("select t.* from x_flow_config_publish t " +
            "inner join x_flow f on f.publish_id = t.publish_id " +
            "inner join x_flow_node_current c on c.flow_id = f.flow_id " +
            "where c.current_id=#{currentId}")
    @Override
    ConfigPublish getByCurrentId(Long currentId);

    @Select("select t.* from x_flow_config_publish t " +
            "inner join x_flow f on f.publish_id = t.publish_id " +
            "inner join x_flow_task c on c.flow_id = f.flow_id " +
            "where c.task_id=#{taskId}")
    @Override
    ConfigPublish getByTaskId(Long taskId);
}
