package top.zephyrs.xflow.entity.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigVO extends Config {

    @Schema(description = "所属分组名称")
    private String groupName;

    @Schema(description = "所属分组名称")
    private String version;

    @Schema(description = "发布时间")
    private Date publishTime;
}
