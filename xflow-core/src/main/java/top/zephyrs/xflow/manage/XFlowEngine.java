package top.zephyrs.xflow.manage;

import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.dto.FlowInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.exceptions.InvalidUserException;
import top.zephyrs.xflow.exceptions.TaskNotClaimedException;
import top.zephyrs.xflow.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XFlowEngine {

    private final ConfigService configService;

    private final FlowDataService flowDataService;

    private final FlowActionService actionService;

    public XFlowEngine(ConfigService configService, FlowDataService flowDataService, FlowActionService actionService) {
        this.configService = configService;
        this.flowDataService = flowDataService;
        this.actionService = actionService;
    }

    /**
     * 开始一个流程
     *
     */
    public Result<FlowInfo> start(String flowCode, String bizId, User operator, Map<String, Object> data, List<User> candidates) {
        ConfigPublish publish = configService.getPublishByConfigCode(flowCode);
        Flow flow = actionService.start(publish, bizId, operator, candidates, data);
        FlowInfo flowInfo = flowDataService.getFlowCurrent(flow.getFlowId());
        return Result.success(flowInfo);
    }

    public Result<FlowInfo> approval(Long taskId, User operator, String remark, Map<String, Object> data, List<User> candidates) {
        ConfigPublish publish = configService.getPublishByTaskId(taskId);
        try{
            FlowTaskLog taskLog = actionService.approval(publish, taskId, operator, candidates, remark, data);
            FlowInfo flowInfo = flowDataService.getFlowCurrent(taskLog.getFlowId());
            return Result.success(flowInfo);
        }catch (InvalidUserException e) {
            return Result.paramError("无权限审核："+ operator.getUserName());
        }catch (TaskNotClaimedException e) {
            return Result.paramError("无权限审核：公共任务未领取");
        }

    }

    public Result<FlowInfo> reject(Long taskId, User operator, String remark, Map<String, Object> data, List<User> candidates) {
        ConfigPublish publish = configService.getPublishByTaskId(taskId);
        try{
            FlowTaskLog taskLog = actionService.reject(publish, taskId, operator, candidates, remark, data);
            FlowInfo flowInfo = flowDataService.getFlowCurrent(taskLog.getFlowId());
            return Result.success(flowInfo);
        }catch (InvalidUserException e) {
            return Result.paramError("无权限审核："+ operator.getUserName());
        }
    }

    /**
     * 领取公共任务
     */
    public Result<FlowInfo> claim(Long taskId, User operator) {
        FlowTask task = actionService.claim(taskId, operator);
        FlowInfo flowInfo = flowDataService.getFlowCurrent(task.getFlowId());
        return Result.success(flowInfo);
    }

    /**
     * 委派.
     * 将任务分给其它审批人处理，此时委派人不能审核，等新的审批人处理后，该任务还是会回到委派人手上,委派人审核通过后进入后续
     */
    public Result<FlowInfo> entrust(Long taskId, User operator, User receiver, String remark) {
        FlowTask task = actionService.entrust(taskId, operator, receiver, remark);
        FlowInfo flowInfo = flowDataService.getFlowCurrent(task.getFlowId());
        return Result.success(flowInfo);
    }

    /**
     * 转办.
     * 将任务转办给新的审批人审核，任务的分配人更改成新的审批人，新的审批人审核后，进入后续，该任务不会回到原来的转办人上
     */
    public Result<FlowInfo> transfer(Long taskId, User operator, User receiver, String remark) {
        FlowTask task = actionService.transfer(taskId, operator, receiver, remark);
        FlowInfo flowInfo = flowDataService.getFlowCurrent(task.getFlowId());
        return Result.success(flowInfo);
    }

    /**
     * 获取待办信息
     * @param flowId 流程唯一标识
     * @return 流程待办信息
     */
    public FlowInfo getCurrentInfo(Long flowId) {
        return flowDataService.getFlowCurrent(flowId);
    }

    /**
     * 获取待办信息
     * @param flowCode 流程编码
     * @param bizId 业务唯一标识
     * @return 流程待办信息
     */
    public FlowInfo getCurrentInfo(String flowCode, String bizId) {
        return flowDataService.getFlowCurrent(flowCode, bizId);
    }

    /**
     * 查询用户已办记录
     * @param flowId 流程唯一标识
     * @return 流程已办记录
     */
    public List<FlowTaskLog> getTaskLogs(Long flowId) {
        return flowDataService.getTaskLogsByFlowId(flowId);
    }

    /**
     * 查询用户已办记录
     * @param flowCode 流程编码
     * @param bizId 业务唯一标识
     * @return 流程已办记录
     */
    public List<FlowTaskLog> getTaskLogs(String flowCode, String bizId) {
        Flow flow = flowDataService.getFlowByConfigIdAndBizId(flowCode, bizId);
        if(flow == null) {
            return Collections.emptyList();
        }
        return flowDataService.getTaskLogsByFlowId(flow.getFlowId());
    }
}
