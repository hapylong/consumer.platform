/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:19:18
 * @version V1.0
 */
package com.iqb.consumer.common.exception;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException {

    private String entity;

    /**
     * Creates a new instance of <code>EntityNotFoundException</code> without detail message.
     */
    public EntityNotFoundException() {}

    /**
     * Constructs an instance of <code>EntityNotFoundException</code> with the specified detail
     * message.
     * 
     * @param message the detail message.
     */
    public EntityNotFoundException(String entity, String message) {
        super(message);
        this.entity = entity;
    }

    public EntityNotFoundException(String entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getClass().getName()).append("[");
        buffer.append("entity=").append(entity);
        buffer.append(", message=").append(getMessage());
        buffer.append("]");
        return buffer.toString();
    }
}
