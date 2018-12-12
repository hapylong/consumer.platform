package com.iqb.consumer.service.layer.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.etep.common.utils.SysUserSession;

import jodd.util.StringUtil;

@Component
public class BaseService {

    @Autowired
    protected MerchantBeanBiz merchantBiz;
    @Autowired
    private SysUserSession sysUserSession;

    // 获取登录商户下所有子商户列表
    private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
        String orgCode = sysUserSession.getOrgCode();
        objs.put("id", orgCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        return list;
    }

    // 获取登录商户下所有子商户列表
    private List<String> getAllMerchantNosByOrgCode(JSONObject objs) {
        String orgCode = sysUserSession.getOrgCode();
        objs.put("id", orgCode);
        List<String> list = merchantBiz.getAllMerchantNosByOrgCode(objs);
        return list;
    }

    private static final String M_LIST = "merList";
	private static final String IQB_MARK = "1";

    public JSONObject getMerchLimitObject(JSONObject jo) {
        // 权限树修改
        String mns = jo.getString("merchNames");// 商户逗号分隔字段
        if (StringUtil.isEmpty(mns) || "全部商户".equals(mns)) {
			if (IQB_MARK.equals(jo.getString("id"))) {
                jo.put(M_LIST, null);
            } else {
                List<MerchantBean> ml = getAllMerListByOrgCode(jo);
                if (ml == null || ml.isEmpty()) {
                    return null;
                }
                jo.put(M_LIST, ml);
            }
        } else {
            List<MerchantBean> ml = merchantBiz.getMerCodeByMerSortNameList(jo);
            jo.put(M_LIST, ml);
        }
        return jo;
    }
    
    public void getMerchantList(JSONObject jo) throws DevDefineErrorMsgException {
        // 权限树修改
        String mns = jo.getString("merchNames");// 商户逗号分隔字段
        if (StringUtil.isEmpty(mns) || "全部商户".equals(mns)) {
			if (IQB_MARK.equals(jo.getString("id"))) {
                jo.put(M_LIST, null);
            } else {
                List<MerchantBean> ml = getAuthorityMerListByOrgCode(jo);
                if (ml == null || ml.isEmpty()) {
                    return;
                }
                jo.put(M_LIST, ml);
            }
        } else {
            List<MerchantBean> ml = merchantBiz.getMerCodeByMerSortNameList(jo);
            jo.put(M_LIST, ml);
        }
    }
    
    protected void getMerchantNosNew(JSONObject jo) {
        String mns = jo.getString("merchNames");// 商户逗号分隔字段
        if (StringUtil.isEmpty(mns) || "全部商户".equals(mns)) {
			if (IQB_MARK.equals(jo.getString("id"))) {
                jo.put(M_LIST, null);
            } else {
                List<String> ml = getAllMerchantNosByOrgCode(jo);
                if (ml == null || ml.isEmpty()) {
                    return;
                }
                jo.put(M_LIST, ml);
            }
        } else {
            List<String> ml = merchantBiz.getAllMerchantNosByNames(jo);
            jo.put(M_LIST, ml);
        }
    }

    private List<MerchantBean> getAuthorityMerListByOrgCode(JSONObject objs) throws DevDefineErrorMsgException {

        String orgCode = sysUserSession.getOrgCode();
        if (StringUtil.isEmpty(orgCode)) {
            throw new DevDefineErrorMsgException("服务器繁忙，请稍后重试");
        }
        IqbCustomerPermissionEntity icpe = merchantBiz.getICPEByOrgCode(orgCode);
        if (icpe != null && !icpe.getAuthorityTreeList().isEmpty()) {
            return merchantBiz.getMBListByATList(icpe.getAuthorityTreeList());
        } else {
            throw new DevDefineErrorMsgException("商户权限未配置，请联系管理员");
        }
    }
}
