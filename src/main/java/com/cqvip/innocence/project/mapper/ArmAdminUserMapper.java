package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 管理用户表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface ArmAdminUserMapper extends BaseMapper<ArmAdminUser> {

    /**
     * 根据id彻底删除管理员账户（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int deleteByIdToXml(@Param("id") Serializable id);

    /**
     * 根据id恢复管理员账户（从回收站恢复）
     * @author Innocence
     * @date 2021/8/25
     * @param id
     * @return int
     */
    int restoreAdminUser(@Param("id") Serializable id);

 List<ArmAdminUser> getAdminUserList(@Param("adminUser") ArmAdminUser adminUser,@Param("page") Page pageParams);
}
