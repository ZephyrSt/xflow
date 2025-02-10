package top.zephyrs.xflow.entity.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeptTree extends Dept {

    private List<DeptTree> children;
}
