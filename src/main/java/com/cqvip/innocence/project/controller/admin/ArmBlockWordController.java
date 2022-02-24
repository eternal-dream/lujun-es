package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmBlockWord;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmBlockWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cqvip.innocence.framework.config.aspect.SensitiveAspect.SENSITIVE_KEY;

/**
 * <p>
 * 屏蔽词表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/arm-block-word")
@Api(tags = "管理端的屏蔽词管理")
public class ArmBlockWordController extends AbstractController {

    @Autowired
    private ArmBlockWordService blockWordService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("add")
    @ApiOperation("屏蔽词的新增，可一次性添加多个，以；（不区分中英文）分隔")
    @Log(title = "屏蔽词的新增", operateType = VipEnums.OperateType.ADD)
    public JsonResult addOrEdit(@NotNull String title){
        List<String> oldSensitiveList = (List<String>) redisUtil.get(SENSITIVE_KEY);
        oldSensitiveList  = (oldSensitiveList != null && !oldSensitiveList.isEmpty()) ? oldSensitiveList : new ArrayList<String>();
        if (oldSensitiveList.isEmpty()){
            LambdaQueryWrapper<ArmBlockWord> select = new QueryWrapper<ArmBlockWord>().lambda().select(ArmBlockWord::getTitle);
            List<ArmBlockWord> list = blockWordService.list(select);
            if (!list.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    oldSensitiveList.add(list.get(i).getTitle());
                }
            }
        }
        title = title.trim();
        if (title.indexOf("；")!=-1){
            title = title.replaceAll("；",";");
        }
        if (title.indexOf(";") != -1 ){
            List<String> list = Arrays.asList(title.split(";"));
            ArrayList<ArmBlockWord> armBlockWords = new ArrayList<>();
            if (oldSensitiveList.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    ArmBlockWord armBlockWord = new ArmBlockWord();
                    armBlockWord.setTitle(list.get(i));
                    armBlockWords.add(armBlockWord);
                    oldSensitiveList.add(list.get(i));
                }
            }else{
                for (int i = 0; i < list.size(); i++) {
                    boolean contains = oldSensitiveList.contains(list.get(i));
                    if (!contains){
                        ArmBlockWord armBlockWord = new ArmBlockWord();
                        armBlockWord.setTitle(list.get(i));
                        armBlockWords.add(armBlockWord);
                        oldSensitiveList.add(list.get(i));
                    }
                }
            }
            blockWordService.saveBatch(armBlockWords);
            redisUtil.set(SENSITIVE_KEY,oldSensitiveList);
            return JsonResult.Get();
        }else {
            if (oldSensitiveList.isEmpty() || !oldSensitiveList.contains(title)){
                ArmBlockWord armBlockWord = new ArmBlockWord();
                armBlockWord.setTitle(title);
                oldSensitiveList.add(title);
                redisUtil.set(SENSITIVE_KEY,oldSensitiveList);
                return JsonResult.Get(blockWordService.save(armBlockWord));
            } else {
                return JsonResult.Get();
            }
        }
    }

    @PostMapping("edit")
    @ApiOperation("屏蔽词的编辑")
    @Log(title = "屏蔽词的编辑", operateType = VipEnums.OperateType.EDITE)
    public JsonResult addOrEdit(ArmBlockWord word){
        return JsonResult.Get(blockWordService.saveOrUpdate(word));
    }

    @GetMapping("getPageList")
    @ApiOperation("获取敏感词分页列表，默认按更新时间倒序，支持模糊检索")
    @Log(title = "获取敏感词分页列表", operateType = VipEnums.OperateType.SEARCH)
    public JsonResult getList(String title){
        LambdaQueryWrapper<ArmBlockWord> lambda = new QueryWrapper<ArmBlockWord>().lambda()
                .orderByDesc(ArmBlockWord::getCreateTime);
        if (StrUtil.isNotBlank(title)){
            lambda.like(ArmBlockWord::getTitle,title);
        }
        Page pageParams = getPageParams();
        return JsonResult.Get().putPage(blockWordService.page(pageParams, lambda));
    }

    @PostMapping("deleteById")
    @Log(title = "删除敏感词",operateType = VipEnums.OperateType.DELETE)
    @ApiOperation("根据ID删除敏感词，支持批量删除")
    public JsonResult deleteById(@RequestBody List<Serializable> ids){
        boolean b = blockWordService.removeByIds(ids);
        if (b){
            List<String> list = new ArrayList<>();
            blockWordService.list().forEach(item-> list.add(item.getTitle()));
            redisUtil.set(SENSITIVE_KEY,list);
        }
        return JsonResult.Get(b);
    }
}

