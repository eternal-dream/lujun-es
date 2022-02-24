package com.cqvip.innocence.project.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 01
 * @date 2021-08-25 15:20
 */
@Data
public class MenuNode {
    private Long id;

    private Date createTime;

    private Date modifyTime;

    private Integer deleted;

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单URL地址
     */
    private String url;
    /**
     * 菜单层级
     */
    private Integer level;
    /**
     * 父级ID
     */
    private Long parentId;
    /**
     * 菜单顺序
     */
    private Integer sort;
    /**
     * 创建者ID
     */
    private Long founderId;
    /**
     * 图标
     */
    private String icon;

    private Boolean isDefault;

    private List<MenuNode> children;

}
