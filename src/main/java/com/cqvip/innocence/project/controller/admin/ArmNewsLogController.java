package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewsLog;
import com.cqvip.innocence.project.service.ArmNewsLogService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

/**
 * <p>
 * 新闻日志 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/armNewsLog")
public class ArmNewsLogController extends AbstractController {
    @Autowired
    ArmNewsLogService armNewsLogService;
    /**
     * 获取新闻日志操作列表（分页）
     * TODO
     * @param armNewsLog
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/9/29 18:07
     */
    @GetMapping("/getNewsLogByPage")
    public JsonResult getNewsLogByPage(ArmNewsLog armNewsLog) {
        Page page = getPageParams();
        QueryWrapper<ArmNewsLog> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotBlank(armNewsLog.getAdminUserName())){
            queryWrapper = queryWrapper.like("ADMIN_USER_NAME", armNewsLog.getAdminUserName());
        }
        if(armNewsLog.getObjId()!=null){
            queryWrapper = queryWrapper.eq("OBJ_ID", armNewsLog.getObjId());
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        IPage<ArmNewsLog> data = armNewsLogService.page(page, queryWrapper);
        return JsonResult.Get().put("data", data);
    }
}

