package com.iqb.consumer.asset.allocation.assetallocine.db.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.AssetAllocationEntity;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.OrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetAllocinePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetDivisionPojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.OrderBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.request.DivisionAssetsDetailsRequestMessage;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;

public interface AssetAllocineRepository {

    List<AssetAllocinePojo> getDivisionAssetsDetails(
            DivisionAssetsDetailsRequestMessage requestMessage);

    AssetAllocinePojo getDivisionAssetsDetailsById(long id);

    int persistAssetAllocationEntity(AssetAllocationEntity aae);

    List<AssetDivisionPojo> getDivisionAssetsPrepare(
            DivisionAssetsDetailsRequestMessage requestMessage);

    int persistChildAssets(List<BreakOrderInfoEntity> list);

    OrderInfoEntity findOrderInfoNeedBreakById(Long id);

    int updateOrderInfoBreakSuccess(OrderInfoEntity oie);

    AssetDivisionPojo getDivisionAssetsPrepareById(Long id);

    RARRequestMessage createRAR(String borderId);

    int updateChildState(BreakOrderInfoEntity boie); // 饭团成功后

    void deleteAssetAllocationEntity(String bid);

    List<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetails(JSONObject requestMessage);

    BigDecimal cgetAssetBreakDetailsAmtTotal(JSONObject requestMessage);

    List<OrderBreakDetailsInfoResponsePojo> cgetOrderBreakDetails(JSONObject requestMessage);

    void markFinshRARPushByRS9(String orderId);

    List<BreakOrderInfoEntity> getBOIEByStatusAndOid(@Param("status") short status, @Param("orderId") String orderId);

    List<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetailsList(JSONObject requestMessage);

    /**
     * 
     * Description:交易所资产分配列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    List<AssetAllocinePojo> assetAllocationList(DivisionAssetsDetailsRequestMessage requestMessage);

    /**
     * 
     * Description:更新交易所资产分配删除标识
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    int updateAssetAllocationById(JSONObject requestMessage);

    /**
     * 
     * Description:更新资产打包状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    int updateBreakOrderInfoStatusByBorderId(JSONObject requestMessage);

    /**
     * 
     * Description:更新交易所订单信息状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    int updateOrderinfoStatusById(JSONObject requestMessage);

    /**
     * 
     * Description:根据id获取breakInfo信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    RARRequestMessage getBreakInfoById(String id);
}
