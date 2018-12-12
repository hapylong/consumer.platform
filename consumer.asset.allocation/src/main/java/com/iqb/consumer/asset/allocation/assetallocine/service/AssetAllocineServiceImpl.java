package com.iqb.consumer.asset.allocation.assetallocine.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.asset.allocation.assetallocine.db.dao.AssetAllocineManager;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity.STATUS_ENUM;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException.Reason;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetAllocinePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetDivisionPojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.OrderBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.request.PersistChildrenAssetRequestMessage;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;
import com.iqb.consumer.asset.allocation.base.config.AssetAllocationfig;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.store.service.ICustomerStoreService;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.biz.wf.OrderOtherInfoBiz;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.JSONUtil;

@Service
public class AssetAllocineServiceImpl implements AssetAllocineService {

    @Autowired
    private AssetAllocineManager assetAllocineManager;

    @Autowired
    private RedisPlatformDao redisPlatformDao;

    @Resource(name = "assetAllocationfig")
    private AssetAllocationfig assetAllocationfig;

    @Resource
    private OrderOtherInfoBiz orderOtherInfoBiz;

    @Autowired
    private ICustomerStoreService customerStoreServiceImpl;

    private static final String LOG_PARTTERN =
            "com.iqb.consumer.asset.allocation.assetallocine.service.AssetAllocineServiceImpl";

    protected static final Logger logger = LoggerFactory
            .getLogger(AssetAllocineServiceImpl.class);

    @Override
    public PageInfo<AssetAllocinePojo> getDivisionAssetsDetails(
            JSONObject requestMessage) {
        return assetAllocineManager.getDivisionAssetsDetails(requestMessage);
    }

    @Override
    public AssetAllocinePojo getDivisionAssetsDetailsById(long id) {
        return assetAllocineManager.getDivisionAssetsDetailsById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {InvalidRARException.class,
            Exception.class})
    public int persistAssetDetails(JSONObject requestMessage)
            throws InvalidRARException {
        // 保存资产分配信息到JYS_ASSETALLOCATION
        requestMessage.put("deleteFlag", 0);
        int result = assetAllocineManager.persistAssetAllocationEntity(requestMessage);
        if (result != 1) {
            throw new InvalidRARException(Reason.DB_ERROR);
        }
        RARRequestMessage rm = createRAR(requestMessage.getString("id"));

        if (rm == null) {
            throw new InvalidRARException(
                    Reason.INVALID_PARAMS);
        }

        requestMessage.put("orderId", rm.getJysOrderId());
        IqbCustomerStoreInfoEntity icse = customerStoreServiceImpl.getCreditInfoById(requestMessage);

        rm.setCardNum(icse.getCreditorBankNo());
        rm.setBankName(icse.getCreditorBankName());
        rm.setPhone(icse.getCreditorPhone());
        rm.setUserName(icse.getCreditorName());
        rm.setIdCard(icse.getCreditorIdNo());
        rm.setChannal(requestMessage.getString("channel"));

        chatWithRARService(rm);
        String orderId = rm.getPackageId();
        List<BreakOrderInfoEntity> boies =
                assetAllocineManager.getBOIEByStatusAndOid(STATUS_ENUM.CHAT_WITH_RAR_SUCCESS.intValue(), orderId);
        if (boies == null || boies.isEmpty()) {
            /** 通过RS9 [riskstatus = 9] 标记该订单已全部推送 **/
            assetAllocineManager.markFinshRARPushByRS9(orderId);
        }
        return result;
    }

    @Override
    public PageInfo<AssetDivisionPojo> getDivisionAssetsPrepare(
            JSONObject requestMessage) {
        return assetAllocineManager.getDivisionAssetsPrepare(requestMessage);
    }

    private static final String PARENT_ID = "id";

    @Override
    public int persistChildAssets(JSONObject requestMessage) throws IqbException {
        PersistChildrenAssetRequestMessage pcar =
                JSONUtil.toJavaObject(requestMessage, PersistChildrenAssetRequestMessage.class);
        if (pcar == null || !pcar.checkRequest()) {
            throw new IqbException(CommonReturnInfo.BASE00090004);
        }
        Long id = requestMessage.getLong(PARENT_ID);
        /** 交易所订单表projectName为null的处理 **/
        if (StringUtil.isEmpty(pcar.getProName())) {
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", pcar.getChildren().get(0).getOrderId());
            OrderOtherInfo orderOtherInfo = orderOtherInfoBiz.selectOne(params);
            if (orderOtherInfo != null && StringUtil.isNotEmpty(orderOtherInfo.getProjectName())) {
                pcar.setProName(orderOtherInfo.getProjectName());
            }
        }
        return assetAllocineManager.persistChildAssets(pcar, id);
    }

    private static Long timeRecord;
    private static final int ONE_MINUTE = 60 * 1000;
    private static Map<String, List<String>> parentId_childIds_map = new HashMap<>();
    private static Map<String, BreakOrderInfoEntity> childId_entity_map = new HashMap<>();

    @Override
    public synchronized List<BreakOrderInfoEntity> childAssetsPrepare(
            JSONObject requestMessage) {
        if (timeRecord == null || (System.currentTimeMillis() - timeRecord) > 30 * ONE_MINUTE) {
            parentId_childIds_map = new HashMap<>();
            childId_entity_map = new HashMap<>();
        }

        List<BreakOrderInfoEntity> result = assetAllocineManager
                .childAssetsPrepare(requestMessage);
        // 按照模式拆分，拆分完毕保存缓存
        timeRecord = System.currentTimeMillis();
        return result;
    }

    @Override
    public int persistChildrenAssets(JSONObject requestMessage)
            throws IqbException {
        PersistChildrenAssetRequestMessage pcar =
                JSONUtil.toJavaObject(requestMessage, PersistChildrenAssetRequestMessage.class);
        if (pcar == null || pcar.getChildren() == null
                || pcar.getChildren().isEmpty()) {
            throw new IqbException(CommonReturnInfo.BASE00000001);
        }
        return 0;
    }

    @Override
    public AssetDivisionPojo getDivisionAssetsPrepareById(Long id) {
        return assetAllocineManager.getDivisionAssetsPrepareById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    // 如果其他bean调用这个方法,在其他bean中声明事务,那就用事务.如果其他bean没有声明事务,那就不用事务.
    public RARRequestMessage createRAR(String borderId)
            throws InvalidRARException {
        try {
            if (StringUtil.isEmpty(borderId)) {
                logger.error(LOG_PARTTERN + "[createRAR] borderId is empty.");
                throw new InvalidRARException(Reason.INVALID_B_ORDER_ID);
            }
            return assetAllocineManager.createRAR(borderId);
        } catch (Throwable e) {
            logger.error(LOG_PARTTERN + "[createRAR]", e);
            throw new InvalidRARException(Reason.DB_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void chatWithRARService(RARRequestMessage rm)
            throws InvalidRARException {
        synchronized (this) {
            if (!rm.checkEntity()) {
                logger.error(LOG_PARTTERN
                        + "[chatWithRARService] invliad entity.");
                throw new InvalidRARException(Reason.INVALID_PARAMS);
            }
            /** 通过订单号查询债权人信息%FINANCE-2002 资产推标饭团的时候将五要素获取发送饭团% **/
            this.chatWithRARServiceInterval(rm);
            BreakOrderInfoEntity boie = new BreakOrderInfoEntity();
            boie.setUpdateTime(new Date());
            boie.setId(rm.getProjectId());
            boie.setStatus(STATUS_ENUM.CHAT_WITH_RAR_SUCCESS.intValue());
            int result = assetAllocineManager.updateChildState(boie);
            if (result != 1) {
                logger.error(LOG_PARTTERN
                        + "[chatWithRARService] db error. result [" + result + "]");
                throw new InvalidRARException(Reason.DB_ERROR);
            }
        }
    }

    private void chatWithRARServiceInterval(RARRequestMessage rm)
            throws InvalidRARException {
        try {
            Map<String, Object> map = rm.createMapString();
            logger.info("推送给饭团数据为:" + JSONObject.toJSONString(map));
            redisPlatformDao.leftPush(
                    assetAllocationfig.getRedisQueueAssetAllocationKey(),
                    JSONUtil.objToJson(map));
        } catch (Throwable e) {
            throw new InvalidRARException(Reason.INVALID_RESPONSE);
        }
    }

    @Override
    public PageInfo<AssetBreakDetailsInfoResponsePojo> cgetAssetBreakDetails(JSONObject requestMessage) {
        return assetAllocineManager.cgetAssetBreakDetails(requestMessage);
    }

    @Override
    public BigDecimal cgetAssetBreakDetailsAmtTotal(JSONObject requestMessage) {
        return assetAllocineManager.cgetAssetBreakDetailsAmtTotal(requestMessage);
    }

    @Override
    public PageInfo<OrderBreakDetailsInfoResponsePojo> cgetOrderBreakDetails(JSONObject requestMessage) {
        return assetAllocineManager.cgetOrderBreakDetails(requestMessage);
    }

    @Override
    public String exportXlsx(JSONObject requestMessage, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            List<OrderBreakDetailsInfoResponsePojo> xlsx =
                    assetAllocineManager.cgetOrderBreakDetailsList(requestMessage);
            // 2.导出excel表格
            HSSFWorkbook workbook = convertXlsx(xlsx);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
            return "fail";
        }
        return "success";
    }

    @Override
    public String exportXlsxAssetBreak(JSONObject requestMessage, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            List<AssetBreakDetailsInfoResponsePojo> xlsx =
                    assetAllocineManager.cgetAssetBreakDetailsList(requestMessage);
            // 2.导出excel表格
            HSSFWorkbook workbook = convertXlsxAssetBreak(xlsx);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
            return "fail";
        }
        return "success";
    }

    private HSSFWorkbook convertXlsx(List<OrderBreakDetailsInfoResponsePojo> list) {
        String[] header =
        {"订单号", "姓名", "手机号", "订单名称", "订单时间", "金额", "期数", "方案", "首付", "保证金", "服务费", "月供", "上收息", "上收月供", "预付款",
                "收取方式", "上标时间", "商户号"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("订单分期");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            hr = hs.createRow(i + 1);
            xu.append(hr, 0, list.get(i).getOrderId());
            xu.append(hr, 1, list.get(i).getRealName());
            xu.append(hr, 2, list.get(i).getRegId());
            xu.append(hr, 3, list.get(i).getOrderName());
            xu.append(hr, 4, list.get(i).getCreateTime());
            xu.append(hr, 5, list.get(i).getOrderAmt());
            xu.append(hr, 6, list.get(i).getOrderItems());
            xu.append(hr, 7, list.get(i).getPlanFullName());
            xu.append(hr, 8, list.get(i).getDownPayment());
            xu.append(hr, 9, list.get(i).getMargin());
            xu.append(hr, 10, list.get(i).getServiceFee());
            xu.append(hr, 11, list.get(i).getMonthInterest());
            xu.append(hr, 12, list.get(i).getFeeAmount());
            xu.append(hr, 13, list.get(i).getTakePayment());
            xu.append(hr, 14, list.get(i).getPreAmt());
            xu.append(hr, 15, list.get(i).getChargeWay() == 0 ? "线上收取" : "线下收取");
            xu.append(hr, 16, list.get(i).getSbTime());
            xu.append(hr, 17, list.get(i).getMerchantShortName());
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);// 订单ID
        hs.setColumnWidth(1, 2000);// 姓名
        hs.setColumnWidth(2, 3500);// 手机号
        hs.setColumnWidth(3, 10000);// 订单名称
        hs.setColumnWidth(4, 4000);// 订单时间
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    private HSSFWorkbook convertXlsxAssetBreak(List<AssetBreakDetailsInfoResponsePojo> list) {
        String[] header =
        {"订单号", "手机号", "姓名", "身份证号", "订单名称", "项目名称", "交易所备案编号", "资金来源", "金额", "保证金", "交易所（全称）", "募集机构", "通道费率",
                "业务类型", "商户",
                "到期日", "订单时间",
                "付息日", "年化利率", "分期方案ID", "方案名称", "开户银行", "挂牌链接", "担保公司", "摘牌机构", "期数"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("订单分期");

        HSSFRow hr = hs.createRow(0);

        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            hr = hs.createRow(i + 1);
            xu.append(hr, 0, list.get(i).getOrderId());
            xu.append(hr, 1, list.get(i).getRegId());
            xu.append(hr, 2, list.get(i).getRealName());
            xu.append(hr, 3, list.get(i).getIdNo());
            xu.append(hr, 4, list.get(i).getProName());
            xu.append(hr, 5, list.get(i).getProjectName1());
            xu.append(hr, 6, list.get(i).getRecordNum());
            xu.append(hr, 7, getChannelName(list.get(i).getChannel()));
            xu.append(hr, 8, list.get(i).getOrderAmt());
            xu.append(hr, 9, list.get(i).getMargin());
            xu.append(hr, 10, list.get(i).getBakOrgan());
            xu.append(hr, 11, list.get(i).getRaiseInstitutions());
            xu.append(hr, 12, list.get(i).getRecharge());
            xu.append(hr, 13, list.get(i).getBizType());
            xu.append(hr, 14, list.get(i).getMerchantNo());
            xu.append(hr, 15, list.get(i).getExpireDate());
            xu.append(hr, 16, list.get(i).getCreatetime());
            xu.append(hr, 17, list.get(i).getPaymentDate());
            xu.append(hr, 18, list.get(i).getFeeratio() == null ? null : list.get(i).getFeeratio() * 12);
            xu.append(hr, 19, list.get(i).getPlanId());
            xu.append(hr, 20, list.get(i).getPlanShortName());
            xu.append(hr, 21, list.get(i).getBankName());
            xu.append(hr, 22, list.get(i).getUrl());
            xu.append(hr, 23, list.get(i).getGuaranteeInstitution());
            xu.append(hr, 24, list.get(i).getDelistingMechanism());
            xu.append(hr, 25, list.get(i).getOrderItems());
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);// 订单ID
        hs.setColumnWidth(1, 2000);// 姓名
        hs.setColumnWidth(2, 3500);// 手机号
        hs.setColumnWidth(3, 10000);// 订单名称
        hs.setColumnWidth(4, 4000);// 订单时间
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    private Object getChannelName(Integer channel) {
        if (channel == null) {
            return "";
        }
        switch (channel) {
            case 1:
                return "饭团";
            case 2:
                return "金鳞";
        }
        return channel;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年2月27日
     */
    @Override
    public PageInfo<AssetAllocinePojo> assetAllocationList(JSONObject obj) {
        if (obj.getString("fund_source") != null) {
            obj.put("channel", obj.getString("fund_source"));
        }

        return assetAllocineManager.assetAllocationList(obj);
    }

    /**
     * 删除资产分配记录
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年2月27日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {InvalidRARException.class,
            Exception.class})
    public int deleteAssetAllocationInfo(JSONObject obj) {
        int resultValue = 0;

        if (obj.getString("breakId") != null && obj.getString("breakId").equals("0")) {
            resultValue = -1;
        } else {
            resultValue = assetAllocineManager.updateAssetAllocationById(obj);

            resultValue += assetAllocineManager.updateBreakOrderInfoStatusByBorderId(obj);
            String id = obj.getString("breakId");
            RARRequestMessage message = assetAllocineManager.getBreakInfoById(id);
            obj.put("id", message.getPackageId());
            resultValue += assetAllocineManager.updateOrderinfoStatusById(obj);
        }
        return resultValue;
    }
}
