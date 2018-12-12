package com.iqb.consumer.service.layer.orderinfo;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.exception.OverDueReturnInfo;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.ArchivesBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.order.OverdueInfoBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SendHttpsUtil;

/**
 * Description:违约信息Service实现类
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月16日上午11:55:01 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class OverdueInfoServiceImpl implements OverdueInfoService {
    protected static Logger logger = LoggerFactory.getLogger(OverdueInfoServiceImpl.class);
    // 结算状态，1未结清 2 已结清
    private final int STATUS = 1;
    @Resource
    private OverdueInfoBiz overdueInfoBiz;
    @Resource
    private UrlConfig urlConfig;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Autowired
    private SysUserSession sysUserSession;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Autowired
    private MerchantBeanBiz merchantBiz;

    private static final String ORDER_SETTLESORTNO_PREFIX = "ORDER_SETTLESORTNO_PREFIX";// 批次号
    private static final String OVERDUE_SETTLESORTNO_ORDERIDS = "OVERDUE_SETTLESORTNO_ORDERIDS";// 提交订单号
    private static final String OVERDUE_ORDERI_PREFIX = "BZJJS";
    private final double PERCENT = 0.4d;

    /**
     * 客户履约保证金结算分页查询
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月16日
     */
    @Override
    public PageInfo<OverdueInfoBean> selectOverdueInfoSettlementList(JSONObject objs) {
        // 获取商户权限的Objs
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
        if (!StringUtil.isNull(merchNames)) {
            if (!"全部商户".equals(merchNames)) {
                objs = getMerchLimitObject(objs);
            }
        }
        List<OverdueInfoBean> list = overdueInfoBiz.selectOverdueInfoSettlementList(objs);
        return new PageInfo<>(list);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月16日
     */
    @Override
    public int updateOverdueInfo(OverdueInfoBean overdueInfoBean) {
        return overdueInfoBiz.updateOverdueInfo(overdueInfoBean);
    }

    /**
     * 批量插入违约信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月16日
     */
    @Override
    public int batchInsertOverdueInfo(JSONObject objs) {
        int result = 0;
        String orderIds = objs.getString("orderIds");
        OverdueInfoBean overdueInfoBean = JSONObject.toJavaObject(objs, OverdueInfoBean.class);
        if (!StringUtil.isNull(orderIds)) {
            String[] orderIdArry = orderIds.split(",");
            List<String> orderList = Arrays.asList(orderIdArry);

            if (!CollectionUtils.isEmpty(orderList)) {
                for (String orderId : orderList) {
                    overdueInfoBean.setOrderId(orderId);
                    overdueInfoBean.setStatus(STATUS);
                    result += overdueInfoBiz.insertOverdueInfo(overdueInfoBean);
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月16日
     */
    @Override
    public int updateOverdueInfoByBatchId(OverdueInfoBean overdueInfoBean) {
        return overdueInfoBiz.updateOverdueInfoByBatchId(overdueInfoBean);
    }

    /**
     * 根据批次号查询总结算金额、总保证金、总笔数、订单信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月19日
     */
    @Override
    public OverdueInfoBean getOverdueInfoByBatchId(JSONObject objs) {
        OverdueInfoBean overdueInfoBean = overdueInfoBiz.getOverdueInfoByBatchId(objs);
        return overdueInfoBean;
    }

    /**
     * 客户违约结算流程启动
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月19日
     */
    @Override
    public int startOverdueInfoProcess(JSONObject objs) throws IqbException {
        int resultValue = 0;
        // 1:生成batchId
        String batchId = getBatchIdForRedis();
        String orderIds = objs.getString("orderIds");

        if (!StringUtil.isNull(orderIds)) {
            String[] orderIdArr = orderIds.split(",");
            List<String> orderList = Arrays.asList(orderIdArr);
            OverdueInfoBean overdueInfoBean = null;
            OrderBean orderBean = null;
            // 2:根据订单号更新batchId、保证金、结算金额
            if (!CollectionUtils.isEmpty(orderList)) {
                // 查询所有订单是否存在流程处理中的
                JSONObject obj = new JSONObject();
                obj.put("orderIds", orderList);
                int selOrderCount = overdueInfoBiz.selOrderCount(obj);
                if (selOrderCount > 0) {
                    // 存在已经发起流程的订单,需提示用户刷新
                    throw new IqbException(OverDueReturnInfo.OVERDUE_DOUBLE_ORDERID);
                }
                // 此处需要优化，一次查询所有orderListBean 在循环处理 最好用个线程池处理
                for (String orderId : orderList) {
                    orderBean = orderBiz.selByOrderId(orderId);

                    overdueInfoBean = new OverdueInfoBean();
                    overdueInfoBean.setBatchId(batchId);
                    overdueInfoBean.setOrderId(orderId);

                    if (orderBean != null) {
                        overdueInfoBean.setSumMarginAmt(StringUtil.isNull(orderBean.getMargin())
                                ? new BigDecimal(0)
                                : new BigDecimal(orderBean.getMargin()));
                        overdueInfoBean.setSumSettlement(StringUtil.isNull(orderBean.getMargin())
                                ? new BigDecimal(0)
                                : new BigDecimal(orderBean.getMargin()).multiply(BigDecimal.valueOf(PERCENT)));
                    } else {
                        overdueInfoBean.setSumMarginAmt(new BigDecimal(0));
                        overdueInfoBean.setSumSettlement(new BigDecimal(0));
                    }
                    resultValue += overdueInfoBiz.updateOverdueInfo(overdueInfoBean);
                }
            }
        }
        // 3:启动违约结算工作流
        resultValue += prepaymentStartWF(batchId);
        return resultValue;
    }

    /**
     * 将客户端提交的订单号保存到redis中
     * 
     * @param orderIds
     * @return
     * @throws IqbException
     */
    @SuppressWarnings("unused")
    private String saveOrderIdsToRedisAndCheck(String orderIds) throws IqbException {
        logger.info("---获取客户违约保证金结算提交的订单号---");
        String resultValue = "";
        // 1. 获取违约结算序号
        Redisson redisson =
                RedisUtils.getInstance()
                        .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
        RLock rLock = null;
        try {
            rLock = RedisUtils.getInstance().getRLock(redisson, "getWFReturnSeqFromRedis");
            if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                String orderIdsFromRedis = redisPlatformDao.getValueByKey(OVERDUE_SETTLESORTNO_ORDERIDS);
                if (orderIdsFromRedis == null || "".equals(orderIdsFromRedis)) {
                    redisPlatformDao.setKeyAndValue(OVERDUE_SETTLESORTNO_ORDERIDS, orderIds);
                    resultValue = orderIds;
                } else {
                    resultValue = orderIdsFromRedis;
                }
            } else {
                throw new IqbException(ConsumerReturnInfo.GET_PROJECT_CREATE_NAME_LOCK);
            }
        } catch (Exception e) {
            throw new IqbException(ConsumerReturnInfo.PROJECT_CREATE_NAME);
        } finally {
            rLock.unlock();
        }
        try {
            if (redisson != null) {
                RedisUtils.getInstance().closeRedisson(redisson);
            }
        } catch (Exception e) {
            logger.error("---获取客户违约保证金结算提交的订单号出现异常---{}", e);
        }
        logger.info("---获取客户违约保证金结算提交的订单号完成---{}", resultValue);
        return resultValue;
    }

    /**
     * BZJJS+年月日+001后3位递增:例如：BZJJS180312001
     */
    private String getBatchIdForRedis() throws IqbException {
        logger.info("---获取违约结算自动增长序号开始---");
        String batchId = null;
        String settleSortNo = redisPlatformDao.getValueByKey(ORDER_SETTLESORTNO_PREFIX);
        if (settleSortNo == null || "".equals(settleSortNo)) {
            int no = 1;
            settleSortNo = String.format("%04d", no);
            redisPlatformDao.setKeyAndValue(ORDER_SETTLESORTNO_PREFIX, settleSortNo);
        } else {
            int no = Integer.parseInt(settleSortNo) + 1;
            settleSortNo = String.format("%04d", no);
            redisPlatformDao.setKeyAndValue(ORDER_SETTLESORTNO_PREFIX, settleSortNo);
        }
        String orderDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        batchId = OVERDUE_ORDERI_PREFIX + orderDate.substring(2, 8) + settleSortNo;
        logger.info("---获取违约结算自动增长序号完成---{}", batchId);
        return batchId;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int prepaymentStartWF(String batchId) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        JSONObject objs = new JSONObject();
        objs.put("batchId", batchId);
        OverdueInfoBean overdueInfoBean = overdueInfoBiz.getOverdueInfoByBatchId(objs);
        if (overdueInfoBean != null) {
            BigDecimal sumSettlement =
                    overdueInfoBean.getSumSettlement() != null ? overdueInfoBean.getSumSettlement() : BigDecimal.ZERO;
            String procBizMemo =
                    "违约保证金结算" + ";" + overdueInfoBean.getTotalNum() + ";" + sumSettlement + ";" + batchId;// 摘要信息

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", urlConfig.getForfeiDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", urlConfig.getTokenUser());
            hmVariables.put("procTokenPass", urlConfig.getTokenPass());
            hmVariables.put("procTaskUser", sysUserSession.getUserCode());
            hmVariables.put("procTaskRole", urlConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", overdueInfoBean.getBatchId());
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", sysUserSession.getOrgCode());
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", "");

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = urlConfig.getStartWfurl();
            // 发送Https请求
            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        } else {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        if (responseMap.get("retCode").toString().equals("00000000")) {
            Map<String, String> map = (HashMap<String, String>) responseMap.get("iqbResult");
            String procInstId = map.get("procInstId");

            // 更新提前结清申请状态
            overdueInfoBean = new OverdueInfoBean();
            overdueInfoBean.setBatchId(batchId);
            // 待结算
            overdueInfoBean.setStatus(2);
            overdueInfoBean.setWfStatus(10);
            overdueInfoBean.setProcessId(procInstId);

            overdueInfoBiz.updateOverdueInfoByBatchId(overdueInfoBean);
        }
        return result;
    }

    /**
     * 保证金结算查询导出
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月20日
     */
    @Override
    public String exportSelectOverdueInfoSettlementList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
            if (!StringUtil.isNull(merchNames)) {
                if (!"全部商户".equals(merchNames)) {
                    objs = getMerchLimitObject(objs);
                }
            }
            // 查询违约信息
            List<OverdueInfoBean> resultList =
                    overdueInfoBiz.selectOverdueInfoSettlementList1(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportOverdueInfoSettlementList(resultList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=overdueInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出保证金结算列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    /**
     * Description:导出Excel表格处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月20日
     */
    private HSSFWorkbook exportOverdueInfoSettlementList(List<OverdueInfoBean> resultList) {
        String[] excelHeader =
        {"订单编号", "姓名", "手机号", "金额", "产品方案", "保证金", "结算金额", "违约行为", "违约时间", "结算状态", "结算时间", "流水号", "批次号",
                "业务类型", "商户名称"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("保证金结算查询列表");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle amtStyle = wb.createCellStyle(); // 货币样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        amtStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < resultList.size(); i++) {
            row = sheet.createRow(i + 1);
            OverdueInfoBean overdueInfoBean = resultList.get(i);

            row.createCell(0).setCellValue(overdueInfoBean.getOrderId());
            row.createCell(1).setCellValue(overdueInfoBean.getRealName());
            row.createCell(2).setCellValue(overdueInfoBean.getRegId());
            row.createCell(3).setCellValue(overdueInfoBean.getOrderAmt() == null ? 0 : overdueInfoBean.getOrderAmt()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(4).setCellValue(overdueInfoBean.getPlanName());
            row.createCell(5).setCellValue(overdueInfoBean.getMargin() == null ? 0 : overdueInfoBean.getMargin()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(6).setCellValue(
                    overdueInfoBean.getSettlementAmt() == null ? 0 : overdueInfoBean.getSettlementAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(7).setCellValue(overdueInfoBean.getOverdueRemark());
            row.createCell(8).setCellValue(overdueInfoBean.getOverdueDate());
            row.createCell(9).setCellValue(doStatus(overdueInfoBean.getStatus()));
            row.createCell(10).setCellValue(overdueInfoBean.getSettlementDate());
            row.createCell(11).setCellValue(overdueInfoBean.getSerialNum());

            row.createCell(12).setCellValue(overdueInfoBean.getBatchId());
            row.createCell(13).setCellValue(overdueInfoBean.getBizType());
            row.createCell(14).setCellValue(overdueInfoBean.getMerchantName());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单编号
        sheet.setColumnWidth(1, 4000);// 姓名
        sheet.setColumnWidth(2, 4000);// 手机号
        sheet.setColumnWidth(3, 4000);// 金额
        sheet.setColumnWidth(4, 5000);// 产品方案
        sheet.setColumnWidth(5, 4000);// 保证金
        sheet.setColumnWidth(6, 4000);// 结算金额
        sheet.setColumnWidth(7, 10000);// 违约行为
        sheet.setColumnWidth(8, 4000);// 违约时间
        sheet.setColumnWidth(9, 4000);// 结算状态
        sheet.setColumnWidth(10, 4000);// 结算时间
        sheet.setColumnWidth(11, 4000);// 流水号
        sheet.setColumnWidth(12, 4000);// 批次号
        sheet.setColumnWidth(13, 4000);// 业务类型
        sheet.setColumnWidth(14, 8000);// 商户名称

        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    private String doStatus(int status) {
        String str = "未结算";
        if (status == 1) {
            str = "未结算";
        } else if (status == 2) {
            str = "待结算";
        } else {
            str = "已结算";
        }
        return str;
    }

    /**
     * 
     * Description:获取商户列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月20日
     */
    private JSONObject getMerchLimitObject(JSONObject objs) {
        objs.put("status", objs.getString("status"));
        // 权限树修改
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
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
     * 档案查询 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月26日
     */
    public PageInfo<ArchivesBean> queryArchivesList(JSONObject objs) {
        // 获取商户权限的Objs
        getMerchLimitObject(objs);
        List<ArchivesBean> list = overdueInfoBiz.queryArchivesList(objs);
        return new PageInfo<>(list);
    }
}
