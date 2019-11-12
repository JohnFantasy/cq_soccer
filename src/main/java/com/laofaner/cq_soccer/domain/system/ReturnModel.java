package com.laofaner.cq_soccer.domain.system;

/**
 * @program: cq_soccer
 * @description: 系统全局统一返回结果封装类
 * @author: fyz
 * @create: 2019-11-12 16:18
 **/
public class ReturnModel<T> {

    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
