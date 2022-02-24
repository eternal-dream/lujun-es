package com.cqvip.innocence.framework.config.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.annotation.SensitiveTag;
import com.cqvip.innocence.common.exception.SensitiveException;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.entity.ArmBlockWord;
import com.cqvip.innocence.project.service.ArmBlockWordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SensitiveAspect
 * @Description 敏感词切面过滤
 * @Author Innocence
 * @Date 2021/1/22 11:44
 * @Version 1.0
 */
@Aspect
@Component
public class SensitiveAspect extends AbstractAspect<SensitiveTag> {
    private static final Logger log = LoggerFactory.getLogger(SensitiveAspect.class);

    @Autowired
    private ArmBlockWordService blockWordService;

    @Autowired
    private RedisUtil redisUtil;

    public static final String SENSITIVE_KEY = "sensitive_key";

    @Pointcut("@annotation(com.cqvip.innocence.common.annotation.SensitiveTag)")
    public void sensitivePointCut() {
    }

    @Before("sensitivePointCut()")
    public void doBeforeReturning(JoinPoint joinPoint){
        handSensitive(joinPoint);
    }

    protected void handSensitive(final JoinPoint joinPoint){
        SensitiveTag tag = getAnnotationTag(joinPoint,SensitiveTag.class);
        if (tag==null){
            return;
        }
        String requestValue = getRequestValue(joinPoint);
        if (StrUtil.isBlank(requestValue)){
            return;
        }
        Object o = redisUtil.get(SENSITIVE_KEY);
        List<String> sensitives ;
        if (o == null){
            LambdaQueryWrapper<ArmBlockWord> select = new QueryWrapper<ArmBlockWord>().lambda().select(ArmBlockWord::getTitle);
            List<ArmBlockWord> list = blockWordService.list(select);
            sensitives = new ArrayList<>();
            list.forEach(item-> sensitives.add(item.getTitle()));
            redisUtil.set(SENSITIVE_KEY,sensitives);
        }else {
            sensitives = (List<String>) o;
        }
        if (sensitives.size()>0){
            sensitives.forEach(item->{
                if (requestValue.indexOf(item) != -1){
                    throw new SensitiveException("存在敏感词："+item+"，请注意！");
                }
            });
        }
    }
}
