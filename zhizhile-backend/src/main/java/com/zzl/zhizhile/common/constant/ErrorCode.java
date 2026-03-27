package com.zzl.zhizhile.common.constant;

public enum ErrorCode {
    BAD_REQUEST("BAD_REQUEST", "请求参数错误"),
    PROJECT_NOT_FOUND("PROJECT_NOT_FOUND", "项目不存在"),
    PATTERN_NOT_FOUND("PATTERN_NOT_FOUND", "图解不存在"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "文件不存在"),
    FILE_INVALID("FILE_INVALID", "上传内容不符合要求"),
    DUPLICATE_UPLOAD("DUPLICATE_UPLOAD", "上传文件重复"),
    INVALID_LINK("INVALID_LINK", "图解链接不合法"),
    PATTERN_SWITCH_FAILED("PATTERN_SWITCH_FAILED", "当前图解切换失败"),
    INTERNAL_ERROR("INTERNAL_ERROR", "系统异常");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
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
