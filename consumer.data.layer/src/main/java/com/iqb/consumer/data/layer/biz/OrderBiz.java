package com.iqb.consumer.data.layer.biz;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerCarInfo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerOrderInfo;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderOtherAmtEntity;
import com.iqb.consumer.data.layer.bean.order.OrderAmtDetailBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.bean.order.mplan.InstPlanEntity;
import com.iqb.consumer.data.layer.bean.pay.ChatFinanceToRefundRequestMessage;
import com.iqb.consumer.data.layer.dao.OrderDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.StringUtil;

@Component
public class OrderBiz extends BaseBiz {
    private static Logger log = LoggerFactory.getLogger(OrderBiz.class);
    @Resource
    private OrderDao orderDao;
    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    /** 序号格式 **/
    private static final String STR_FORMAT_IMPORT = "0000";

    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    @Autowired
    private RedisPlatformDao redisPlatformDao;

    public int saveOrderInfo(OrderBean orderBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.saveOrderInfo(orderBean);
    }

    /**
     * 保存详细的订单信息
     * 
     * @param orderBean
     * @return
     */
    public int saveFullOrderInfo(OrderBean orderBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.saveFullOrderInfo(orderBean);
    }

    /**
     * 修改预付款信息
     * 
     * @param params
     * @return
     */
    public int updatePreInfo(Map<String, Object> params) {
        super.setDb(0, super.MASTER);
        return orderDao.updatePreInfo(params);
    }

    /**
     * 保存流程ID
     * 
     * @param params
     * @return
     */
    public int updateProcInstId(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return orderDao.updateProcInstId(params);
    }

    public int updateWfStatus(String orderId, String wfStatus, String preAmtStatus) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        if (StringUtil.isEmpty(preAmtStatus)) {
            return orderDao.updateWfStatus(orderId, wfStatus);
        }
        return orderDao.updateWfStatus2(orderId, wfStatus, preAmtStatus);
    }

    /**
     * 
     * Description: 质押车风控状态修改
     * 
     * @param backFlag
     * @param preAmt
     * 
     * @param
     * @return int
     * @throws @Author wangxinbang Create Date: 2017年4月11日 下午4:01:02
     */
    public int updatePledgeWfStatus(String orderId, String riskStatus, String wfStatus, String preAmtStatus,
            String specialTime, String backFlag, String preAmt, String showContract) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updatePledgeWfStatus(orderId, riskStatus, wfStatus, preAmtStatus, specialTime, backFlag,
                preAmt, showContract);
    }

    /**
     * 通过订单号查询订单信息
     * 
     * @param orderId
     * @return
     */
    public OrderBean selByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        return orderDao.selByOrderId(orderId);
    }

    public int updateStatus(String orderId, String status) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updateStatus(orderId, status);
    }

    public int updatePreStatusByNO(Map<String, Object> params) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updatePreStatusByNO(params);
    }

    public int updateStatusByIDNO(Map<String, Object> params) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updateStatusByIDNO(params);
    }

    public int updateOrderInfo(OrderBean orderBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updateOrderInfo(orderBean);
    }

    public int newUpdateOrderInfo(OrderBean orderBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.newUpdateOrderInfo(orderBean);
    }

    /**
     * 保存金额和期数
     * 
     * @param orderBean
     * @return
     */
    public int saveAmtAndItems(JSONObject objs) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.saveAmtAndItems(objs);
    }

    public List<OrderBean> selectSelective(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<OrderBean> list = orderDao.selectSelective(objs);
        return list;
    }

    public List<OrderBean> getStageOrderList(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<OrderBean> list = orderDao.getStageOrderList(objs);
        return list;
    }

    public OrderBean selectOne(Map<String, Object> params) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return orderDao.selectOne(params);
    }

    public List<OrderBean> selectPreOrderList(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);

        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<OrderBean> list = orderDao.selectPreOrderList(objs);
        if (list != null && !list.isEmpty()) {
            for (OrderBean ob : list) {
                BigDecimal preAmt = new BigDecimal(StringUtil.isEmpty(ob.getPreAmt()) ? "0" : ob.getPreAmt());
                BigDecimal rp = ob.getReceivedPreAmt() == null ? BigDecimal.ZERO : ob.getReceivedPreAmt();
                ob.setCaculateAmt(BigDecimalUtil.format(preAmt.subtract(rp)));
            }
        }
        return list;
    }

    public int getMaxCarSortNo(String prefix, String merchantNo) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        Map<String, Object> params = new HashMap<>();
        params.put("prefix", prefix);
        params.put("merchantNo", merchantNo);
        return orderDao.getMaxCarSortNo(params);
    }

    public List<OrderBean> selectSelective2(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        if (objs.get("type") != null && objs.get("type").toString().equals("2")) {
            return orderDao.getStageOrderList(objs);
        }
        List<OrderBean> list = orderDao.selectSelective(objs);
        return list;
    }

    public OrderOtherInfo selectOtherOne(String orderId) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return orderDao.selectOtherOne(orderId);
    }

    public int insertOrderOtherInfo(OrderOtherInfo orderOtherInfo) {
        // 设置数据源为从库
        setDb(0, super.MASTER);
        return orderDao.insertOrderOtherInfo(orderOtherInfo);
    }

    public List<OrderBean> getAuthorityOrderList(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<OrderBean> list = orderDao.getAuthorityOrderList(objs);
        return list;
    }

    public int batchInsertJysOrder(List<JYSOrderBean> list) {
        // 设置数据源为从库
        setDb(0, super.MASTER);
        return orderDao.batchInsertJysOrder(list);
    }

    public String generateOrderId(String merchantNo, String bizType) {
        if (StringUtil.isEmpty(merchantNo) || StringUtil.isEmpty(bizType)) {
            throw new RuntimeException("生成订单id失败，传入商户号为空");
        }
        String seqRedisKey = this.getOrderRedisKey(merchantNo, bizType);
        return seqRedisKey + this.getSeqFromRedis(seqRedisKey, false);
    }

    public String getOrderRedisKey(String merchantNo, String bizType) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return merchantNo.toUpperCase() + bizType + todayStr;
    }

    private synchronized String getSeqFromRedis(String key, boolean isSub) {
        /** 数字格式化 **/
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        /** 从redis中取值 **/
        String val = this.redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(val)) {
            this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
            return df.format(Integer.parseInt(this.INITIAL_SEQ));
        }
        Integer seq = Integer.parseInt(val);

        /** 判断是否进行减法操作 **/
        if (isSub) {
            seq = seq - 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        } else {
            seq = seq + 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        }
        return df.format(seq);
    }

    /**
     * 
     * Description: 根据订单id获取机构代码
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 上午10:42:19
     */
    public String getOrgCodeByOrderId(String orderId) {
        super.setDb(0, SLAVE);
        return this.orderDao.getOrgCodeByOrderId(orderId);
    }

    public InstOrderInfoEntity getIOIEByOid(String orderId) {
        super.setDb(0, SLAVE);
        return this.orderDao.getIOIEByOid(orderId);
    }

    /**
     * Description: FINANCE-1301 (F)逾期代扣FINANCE-1322 平账 && 从库中获取 账务系统上行参数
     * 
     * @param
     * @return ChatFinanceToRefundRequestMessage
     * @throws
     * @Author adam Create Date: 2017年6月21日 下午5:16:09
     */
    public ChatFinanceToRefundRequestMessage getSCBCRequestMessageByOid(String orderId) {
        super.setDb(0, SLAVE);
        return orderDao.getSCBCRequestMessageByOid(orderId);
    }

    /**
     * 
     * Description: 【以租代售抵质押物估价节点|抵押车车价复评节点，通过】记录时刻
     * 
     * @param
     * @return void
     * @Author adam
     * @throws Create Date: 2017年7月5日 下午4:27:30
     */
    public void updateSpecialTimeByOid(String orderId) {
        super.setDb(0, MASTER);
        orderDao.updateSpecialTimeByOid(orderId);
    }

    /**
     * 
     * Description:蒲公英行-更新返回标识
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    public int updateInstOrderBackFlag(String orderId, String backFlag) {
        super.setDb(0, MASTER);
        return orderDao.updateInstOrderBackFlag(orderId, backFlag);
    }

    /**
     * 
     * Description:蒲公英行流程回调修改风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月25日
     */
    public int updateOrderInfoForDandelion(OrderBean orderBean) {
        super.setDb(0, MASTER);
        return orderDao.updateOrderInfoForDandelion(orderBean);
    }

    public Object queryHouseBill(JSONObject requestMessage, boolean isPage) {
        super.setDb(0, super.MASTER);
        if (isPage) {
            PageHelper.startPage(getPagePara(requestMessage));
            return new PageInfo<>(orderDao.queryHouseBill(requestMessage));
        }
        return orderDao.queryHouseBill(requestMessage);
    }

    public HouseOrderEntity queryHOEByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return orderDao.queryHOEByOid(orderId);
    }

    public InstPlanEntity queryMPEByPid(Long id) {
        super.setDb(0, super.SLAVE);
        return orderDao.queryMPEByPid(id);
    }

    public void updateHouseOrderHandler(HouseOrderEntity hoe) {
        super.setDb(0, super.MASTER);
        orderDao.updateHouseOrderHandler(hoe);
    }

    public void updateIOIEResetAmt(InstOrderInfoEntity ioie) {
        super.setDb(0, super.MASTER);
        orderDao.updateIOIEResetAmt(ioie);
    }

    public void updateIOIE(InstOrderInfoEntity ioie) {
        super.setDb(0, super.MASTER);
        orderDao.updateIOIE(ioie);
    }

    /**
     * 获取放款订单列表信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public List<InstOrderBean> getLoanOrderList(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        List<InstOrderBean> list = orderDao.getLoanOrderList(objs);
        return list;
    }

    /**
     * 获取放款订单列表信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public List<InstOrderBean> getLoanOrderListForExport(JSONObject objs) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        List<InstOrderBean> list = orderDao.getLoanOrderList(objs);
        return list;
    }

    /**
     * 
     * Description:根据订单号修改放款日期
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    public long updateLoanDateByOrderIds(JSONObject objs) {
        setDb(0, super.SLAVE);
        return orderDao.updateLoanDateByOrderIds(objs);
    }

    public void saveOtherAmtEntity(InstOrderOtherAmtEntity iooae) {
        setDb(0, super.MASTER);
        orderDao.saveOtherAmtEntity(iooae);
    }

    public int updateOtherAmtEntity(InstOrderOtherAmtEntity iooae) {
        setDb(0, super.MASTER);
        return orderDao.updateOtherAmtEntity(iooae);
    }

    public List<InstOrderOtherAmtEntity> getOtherAmtList(JSONObject requestMessage, Boolean isPage) {
        setDb(0, super.SLAVE);
        if (isPage) {
            PageHelper.startPage(getPagePara(requestMessage));
        }
        InstOrderOtherAmtEntity iooae = JSONObject.toJavaObject(requestMessage, InstOrderOtherAmtEntity.class);
        return orderDao.getOtherAmtList(iooae);
    }

    public BigDecimal getFinishBillAmt(String orderId) {
        setDb(0, super.SLAVE);
        return orderDao.getFinishBillAmt(orderId);
    }

    public void updatePreamtByOid(String orderId, BigDecimal amt) {
        setDb(0, super.MASTER);
        orderDao.updatePreamtByOid(orderId, amt);
    }

    public InstOrderOtherAmtEntity getOtherAmtEntity(String orderId) {
        setDb(0, super.MASTER);
        return orderDao.getOtherAmtEntity(orderId);
    }

    public void createIOIE(InstOrderInfoEntity ioie) {
        setDb(0, super.MASTER);
        orderDao.createIOIE(ioie);
    }

    public List<CarOwnerOrderInfo> getCarOwnerOrderInfo(JSONObject requestMessage, boolean isPage) {
        /** 只查询业务类型为车主带订单 **/
        requestMessage.put("bizType", "2400");
        setDb(0, super.SLAVE);
        if (isPage) {
            PageHelper.startPage(getPagePara(requestMessage));
        }
        return orderDao.getCarOwnerOrderInfo(requestMessage);
    }

    public HSSFWorkbook convertCarOwnerOrderInfoXlsx(List<CarOwnerOrderInfo> list) {
        String[] header =
        {"订单号", "姓名", "手机号", "订单时间", "分期时间", "借款金额", "期数", "方案", "保证金", "服务费", "GPS流量费/月", "GPS安装费用", "月供", "预付款",
                "核准金额", "订单状态", "工作流状态", "商户"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

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
            xu.append(hr, 0, list.get(i).getOrderId()); //
            xu.append(hr, 1, list.get(i).getRealName()); //
            xu.append(hr, 2, list.get(i).getRegId()); //
            xu.append(hr, 3, list.get(i).getOrderDate()); // 订单时间
            xu.append(hr, 4, list.get(i).getStageDate()); // 订单时间
            xu.append(hr, 5, list.get(i).getAmt()); // 借款金额
            xu.append(hr, 6, list.get(i).getOrderItem()); // 期数
            xu.append(hr, 7, list.get(i).getPlanShortName()); // 方案
            xu.append(hr, 8, list.get(i).getMargin()); // 保证金
            xu.append(hr, 9, list.get(i).getServiceFee()); // 服务费
            xu.append(hr, 10, list.get(i).getGpsTrafficFee()); // GPS流量费
            xu.append(hr, 11, list.get(i).getGpsAmt()); // GPS安装费
            xu.append(hr, 12, list.get(i).getMonthInterest()); // 月供
            xu.append(hr, 13, list.get(i).getPreAmt()); // 预付款
            xu.append(hr, 14, list.get(i).getOrderAmt()); // 核准金额
            xu.append(hr, 15, convertRiskStatus(list.get(i).getRiskStatus())); // 订单状态
            xu.append(hr, 16, convertWfStatus(list.get(i).getWfStatus())); // 工作流状态
            xu.append(hr, 17, list.get(i).getMerchName()); // 商户号
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    public List<CarOwnerCarInfo> getCarOwnerCarInfo(JSONObject requestMessage, boolean isPage) {
        super.setDb(0, super.MASTER);
        if (isPage) {
            PageHelper.startPage(getPagePara(requestMessage));
        }
        return orderDao.getCarOwnerCarInfo(requestMessage);
    }

    public HSSFWorkbook convertCarOwnerCarInfoXlsx(List<CarOwnerCarInfo> list) {
        String[] header =
        {"车牌号", "车架号", "车辆品牌", "车辆型号", "初次登记日期", "抵押类型", "抵押机构", "车辆评估价格", "核准金额", "借款金额", "订单号", "姓名", "手机号",
                "订单时间", "年龄", "性别", "商户"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

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
            xu.append(hr, 0, list.get(i).getPlate()); //
            xu.append(hr, 1, list.get(i).getCarNo()); //
            xu.append(hr, 2, list.get(i).getPinPai()); //
            xu.append(hr, 3, list.get(i).getXingHao()); //
            xu.append(hr, 4, list.get(i).getFirstRegDate()); //
            xu.append(hr, 5, list.get(i).getMortgageType()); //
            xu.append(hr, 6, list.get(i).getMortgageCompany()); //
            xu.append(hr, 7, list.get(i).getAssessPrice()); //
            xu.append(hr, 8, list.get(i).getOrderAmt()); // 核准金额
            xu.append(hr, 9, list.get(i).getOrderAmt()); // 借款金额
            xu.append(hr, 10, list.get(i).getOrderId()); //
            xu.append(hr, 11, list.get(i).getRealName()); //
            xu.append(hr, 12, list.get(i).getRegId()); //
            xu.append(hr, 13, list.get(i).getCreateTime()); //
            xu.append(hr, 14, list.get(i).getAge()); //
            xu.append(hr, 15, list.get(i).getGender()); //
            xu.append(hr, 16, list.get(i).getMerchantName()); //
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    private String convertWfStatus(String wfStatus) {
        switch (wfStatus) {
            case "10":
                return "待车辆勘察评估";
            case "15":
                return "待门店风控审批";
            case "20":
                return "待读脉审核";
            case "25":
                return "待客服电核";
            case "30":
                return "待风控初审";
            case "35":
                return "待风控终审";
            case "40":
                return "待门店签约";
            case "45":
                return "待确认gps安装";
            case "50":
                return "待合同审核";
            case "55":
                return "待gps信号确认";
            case "60":
                return "待结算岗";
            case "65":
                return "待放款确认";
            case "100":
                return "流程结束";
            case "0":
                return "流程拒绝";
            default:
                /** @author adam **/
                log.error("本类中 convertWfStatus 方法对 数字 【%s】进行了中文转换 ， 如果返回了数字，请升级相关方法！", wfStatus);
                return wfStatus;
        }
    }

    private String convertRiskStatus(String riskStatus) {
        switch (riskStatus) {
            case "0":
                return "审核通过";
            case "1":
                return "审核拒绝";
            case "2":
                return "审核中";
            case "3":
                return "已分期";
            case "4":
                return "待支付";
            case "5":
                return "待确认";
            case "6":
                return "已取消";
            case "7":
                return "已放款";
            case "10":
                return "已结清";
            case "11":
                return "已终止";
            case "21":
                return "待估价";
            case "22":
                return "已估价";
            default:
                /** @author adam **/
                log.error("本类中 convertRiskStatus 方法对 数字【%s】 进行了中文转换 ， 如果返回了数字，请升级相关方法！", riskStatus);
                return riskStatus;
        }
    }

    /**
     * 
     * Description:车主贷订单分期
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    public List<CarOwnerOrderInfo> getOrderBreakInfo(JSONObject requestMessage, boolean isPage) {
        super.setDb(0, super.MASTER);
        if (isPage) {
            PageHelper.startPage(getPagePara(requestMessage));
        }
        return orderDao.getOrderBreakInfo(requestMessage);
    }

    /**
     * 
     * Description:订单信息导入分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月7日
     */
    public List<OrderBean> selectOrderInfoForImport(JSONObject objs) {
        PageHelper.startPage(getPagePara(objs));
        return orderDao.selectOrderInfoForImport(objs);
    }

    /**
     * 批量插入订单信息
     */
    public int batchInsertOrderInfo(List<OrderBean> list) {
        setDb(0, super.MASTER);
        return orderDao.batchInsertOrderInfo(list);
    }

    /**
     * 
     * Description:订单导入生成订单号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    public String generateOrderIdForImport(String merchantNo, String bizType) {
        if (StringUtil.isEmpty(merchantNo) || StringUtil.isEmpty(bizType)) {
            throw new RuntimeException("生成订单id失败，传入商户号为空");
        }
        String seqRedisKey = this.getOrderRedisKeyForImport(merchantNo, bizType);
        return seqRedisKey + this.getSeqFromRedisForImport(seqRedisKey, false);
    }

    public String getOrderRedisKeyForImport(String merchantNo, String bizType) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return merchantNo.toUpperCase() + bizType + todayStr;
    }

    private synchronized String getSeqFromRedisForImport(String key, boolean isSub) {
        /** 数字格式化 **/
        DecimalFormat df = new DecimalFormat(STR_FORMAT_IMPORT);
        /** 从redis中取值 **/
        String val = this.redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(val)) {
            this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
            return df.format(Integer.parseInt(this.INITIAL_SEQ));
        }
        Integer seq = Integer.parseInt(val);

        /** 判断是否进行减法操作 **/
        if (isSub) {
            seq = seq - 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        } else {
            seq = seq + 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        }
        return df.format(seq);
    }

    /**
     * 
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    public String generateProjectNameByOrderId(String merchantNo, String projectPrefix) {
        if (StringUtil.isEmpty(merchantNo) || StringUtil.isEmpty(projectPrefix)) {
            throw new RuntimeException("生成订单id失败，传入商户号为空");
        }
        String seqRedisKey = this.getOrderRedisKeyProjectNameByOrderId(merchantNo, projectPrefix);
        return seqRedisKey + this.getSeqFromRedisForProjectNameByOrderId(seqRedisKey, false);
    }

    public String getOrderRedisKeyProjectNameByOrderId(String merchantNo, String projectPrefix) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return projectPrefix + merchantNo.toUpperCase() + todayStr;
    }

    private synchronized String getSeqFromRedisForProjectNameByOrderId(String key, boolean isSub) {
        /** 数字格式化 **/
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        /** 从redis中取值 **/
        String val = this.redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(val)) {
            this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
            return df.format(Integer.parseInt(this.INITIAL_SEQ));
        }
        Integer seq = Integer.parseInt(val);

        /** 判断是否进行减法操作 **/
        if (isSub) {
            seq = seq - 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        } else {
            seq = seq + 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        }
        return df.format(seq);
    }

    @SuppressWarnings("rawtypes")
    public Map getOrderInfoByListTotal(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return orderDao.getOrderInfoByListTotal(objs);
    }

    public OrderBean getOrderInfo(JSONObject objs) {
        setDb(0, super.SLAVE);
        return orderDao.getOrderInfo(objs);
    }

    public int updateOrderInfo1(OrderBean objs) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.updateOrderInfo1(objs);
    }

    /**
     * 
     * Description:查询所有订单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public List<OrderBean> selectAllOrderList(JSONObject objs) {
        super.setDb(1, SLAVE);
        return this.orderDao.selectAllOrderList();
    }

    /**
     * 
     * Description:修改订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public int updateOrderItemsByOrderId(String orderId) {
        super.setDb(0, SLAVE);
        return this.orderDao.updateOrderItemsByOrderId(orderId);
    }

    /**
     * 
     * Description:根据订单号修改剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int updateOrderInfoForLeftMonthByOrderId(OrderBean orderBean) {
        super.setDb(0, SLAVE);
        return this.orderDao.updateOrderInfoForLeftMonthByOrderId(orderBean);
    }

    /**
     * 
     * Description:保存订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月2日
     */
    public int saveOrderInfoForHY(OrderBean orderBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return orderDao.saveOrderInfoForHY(orderBean);
    }

    public Map getLoanOrderListTotal(JSONObject objs) {
        // 设置数据源为主库
        super.setDb(0, SLAVE);
        return orderDao.getLoanOrderListTotal(objs);
    }

    public List<InstOrderBean> getSelectLoanOrderList(JSONObject objs) {
        // 设置数据源为主库
        super.setDb(0, SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return orderDao.getSelectLoanOrderList(objs);
    }

    public List<InstOrderBean> getSelectLoanOrderListForExport(JSONObject objs) {
        // 设置数据源为主库
        super.setDb(0, SLAVE);
        return orderDao.getSelectLoanOrderList(objs);
    }

    public String getSysItem2Excel(String itemCode, String value) {
        // 设置数据源为主库
        super.setDb(0, SLAVE);
        return orderDao.getSysItem2Excel(itemCode, value);
    }

    public InstOrderBean getOrderInfoNew(JSONObject objs) {
        // 设置数据源为主库
        super.setDb(0, SLAVE);
        return orderDao.getOrderInfoNew(objs);
    }

    public void saveApprovalOpinion(JSONObject objs) {
        // 设置数据源为主库
        super.setDb(0, MASTER);
        orderDao.saveApprovalOpinion(objs);

    }

    public Map<String, String> getApprovalOpinion(JSONObject objs) {
        super.setDb(0, SLAVE);
        return orderDao.getApprovalOpinion(objs);

    }

    /**
     * 
     * Description:根据条件查询以租代购费用明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    public List<OrderAmtDetailBean> selectOrderAmtDetailList(JSONObject objs) {
        super.setDb(1, SLAVE);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        return orderDao.selectOrderAmtDetailList(objs);
    }

    /**
     * 
     * Description:根据订单号查询车秒贷金额相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    public OrderAmtDetailBean getOrderAmtDetail(String orderId) {
        super.setDb(1, SLAVE);
        return orderDao.getOrderAmtDetail(orderId);
    }

    /**
     * 
     * Description:根据手机号获取订单列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月24日
     */
    public List<OrderBean> getOrderListByRegId(String regId) {
        super.setDb(1, SLAVE);
        return orderDao.getOrderListByRegId(regId);
    }
}
