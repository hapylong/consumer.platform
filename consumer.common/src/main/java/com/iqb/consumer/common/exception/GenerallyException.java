package com.iqb.consumer.common.exception;

/**
 * 
 * Description: 通用例外
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月8日    adam       1.0        1.0 Version 
 * </pre>
 */
public class GenerallyException extends Exception {

    private static final long serialVersionUID = 1L;
    private Reason reason;
    private Location location;
    private Layer layer;

    public static enum Layer {
        CONTROLLER, SERVICE, MANAGER, REPOSITORY, BASIC_SERVICE, UTIL
    }

    public static enum Location {
        A, B, C, D, E, BASIC_SERVICE
    }

    public static enum Reason {
        INVALID_REQUEST_PARAMS(1, "请求参数校验失败."),
        DB_NOT_FOUND(2, "出错了~ 相关数据检索异常."),
        INVALID_ENTITY(3, "出错了~ 数据库模型数据残缺."),
        UNKNOW_TYPE(4, "出错了~"),
        DB_ERROR(5, "数据库出错了~"),
        UNKNOWN_ERROR(6, "出错了~ 系统内部错误哦~"),
        CHAT_RESPONSE_ERROR(7, "系统内部请求出错了~"),
        CHAT_DECODE_ERROR(8, "请求信息解密失败~"),
        ERR_MSG_SELF_DEFINE(9, "返回信息自定义~");
        private int ec;
        private String sc;

        private Reason(int errCode, String errMsg) {
            ec = errCode;
            sc = errMsg;
        }

        public int getEc() {
            return ec;
        }

        public void setEc(int ec) {
            this.ec = ec;
        }

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

    }

    public GenerallyException(Reason reason, Layer layer,
            Location location) {
        this.reason = reason;
        this.location = location;
        this.layer = layer;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
