package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据库基础信息表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface DbInfoService extends IService<DbInfo> {

    /**
     * 获取数据库基础信息分页
     * @author Innocence
     * @date 2021/8/20
     * @param info
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.DbInfo>
     */
    Page<DbInfo> getPageList(Page page,DbInfo info);

}
