package top.zephyrs.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import top.zephyrs.web.entity.ParamsQuery;
import top.zephyrs.xflow.entity.Result;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.config.ConfigVO;
import top.zephyrs.xflow.service.ConfigService;

import java.util.List;

/**
 * 流程定义配置
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 分页查询配置信息
     * @param query
     * @return
     */
    @PostMapping("/queryList")
    public Result<PageInfo<ConfigVO>> queryList(@RequestBody ParamsQuery query) {
        PageHelper.startPage(query.getPage());
        List<ConfigVO> dataList = configService.getByQuery(query);
        PageInfo<ConfigVO> pageInfo = new PageInfo<>(dataList);
        return Result.success(pageInfo);
    }

    /**
     * 查询配置信息
     * @param configId
     * @return
     */
    @GetMapping("/get/{configId}")
    public Result<Config> getById(@PathVariable("configId") Long configId) {
        Config config = configService.getConfigInfoById(configId);
        return Result.success(config);
    }

    /**
     * 保存流程信息
     * @param configInfo
     * @return
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Config configInfo) {
        configService.saveConfig(configInfo);
        return Result.success();
    }

    /**
     * 保存流程配置
     * @param configInfo
     * @return
     */
    @PostMapping("/layout")
    public Result<?> layout(@RequestBody Config configInfo) {
        configService.saveConfig(configInfo);
        return Result.success();
    }

    /**
     * 发布配置
     * @param configId
     * @return
     */
    @PostMapping("/publish/{configId}")
    public Result<Config> publish(@PathVariable("configId") Long configId) {
        configService.publish(configId);
        return Result.success();
    }
}
