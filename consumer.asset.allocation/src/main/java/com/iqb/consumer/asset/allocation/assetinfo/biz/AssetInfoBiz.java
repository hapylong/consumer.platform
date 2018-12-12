package com.iqb.consumer.asset.allocation.assetinfo.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoFormBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetObjectInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushRecord;
import com.iqb.consumer.asset.allocation.assetinfo.bean.BankCardBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.ImageBean;
import com.iqb.consumer.asset.allocation.assetinfo.dao.AssetInfoDao;
import com.iqb.consumer.asset.allocation.base.AssetAllocationBiz;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: 资产分配biz
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class AssetInfoBiz extends AssetAllocationBiz {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(AssetInfoBiz.class);

    @Autowired
    private AssetInfoDao assetInfoDao;

    /**
     * 
     * Description: 获取资产信息列表
     * 
     * @param
     * @return List<AssetInfoBean>
     * @throws
     * @Author wangxinbang Create Date: 2016年9月29日 下午4:49:45
     */
    public List<AssetInfoBean> getAssetInfoList(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return assetInfoDao.getAssetInfoList(objs);
    }

    /**
     * 
     * Description: 设置资金来源
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月30日 上午11:24:18
     */
    public Integer setFundSource(AssetInfoBean bean) {
        if (bean == null || StringUtil.isEmpty(bean.getOrderId()) || StringUtil.isEmpty(bean.getFundSource())) {
            logger.error("传入资金来源信息为空！！！");
            return 0;
        }
        super.setDb(0, super.MASTER);
        AssetInfoBean dbAssetInfoBean = null;
        try {
            dbAssetInfoBean = assetInfoDao.getAssetInfoByOrderId(bean.getOrderId());
        } catch (Exception e) {
            logger.error("数据库查询异常" + JSONUtil.objToJson(bean), e);
            return 0;
        }
        /**
         * 空则插入
         */
        if (dbAssetInfoBean == null) {
            return assetInfoDao.setFundSource(bean);
        }
        return assetInfoDao.updateFundSource(bean);
    }

    public AssetInfoFormBean getAssetInfoDetails(String orderId) {
        super.setDb(0, super.SLAVE);
        return assetInfoDao.getAssetInfoDetails(orderId);
    }

    /**
     * 
     * Description: 设置资金来源
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月30日 上午11:35:03
     */
    public Integer setFundSource(String orderId, String fundSource) {
        AssetInfoBean bean = new AssetInfoBean();
        bean.setOrderId(orderId);
        bean.setFundSource(fundSource);
        return this.setFundSource(bean);
    }

    /**
     * Description: 获取推送信息
     * 
     * @param orderId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public AssetPushInfo getPushAssetInfo(Map beanMap) {
        super.setDb(0, super.SLAVE);
        return assetInfoDao.getPushAssetInfo(beanMap);
    }

    /**
     * Description: 记录推送状态
     * 
     * @param record
     */
    public synchronized void insertAssetPushRecord(AssetPushRecord record) {
        super.setDb(0, super.MASTER);
        this.assetInfoDao.insertAssetPushRecord(record);
    }

    /**
     * Description: 更新资产分配状态
     * 
     * @param info
     */
    public synchronized void updateRequestMoneyByAsset(AssetPushInfo info) {
        super.setDb(0, super.MASTER);
        this.assetInfoDao.updateRequestMoneyByAsset(info);
    }

    public Map<Integer, Integer> validataRequesttimes(String orderId) {
        super.setDb(0, super.SLAVE);
        return assetInfoDao.validataRequesttimes(orderId);
    }

    /**
     * 
     * Description: 获取资产推送信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午2:14:05
     */
    public Map<String, Object> getAssertInfoForIQB(String orderId) {
        super.setDb(0, super.SLAVE);
        return assetInfoDao.getAssertInfoForIQB(orderId);
    }

    /**
     * 
     * Description: 根据订单id获取图片列表
     * 
     * @param
     * @return List<ImageBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午5:43:56
     */
    public List<ImageBean> getImgList(String orderId) {
        super.setDb(0, super.SLAVE);
        return this.assetInfoDao.getImgList(orderId);
    }

    /**
     * 
     * Description: 删除资产分配信息
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2017年4月10日 下午4:42:06
     */
    public long deleteReqMoney(String orderId) {
        setDb(0, super.MASTER);
        return this.assetInfoDao.deleteReqMoney(orderId);
    }

    /**
     * 
     * Description:根据商户号查询担保人部分信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月18日
     */
    public Map<String, String> selectCustomerBaseInfoById(String merchantId) {
        setDb(1, super.MASTER);
        return this.assetInfoDao.selectCustomerBaseInfoById(merchantId);
    }

    /**
     * 
     * Description:根据订单号获取车牌号车架号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月21日
     */
    public Map<String, String> getCarInfoByOrderId(String orderId) {
        setDb(1, super.MASTER);
        return this.assetInfoDao.getCarInfoByOrderId(orderId);
    }

    /**
     * 
     * Description:根据订单号查询房贷信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月22日
     */
    public Map<String, Object> getHouseInfoByOrderNo(String orderNo) {
        setDb(1, super.MASTER);
        return this.assetInfoDao.getHouseInfoByOrderNo(orderNo);
    }

    /**
     * 
     * Description:根据订单号查询借款人银行卡信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月23日
     */
    public BankCardBean selectCardInfoByOrderId(String orderId) {
        setDb(1, super.MASTER);
        return this.assetInfoDao.selectCardInfoByOrderId(orderId);
    }

    /**
     * 
     * 根据条件查询资产分配标的信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public List<AssetObjectInfoBean> selectAssetObjectInfo(JSONObject objs) {
        setDb(1, super.MASTER);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<AssetObjectInfoBean> list = assetInfoDao.selectAssetObjectInfo(objs);
        if (!CollectionUtils.isEmpty(list)) {
            for (AssetObjectInfoBean bean : list) {
                BigDecimal sbAmt = bean.getApplyAmt() != null ? bean.getApplyAmt() : BigDecimal.ZERO;
                sbAmt = sbAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
                bean.setApplyAmt(sbAmt);
            }
        }
        return list;
    }
}
