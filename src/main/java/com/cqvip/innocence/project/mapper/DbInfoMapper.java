package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.DbInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数据库基础信息表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface DbInfoMapper extends BaseMapper<DbInfo> {

    /**
     * 获取数据库基础信息分页
     * @author Innocence
     * @date 2021/8/20
     * @param info
     * @param page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.DbInfo>
     */
    Page<DbInfo> getPageList(Page page,@Param("info") DbInfo info);

}
