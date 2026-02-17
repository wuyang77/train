package org.wuyang.common.exception;

public enum BusinessExceptionEnum {
    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("手机号不存在，请先获取短信验证码"),
    MEMBER_MOBILE_CODE_ERROR("短信验证码错误"),
    BUSINESS_STATION_NAME_UNIQUE_ERROR("车站已存在")
    ;

    // 定义一个描述
    private String desc;

    BusinessExceptionEnum(String desc) {this.desc = desc;}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
