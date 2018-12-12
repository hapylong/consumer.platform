package com.iqb.consumer.asset.allocation.assetinfo.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoFormBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetObjectInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushInfo;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 资产分配服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version
 *          </pre>
 */
@SuppressWarnings("rawtypes")
public interface IAssetInfoService {

    /**
     * 
     * Description: 获取资产分配列表
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年9月29日 下午4:33:05
     */
    public PageInfo<AssetInfoBean> getAssetInfoList(JSONObject objs) throws IqbException;

    /**
     * Description: 获取订单详细信息
     * 
     * @param orderId
     * @return
     * @throws IqbException
     */
    public AssetInfoFormBean getAssetInfoDetails(String orderId) throws IqbException;

    /**
     * Description:推送资产
     * 
     * @param m
     * @throws IqbException
     */

    public void pushAssetInfoToPlatform(Map beanMap, JSONObject objs) throws IqbException;

    public void pushAssetIntoToRedis(Map beanMap, JSONObject objs) throws IqbException;

    /**
     * 校验申请期数
     */
    public Map<Integer, Integer> validataRequesttimes(String orderId) throws IqbException;

    /**
     * 
     * Description:根据订单号验证其商户是否是蒲公英行
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月20日
     */
    public boolean validatePGYMerchant(String fundSourceId, String orderId) throws IqbException;

    /**
     * 
     * Description:插入交易所债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    public long insertJysCreditInfo(JSONObject objs);

    /**
     * 
     * Description:根据手机号码获取系统用户id
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月17日
     */
    public UserBean getSysUserByRegId(Map<String, Object> params);

    /**
     * 
     * Description:推送资产到饭团
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月16日
     */
    public int pushAssetInfoToFANT(AssetPushInfo assetPushInfo, JSONObject objs) throws IqbException;

    /**
     * 
     * 根据条件查询资产分配标的信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public PageInfo<AssetObjectInfoBean> selectAssetObjectInfo(JSONObject objs);

    /**
     * 导出标的明细信息
     * 
     * @param objs
     * @param response
     * @return
     */
    public String exportAssetObjectInfo(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * Description:根据订单号获、预计放款时间取当前期数以及剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    public Map<String, Object> getAssetInfo(JSONObject objs);
}
