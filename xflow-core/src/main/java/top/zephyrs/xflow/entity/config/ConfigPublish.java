package top.zephyrs.xflow.entity.config;

import lombok.Data;

import java.util.Date;

/**
 * 已发布流程信息
 *
 * 保存已发布流程信息，同一流程只能有一个生效中的已发布流程
 */
@Data
public class ConfigPublish {

    private Long publishId;
    /**
     * 版本号
     */
    private String version;

    /**
     * 当前是否生效
     */
    private boolean isActive;

    /**
     * 当前流程配置信息
     */
    private Config config;
    /**
     * 流程定义ID
     */
    private Long configId;
    /**
     * 创建时间
     */
    private Date createTime;

}
