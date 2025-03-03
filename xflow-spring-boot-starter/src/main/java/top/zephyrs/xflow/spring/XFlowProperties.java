package top.zephyrs.xflow.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.zephyrs.xflow.enums.ScheduleTypeEnum;
import top.zephyrs.xflow.enums.RejectStrategyEnum;

@ConfigurationProperties(prefix = "xflow")
@Data
public class XFlowProperties {

    /**
     * ID生成策略-机器码（雪花算法）
     */
    private Integer workerId = 0;

    /**
     * 默认的退回策略
     */
    private RejectStrategyEnum defaultRejectStrategy = RejectStrategyEnum.start;

    /**
     * 任务处理方式, 异步方式暂未实现
     */
    @Deprecated
    private ScheduleTypeEnum flowType = ScheduleTypeEnum.SYNC;

}
