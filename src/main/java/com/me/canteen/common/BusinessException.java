package com.me.canteen.common;

import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    /**
     * 使用错误码构造异常
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * 使用错误码和自定义消息构造异常
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.message = customMessage;
    }

    /**
     * 使用错误码和消息构造异常
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 仅使用消息构造异常（默认使用通用错误码 1）
     */
    public BusinessException(String message) {
        super(message);
        this.code = 1;
        this.message = message;
    }
}
