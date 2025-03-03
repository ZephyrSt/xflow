package top.zephyrs.xflow.configs;

import lombok.Getter;
import top.zephyrs.xflow.enums.ScheduleTypeEnum;
import top.zephyrs.xflow.enums.RejectStrategyEnum;

@Getter
public class XFlowConfig {

    /**
     * Id生成器机器码
     */
    private final Integer  workId;

    /**
     * 默认的拒绝策略
     */
    private final RejectStrategyEnum defaultRejectStrategy;

    /**
     * 任务处理方式
     */
    private final ScheduleTypeEnum flowType;


    public XFlowConfig(Integer workId, ScheduleTypeEnum flowType, RejectStrategyEnum defaultRejectStrategy) {
        this.workId = workId;
        this.flowType = flowType;
        this.defaultRejectStrategy = defaultRejectStrategy;
    }




}
