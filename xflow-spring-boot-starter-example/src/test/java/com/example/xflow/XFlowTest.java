package com.example.xflow;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.dto.FlowInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.NodeTypeEnum;
import top.zephyrs.xflow.manage.XFlowEngine;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class XFlowTest {


    @Resource
    private XFlowEngine xFlowEngine;

    private TestConfigBuilder testConfigBuilder =  new TestConfigBuilder();
    private Config config = testConfigBuilder.getTestConfig();

    @Test
    public void test(){
        String bizId = "abc0001";
        Map<String, Object> data = new HashMap<>();
        data.put("age", 16);
        User user = testConfigBuilder.startUser().get(0);
        Result<FlowInfo> result = xFlowEngine.start(config.getConfigCode(), bizId, user, data, null);
        log.info("start: {}",result);
        assert result.isSuccess();
        List<FlowTask> tasks = result.getData().getTasks();

        Long taskId = tasks.get(0).getTaskId();

        result = xFlowEngine.reject(taskId, user, "我要拒绝",data, null);
        log.info("approvalNode reject: {}",result);
        assert !result.isSuccess();

        user = testConfigBuilder.approvalUser().get(0);
        result = xFlowEngine.reject(taskId, user, "我要拒绝",data, null);
        log.info("approvalNode reject: {}",result);
        assert result.isSuccess();

        result = xFlowEngine.start(config.getConfigCode(), bizId, user, data, null);
        log.info("start: {}",result);
        assert result.isSuccess();

        tasks = result.getData().getTasks();
        taskId = tasks.get(0).getTaskId();

        result = xFlowEngine.approval(taskId, user, "我要同意",data, null);
        log.info("approvalNode approval: {}",result);
        assert result.isSuccess();


        tasks = result.getData().getTasks();
        for(FlowTask task: tasks) {
            result = xFlowEngine.approval(task.getTaskId(), new User(task.getUserId(), task.getUserName()), "我要同意",data, null);
            log.info("jointlyNode approval: {}",result);
            assert result.isSuccess();
        }

        assert result.getData().getCurrents().get(0).getNodeId().equals("vote_5");

        tasks = result.getData().getTasks();
        assert tasks.size() == testConfigBuilder.voteUser().size();

        int i = 0;
        for(FlowTask task: tasks) {
          i ++;
          if(i<=2) {
              result = xFlowEngine.approval(task.getTaskId(), new User(task.getUserId(), task.getUserName()), "我要同意",data, null);
              log.info("voteNode approval: {}",result);
          }
        }
        assert  result.getData().getCurrents().get(0).getNodeType() != NodeTypeEnum.vote;

        tasks = result.getData().getTasks();
        FlowTask task = tasks.get(0);
        user = testConfigBuilder.claimUser().get(0);
        result = xFlowEngine.approval(task.getTaskId(), user, "我要同意",data, null);
        log.info("claimNode approval: {}",result);
        assert !result.isSuccess();

        xFlowEngine.claim(task.getTaskId(), user);
        result = xFlowEngine.approval(task.getTaskId(), user, "我要同意",data, null);
        log.info("claimNode approval: {}",result);
        assert result.isSuccess();


    }

}
