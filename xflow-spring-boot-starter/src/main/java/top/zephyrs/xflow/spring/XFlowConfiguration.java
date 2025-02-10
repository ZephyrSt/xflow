package top.zephyrs.xflow.spring;

import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.zephyrs.xflow.data.*;
import top.zephyrs.xflow.data.keys.XFlowKeySnowflake;
import top.zephyrs.xflow.manage.XFlowEngine;
import top.zephyrs.xflow.service.*;

@Configuration
@EnableConfigurationProperties(value = XFlowProperties.class)
@MapperScan("top.zephyrs.xflow.data.mapper")
public class XFlowConfiguration {

    @Resource
    private XFlowProperties xFlowProperties;

    @Bean
    @ConditionalOnMissingBean
    public XFlowKeySnowflake snowflake() {
        return new XFlowKeySnowflake(xFlowProperties.getWorkerId());
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowLock flowLock(@Autowired LockDAO lockDAO) {
        return new DefaultFlowLock(lockDAO);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigService configService(XFlowKeySnowflake snowflake, @Autowired ConfigDAO configDAO, @Autowired ConfigPublishDAO configPublishDAO) {
        return new ConfigService(configDAO, configPublishDAO, snowflake);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowEventService flowNoticeService() {
        return new DefaultFlowEventService();
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowUserService flowUserService(@Autowired UserDAO userDAO){
        return new DefaultFlowUserService(userDAO);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowDataService flowDataService(XFlowKeySnowflake snowflake, FlowDAO flowMapper,
                                           NodeCurrentDAO nodeCurrentMapper,
                                           NodeCurrentLogDAO nodeLogMapper,
                                           TaskDAO taskMapper,
                                           TaskLogDAO taskLogMapper,
                                           FlowEventService flowEventService){
        return new FlowDataService(flowMapper, nodeCurrentMapper, nodeLogMapper, taskMapper, taskLogMapper, flowEventService, snowflake);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupService groupService(XFlowKeySnowflake snowflake, GroupDAO groupDAO){
        return new GroupService(groupDAO, snowflake);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowActionService flowActionService(ConfigService configService,
                                        FlowDataService flowDataService,
                                        FlowUserService flowUserService,
                                        FlowLock flowLock){
        return new FlowActionService(configService, flowDataService, flowUserService, flowLock);
    }

    @Bean
    @ConditionalOnMissingBean
    public XFlowEngine xFlowEngine(ConfigService configService, FlowDataService flowDataService, FlowActionService flowActionService) {
        return new XFlowEngine(configService, flowDataService, flowActionService);
    }

}
