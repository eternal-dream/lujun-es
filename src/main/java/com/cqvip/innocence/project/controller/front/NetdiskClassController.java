package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.cqvip.innocence.project.model.entity.ArmNetdiskClass;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmNetdiskClassService;
import com.cqvip.innocence.project.service.ArmNetdiskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author eternal
 * @Date 2021/9/29
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/netdiskClass")
public class NetdiskClassController extends AbstractController {

    @Autowired
    private ArmNetdiskClassService netdiskClassService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArmNetdiskService netdiskService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取当前用户的自定义共享资源分类
     */
    @GetMapping("getNetdiskClasses")
    public JsonResult getNetdiskClasses(HttpServletRequest request, VipEnums.DiskType diskType) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisUtil.get(sessionId);
        LambdaQueryWrapper<ArmNetdiskClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmNetdiskClass::getUserId, user.getId())
                .eq(diskType != null, ArmNetdiskClass::getDiskType, diskType);
        List<ArmNetdiskClass> list = netdiskClassService.list(wrapper);
        return JsonResult.Get("netdiskClasses", list);
    }

    @PostMapping("saveNetdiskClass")
    @Log(title = "添加共享资源分类",operateType = VipEnums.OperateType.ADD)
    public JsonResult saveNetdiskClass(HttpServletRequest request, ArmNetdiskClass netdiskClass) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisUtil.get(sessionId);
        netdiskClass.setUserId(user.getId());
        netdiskClass.insert();
        return JsonResult.Get("netdiskClass", netdiskClass);
    }

    @PostMapping("deleteById")
    @Log(title = "删除共享资源分类",operateType = VipEnums.OperateType.DELETE)
    public JsonResult deleteById(Long id) {
        LambdaQueryWrapper<ArmNetdisk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmNetdisk::getClassId, id);
        int count = netdiskService.count(wrapper);
        if (count != 0) {
            return JsonResult.Get(false, "存在引用此分类的资源，删除失败");
        }
        return JsonResult.Get(netdiskClassService.removeById(id));
    }

    @GetMapping("getAllClasses")
    @ApiOperation("获取所有用户的自定义分类列表（资源共享平台需要的热门类型列表）")
    public JsonResult getAllClasses(){
        LambdaQueryWrapper<ArmNetdisk> select = new LambdaQueryWrapper<ArmNetdisk>()
                .groupBy(ArmNetdisk::getClassId).select(ArmNetdisk::getClassId);
        List<ArmNetdisk> list = netdiskService.list(select);
        ArrayList<Long> classIds = new ArrayList<>();
        list.forEach(item -> classIds.add(item.getClassId()));
        return JsonResult.Get().putList(netdiskClassService.listByIds(classIds));
    }


}
