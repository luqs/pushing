package com.skysea.pushing.api;

/**
 * 发布异常。
 * Created by zhangzhi on 2014/11/12.
 */
public class PublishException extends Exception {
    public PublishException(String message){
        super(message);
    }
    public PublishException(String message, Exception exp) {
        super(message, exp);
    }
}
