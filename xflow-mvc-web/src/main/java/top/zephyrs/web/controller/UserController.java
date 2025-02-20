package top.zephyrs.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import top.zephyrs.web.entity.ParamsQuery;
import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.users.DeptTree;
import top.zephyrs.xflow.entity.users.Role;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.service.FlowUserService;

import java.util.List;

/**
 * 提供默认配置页面的用户信息查询接口
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final FlowUserService userService;

    public UserController(FlowUserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户
     * @param query
     * @return
     */
    @PostMapping("/userList")
    public Result<PageInfo<User>> userList(@RequestBody ParamsQuery query) {
        PageHelper.startPage(query.getPage());
        List<User> dataList = userService.getUserByQuery(query);
        PageInfo<User> pageInfo = new PageInfo<>(dataList);
        return Result.success(pageInfo);
    }

    /**
     * 分页查询角色
     * @param query
     * @return
     */
    @PostMapping("/roleList")
    public Result<PageInfo<Role>> roleList(@RequestBody ParamsQuery query) {
        PageHelper.startPage(query.getPage());
        List<Role> dataList = userService.getRoleByQuery(query);
        PageInfo<Role> pageInfo = new PageInfo<>(dataList);
        return Result.success(pageInfo);
    }

    /**
     * 查询组织树
     * @param rootId
     * @return
     */
    @GetMapping("/dept/tree")
    public Result<List<DeptTree>> getTree(@RequestParam(name = "parent", required = false, defaultValue = "") String rootId) {
        return Result.success(userService.getDeptTree(rootId));
    }


}
