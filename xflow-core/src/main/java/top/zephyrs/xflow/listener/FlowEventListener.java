package top.zephyrs.xflow.listener;

import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;

import java.util.List;

/**
 * 流程事件监听
 */
public interface FlowEventListener {

    /**
     * 任务创建
     * @param tasks
     */
    void taskCreated(List<FlowTask> tasks);

    /**
     * 任务认领
     * @param task
     */
    void taskClaimed(FlowTask task);

    /**
     * 任务完成
     * @param taskLog
     */
    void taskFinished(FlowTaskLog taskLog);

    /**
     * 节点创建
     * @param current
     */
    void nodeCreated(FlowNodeCurrent current);

    /**
     * 节点办结
     * @param currentLog
     */
    void nodeFinished(FlowNodeCurrentLog currentLog);

    /**
     * 流程创建
     * @param flow
     */
    void flowCreated(Flow flow);

    /**
     * 流程办结
     * @param flow
     */
    void flowFinished(Flow flow);
}
