package top.zephyrs.xflow.entity.flow.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class FlowInfo {

    private Flow flow;

    private List<FlowNodeCurrent> currents;

    private List<FlowTask> tasks;

}
