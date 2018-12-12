/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月5日 下午2:33:39
 * @version V1.0
 */
package com.iqb.consumer.common.exception;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 6896718542004552268L;

    private String code;
    private String msg;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * Creates a new instance of <code>ServiceException</code> without detail message.
     */
    public ServiceException() {}

    /**
     * Constructs an instance of <code>ServiceException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
