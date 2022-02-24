package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfo;
import com.cqvip.innocence.project.mapper.DbInfoMapper;
import com.cqvip.innocence.project.service.DbInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据库基础信息表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Service
public class DbInfoServiceImpl extends ServiceImpl<DbInfoMapper, DbInfo> implements DbInfoService {

    @Override
    public Page<DbInfo> getPageList(Page page,DbInfo info) {
        return baseMapper.getPageList(page,info);
    }
}
