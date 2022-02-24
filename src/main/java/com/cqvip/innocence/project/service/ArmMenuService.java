package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.entity.ArmMenu;

import java.util.List;

/**
 * <p>
 * 菜单管理表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
public interface ArmMenuService extends IService<ArmMenu> {
    /**
     * 根据level级别查询对应level深度的前台菜单数据
     * @param level tree深度,如tree=3,则只查询从父节点开始，高度为3的数据。
     *              若level为0表示任意树高的数据均查询
     * @return {@link List<MenuNode> }
     */
    List<MenuNode> getMenuTreeDataByLevel(Integer level);

    List<MenuNode> getParentMenuByUrl(String url);
}
