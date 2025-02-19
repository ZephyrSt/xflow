package top.zephyrs.xflow.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import top.zephyrs.xflow.entity.ParamsQuery;
import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.config.Group;
import top.zephyrs.xflow.entity.config.GroupTree;
import top.zephyrs.xflow.service.GroupService;

import java.util.List;

/**
 * 流程分组管理
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    public static final Long ROOT_PARENT = -1L;

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 分页查询(根据父级ID)
     * @param parentId
     * @return
     */
    @GetMapping("/queryList")
    public Result<PageInfo<Group>> queryList(@RequestParam(name="parentId",required = false, defaultValue = "-1") Long parentId) {
        PageHelper.startPage(new ParamsQuery().getPage());
        List<Group> dataList = groupService.getByParent(parentId);
        PageInfo<Group> pageInfo = new PageInfo<>(dataList);
        return Result.success(pageInfo);
    }

    /**
     * 查询分组树
     * @param parentId
     * @return
     */
    @GetMapping("/getTree")
    public Result<List<GroupTree>> getTree(@RequestParam(name="parent",required = false, defaultValue = "-1") Long parentId) {
        return Result.success(groupService.getTree(parentId));
    }

    @PostMapping("/save")
    public Result<Group> save(@RequestBody Group group) {
        if(group.getParentId() == null) {
            group.setParentId(ROOT_PARENT);
        }
        groupService.save(group);
        return Result.success(group);
    }
}
