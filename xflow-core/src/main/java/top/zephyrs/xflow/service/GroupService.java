package top.zephyrs.xflow.service;

import top.zephyrs.xflow.data.GroupDAO;
import top.zephyrs.xflow.data.keys.XFlowKeySnowflake;
import top.zephyrs.xflow.entity.config.Group;
import top.zephyrs.xflow.entity.config.GroupTree;
import top.zephyrs.xflow.utils.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GroupService {

    private final GroupDAO groupMapper;
    private final XFlowKeySnowflake snowflake;

    public GroupService(GroupDAO groupMapper, XFlowKeySnowflake snowflake) {
        this.groupMapper = groupMapper;
        this.snowflake = snowflake;
    }

    public List<Group> getByParent(Long parentId) {
        return groupMapper.selectByParentId(parentId);
    }

    public List<GroupTree> getTree(Long parentId) {
        List<Group> all = groupMapper.selectAll();
        List<GroupTree> treeList = all.parallelStream()
                .map(dict -> BeanUtils.convertBean(dict, GroupTree.class))
                .collect(Collectors.toList());
        return streamToTree(treeList, parentId);
    }

    private List<GroupTree> streamToTree(List<GroupTree> treeList, Long rootId) {
        return treeList.stream()
                // 过滤父节点
                .filter(parent -> parent.getParentId().equals(rootId))
                // 把父节点children递归赋值成为子节点
                .peek(child -> child.setChildren(streamToTree(treeList, child.getGroupId())))
                .collect(Collectors.toList());
    }

    public boolean save(Group group) {
        Long id = group.getGroupId();
        if (id == null) {
            group.setGroupId(snowflake.nextId());
            return groupMapper.insert(group)>0;
        }
        Group exists = groupMapper.selectById(id);
        if (exists == null) {
            group.setGroupId(snowflake.nextId());
            return groupMapper.insert(group)>0;
        } else {
            return groupMapper.updateById(group)>0;
        }
    }

}
