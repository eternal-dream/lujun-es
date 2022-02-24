package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfoUpdateLog;
import com.cqvip.innocence.project.mapper.DbInfoUpdateLogMapper;
import com.cqvip.innocence.project.service.DbInfoUpdateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 数据库资源更新日志表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Service
public class DbInfoUpdateLogServiceImpl extends ServiceImpl<DbInfoUpdateLogMapper, DbInfoUpdateLog> implements DbInfoUpdateLogService {

    @Override
    public Page<DbInfoUpdateLog> getPageList(Page page, Long id) {
        return baseMapper.getPageList(page,id);
    }

    @Override
    public List<DbInfoUpdateLog> getLimitList() {
        return baseMapper.getLimitList();
    }

    @Override
    public Page<DbInfoUpdateLog> getPageList(Page page) {
        return baseMapper.getPageListToFront(page);
    }
}
