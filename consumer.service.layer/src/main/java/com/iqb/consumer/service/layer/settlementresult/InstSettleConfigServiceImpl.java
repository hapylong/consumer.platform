/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月11日上午11:28:09 haojinlong   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.service.layer.settlementresult;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.settlementresult.InstSettleConfigBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.settlementresult.InstSettleConfigBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * @author haojinlong
 * 
 */
@Service
public class InstSettleConfigServiceImpl extends BaseService implements InstSettleConfigService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(InstSettleConfigServiceImpl.class);

    @Resource
    private InstSettleConfigBiz instSettleConfigBiz;
    @Autowired
    private MerchantBeanBiz merchantBiz;
    @Autowired
    private SysUserSession sysUserSession;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月11日
     */
    @SuppressWarnings("unchecked")
    @Override
    public int saveInstSettleConfig(JSONObject objs) throws IqbException {
        List<InstSettleConfigBean> beanlist = new ArrayList<>();
        try {
            objs = getMerchLimitObjectFor(objs);
            if (objs == null) {
                throw new IqbException(ConsumerReturnInfo.CONSUMER_INSTSETTLECONFIG_01000001);
            } else {
                List<MerchantBean> list = (List<MerchantBean>) objs.get("merList");
                if (!CollectionUtils.isEmpty(list)) {
                    InstSettleConfigBean configBean = null;
                    for (MerchantBean merchantBean : list) {
                        configBean = new InstSettleConfigBean();
                        configBean.setMerchantId(merchantBean.getId());
                        configBean.setMerchantNo(merchantBean.getMerchantNo());
                        configBean.setDescription(objs.getString("description"));
                        configBean.setStartDate(StringUtil.isNull(objs.getString("startDate")) ? null : objs
                                .getString("startDate"));
                        configBean.setFlag(objs.getString("flag"));
                        configBean.setStatus("1");
                        configBean.setOrderDate(StringUtil.isNull(objs.getString("orderDate")) ? null : objs
                                .getString("orderDate"));
                        configBean.setBizType(objs.getString("bizType"));
                        beanlist.add(configBean);
                    }
                }
            }
            return instSettleConfigBiz.saveInstSettleConfig(beanlist);
        } catch (Exception e) {
            logger.error("---报错商户开口配置报错---{}", e.getMessage());
            throw new IqbException(ConsumerReturnInfo.CONSUMER_INSTSETTLECONFIG_01000001);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月11日
     */
    @SuppressWarnings("unchecked")
    @Override
    public int updateInstSettleConfigById(JSONObject objs) {
        objs = getMerchLimitObjectFor(objs);
        List<MerchantBean> list = (List<MerchantBean>) objs.get("merList");
        if (!CollectionUtils.isEmpty(list)) {
            MerchantBean bean = list.get(0);
            objs.put("merchantId", bean.getId());
            objs.put("merchantNo", bean.getMerchantNo());
            objs.put("startDate", StringUtil.isNull(objs.getString("startDate")) ? null : objs.getString("startDate"));
            objs.put("orderDate", StringUtil.isNull(objs.getString("orderDate")) ? null : objs.getString("orderDate"));

        }
        return instSettleConfigBiz.updateInstSettleConfigById(objs);
    }

    /**
     * 根据条件获取商户代扣配置信息-带分页
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月11日
     */
    @Override
    public PageInfo<InstSettleConfigBean> selectInstSettleConfigResultByParams(JSONObject objs) {
        logger.debug("---根据条件获取商户代扣配置信息开始---{}", objs);
        // 获取商户权限的Objs
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
        if (StringUtil.isNull(merchNames) || merchNames.equals("全部商户")) {
            objs.put("merList", null);
        } else if (!merchNames.equals("全部商户")) {
            objs = getMerchLimitObject(objs);
            if (objs == null) {
                return null;
            }
        }

        logger.debug("---根据条件获取商户代扣配置信息结束---{}", objs);
        return new PageInfo<>(instSettleConfigBiz.selectInstSettleConfigResultByParams(objs));
    }

    // 获取商户权限的Objs
    private JSONObject getMerchLimitObjectFor(JSONObject objs) {
        objs.put("status", objs.getString("status"));
        // 权限树修改
        String merchNames = objs.getString("merchantNo");// 商户逗号分隔字段
        if (merchNames == null || "".equals(merchNames) || "全部商户".equals(merchNames)) {
            if ("1".equals(objs.getString("id"))) {
                objs.put("merList", null);
            } else {
                List<MerchantBean> merchList = getAllMerListByOrgCode(objs);
                if (merchList == null || merchList.size() == 0) {
                    return null;
                }
                objs.put("merList", merchList);
            }
        } else {
            objs.put("merchNames", merchNames);
            List<MerchantBean> merchList = merchantBiz.getMerCodeByMerSortNameList(objs);
            objs.put("merList", merchList);
        }

        return objs;
    }

    // 获取登录商户下所有子商户列表
    private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
        String orgCode = sysUserSession.getOrgCode();
        objs.put("id", orgCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        return list;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月16日
     */
    @SuppressWarnings("unchecked")
    @Override
    public int updateStatus(String status, JSONObject objs) {
        List<String> ids = (List<String>) objs.get("id");
        return instSettleConfigBiz.updateStatus(ids, status);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月16日
     */
    @Override
    public InstSettleConfigBean queryById(JSONObject objs) {
        return instSettleConfigBiz.queryById(objs);
    }
}
