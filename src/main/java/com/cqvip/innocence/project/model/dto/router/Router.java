package com.cqvip.innocence.project.model.dto.router;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Routers
 * @Description 路由实体类
 * @Author Innocence
 * @Date 2020/11/22
 * @Version 1.0
 */
@Data
public class Router {

    private Long id;

    private Long parentId;

    private boolean alwaysShow;

    private String component;

    private String name;

    private String path;

    private String redirect;

    private boolean hidden;

    private Meta meta;

    private List<Router> children;

    private String title;

    private String icon;

    private String menuType;

}
