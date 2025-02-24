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
import java.util.List;

@Schema(name = "流程定义", description = "流程定义")
@Data
@Table("x_flow_config")
public class Config {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @Primary
    private Long configId;
    @Schema(description = "流程定义名称")
    private String configName;
    @Schema(description = "流程定义编码")
    private String configCode;

    @Schema(description = "所属分组Id")
    private Long groupId;

    @Schema(description = "说明")
    private String remark;

    @Schema(description = "节点")
    @Column(typeHandler = JsonTypeHandler.class)
    private List<ConfigNode> nodes;

    @Schema(description = "连接线")
    @Column(typeHandler = JsonTypeHandler.class)
    private List<ConfigEdge> edges;

    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "最后修改时间")
    private Date modifyTime;

}

