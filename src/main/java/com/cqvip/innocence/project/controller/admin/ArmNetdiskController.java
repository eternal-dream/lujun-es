package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.cqvip.innocence.project.model.entity.ArmNews;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import com.cqvip.innocence.project.service.ArmNetdiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户网盘表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/arm-netdisk/")
@Api(tags="管理端对个人网盘的管理接口")
public class ArmNetdiskController extends AbstractController {

    @Autowired
    private ArmNetdiskService netdiskService;
    @Autowired
    private ArmAnnexService armAnnexService;
    @GetMapping("getPageList")
    @ApiOperation("管理员获取个人网盘分页列表")
    public JsonResult getPageList(ArmNetdisk disk){
        Page pageParams = getPageParams();
        return JsonResult.Get().putPage(netdiskService.getPageList(pageParams,disk));
    }

    @PostMapping("deleteById")
    @ApiOperation("管理端根据id删除个人网盘信息,支持批量删除")
    public JsonResult deleteById(@RequestBody List<Serializable> ids){
        return JsonResult.Get(netdiskService.deleteByIdsTransactional(ids));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("修改网盘信息")
    @XssExclusion
    public JsonResult saveOrUpdate(ArmNetdisk armNetdisk,
                                   MultipartFile annex,
                                   @RequestParam(required = false)String oldFile,
                                    @RequestParam(required = false)VipEnums.AnnexSrc annexSrc) {
        return JsonResult.Get(netdiskService.saveOrUpdateInfo(armNetdisk,annex,oldFile,annexSrc));
    }

    @GetMapping("/getNewsDiskById/{id}")
    @ApiOperation("通过id获取网盘信息")
    public JsonResult getNewsDiskById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        ArmNetdisk newDisk = netdiskService.getById(id);
        //获取相关附件
        QueryWrapper<ArmAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REAL_ID", newDisk.getId());
        List<ArmAnnex> armAnnexList = armAnnexService.list(queryWrapper);
        newDisk.setAnnexList(armAnnexList);
        return JsonResult.Get().put("data", newDisk);
    }

    @ApiOperation("置顶操作")
    @PostMapping("/handleTop")
    public JsonResult handleTop(Long id){
        ArmNetdisk netDisk = netdiskService.getById(id);
        QueryWrapper<ArmNetdisk> queryWrapper = new QueryWrapper<>();
        ArmNetdisk maxNetDis = netdiskService.getOne(queryWrapper.orderByDesc("SORT_ID").last("limit 1"));
        netDisk.setSortId(maxNetDis.getSortId() + 1);
        try{
            netdiskService.saveOrUpdate(netDisk);
        }catch(Exception ex){
            log.info("置顶操作失败，操作参数id="+id);
            return JsonResult.Get(false);
        }
        return JsonResult.Get();
    }
}

