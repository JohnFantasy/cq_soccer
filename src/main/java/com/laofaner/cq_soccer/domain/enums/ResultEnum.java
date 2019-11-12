package com.laofaner.cq_soccer.domain.enums;

public enum ResultEnum {
    SUCCESS(0,"success"),
    FAILED(1,"failed"),
    UNKNOWN(-1,"unknown error")
    ;

    private int code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
