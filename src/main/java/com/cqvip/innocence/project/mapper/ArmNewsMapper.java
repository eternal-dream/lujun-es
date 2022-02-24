package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNews;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新闻表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsMapper extends BaseMapper<ArmNews> {
    /**
     * 获取栏目类容ByPage
     *
     * @param armNews
     * @param beginTime
     * @param endTime
     * @param page
     * @param order 排序方式 {createTime 按创建时间倒序，}
     * @return {@link Page< Map< String, Object>>}
     * @throws
     * @author 01
     * @date 2021/9/9 0:27
     */
    Page<ArmNews> getNewsByPage(@Param("armNews") ArmNews armNews,  @Param("beginTime")Date beginTime, @Param("endTime") Date endTime, Page page,@Param("order") String order);

    /**
     * 根据id彻底删除新闻（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int deleteByIdWithXml(@Param("id") Serializable id);

    /**
     * 恢复新闻数据
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int restore(@Param("id") Serializable id);

    List<Map<String, Object>> getNewsListToIndex();

    Page<ArmNews> getNewsPageList(Page page);
}
