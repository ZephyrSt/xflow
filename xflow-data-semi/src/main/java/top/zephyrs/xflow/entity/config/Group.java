package top.zephyrs.xflow.entity.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.mybatis.semi.annotations.Column;
import top.zephyrs.mybatis.semi.annotations.Primary;
import top.zephyrs.mybatis.semi.annotations.Table;

@Schema(name = "流程分组")
@Data
@Table("x_flow_group")
public class Group {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @Primary
    private Long groupId;

    @Schema(description = "名称")
    private String groupName;


    @Schema(description = "备注")
    private String remark;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "父级名称")
    @Column(exists = false)
    private String parentName;
}
