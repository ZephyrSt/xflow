package top.zephyrs.xflow.service;

import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;

import java.util.List;

public interface FlowEventService {

    void taskCreated(List<FlowTask> tasks);

    void taskClaimed(FlowTask task);
    void taskFinished(FlowTaskLog taskLog);

    void nodeCreated(FlowNodeCurrent current);

    void nodeFinished(FlowNodeCurrentLog currentLog);

    void flowCreated(Flow flow);

    void flowFinished(Flow flow);
}
