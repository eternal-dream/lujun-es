package com.cqvip.innocence.project.model.dto;


import java.util.HashMap;
import java.util.List;

/**
 * @ClassName JsonResult
 * @Description 定义统一的responseDto
 * @Author Innocence
 * @Date 2020/7/14 10:03
 * @Version 1.0
 */
public class JsonResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1628608837693261312L;
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MSG = "msg";
    private static final String KEY_CODE = "code";
    public static final String CODE_SUCCESS = "success";
    public static final String CODE_ERROR = "error";
    public static final String CODE_NOLOGIN = "Unauthorized";
    public static final String CODE_EXCEPTION = "exception";
    public static final String CODE_TIMEOUT = "timeout";
    public static final String CODE_NOAUTH = "noauth";
    public static final int ERROR_NOLOGIN_CODE = 401;
    public static final int ERROR_NOAUTH_CODE = 403;
    public static final int RESUBMIT_CODE = 666;

    public static final String SENSITIVE_ERROR = "sensitive_error";
    public static final int ERROR_SENSITIVE_CODE = 618;


    public static JsonResult Get() {
        return new JsonResult();
    }

    public static JsonResult Get(boolean success) {
        return new JsonResult(success);
    }

    public static JsonResult Get(boolean success, String msg) {
        return new JsonResult(success, msg);
    }

    public static JsonResult Get(boolean success, String msg, String code) {
        return new JsonResult(success, msg, code);
    }

    public static JsonResult Get(String key, Object value) {
        return (new JsonResult()).put(key, value);
    }

    public JsonResult() {
        this(true);
    }

    public JsonResult(boolean success) {
        this(success, "");
    }

    public JsonResult(boolean success, String msg) {
        this(success, msg, "");
    }

    public JsonResult(boolean success, String msg, String code) {
        this.set(success, msg, code);
    }

    public JsonResult set(boolean success, String msg) {
        this.setSuccess(success);
        this.setMsg(msg);
        if (this.getCode() == null) {
            this.setCode(success ? "success" : "error");
        }

        return this;
    }

    public JsonResult set(boolean success, String msg, String code) {
        this.setSuccess(success);
        this.setMsg(msg);
        this.setCode(code);
        return this;
    }

    public boolean isSuccess() {
        return (Boolean)super.get("success");
    }

    public JsonResult setSuccess(boolean success) {
        super.put("success", success);
        return this;
    }

    public Boolean getSuccess(){
        return (Boolean) super.get("success");
    }

    public String getMsg() {
        return (String)super.get("msg");
    }

    public JsonResult setMsg(String msg) {
        super.put("msg", msg);
        return this;
    }

    public String getCode() {
        return (String)super.get("code");
    }

    public JsonResult setCode(String code) {
        super.put("code", code);
        return this;
    }

    @Override
    public JsonResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public JsonResult putRes(Object value){
        super.put("res",value);
        return this;
    }

    public JsonResult putList(List<?> list){
        super.put("list",list);
        return this;
    }

    public JsonResult putPage(Object value){
        super.put("page",value);
        return this;
    }
}
