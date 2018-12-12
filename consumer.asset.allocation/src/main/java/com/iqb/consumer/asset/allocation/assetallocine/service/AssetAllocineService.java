package com.iqb.consumer.asset.allocation.assetallocine.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetAllocinePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetDivisionPojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.OrderBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;
import com.iqb.etep.common.exception.IqbException;

public interface AssetAllocineService {

    public PageInfo<AssetAllocinePojo> getDivisionAssetsDetails(
            JSONObject requestMessage);

    public AssetAllocinePojo getDivisionAssetsDetailsById(long id);

    public int persistAssetDetails(JSONObject requestMessage)
            throws InvalidRARException;

    public PageInfo<AssetDivisionPojo> getDivisionAssetsPrepare(
            JSONObject requestMessage);

    public int persistChildAssets(JSONObject requestMessage) throws IqbException;

    public List<BreakOrderInfoEntity> childAssetsPrepare(
            JSONObject requestMessage);

    public int persistChildrenAssets(JSONObject requestMessage) throws IqbException;

    public AssetDivisionPojo getDivisionAssetsPrepareById(Long id);

    public RARRequestMessage createRAR(String borderId) throws InvalidRARException;

    public PageInfo<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetails(JSONObject requestMessage);

    public BigDecimal cgetAssetBreakDetailsAmtTotal(JSONObject requestMessage);

    public PageInfo<OrderBreakDetailsInfoResponsePojo> cgetOrderBreakDetails(JSONObject requestMessage);

    public String exportXlsx(JSONObject requestMessage, HttpServletResponse response);

    public String exportXlsxAssetBreak(JSONObject requestMessage, HttpServletResponse response);

    void chatWithRARService(RARRequestMessage rm) throws InvalidRARException;

    /**
     * 
     * Description:交易所资产分配列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public PageInfo<AssetAllocinePojo> assetAllocationList(JSONObject obj);

    /**
     * 
     * Description:删除资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public int deleteAssetAllocationInfo(JSONObject obj);
}
