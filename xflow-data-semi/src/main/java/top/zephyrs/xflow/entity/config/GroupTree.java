package top.zephyrs.xflow.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupTree extends Group {

    private List<GroupTree> children;
}
