package com.cqvip.innocence.project.model.enums;

/**
 * 业务操作类型
 *
 * @author ruoyi
 */
public enum BusinessType {
    /**
     * 其它
     */
    OTHER("其他"),

    /**
     * 新增
     */
    INSERT("新增"),

    /**
     * 修改
     */
    UPDATE("修改"),

    SAVE_OR_UPDATE("新增或修改"),

    /**
     * 删除
     */
    DELETE("删除"),

    /**
     * 查询
     */
    SELECT("查询");


    private String value;


    BusinessType(String value) {
        this.value = value;
    }

    public static String getName(String value) {
        for (BusinessType c : BusinessType.values()) {
            if (c.getValue().equals(value)) {
                return c.value;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
