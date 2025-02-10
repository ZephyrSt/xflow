package top.zephyrs.xflow.entity.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class FlowNodeCurrentInfo {

    /**
     * 当前节点信息
     */
    private FlowNodeCurrent current;

    /**
     * 待办节点信息
     */
    private List<FlowTask> tasks;
}
