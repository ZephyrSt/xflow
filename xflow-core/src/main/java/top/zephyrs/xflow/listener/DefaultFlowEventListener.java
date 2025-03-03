package top.zephyrs.xflow.listener;

import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;
import top.zephyrs.xflow.listener.FlowEventListener;

import java.util.List;

/**
 * 默认的流程状态变更的空实现
 */
public class DefaultFlowEventListener implements FlowEventListener {

    @Override
    public void taskCreated(List<FlowTask> tasks) {

    }

    @Override
    public void taskClaimed(FlowTask task) {

    }

    @Override
    public void taskFinished(FlowTaskLog taskLog) {

    }

    @Override
    public void nodeCreated(FlowNodeCurrent current) {

    }

    @Override
    public void nodeFinished(FlowNodeCurrentLog currentLog) {

    }

    @Override
    public void flowCreated(Flow flow) {

    }

    @Override
    public void flowFinished(Flow flow) {

    }
}
