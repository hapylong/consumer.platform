package com.iqb.consumer.carthree.front;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.etep.common.base.service.BaseService;

/**
 * Description: advancer
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
@Controller
public abstract class BasicService extends BaseService {

    protected enum StatusEnum {
        success, fail
    }

    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);

    protected static final String KEY_RESULT = "result";
    protected static final String KEY_SUCCESS = "success";
    protected static final String KEY_ERR_CODE = "errCode";
    protected static final String KEY_RET_USER_INFO = "retUserInfo";
    protected static final String KEY_IQB_RESULT = "iqbResult";
    protected static final String KEY_ORDER_ID = "orderId";
    protected static final String KEY_ID = "id";

    private static final int STATUS_FAIL = 2;

    private static final String ERROR_FORMAT = " ServiceCode[%s] Reason [%s] Layer [%s] Location [%s] Exception [%s]";

    public abstract int getGroupCode();

    public Object generateFailResponseMessage(Throwable e, int serviceCode) {
        logger.error("serviceCode [" + serviceCode + "]", e);
        return generateFailResponseMessage(new GenerallyException(Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A),
                serviceCode);
    }

    public Object generateFailResponseMessage(GenerallyException e, int serviceCode) {
        if (e == null) {
            e = new GenerallyException(Reason.UNKNOW_TYPE, Layer.BASIC_SERVICE, Location.BASIC_SERVICE);
        }
        String code = generateCode(serviceCode, e.getReason());
        print(code, e);
        return returnFailtrueInfo(e.getReason(), code);
    }

    public Object generateFailResponseMessage(GenerallyException e, int serviceCode, String errMsg) {
        if (e == null) {
            e = new GenerallyException(Reason.UNKNOW_TYPE, Layer.BASIC_SERVICE, Location.BASIC_SERVICE);
        }
        String code = generateCode(serviceCode, e.getReason());
        print(code, e);
        return returnFailtrueInfo(e.getReason(), code, errMsg);
    }

    public void print(String code, GenerallyException e) {
        logger.error(String.format(ERROR_FORMAT, code,
                e.getReason(), e.getLayer(), e.getLocation(), e));
    }

    protected String generateCode(int serviceCode, Reason r) {
        String gc = format(getGroupCode());
        String sc = format(serviceCode);
        String ec = format(r.getEc());
        return gc + "-" + sc + "-" + ec;
    }

    private String format(int i) {
        String is = "";
        if (i < 10) {
            is = "00" + i;
        } else if (i < 100) {
            is = "0" + i;
        } else {
            is = "" + i;
        }
        return is;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map returnFailtrueInfo(Reason e, String errCode) {
        Map result = new LinkedHashMap();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        result.put(KEY_SUCCESS, STATUS_FAIL);// 发生业务异常 - 有提示信息
        result.put(KEY_ERR_CODE, errCode);// 错误代码
        result.put(KEY_RET_USER_INFO, e.getSc());// 用户响应信息
        result.put(KEY_IQB_RESULT, linkedHashMap);
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map returnFailtrueInfo(Reason e, String errCode, String errorMsg) {
        Map result = new LinkedHashMap();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        result.put(KEY_SUCCESS, STATUS_FAIL);// 发生业务异常 - 有提示信息
        result.put(KEY_ERR_CODE, errCode);// 发生业务异常 - 有提示信息
        result.put(KEY_RET_USER_INFO, errorMsg);// 用户响应信息
        result.put(KEY_IQB_RESULT, linkedHashMap);
        return result;
    }

    class generateResponseMessage<T> {
        private Integer success; // 0成功 2表示系统内部错误
        private String errCode; // 错误代码
        private String retUserInfo; // 用户响应信息
        private T iqbResult; // 用户响应信息

        public Integer getSuccess() {
            return success;
        }

        public void setSuccess(Integer success) {
            this.success = success;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getRetUserInfo() {
            return retUserInfo;
        }

        public void setRetUserInfo(String retUserInfo) {
            this.retUserInfo = retUserInfo;
        }

        public T getIqbResult() {
            return iqbResult;
        }

        public void setIqbResult(T iqbResult) {
            this.iqbResult = iqbResult;
        }
    }

    protected LinkedHashMap<String, Object> getSuccResponse(Object obj) {
        LinkedHashMap<String, Object> responseSucc = new LinkedHashMap<>();
        if (obj == null) {
            responseSucc.put(KEY_RESULT, StatusEnum.success);
        } else {
            responseSucc.put(KEY_RESULT, obj);
        }
        return responseSucc;
    }
}
