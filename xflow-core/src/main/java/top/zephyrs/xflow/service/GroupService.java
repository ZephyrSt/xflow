package top.zephyrs.xflow.service;

import top.zephyrs.xflow.configs.XFlowConfig;
import top.zephyrs.xflow.data.GroupDAO;
import top.zephyrs.xflow.entity.config.Group;
import top.zephyrs.xflow.entity.config.GroupTree;
import top.zephyrs.xflow.utils.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GroupService {

    private final GroupDAO groupMapper;
    private final XFlowConfig flowConfig;

    public GroupService(GroupDAO groupMapper, XFlowConfig flowConfig) {
        this.groupMapper = groupMapper;
        this.flowConfig = flowConfig;
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
            return groupMapper.insert(group) > 0;
        }
        Group exists = groupMapper.selectById(id);
        if (exists == null) {
            return groupMapper.insert(group) > 0;
        } else {
            return groupMapper.updateById(group) > 0;
        }
    }

}
