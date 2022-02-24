package com.cqvip.innocence.project.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.cqvip.innocence.project.model.entity.ArmFavouriteClass;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.service.ArmFavouriteClassService;
import com.cqvip.innocence.project.service.ArmFavouriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户收藏分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/${base-url.front}/favouriteClass/")
@Api(tags = "前台收藏分类接口")
public class ArmFavouriteClassController extends AbstractController {

    @Autowired
    private ArmFavouriteClassService favouriteClassService;

    @Autowired
    private ArmFavouriteService favouriteService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("getUserFavouriteClassList")
    @ApiOperation("获取用户收藏的分类列表")
    public JsonResult getList( HttpServletRequest request){
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        if (user == null){
            return JsonResult.Get(false,"当前用户未登录");
        }
        LambdaQueryWrapper<ArmFavouriteClass> eq = new QueryWrapper<ArmFavouriteClass>().lambda().eq(ArmFavouriteClass::getUserId, user.getId());
        List<ArmFavouriteClass> list = favouriteClassService.list(eq);
        return JsonResult.Get().putList(list);
    }

    @PostMapping("addOrEdit")
    @ApiOperation("用户新增编辑收藏的分类")
    public JsonResult addOrEdit(ArmFavouriteClass favouriteClass,HttpServletRequest request){
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        favouriteClass.setUserId(user.getId());
        favouriteClassService.saveOrUpdate(favouriteClass);
        return JsonResult.Get("favouriteClass",favouriteClass);
    }

    @PostMapping("deleteById")
    @ApiOperation("用户删除收藏分类")
    public JsonResult deleteById(Long id, HttpServletRequest request){
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        if (user == null){
            return JsonResult.Get(false,"当前用户未登录");
        }
        LambdaQueryWrapper<ArmFavourite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmFavourite::getClassId,id)
         .eq(ArmFavourite::getUserId,user.getId());
        int count = favouriteService.count(wrapper);
        if(count > 0){
            return JsonResult.Get(false,"您尚有收藏的文章使用此分类，删除失败");
        }
        favouriteClassService.deleteById(user.getId(), id);
        return JsonResult.Get();
    }

}

