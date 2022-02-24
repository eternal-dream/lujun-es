package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.DbInfoUpdateLog;
import com.cqvip.innocence.project.service.DbInfoUpdateLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DbUpdateLogController
 * @Description TODO
 * @Author Innocence
 * @Date 2021/10/20 14:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/dbUpdate/")
public class DbUpdateLogController extends AbstractController {

    @Autowired
    private DbInfoUpdateLogService logService;

    @GetMapping("getPageList")
    @ApiOperation("前端获取数据库更新历史分页")
    public JsonResult getPageList(){
        Page page = getPageParams();
        Page<DbInfoUpdateLog> pageList = logService.getPageList(page);
        return JsonResult.Get().putPage(pageList);
    }


    @GetMapping("getDetailById")
    @ApiOperation("根据id获取更新历史详情")
    public JsonResult getDetailById(Long id){
        return JsonResult.Get().putRes(logService.getById(id));
    }

    @GetMapping("getDbUpdateLog")
    public JsonResult getDbUpdateLog(){
        
        return JsonResult.Get();
    }
}
