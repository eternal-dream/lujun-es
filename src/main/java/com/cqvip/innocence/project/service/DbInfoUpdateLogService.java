package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfoUpdateLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据库资源更新日志表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface DbInfoUpdateLogService extends IService<DbInfoUpdateLog> {

    /**
     * 管理员获取数据库更新日志分页
     * @author Innocence
     * @date 2021/8/20
     * @param page
     * @param dbId
     * @return org.springframework.data.domain.Page<com.cqvip.innocence.project.model.entity.DbInfoUpdateLog>
     */
    Page<DbInfoUpdateLog> getPageList(Page page, Long dbId);

    /**
     * 首页获取更新历史
     * @author Innocence
     * @date 2021/10/19
     * @return java.util.List<com.cqvip.innocence.project.model.entity.DbInfoUpdateLog>
     */
    List<DbInfoUpdateLog> getLimitList();

    /**
     * 前端获取更新历史分页
     * @author Innocence
     * @date 2021/10/20
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.DbInfoUpdateLog>
     */
    Page<DbInfoUpdateLog> getPageList(Page page);
}
