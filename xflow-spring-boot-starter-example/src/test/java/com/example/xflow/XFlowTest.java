package com.example.xflow;

import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.flow.dto.FlowInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.manage.XFlowEngine;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class XFlowTest {


    @Resource
    private XFlowEngine xFlowEngine;

    public static String flowCode = "10001";
    public static User user = new User("0","张三");

    public static User audit1 = new User("1", "写向东");

    public static User audit2 = new User("2", "终审人");


    @Test
    public void start(){
        Map<String, Object> data = new HashMap<>();
        data.put("age", 18);
        Result<FlowInfo> result = xFlowEngine.start(flowCode, "abc0001", user, data, null);
        System.out.println(result);
        assert result.isSuccess();
    }


    @Test
    public void reject(){
        Long taskId = 1881993545359818752L;
        Map<String, Object> data = new HashMap<>();
        data.put("age", 18);
        Result<FlowInfo> result = xFlowEngine.reject(taskId, audit1, "我要拒绝",data, null);
        System.out.println(result);
        assert result.isSuccess();
    }


    @Test
    public void approval(){
        Long taskId = 1882600691222970368L;
        Map<String, Object> data = new HashMap<>();
        data.put("age", 18);
        Result<FlowInfo> result = xFlowEngine.approval(taskId, new User("6", "孙娜"), "我要同一",data, null);
        System.out.println(result);
        assert result.isSuccess();
    }

    @Test
    public void approval2(){
        Long taskId = 1882600690983895040L;
        Map<String, Object> data = new HashMap<>();
        data.put("age", 18);
        Result<FlowInfo> result = xFlowEngine.approval(taskId, new User("4", "魏静"), "我要同一",data, null);
        System.out.println(result);
        assert result.isSuccess();
    }

}
