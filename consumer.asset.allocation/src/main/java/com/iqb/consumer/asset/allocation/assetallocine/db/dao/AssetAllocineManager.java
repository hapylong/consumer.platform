package com.iqb.consumer.asset.allocation.assetallocine.db.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.AssetAllocationEntity;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.OrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetAllocinePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetDivisionPojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.OrderBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.request.DivisionAssetsDetailsRequestMessage;
import com.iqb.consumer.asset.allocation.assetallocine.request.PersistChildrenAssetRequestMessage;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

@Component
public class AssetAllocineManager extends BaseBiz {

    protected static final Logger logger = LoggerFactory.getLogger(AssetAllocineManager.class);

    @Autowired
    private AssetAllocineRepository assetAllocineRepository;

    public PageInfo<AssetAllocinePojo> getDivisionAssetsDetails(JSONObject obj) {
        super.setDb(0, super.MASTER);
        PageHelper.startPage(getPagePara(obj));
        DivisionAssetsDetailsRequestMessage requestMessage =
                JSONUtil.toJavaObject(obj, DivisionAssetsDetailsRequestMessage.class);
        return new PageInfo<AssetAllocinePojo>(
                assetAllocineRepository
                        .getDivisionAssetsDetails(requestMessage));
    }

    public AssetAllocinePojo getDivisionAssetsDetailsById(long id) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository
                .getDivisionAssetsDetailsById(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public int persistAssetAllocationEntity(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        AssetAllocationEntity aae = JSONObject.toJavaObject(requestMessage,
                AssetAllocationEntity.class);
        aae.setCreateTime(new Date());
        return assetAllocineRepository.persistAssetAllocationEntity(aae);
    }

    public PageInfo<AssetDivisionPojo> getDivisionAssetsPrepare(
            JSONObject obj) {
        super.setDb(0, super.MASTER);
        PageHelper.startPage(getPagePara(obj));
        DivisionAssetsDetailsRequestMessage requestMessage = JSONUtil
                .toJavaObject(obj, DivisionAssetsDetailsRequestMessage.class);
        return new PageInfo<AssetDivisionPojo>(
                assetAllocineRepository
                        .getDivisionAssetsPrepare(requestMessage));
    }

    private static final Short BREAK_ORDER_SUCCESS = 5;

    private static final String ERROR_MSG_PERSIST_CHILD_ASSETS =
            "com.iqb.consumer.asset.allocation.assetallocine.db.dao.AssetAllocineManager[persistChildAssets] : ";

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int persistChildAssets(PersistChildrenAssetRequestMessage pcar, Long id)
            throws IqbException {
        if (id == null) {
            logger.error("com.iqb.consumer.asset.allocation.assetallocine.db.dao.AssetAllocineManager[persistChildAssets] : id is empty.");
        }
        super.setDb(0, super.MASTER);
        OrderInfoEntity oie = assetAllocineRepository.findOrderInfoNeedBreakById(id);
        if (oie == null) {
            logger.error(ERROR_MSG_PERSIST_CHILD_ASSETS + "oie is not exist.");
            throw new IqbException(CommonReturnInfo.BASE00090004);
        }
        Set<String> CHECK_BROWSER_CACHE = new HashSet<String>();
        BigDecimal CHECK_ORDER_AMT = BigDecimal.ZERO;
        for (BreakOrderInfoEntity boie : pcar.getChildren()) {
            if (!boie.checkEntity()) {
                logger.error(ERROR_MSG_PERSIST_CHILD_ASSETS
                        + "boie check error.");
                throw new IqbException(CommonReturnInfo.BASE00090004);
            }
            if (CHECK_BROWSER_CACHE.contains(boie.getBOrderId())) {
                logger.error(ERROR_MSG_PERSIST_CHILD_ASSETS
                        + "browser cache check error.");
                throw new IqbException(CommonReturnInfo.BASE00090004);
            }
            CHECK_BROWSER_CACHE.add(boie.getBOrderId());
            CHECK_ORDER_AMT = CHECK_ORDER_AMT.add(boie.getBOrderAmt());
        }
        if (oie.getOrderAmt().compareTo(CHECK_ORDER_AMT) != 0) {
            logger.error(ERROR_MSG_PERSIST_CHILD_ASSETS
                    + "order amt check error.");
            throw new IqbException(CommonReturnInfo.BASE00090004);
        }
        int result = assetAllocineRepository.persistChildAssets(pcar.getChildren());
        if (result > 0) {
            oie.setUpdateTime(new Date());
            oie.setStatus(BREAK_ORDER_SUCCESS);
            oie.setProName(pcar.getProName());
            oie.setBakOrgan(pcar.getBakOrgan());
            oie.setUrl(pcar.getUrl());
            oie.setProDetail(pcar.getProDetail());
            oie.setTranCondition(pcar.getTranCondition());
            oie.setSafeWay(pcar.getSafeWay());
            oie.setOrderName(pcar.getProName());
            return assetAllocineRepository.updateOrderInfoBreakSuccess(oie);
        }
        return result;
    }

    public List<BreakOrderInfoEntity> childAssetsPrepare(
            JSONObject requestMessage) {
        /*
         * BreakRegulationRequestMessage brrm = return null;
         */
        return null;
    }

    public AssetDivisionPojo getDivisionAssetsPrepareById(Long id) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.getDivisionAssetsPrepareById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RARRequestMessage createRAR(String borderId) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.createRAR(borderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateChildState(BreakOrderInfoEntity boie) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.updateChildState(boie);
    }

    public void deleteAssetAllocationEntity(String bid) {
        super.setDb(0, super.MASTER);
        assetAllocineRepository.deleteAssetAllocationEntity(bid);
    }

    public PageInfo<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetails(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return new PageInfo<AssetBreakDetailsInfoResponsePojo>(
                assetAllocineRepository
                        .cgetAssetBreakDetails(requestMessage));
    }

    public BigDecimal cgetAssetBreakDetailsAmtTotal(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return assetAllocineRepository
                .cgetAssetBreakDetailsAmtTotal(requestMessage);
    }

    public PageInfo<OrderBreakDetailsInfoResponsePojo> cgetOrderBreakDetails(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return new PageInfo<OrderBreakDetailsInfoResponsePojo>(
                assetAllocineRepository
                        .cgetOrderBreakDetails(requestMessage));
    }

    /**
     * 
     * Description: 通过RS9 [*riskstatus = 9*] 标记该订单已全部推送
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月13日 下午5:54:53
     */
    public void markFinshRARPushByRS9(String orderId) {
        super.setDb(0, super.SLAVE);
        assetAllocineRepository.markFinshRARPushByRS9(orderId);
    }

    /**
     * 
     * Description: 根据status查找list， status=2代表资金已推送
     * 
     * @param
     * @return List<BreakOrderInfoEntity>
     * @throws
     * @Author adam Create Date: 2017年6月13日 下午5:55:12
     */
    public List<BreakOrderInfoEntity> getBOIEByStatusAndOid(short status, String orderId) {
        super.setDb(0, super.SLAVE);
        return assetAllocineRepository.getBOIEByStatusAndOid(status, orderId);
    }

    public List<OrderBreakDetailsInfoResponsePojo> cgetOrderBreakDetailsList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return assetAllocineRepository
                .cgetOrderBreakDetails(requestMessage);
    }

    public List<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetailsList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return assetAllocineRepository
                .cgetAssetBreakDetailsList(requestMessage);
    }

    /**
     * 
     * Description:交易所资产分配列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public PageInfo<AssetAllocinePojo> assetAllocationList(JSONObject obj) {
        super.setDb(0, super.MASTER);
        PageHelper.startPage(getPagePara(obj));
        DivisionAssetsDetailsRequestMessage requestMessage =
                JSONUtil.toJavaObject(obj, DivisionAssetsDetailsRequestMessage.class);
        return new PageInfo<AssetAllocinePojo>(
                assetAllocineRepository
                        .assetAllocationList(requestMessage));
    }

    /**
     * 
     * Description:更新交易所资产分配删除标识
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public int updateAssetAllocationById(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.updateAssetAllocationById(requestMessage);
    }

    /**
     * 
     * Description:更新资产打包状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public int updateBreakOrderInfoStatusByBorderId(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.updateBreakOrderInfoStatusByBorderId(requestMessage);
    }

    /**
     * 
     * Description:更新交易所订单信息状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public int updateOrderinfoStatusById(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        return assetAllocineRepository.updateOrderinfoStatusById(requestMessage);
    }

    /**
     * 
     * Description:根据id获取breakInfo信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    public RARRequestMessage getBreakInfoById(String id) {
        super.setDb(1, super.MASTER);
        return assetAllocineRepository.getBreakInfoById(id);
    }
}
