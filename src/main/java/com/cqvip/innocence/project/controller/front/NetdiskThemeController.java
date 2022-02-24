package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNetdisk;
import com.cqvip.innocence.project.model.entity.ArmNetdiskClass;
import com.cqvip.innocence.project.model.entity.ArmNetdiskTheme;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.service.ArmNetdiskService;
import com.cqvip.innocence.project.service.ArmNetdiskThemeService;
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
@RequestMapping("/${base-url.front}/netdiskTheme/")
public class NetdiskThemeController extends AbstractController {

    @Autowired
    private ArmNetdiskThemeService netdiskThemeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArmNetdiskService netdiskService;

    /**
     * 获取当前用户的分享专题
     *
     * @return
     */
    @GetMapping("getNetdiskThemes")
    public JsonResult getNetdiskThemes(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        LambdaQueryWrapper<ArmNetdiskTheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmNetdiskTheme::getUserId, user.getId());
        List<ArmNetdiskTheme> list = netdiskThemeService.list(wrapper);
        return JsonResult.Get("netdiskThemes", list);
    }

    @PostMapping("saveNetdiskTheme")
    private JsonResult saveNetdiskTheme(HttpServletRequest request, ArmNetdiskTheme netdiskTheme) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        netdiskTheme.setUserId(user.getId());
        netdiskTheme.insert();
        return JsonResult.Get("netdiskTheme", netdiskTheme);
    }

    @PostMapping("deleteById")
    public JsonResult deleteById(Long id) {
        LambdaQueryWrapper<ArmNetdisk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmNetdisk::getClassId, id);
        int count = netdiskService.count(wrapper);
        if (count != 0) {
            return JsonResult.Get(false, "存在引用此分类的资源，删除失败");
        }
        return JsonResult.Get(netdiskThemeService.removeById(id));
    }

    @GetMapping("getAllThemes")
    @ApiOperation("获取所有主题（资源共享平台的热门主题）")
    public JsonResult getAllThemes(){
     LambdaQueryWrapper<ArmNetdisk> select = new LambdaQueryWrapper<ArmNetdisk>()
             .groupBy(ArmNetdisk::getClassId).select(ArmNetdisk::getClassId);
     List<ArmNetdisk> list = netdiskService.list(select);
     ArrayList<Long> classIds = new ArrayList<>();
     list.forEach(item -> classIds.add(item.getClassId()));
     return JsonResult.Get().putList(netdiskThemeService.listByIds(classIds));
    }
}
