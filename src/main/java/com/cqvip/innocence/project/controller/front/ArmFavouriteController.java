package com.cqvip.innocence.project.controller.front;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.model.entity.DbInfo;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmFavouriteService;
import com.cqvip.innocence.project.service.DbInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 用户收藏表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/${base-url.front}/arm-favourite/")
public class ArmFavouriteController extends AbstractController {

    @Autowired
    private ArmFavouriteService service;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DocumentService<ArticleDoc> documentService;

    @Autowired
    private DbInfoService infoService;

    @PostMapping("add")
    @ApiOperation("前台用户新增收藏")
    @Log(title = "收藏文章",operateType = VipEnums.OperateType.ADD)
    public JsonResult addFavourite(@RequestBody ArmFavourite favourite, HttpServletRequest request){
        if (favourite.getRealIds().isEmpty()){
            return JsonResult.Get(false,"请选择需要收藏的对象!");
        }
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        LambdaQueryWrapper<ArmFavourite> in = new QueryWrapper<ArmFavourite>().lambda()
                .eq(ArmFavourite::getClassId, favourite.getClassId())
                .eq(ArmFavourite::getFavouriteSrc, favourite.getFavouriteSrc())
                .eq(ArmFavourite::getUserId, user.getId())
                .in(ArmFavourite::getRealId, favourite.getRealIds());
        List<ArmFavourite> list = service.list(in);
        List<String> notFavouriteIds;
        if (!list.isEmpty()){
            List<String> existIds = new ArrayList<>();
            list.forEach(item -> existIds.add(item.getRealId()));
            List<String> realIds = favourite.getRealIds();
            Iterator<String> it = realIds.iterator();
            while (it.hasNext()){
                if (existIds.contains(it.next())){
                    it.remove();
                }
            }
            notFavouriteIds = realIds;
        }else{
            notFavouriteIds = favourite.getRealIds();
        }
        if (notFavouriteIds.isEmpty()){
            return JsonResult.Get();
        }
        List<ArticleDoc> entityByIds = documentService.getEntityByIds(notFavouriteIds, ArticleDoc.class);
        List<DbInfo> dbList = infoService.list();
        List<ArmFavourite> favourites = new ArrayList<>();
        entityByIds.forEach(item->{
            ArmFavourite armFavourite = new ArmFavourite();
            armFavourite.setClassId(favourite.getClassId());
            armFavourite.setUserId(user.getId());
            armFavourite.setTitle(item.getTitle());
            armFavourite.setFavouriteSrc(favourite.getFavouriteSrc());
            armFavourite.setRealId(item.getId());
            if (favourite.getFavouriteSrc().equals(VipEnums.FavouriteSrc.ARTICLESTORE)){
                armFavourite.setDbName(getDbNames(item.getProvider(),dbList));
                armFavourite.setResourceSrc(getDbNames(item.getProvider(),dbList));
            }
            favourites.add(armFavourite);
        });
        return JsonResult.Get(service.saveBatch(favourites));
    }

    @PostMapping("deleteByIds")
    @ApiOperation("用户根据ids删除收藏")
    @Log(title = "取消收藏",operateType = VipEnums.OperateType.DELETE)
    public JsonResult deleteByIds(@RequestBody List<Serializable> ids){
        return JsonResult.Get(service.removeByIds(ids));
    }

    @GetMapping("getPageList")
    @ApiOperation("获取个人收藏的分页列表，支持按收藏分类，收藏来源查询")
    @Log(title = "获取个人收藏",operateType = VipEnums.OperateType.SEARCH)
    public JsonResult getPageList(ArmFavourite favourite,HttpServletRequest request){
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        Page page = getPageParams();
        LambdaQueryWrapper<ArmFavourite> wrapper = new QueryWrapper<ArmFavourite>()
                .lambda()
                .eq(ArmFavourite::getUserId, user.getId());
        if (null != favourite.getClassId()){
            wrapper.eq(ArmFavourite::getClassId,favourite.getClassId());
        }
        if (null != favourite.getResourceSrc()){
            wrapper.eq(ArmFavourite::getFavouriteSrc,favourite.getFavouriteSrc());
        }
        return JsonResult.Get().putPage(service.page(page, wrapper));
    }

    @GetMapping("checkFavourited")
    @ApiOperation("收藏资源前预检是否已经收藏")
    public JsonResult checkFavourited(ArmFavourite favourite,HttpServletRequest request){
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(request.getSession().getId());
        LambdaQueryWrapper<ArmFavourite> lambda = new QueryWrapper<ArmFavourite>().lambda()
                .eq(ArmFavourite::getUserId,user.getId())
                .eq(ArmFavourite::getFavouriteSrc,favourite.getFavouriteSrc())
                .eq(ArmFavourite::getRealId,favourite.getRealId());
        int count = service.count(lambda);
        if (count > 0){
            return JsonResult.Get();
        }
        return JsonResult.Get(false,"未收藏");
    }

    /**
     * 获取来源数据库名称
     * @author Innocence
     * @date 2021/9/27
     * @return java.lang.String
     */
    private String getDbNames(List<String> tagList,List<DbInfo> dbList){
        List<String> nameList = new ArrayList<>();
        tagList.forEach(item -> dbList.forEach(db ->{
            if (item.equals(db.getProvider())){
                nameList.add(db.getTitle());
            }
        }));
        if (!nameList.isEmpty()){
            return StrUtil.join(";",nameList);
        }else {
            return null;
        }
    }

}

