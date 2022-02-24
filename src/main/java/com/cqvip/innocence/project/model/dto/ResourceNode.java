package com.cqvip.innocence.project.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 01
 * @date 2021-09-02 18:17
 */
@Data
public class ResourceNode {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private Integer deleted;

    private String menuUrl;

    private String name;

    private String permission;

    private String sort;

    private String menuIcon;

    private Long parentId;

    private String menuType;

    private String component;

    private Boolean isCache;

    private Boolean isHidden;

    private List<ResourceNode> children;

}
