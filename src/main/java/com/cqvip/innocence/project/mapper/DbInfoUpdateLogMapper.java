package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfoUpdateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据库资源更新日志表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface DbInfoUpdateLogMapper extends BaseMapper<DbInfoUpdateLog> {

    /**
     * 管理员获取数据库更新日志分页
     * @author Innocence
     * @date 2021/8/20
     * @param page
     * @return org.springframework.data.domain.Page<com.cqvip.innocence.project.model.entity.DbInfoUpdateLog>
     */
    Page<DbInfoUpdateLog> getPageList(Page page, @Param("dbId") Long dbId);

    /**
     * 首页获取更新历史
     * @author Innocence
     * @date 2021/10/19
     * @return java.util.List<com.cqvip.innocence.project.model.entity.DbInfoUpdateLog>
     */
    List<DbInfoUpdateLog> getLimitList();

    Page<DbInfoUpdateLog> getPageListToFront(Page page);
}
