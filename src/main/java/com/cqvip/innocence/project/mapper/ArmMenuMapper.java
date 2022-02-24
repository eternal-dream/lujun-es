package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.entity.ArmMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
public interface ArmMenuMapper extends BaseMapper<ArmMenu> {
    /**
     * 获取所有非顶级菜单
     * @return
     */
    List<MenuNode> getAllNotSuperMenu();

    /**
     * 获取所有顶级菜单
     * @return
     */
    List<MenuNode> getAllSuperMenu();

    MenuNode getMenuNodeById(@Param("id") Long parentId);
}
