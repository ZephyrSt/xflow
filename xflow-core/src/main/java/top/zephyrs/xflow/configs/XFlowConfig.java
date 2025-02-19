package top.zephyrs.xflow.configs;

import lombok.Getter;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.RejectStrategyEnum;

@Getter
public class XFlowConfig {

    /** 系统执行操作人*/
    public static final User SYSTEM_USER = new User("system", "system");

    /**用户筛选-指定用户*/
    public static final String USER_FILTER_TYPE_USER = "user";

    /**用户筛选-指定角色*/
    public static final String USER_FILTER_TYPE_ROLE = "role";

    /**用户筛选-提交人指定人员*/
    public static final String USER_FILTER_TYPE_SELECT = "select";


    /**用户筛选-筛选方式-全部人员*/
    public static final String USER_FILTER_FILTER_ALL = "all";
    /**用户筛选-筛选方式-提交人所在部门*/
    public static final String USER_FILTER_FILTER_DEPT = "dept";
    /**用户筛选-筛选方式-提交人上级部门*/
    public static final String USER_FILTER_FILTER_MANAGE_DEPT = "manage";

    /**
     * Id生成器机器码
     */
    private final Integer  workId;

    /**
     * 默认的拒绝策略
     */
    private final RejectStrategyEnum defaultRejectStrategy;


    public XFlowConfig(Integer workId, RejectStrategyEnum defaultRejectStrategy) {
        this.workId = workId;
        this.defaultRejectStrategy = defaultRejectStrategy;
    }

}
