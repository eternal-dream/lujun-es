package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.service.DbInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DbInfoController
 * @Description TODO
 * @Author Innocence
 * @Date 2021/9/23 14:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/db-info/")
public class FrontDbInfoController {

    @Autowired
    private DbInfoService infoService;

    @GetMapping("getDbList")
    @ApiOperation("获取数据库信息")
    public JsonResult getDbList(){
        return JsonResult.Get().putList(infoService.list());
    }

}
