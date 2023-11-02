package com.wanfeng.shop.exception;

import com.wanfeng.shop.enums.BizCodeEnum;
import lombok.Data;

@Data
public class BizException extends RuntimeException {
    private int code;
    private String msg;

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public BizException(BizCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
    }
}
