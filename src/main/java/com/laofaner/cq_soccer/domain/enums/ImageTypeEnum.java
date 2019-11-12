package com.laofaner.cq_soccer.domain.enums;

/**
 * @Description:
 * @Author: fyz
 * @CreateDate: 2018/5/24 9:38
 * @Version: 1.0
 **/
public enum ImageTypeEnum {

    //图片压缩
    RESIZE_SMALL("11","resize,m_lfit,w_160,h_160,limit_0"),
    RESIZE_MID("12","resize,m_lfit,w_750,h_750,limit_0"),
    RESIZE_BIG("13","resize,m_lfit,w_1400,h_1400,limit_0"),
    MERCHANT_BANNER("14","resize,m_lfit,w_750,h_350,limit_0"),
    COUPON_BANNER("15","resize,m_lfit,w_750,h_500,limit_0"),
    EVALUATION_RESIZE("16","resize,m_lfit,w_140,h_140,limit_0"),

    //图片裁剪
    CROP_DIY("21","crop,w_50,h_50,x_150,y_50");


    ImageTypeEnum(String id, String value){
        this.id = id;
        this.value = value;
    }
    private String id;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
