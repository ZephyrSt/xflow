package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.config.Group;

import java.util.List;

public interface GroupDAO {

    int insert(Group group);

    int updateById(Group group);

    Group selectById(Long id);

    List<Group> selectAll();

    List<Group> selectByParentId(Long parentId);

}
