package com.iqb.consumer.asset.allocation.assetinfo.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoFormBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetObjectInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushRecord;
import com.iqb.consumer.asset.allocation.assetinfo.bean.BankCardBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.ImageBean;

/**
 * 
 * Description: 资产分配dao
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
public interface AssetInfoDao {

    /**
     * 
     * Description: 获取资产信息列表
     * 
     * @param
     * @return List<AssetInfoBean>
     * @throws
     * @Author wangxinbang Create Date: 2016年9月29日 下午4:51:29
     */
    public List<AssetInfoBean> getAssetInfoList(JSONObject objs);

    /**
     * Description 获取资产详细信息
     * 
     * @param orderId
     * @return
     */
    public AssetInfoFormBean getAssetInfoDetails(String orderId);

    /**
     * Description 获取推送详细信息
     * 
     * @param orderId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public AssetPushInfo getPushAssetInfo(Map beanMap);

    /**
     * Description 保存推送记录
     * 
     * @param record
     */
    public void insertAssetPushRecord(AssetPushRecord record);

    /**
     * Description 更新资产推送状态
     * 
     * @param info
     */
    public void updateRequestMoneyByAsset(AssetPushInfo info);

    /**
     * 
     * Description: 设置资金来源
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月30日 上午11:30:47
     */
    public Integer setFundSource(AssetInfoBean bean);

    /**
     * 
     * Description: 根据订单信息获取资产分配信息
     * 
     * @param
     * @return AssetInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2016年9月30日 上午11:41:05
     */
    public AssetInfoBean getAssetInfoByOrderId(String orderId);

    /**
     * 
     * Description: 更新资产分配信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月30日 上午11:47:11
     */
    public Integer updateFundSource(AssetInfoBean bean);

    /**
     * Description: 校验申请期数
     */
    public Map<Integer, Integer> validataRequesttimes(String orderId);

    /**
     * 
     * Description: 获取资产推送信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午2:14:24
     */
    public Map<String, Object> getAssertInfoForIQB(String orderId);

    /**
     * 
     * Description: 根据订单id获取图片列表
     * 
     * @param
     * @return List<ImageBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午5:45:04
     */
    public List<ImageBean> getImgList(String orderId);

    /**
     * 
     * Description: 删除资产分配信息
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2017年4月10日 下午4:42:39
     */
    public long deleteReqMoney(String orderId);

    /**
     * 
     * Description:根据商户号查询担保人部分信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月18日
     */
    public Map<String, String> selectCustomerBaseInfoById(String merchantId);

    /**
     * 
     * Description:根据订单号获取车牌号车架号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月21日
     */
    public Map<String, String> getCarInfoByOrderId(String orderId);

    /**
     * 
     * Description:根据订单号查询房贷信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月22日
     */
    public Map<String, Object> getHouseInfoByOrderNo(String orderNo);

    /**
     * 
     * Description:根据订单号查询借款人银行卡信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月23日
     */
    public BankCardBean selectCardInfoByOrderId(String orderId);

    /**
     * 
     * 根据条件查询资产分配标的信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public List<AssetObjectInfoBean> selectAssetObjectInfo(Map<String, Object> params);
}
