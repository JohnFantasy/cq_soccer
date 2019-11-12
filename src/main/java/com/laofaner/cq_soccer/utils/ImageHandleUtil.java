package com.laofaner.cq_soccer.utils;

import com.laofaner.cq_soccer.domain.enums.ImageTypeEnum;
import org.springframework.util.StringUtils;

/**
 * @Description:图片压缩比转换工具类
 * @Author: fyz
 * @CreateDate: 2018/5/24 9:36
 * @Version: 1.0
 **/
public class ImageHandleUtil {

    private static final String SUFFIX = "?x-oss-process=image/";

    public static String handleImage(ImageTypeEnum typeEnum, String imageUrl){
        if(StringUtils.isEmpty(imageUrl)){
            return imageUrl;
        }
        String url = imageUrl + SUFFIX + typeEnum.getValue();
        return url;
    }
}
