package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

/**
 * 会签节点
 */
public class ApproverNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {

    protected ApproverNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        super(configService, flowDataService, wrapper);
    }

}
