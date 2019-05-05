package com.wd.enums;

public enum StatusEnum {
    SUCCESS("9000", "成功");


    /**
     * 枚举值码
     */
    private String code;
    /**
     * 枚举值描述
     */
    private String message;

    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}