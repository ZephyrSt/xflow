package top.zephyrs.xflow.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.zephyrs.xflow.configs.XFlowConfig;
import top.zephyrs.xflow.data.*;
import top.zephyrs.xflow.listener.DefaultFlowEventListener;
import top.zephyrs.xflow.listener.FlowEventListener;
import top.zephyrs.xflow.manage.XFlowEngine;
import top.zephyrs.xflow.service.*;
import top.zephyrs.xflow.service.nodes.NodeStrategyWrapper;

@Configuration
@EnableConfigurationProperties(value = XFlowProperties.class)
@MapperScan("top.zephyrs.xflow.mapper")
@ComponentScan("top.zephyrs.xflow")
public class XFlowConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public XFlowConfig xflowConfig(XFlowProperties xFlowProperties) {
        return new XFlowConfig(xFlowProperties.getWorkerId(), xFlowProperties.getFlowType(), xFlowProperties.getDefaultRejectStrategy());
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowLock flowLock(@Autowired LockDAO lockDAO) {
        return new DefaultFlowLock(lockDAO);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigService configService(@Autowired ConfigDAO configDAO, @Autowired ConfigPublishDAO configPublishDAO) {
        return new ConfigService(configDAO, configPublishDAO);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowEventListener flowEventService() {
        return new DefaultFlowEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowUserManager flowUserService(@Autowired UserDAO userDAO){
        return new DefaultFlowUserManager(userDAO);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowDataService flowDataService(FlowDAO flowMapper,
                                           NodeCurrentDAO nodeCurrentMapper,
                                           NodeCurrentLogDAO nodeLogMapper,
                                           TaskDAO taskMapper,
                                           TaskLogDAO taskLogMapper,
                                           FlowEventListener flowEventListener){
        return new FlowDataService(flowMapper, nodeCurrentMapper, nodeLogMapper, taskMapper, taskLogMapper, flowEventListener);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupService groupService(XFlowConfig flowConfig, GroupDAO groupDAO){
        return new GroupService(groupDAO, flowConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public NodeStrategyWrapper nodeStrategyWrapper(ConfigService configService,
                                                   FlowDataService flowDataService,
                                                   FlowUserManager flowUserManager,
                                                   XFlowConfig flowConfig,
                                                   FlowLock flowLock) {
        return new NodeStrategyWrapper(configService, flowDataService, flowUserManager, flowConfig, flowLock);
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowActionService flowActionService(NodeStrategyWrapper wrapper, ConfigService configService,
                                        FlowDataService flowDataService,
                                               FlowLock flowLock){
        return new FlowActionService(wrapper, configService, flowDataService, flowLock);
    }

    @Bean
    @ConditionalOnMissingBean
    public XFlowEngine xFlowEngine(ConfigService configService, FlowDataService flowDataService, FlowActionService flowActionService) {
        return new XFlowEngine(configService, flowDataService, flowActionService);
    }

}
