package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmWebsiteConfig;
import com.cqvip.innocence.project.service.ArmWebsiteConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 网站风格管理表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/${base-url.manager}/armWebsiteConfig")
public class ArmWebsiteConfigController extends AbstractController {
    @Autowired
    ArmWebsiteConfigService armWebsiteConfigService;

    @GetMapping("/getWebsiteConfig")
    @ApiOperation("获取网站风格管理表")
    public JsonResult getWebsiteConfig() {
        List<ArmWebsiteConfig> websiteConfigList = armWebsiteConfigService.list();
        ArmWebsiteConfig websiteConfig =
                websiteConfigList.size() > 0 ? websiteConfigList.get(0) : null;
        return JsonResult.Get().put("config", websiteConfig);
    }

    @PostMapping("/saveOrUpdateWebsiteConfig")
    @ApiOperation("保存或修改 网站风格管理表")
    @XssExclusion
    public JsonResult saveOrUpdateWebsiteConfig(ArmWebsiteConfig armWebsiteConfig){
        if(armWebsiteConfig==null){
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        try{
            armWebsiteConfigService.saveOrUpdate(armWebsiteConfig);
        }catch(Exception ex){
            log.info("保存或修改 网站风格管理表 操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get(false).setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get();
    }
}

