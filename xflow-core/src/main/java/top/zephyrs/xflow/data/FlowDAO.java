package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.flow.Flow;

public interface FlowDAO {

    int insert(Flow flow);

    int updateById(Flow flow);

    Flow selectById(Long flowId);
    Flow selectByConfigIdAndBizId(Long configId, String bizId);

    Flow selectByConfigCodeAndBizId(String configCode, String bizId);
}

