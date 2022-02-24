package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmNewsColumn;
import com.cqvip.innocence.project.service.ArmNewsColumnService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新闻栏目表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Slf4j
@RestController
@RequestMapping("/${base-url.manager}/armNewsColumn")
public class ArmNewsColumnController extends AbstractController {
    @Autowired
    ArmNewsColumnService armNewsColumnService;

    @GetMapping("/getNewsColumnsOfTreeData")
    public JsonResult getNewsColumnsOfTreeData(@RequestParam(required = false,defaultValue = "0") Integer level){
        List<ArmNewsColumn> newsColumnsData = armNewsColumnService.getNewsColumnsOfTreeData(level);
        return JsonResult.Get().put("data",newsColumnsData);
    }

    /**
     * 获取 新闻栏目 列表（分页）
     *
     * @param armNewsColumn 查询条件
     * @param page          分页参数
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 13:51
     */
    @GetMapping("/getNewsColumnsByPage")
    @ApiOperation("获取 新闻栏目 列表（分页）")
    public JsonResult getNewsColumnsByPage(ArmNewsColumn armNewsColumn, Page page) {
        IPage<Map<String, Object>> pageParams = new Page<>(page.getCurrent(), page.getSize());
        //TODO  查询条件待定
        QueryWrapper<ArmNewsColumn> queryWrapper = new QueryWrapper<>();
        IPage<Map<String, Object>> data = armNewsColumnService.pageMaps(pageParams, queryWrapper.orderByAsc("NUMERICAL_ORDER"));
        return JsonResult.Get().put("data", data);
    }

    /**
     * 通过id查询NewsColumn信息
     *
     * @param id NewsColumn.Id
     * @return {@link com.cqvip.innocence.project.model.dto.JsonResult}
     * @author 01
     * @date 2021/8/20 15:00
     */
    @GetMapping("/getNewsColumnById/{id}")
    @ApiOperation("通过id查询NewsColumn信息")
    public JsonResult getNewsColumnById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmNewsColumn armNewsColumn = armNewsColumnService.getById(id);
        return JsonResult.Get().put("data", armNewsColumn);
    }

    /**
     * 添加或修改 新闻栏目
     *
     * @param armNewsColumn 新闻栏目信息
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 14:00
     */
    @PostMapping("/saveOrUpdateNewsColumn")
    @ApiOperation("添加或修改“新闻栏目”")
    public JsonResult saveOrUpdateNewsColumn(ArmNewsColumn armNewsColumn) {
        if (armNewsColumn == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            ArmAdminUser armAdminUser =(ArmAdminUser) subject.getPrincipal();
            armNewsColumn.setFounderId(armAdminUser.getId());
            armNewsColumnService.saveOrUpdate(armNewsColumn);
        } catch (Exception ex) {
            log.info("修改或新增“新闻栏目信息”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    /**
     * 通过NewsColumn id逻辑删除
     *
     * @param id NewsColumn.id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:01
     */
    @PostMapping("/deleteNewsColumnById")
    @ApiOperation("通过NewsColumn.id逻辑删除")
    public JsonResult deleteNewsColumnById(@RequestParam(name = "id", required = true) Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armNewsColumnService.removeById(id);
        } catch (Exception ex) {
            log.info("通过id删除NewsColumn失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }

        return JsonResult.Get().setMsg("操作成功");
    }

}

