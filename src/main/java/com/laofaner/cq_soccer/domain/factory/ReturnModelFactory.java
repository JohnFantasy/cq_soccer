package com.laofaner.cq_soccer.domain.factory;

import com.laofaner.cq_soccer.domain.enums.ResultEnum;
import com.laofaner.cq_soccer.domain.system.ReturnModel;

import java.io.Serializable;

/**
 * @program: cq_soccer
 * @description: 系统全局统一返回结果封装类工厂类，用于生产常用的返回结果实例
 * @author: fyz
 * @create: 2019-11-12 16:20
 **/
public class ReturnModelFactory {

    /**
     * 指定枚举结果和返回数据构造ReturnModel
     *
     * @param resultEnum
     * @param data
     * @param <T>
     * @return ReturnModel
     */
    public static <T extends Serializable> ReturnModel<T> generateReturnModel(ResultEnum resultEnum, T data) {
        ReturnModel<T> result = new ReturnModel<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 根据传入的实际data类型构造ReturnModel（默认为success）
     * @param data
     * @param <T>
     * @return ReturnModel
     */
    public static <T extends Serializable> ReturnModel<T> generateReturnModel(T data) {
        ReturnModel<T> result = new ReturnModel<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }
}
