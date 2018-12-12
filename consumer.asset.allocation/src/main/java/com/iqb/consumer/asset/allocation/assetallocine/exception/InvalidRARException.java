package com.iqb.consumer.asset.allocation.assetallocine.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.IReturnInfo;
import com.iqb.etep.common.exception.IqbException;

public class InvalidRARException extends IqbException {
    public InvalidRARException(IReturnInfo returnInfo) {
        super(returnInfo);
        try {
            Reason r = Reason.valueOf(returnInfo.getRetFactInfo());
            setReason(r);
        } catch (Exception e) {
            setReason(Reason.UNKNOWN_ERROR);
        }

    }

    /*
     * public InvalidRARException(IReturnInfo returnInfo, Throwable throwable) { super(returnInfo,
     * throwable); }
     */

    private static final long serialVersionUID = 1L;

    public static enum Reason implements IReturnInfo {
        INVALID_B_ORDER_ID("00010011", "INVALID_B_ORDER_ID", "处理失败"),
        INVALID_PARAMS("00010012", "INVALID_PARAMS", "请联系管理员"),
        INVALID_RESPONSE("00010012", "INVALID_RESPONSE", "处理失败"),
        DB_ERROR("00010012", "DB_ERROR", "系统异常，请联系管理员"),
        UNKNOWN_ERROR("00000099", "UNKNOWN_ERROR", "不存在的返回码，请联系管理员");

        private Reason(String retCode, String retFactInfo, String retUserInfo) {
            this.retCode = retCode;
            this.retFactInfo = retFactInfo;
            this.retUserInfo = retUserInfo;
        }

        /** 响应代码 **/
        private String retCode = "";

        /** 提示信息-用户提示信息 **/
        private String retUserInfo = "";

        /** 响应码含义-实际响应信息 **/
        private String retFactInfo = "";

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetUserInfo() {
            return retUserInfo;
        }

        public void setRetUserInfo(String retUserInfo) {
            this.retUserInfo = retUserInfo;
        }

        public String getRetFactInfo() {
            return retFactInfo;
        }

        public void setRetFactInfo(String retFactInfo) {
            this.retFactInfo = retFactInfo;
        }

        private static Map<String, Reason> reasons = new HashMap<String, Reason>();

        /** 将所有枚举缓存 */
        static {
            EnumSet<Reason> rs = EnumSet.allOf(Reason.class);
            for (Reason r : rs) {
                reasons.put(r.getRetCode(), r);
            }
        }

        @Override
        public IReturnInfo getReturnCodeInfoByCode(IReturnInfo returnInfo) {
            if (reasons.get(returnInfo.getRetCode()) != null) {
                return reasons.get(returnInfo.getRetCode());
            } else {
                return Reason.UNKNOWN_ERROR;
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(Reason.DB_ERROR.getRetFactInfo());
        System.out.println(Reason.DB_ERROR.getRetCode());
        System.out.println(Reason.DB_ERROR.getRetUserInfo());
    }

    private String borderId;
    private Reason reason;

    /*
     * public InvalidRARException(String borderId, Reason reason) {
     * super(String.format("Invalid RAR on borderId[%s] for reason[%s]", borderId,
     * reason.toString())); setBorderId(borderId); setReason(reason); }
     */

    public String getBorderId() {
        return borderId;
    }

    public void setBorderId(String borderId) {
        this.borderId = borderId;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

}
