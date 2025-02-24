package com.example.xflow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.manage.XFlowEngine;
import top.zephyrs.xflow.service.ConfigService;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class ConfigTest {


    @Resource
    private XFlowEngine xFlowEngine;

    @Resource
    private ConfigService configService;


    @Test
    public void config(){
        Config config = new TestConfigBuilder().getTestConfig();
        configService.saveConfig(config);
        configService.publish(config.getConfigId());
        ConfigPublish publish = configService.getPublishByConfigCode(config.getConfigCode());

        assert  publish.isActive();
        assert  publish.getVersion().equals("1");
    }

}
