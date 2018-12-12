package com.iqb.consumer.service.layer.carstatus;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.carstatus.entity.InstManageCarInfoEntity;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.GPSInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.sublet.pojo.ChatToGetRepayParamsResponseMessage;
import com.iqb.consumer.data.layer.biz.carstatus.CarStatusManager;
import com.iqb.consumer.data.layer.dao.OrderDao;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.dandelion.DandelionCenterServiceImpl;
import com.iqb.etep.common.utils.DateTools;

/**
 * 
 * Description: 车辆状态处置中心
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年7月10日    adam       1.0        1.0 Version 
 * </pre>
 */
@Service
public class CarStatusCenterServiceImpl extends BaseService implements CarStatusCenterService {

    protected static final Logger logger = LoggerFactory.getLogger(DandelionCenterServiceImpl.class);
    @Autowired
    private CarStatusManager carStatusManager;
    @Resource
    private UrlConfig urlConfig;
    @Resource
    private OrderDao orderDao;

    private static final int CHE_LIANG_ZHUANG_TAI_GEN_ZONG = 1; // 车辆状态跟踪
    private static final int DAIHOU_CHUKU_ZHUANGTAI = 2; // 贷后处置/车辆出库/车辆状态

    @Override
    public PageInfo<CgetCarStatusInfoResponseMessage> cgetInfoList(JSONObject requestMessage)
            throws GenerallyException {
        // 获取商户权限的Objs
        requestMessage = getMerchLimitObject(requestMessage);
        if (requestMessage == null) {
            return null;
        }

        Integer channal = requestMessage.getInteger("channal");
        switch (channal) {
            case CHE_LIANG_ZHUANG_TAI_GEN_ZONG:
                return new PageInfo<>(carStatusManager.cgetInfoList(requestMessage));
            case DAIHOU_CHUKU_ZHUANGTAI:
                return new PageInfo<>(carStatusManager.cgetInfo2List(requestMessage, true));
            default:
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public void persisitImci(String orderId) throws GenerallyException {
        int u1 = carStatusManager.markIOIEByCarstatus(orderId);
        if (u1 != 1) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }
        carStatusManager.persisitImci(orderId);
        // 保存成功说明进入贷后,将电话提醒和电催存在的订单状态置为3已处理
        carStatusManager.updateRemindPhone(orderId);
    }

    @Override
    public CgetCarStatusInfoResponseMessage cgetInfo(JSONObject requestMessage)
            throws GenerallyException {
        List<CgetCarStatusInfoResponseMessage> ccsi = carStatusManager.cgetInfo2List(requestMessage, false);
        if (!CollectionUtils.isEmpty(ccsi)) {
            if (ccsi.size() > 1) {
                throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
            }
            return ccsi.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void updateInfo(JSONObject requestMessage) throws GenerallyException {
        InstManageCarInfoEntity imci = JSONObject.toJavaObject(requestMessage, InstManageCarInfoEntity.class);
        if (imci == null || !imci.checkEntity()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        carStatusManager.updateIMCI(imci);
    }

    /**
     * 根据订单号修改inst_managercar_info 信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月10日
     */
    @Override
    public int updateManagerCarInfoByOrderId(JSONObject requestMessage) {
        JSONArray array = requestMessage.getJSONArray("dealerEvaluatesInfo");
        if (array != null) {
            requestMessage.put("dealerEvaluatesInfo", JSON.toJSONString(array));
        }
        JSONObject subleaseObj = requestMessage.getJSONObject("subleaseInfo");
        if (subleaseObj != null) {
            requestMessage.put("subleaseInfo", JSON.toJSONString(subleaseObj));
        }
        JSONObject returnInfoObj = requestMessage.getJSONObject("returnInfo");
        if (returnInfoObj != null) {
            requestMessage.put("returnInfo", JSON.toJSONString(returnInfoObj));
        }
        return carStatusManager.updateManagerCarInfoByOrderId(requestMessage);
    }

    /**
     * 根据订单号查询车辆状态跟踪回显信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月10日
     */
    @Override
    public ManageCarInfoBean selectOrderInfoByOrderId(JSONObject requestMessage) {
        return carStatusManager.selectOrderInfoByOrderId(requestMessage);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月11日
     */
    @Override
    public ManageCarInfoBean selectSubleaseInfoByOrderId(JSONObject requestMessage) {
        return carStatusManager.selectSubleaseInfoByOrderId(requestMessage);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月12日
     */
    @Override
    public int updateStatusByOrderId(Map<String, String> params) {
        return carStatusManager.updateStatusByOrderId(params);
    }

    /**
     * 
     * Description:根据订单号查询以及GPS状态回显信息 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 11:31:59
     */
    @Override
    public CgetCarStatusInfoResponseMessage getOrderInfoAndGPSInfoByOrderId(JSONObject requestMessage) {
        return carStatusManager.getOrderInfoAndGPSInfoByOrderId(requestMessage);
    }

    /**
     * 
     * Description:保存GPS状态到inst_gpsinfo表 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 14:47:18
     */
    @Override
    public void saveGPSInfo(JSONObject requestMessage) {
        carStatusManager.saveGPSInfo(requestMessage);
    }

    /**
     * 
     * Description:根据orderID查询展示inst_gpsinfo表数据 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 15:10:18
     */
    @Override
    public PageInfo<GPSInfo> selectGPSInfoListByOrderId(JSONObject requestMessage) {
        return new PageInfo<>(carStatusManager.selectGPSInfoListByOrderId(requestMessage));
    }

    /**
     * 
     * Description:车辆状态跟踪模块列表与查询接口 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 17:02:31
     */
    @Override
    public PageInfo<CgetCarStatusInfoResponseMessage> selectCarToGPSList(JSONObject requestMessage) {
        return new PageInfo<>(carStatusManager.selectCarToGPSList(requestMessage));
    }

    /**
     * 
     * Description:FINANCE-3031 车辆状态查询页面增加【导出】功能，导出列表页的所有信息；FINANCE-3057车辆状态查询页面增加【导出】功能，导出列表页的所有信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen 2018年3月7日 14:54:52
     */
    @Override
    public String exportXlsxCgetinfolist(JSONObject requestMessage, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            List<CgetCarStatusInfoResponseMessage> xlsx =
                    carStatusManager.cgetInfo2List(requestMessage, false);
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

    private HSSFWorkbook convertXlsxAssetBreak(List<CgetCarStatusInfoResponseMessage> list) {
        String[] header =
        {"订单号", "姓名", "手机号", "品牌车系", "车架号", "车牌号", "车辆状态", "贷后状态", "GPS状态", "借款金额", "总期数", "商户名称", "当前期数", "账单状态"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("车辆状态查询");

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
            xu.append(hr, 4, list.get(i).getCarNo());
            xu.append(hr, 5, list.get(i).getPlate());
            // 10.贷后处置中 15.待失联处置 20.已失联 25.待入库 30.已入库 35.待出售 40.已出售 45.待转租 50.已转租 55.待返还 60.已返还

            if (list.get(i).getStatus() == null || "".equals(list.get(i).getStatus())) {
                xu.append(hr, 6, "正常");// 车辆状态
            } else {
                if (list.get(i).getStatus().equals("10")) {
                    xu.append(hr, 6, "贷后处置中");// 车辆状态
                } else if (list.get(i).getStatus().equals("15")) {
                    xu.append(hr, 6, "待失联处置");// 车辆状态
                } else if (list.get(i).getStatus().equals("20")) {
                    xu.append(hr, 6, "已失联");// 车辆状态
                } else if (list.get(i).getStatus().equals("25")) {
                    xu.append(hr, 6, "待入库");// 车辆状态
                } else if (list.get(i).getStatus().equals("30")) {
                    xu.append(hr, 6, "已入库 ");// 车辆状态
                } else if (list.get(i).getStatus().equals("35")) {
                    xu.append(hr, 6, "待出售");// 车辆状态
                } else if (list.get(i).getStatus().equals("40")) {
                    xu.append(hr, 6, "已出售");// 车辆状态
                } else if (list.get(i).getStatus().equals("45")) {
                    xu.append(hr, 6, "待转租");// 车辆状态
                } else if (list.get(i).getStatus().equals("50")) {
                    xu.append(hr, 6, "已转租");// 车辆状态
                } else if (list.get(i).getStatus().equals("55")) {
                    xu.append(hr, 6, "待返还");// 车辆状态
                } else if (list.get(i).getStatus().equals("60")) {
                    xu.append(hr, 6, "已返还");// 车辆状态
                }
            }
            if (list.get(i).getProcessStatus() == null || "".equals(list.get(i).getProcessStatus())
                    || "0".equals(list.get(i).getProcessStatus())) {
                xu.append(hr, 7, "");// 贷后状态
            }
            else {
                if (list.get(i).getProcessStatus().equals("5")) {
                    xu.append(hr, 7, "待贷后处理");// 车辆状态
                } else if (list.get(i).getProcessStatus().equals("10")) {
                    xu.append(hr, 7, "贷后处理中");// 车辆状态
                } else if (list.get(i).getProcessStatus().equals("15")) {
                    xu.append(hr, 7, "待外包处理");// 车辆状态
                } else if (list.get(i).getProcessStatus().equals("20")) {
                    xu.append(hr, 7, "外包处理中");// 车辆状态
                } else if (list.get(i).getProcessStatus().equals("25")) {
                    xu.append(hr, 7, "法务处理中");// 车辆状态
                } else if (list.get(i).getProcessStatus().equals("30")) {
                    xu.append(hr, 7, "处理结束");// 车辆状态
                }
            }

            if (list.get(i).getGpsStatus() == null || "".equals(list.get(i).getGpsStatus())
                    || "0".equals(list.get(i).getGpsStatus())) {
                xu.append(hr, 8, "未标记");// 车辆状态
            }
            else {
                if (list.get(i).getGpsStatus().equals("2")) {
                    xu.append(hr, 8, "有线离线");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("3")) {
                    xu.append(hr, 8, "无线离线");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("4")) {
                    xu.append(hr, 8, "断电报警");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("5")) {
                    xu.append(hr, 8, "拆除报警");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("6")) {
                    xu.append(hr, 8, "其他 ");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("1")) {
                    xu.append(hr, 8, "正常 ");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("7")) {
                    xu.append(hr, 8, "全部离线 ");// 车辆状态
                } else if (list.get(i).getGpsStatus().equals("8")) {
                    xu.append(hr, 8, "出省 ");// 车辆状态
                }
            }
            xu.append(hr, 9, list.get(i).getOrderAmt().toString());
            xu.append(hr, 10, list.get(i).getOrderItems().toString());
            xu.append(hr, 11, list.get(i).getMerchantShortName());
            xu.append(hr, 12, list.get(i).getCurItems());
            if (StringUtil.isNotEmpty(list.get(i).getBillStatus())) {
                if (list.get(i).getBillStatus().equals("0")) {
                    xu.append(hr, 13, "逾期");
                } else if (list.get(i).getBillStatus().equals("1")) {
                    xu.append(hr, 13, "待还款");
                } else if (list.get(i).getBillStatus().equals("2")) {
                    xu.append(hr, 13, "部分还款");
                } else if (list.get(i).getBillStatus().equals("3")) {
                    xu.append(hr, 13, "已还款");
                }
            }

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

    /**
     * 根据订单号获取应还金额 应还金额=剩余本金+违约金+罚息(总罚息-减免金额) Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    @Override
    public Map<String, BigDecimal> getCurrentBalance(JSONObject objs) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        // 是否结清车秒贷标识 1 是 2 否
        String flag = objs.getString("flag");
        String orderId = objs.getString("orderId");
        String orderIdX = orderId + "X";
        BigDecimal currentBanlance = BigDecimal.ZERO;
        List<String> orderIdList = new ArrayList<>();

        if (StringUtil.isNotEmpty(orderId) && StringUtil.isNotEmpty(flag)) {
            orderIdList.add(orderId);
            // 获取车秒贷订单信息
            if (flag.equals("1")) {
                OrderBean orderBean = orderDao.selByOrderId(orderIdX);
                if (orderBean != null) {
                    orderIdList.add(orderIdX);
                }
            }

            for (String tempOrderId : orderIdList) {
                try {
                    ChatToGetRepayParamsResponseMessage billBean = chatToGetRepayStartParams(tempOrderId);
                    if (billBean.getResult() != null) {
                        currentBanlance =
                                BigDecimalUtil.add(billBean.getResult().getOverdueAmt(), billBean.getResult()
                                        .getTotalOverdueInterest(), billBean.getResult().getRemainPrincipal(),
                                        currentBanlance);
                    }
                } catch (GenerallyException e) {
                    logger.error("--根据订单号获取应还金额调用账户系统接口报错--{}", e);
                }
            }
        }
        currentBanlance.setScale(2, BigDecimal.ROUND_HALF_UP);
        resultMap.put("currentBanlance", currentBanlance);
        return resultMap;
    }

    /**
     * 
     * Description:调用账户系统获取剩余未还本金 罚息 违约金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    private ChatToGetRepayParamsResponseMessage chatToGetRepayStartParams(String orderId) throws GenerallyException {
        logger.info("---调用账务系统接口getFactorys---参数--{}", orderId);
        String response;
        ChatToGetRepayParamsResponseMessage crp = null;
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("orderId", orderId);
            response = SimpleHttpUtils.httpPost(
                    urlConfig.getRepayParamsUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(request), urlConfig.getCommonPrivateKey()));
            logger.info("---调用账务系统接口getFactorys---返回--{}" + JSONObject.toJSONString(response));

            if (StringUtil.isEmpty(response)) {
                throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("---调用账务系统接口getFactorys---报错{}:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.B);
        }
        try {
            crp = JSONObject.parseObject(response, ChatToGetRepayParamsResponseMessage.class);
        } catch (Exception e) {
            logger.error("---调用账务系统接口getFactorys---解析返回结果报错{}  :", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.C);
        }
        return crp;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月22日
     */
    @Override
    public Map<String, String> validateOrder(JSONObject objs) {
        Map<String, String> resultMap = new HashMap<>();
        String orderId = objs.getString("orderId");
        String orderIdX = orderId + "X";

        if (StringUtil.isNotEmpty(orderIdX)) {
            OrderBean orderBean = orderDao.selByOrderId(orderIdX);
            if (orderBean != null) {
                resultMap.put("flag", "Y");
            } else {
                resultMap.put("flag", "N");
            }
        }
        return resultMap;
    }
}
