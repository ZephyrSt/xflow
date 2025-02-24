package top.zephyrs.xflow.entity.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.mybatis.semi.annotations.Column;
import top.zephyrs.mybatis.semi.annotations.Primary;
import top.zephyrs.mybatis.semi.annotations.Table;
import top.zephyrs.mybatis.semi.plugins.typehandlers.JsonTypeHandler;

import java.util.Date;

@Schema(name = "已发布流程信息", description = "保存已发布流程信息，同一流程只能有一个生效中的已发布流程")
@Data
@Table("x_flow_config_publish")
public class ConfigPublish {

    @Primary
    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long publishId;
    @Schema(description = "流程定义ID")
    private Long configId;
    @Schema(description = "版本号")
    private String version;
    @Schema(description = "当前是否生效")
    private boolean isActive;
    @Schema(description = "流程配置信息")
    @Column(typeHandler = JsonTypeHandler.class)
    private Config config;

    @Schema(description = "创建时间")
    private Date createTime;

}
