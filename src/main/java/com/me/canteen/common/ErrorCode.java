package com.me.canteen.common;

import lombok.Getter;

/**
 * 统一错误码定义
 */
@Getter
public enum ErrorCode {

    // 通用错误码 (1xxx)
    SUCCESS(0, "操作成功"),
    SYSTEM_ERROR(1000, "系统内部错误"),
    PARAM_ERROR(1001, "参数校验失败"),
    UNAUTHORIZED(1002, "未登录或登录已过期"),
    FORBIDDEN(1003, "无权限访问"),
    NOT_FOUND(1004, "资源不存在"),

    // 用户相关 (2xxx)
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_PASSWORD_ERROR(2002, "密码错误"),
    USER_PHONE_EXISTS(2003, "手机号已注册"),

    // 购物车相关 (3xxx)
    CART_EMPTY(3001, "购物车为空"),
    CART_ITEM_NOT_FOUND(3002, "购物车项不存在"),
    FOOD_NOT_FOUND(3003, "菜品不存在"),
    FOOD_STOCK_INSUFFICIENT(3004, "库存不足"),

    // 订单相关 (4xxx)
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态异常"),

    // 预约相关 (5xxx)
    SEAT_RESERVED(5001, "座位已被预约"),
    RESERVATION_NOT_FOUND(5002, "预约记录不存在"),
    TIME_SLOT_INVALID(5003, "时间段无效"),

    // 二维码相关 (6xxx)
    QR_CODE_EXPIRED(6001, "二维码已过期"),
    QR_CODE_USED(6002, "二维码已使用"),
    QR_CODE_INVALID(6003, "二维码无效");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
