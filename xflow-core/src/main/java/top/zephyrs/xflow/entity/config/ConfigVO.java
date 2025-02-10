package top.zephyrs.xflow.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigVO extends Config {

    /**
     * 所属分组名称
     */
    private String groupName;

    /**
     * 版本
     */
    private String version;

    /**
     * 发布时间
     */
    private Date publishTime;
}
