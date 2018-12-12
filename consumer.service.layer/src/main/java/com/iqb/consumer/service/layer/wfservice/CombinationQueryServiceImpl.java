/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 上午10:29:26
 * @version V1.0
 */
package com.iqb.consumer.service.layer.wfservice;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.data.layer.biz.CombinationQueryBiz;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.reqmoney.RequestMoneyBiz;
import com.iqb.consumer.data.layer.biz.wf.OrderOtherInfoBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class CombinationQueryServiceImpl extends BaseService implements CombinationQueryService {
    private static final Logger logger = LoggerFactory.getLogger(CombinationQueryServiceImpl.class);
    @Autowired
    private CombinationQueryBiz combinationQueryBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private RequestMoneyBiz requestMoneyBiz;
    @Resource
    private OrderOtherInfoBiz orderOtherInfoBiz;

    @Autowired
    private DictService dictServiceImpl;

    @Override
    public CombinationQueryBean getByOrderId(String orderId) {
        CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(orderId);
        if (cqb != null) {
            if (StringUtil.isEmpty(cqb.getAmtLines())) {
                cqb.setUseCreditLoan(0);
            } else {
                cqb.setUseCreditLoan(1);
            }
        }
        return cqb;
    }

    @Override
    public void updateStatus(String orderId, String status) {
        orderBiz.updateStatus(orderId, status);
    }

    @Override
    public int updateOrderInfo(OrderBean orderBean) {
        return orderBiz.updateOrderInfo(orderBean);
    }

    @Override
    public OrderBean selectOne(Map<String, Object> params) {
        return orderBiz.selectOne(params);
    }

    @Override
    public List<PlanBean> getPlanByMerNo(String merchantNo) {
        return qrCodeAndPlanBiz.getPlanByMerNo(merchantNo);
    }

    @Override
    public PlanBean getPlanByID(long id) {
        return qrCodeAndPlanBiz.getPlanByID(id);
    }

    @Override
    public PageInfo<RequestMoneyBean> getAllRequest(JSONObject objs) {
        // 获取商户权限的Objs
        // objs = getMerchLimitObject(objs);
        String allotStatus = objs.getString("allotStatus");
        if (StringUtil.isNull(allotStatus)) {
            objs.put("allotStatus", "0");
        }
        // 分页插件需要对外包装为PageInfo<T>类型,其中会额外封装一些分页参数(如：total)
        return new PageInfo<>(requestMoneyBiz.getAllRequest(objs));
    }

    @Override
    public long insertReqMoney(RequestMoneyBean requestMoneyBean) {
        return requestMoneyBiz.insertReqMoney(requestMoneyBean);
    }

    @Override
    public int updateOrderOtherInfo(OrderOtherInfo orderOtherInfo) {
        return orderOtherInfoBiz.updateOrderOtherInfo(orderOtherInfo);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月4日
     */
    @Override
    public List<RequestMoneyBean> getAllotDetailByOrderId(JSONObject objs) {
        return requestMoneyBiz.getAllotDetailByOrderId(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月5日
     */
    @Override
    public PageInfo<RequestMoneyBean> getAllReqAllotMoney(JSONObject objs) {
        // 获取商户权限的Objs
        String allotStatus = objs.getString("allotStatus");
        if (StringUtil.isNull(allotStatus)) {
            objs.put("allotStatus", "0");
        }
        // 分页插件需要对外包装为PageInfo<T>类型,其中会额外封装一些分页参数(如：total)
        return new PageInfo<>(requestMoneyBiz.getAllReqAllotMoney(objs, true));
    }

    @Override
    public void exportAllReqAllotMoney(JSONObject requestMessage, HttpServletResponse response) {
        List<RequestMoneyBean> xlsx = null;
        try {
            String allotStatus = requestMessage.getString("allotStatus");
            if (StringUtil.isNull(allotStatus)) {
                requestMessage.put("allotStatus", "0");
            }
            xlsx = requestMoneyBiz.getAllReqAllotMoney(requestMessage, false);
        } catch (Exception e) {
            response.setStatus(500);
            return;
        }
        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = convertReqAllotMoneyXlsx(xlsx);
            String fileName = "attachment;filename=allReqAllotMoney-" + DateTools.getYmdhmsTime() + ".xls";
            response.setContentType("application/vnd.ms-excel");

            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
        }
    }

    private HSSFWorkbook convertReqAllotMoneyXlsx(List<RequestMoneyBean> list) throws Exception {
        String[] header =
        {"项目名称", "机构名称", "预计放款时间", "资产分配时间", "资产到期日", "标的到期日", "借款人", "订单号", "订单金额", "上标金额", "产品方案", "订单日期", "期数",
                "剩余期数", "订单状态", "资金来源",};
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
        dictServiceImpl.changeDictValue(list);
        for (int i = 0; i < list.size(); i++) {
            hr = hs.createRow(i + 1);
            xu.append(hr, 0, list.get(i).getProjectName());
            xu.append(hr, 1, list.get(i).getMerchName());

            String tmp = list.get(i).getPlanLendingTime();
            if (tmp == null)
                tmp = "";
            else {
                tmp = tmp.trim();
                if (tmp.length() >= 10)
                    tmp = tmp.substring(0, 4) + "年" + tmp.substring(5, 7) + "月" + (tmp + "0").substring(8, 10) + "日";
            }
            xu.append(hr, 2, tmp);

            tmp = list.get(i).getApplyTime();
            if (tmp == null)
                tmp = "";
            else {
                tmp = tmp.trim();
                if (tmp.length() >= 10)
                    tmp = tmp.substring(0, 4) + "年" + tmp.substring(5, 7) + "月" + (tmp + "0").substring(8, 10) + "日";
            }
            xu.append(hr, 3, tmp);

            if (list.get(i).getAssetDueDate() == null)
                tmp = "";
            else {
                tmp = DateUtil.getDateString(list.get(i).getAssetDueDate(), DateUtil.SHORT_DATE_FORMAT_CN);
            }

            xu.append(hr, 4, tmp);

            tmp = list.get(i).getDeadline();
            if (tmp == null)
                tmp = "";
            else {
                tmp = tmp.trim();
                if (tmp.length() >= 10)
                    tmp = tmp.substring(0, 4) + "年" + tmp.substring(5, 7) + "月" + (tmp + "0").substring(8, 10) + "日";
            }

            xu.append(hr, 5, tmp);
            xu.append(hr, 6, list.get(i).getRealName());

            xu.append(hr, 7, list.get(i).getOrderId());
            xu.append(hr, 8, list.get(i).getOrderAmt());
            xu.append(hr, 9, list.get(i).getApplyAmt());
            xu.append(hr, 10, list.get(i).getPlanFullName());

            if (list.get(i).getCreateTime() == null)
                tmp = "";
            else {
                tmp = DateUtil.getDateString(list.get(i).getCreateTime(), DateUtil.SHORT_DATE_FORMAT_CN);
            }
            xu.append(hr, 11, tmp);

            xu.append(hr, 12, list.get(i).getOrderItems());
            xu.append(hr, 13, list.get(i).getLeftItems());
            xu.append(hr, 14, getRisk2Excel(list.get(i).getRiskStatus(), list.get(i).getWfStatus()));
            xu.append(hr, 15, list.get(i).getSourcesFunding());
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    /**
     * 资产分配总个数,金额
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    @Override
    public Map getAllRequestTotal(JSONObject objs) {
        // 获取商户权限的Objs
        String allotStatus = objs.getString("allotStatus");
        if (StringUtil.isNull(allotStatus)) {
            objs.put("allotStatus", "0");
        }
        return requestMoneyBiz.getAllRequestTotal(objs);
    }

    @Override
    public Map getAllReqAllotMoneyTotal(JSONObject objs) {
        // 获取商户权限的Objs
        String allotStatus = objs.getString("allotStatus");
        if (StringUtil.isNull(allotStatus)) {
            objs.put("allotStatus", "0");
        }
        return requestMoneyBiz.getAllReqAllotMoneyTotal(objs, true);
    }

    /**
     * 查询资产赎回分页信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月12日
     */
    @Override
    public PageInfo<RequestMoneyBean> geAllotRedemptionInfo(JSONObject objs) {
        // 分页插件需要对外包装为PageInfo<T>类型,其中会额外封装一些分页参数(如：total)
        return new PageInfo<>(requestMoneyBiz.geAllotRedemptionInfo(objs, true));
    }

    /**
     * 资产赎回
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月12日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int allotRedemption(JSONObject objs) throws IqbException {
        int result = 0;

        if (objs.getString("redemptionDate") == null || objs.getInteger("id") == 0) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000005);
        }
        // 设置写锁
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.writeLock().lock();
        // 资产赎回时间
        String redemptionDate = objs.getString("redemptionDate");

        RequestMoneyBean requestBean = requestMoneyBiz.getRequestMoneyById(objs.getInteger("id"));
        if (requestBean != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", requestBean.getOrderId());
            OrderBean orderBean = orderBiz.selectOne(params);
            // 计算时间差值 = (标的到期日-赎回时间)+剩余天数
            long days = DateUtil.differenceDays(redemptionDate, requestBean.getDeadline());
            // 资产赎回时间与资产到期日的差值
            long dayA = DateUtil.differenceDays(requestBean.getApplyTime(), redemptionDate);
            if (dayA > 0) {
                if (days > 0) {
                    days += orderBean.getLeftInstIDay();
                    Map<String, Long> map = DateUtil.getMonthAndDays(days);
                    RequestMoneyBean bean = JSONObject.toJavaObject(objs, RequestMoneyBean.class);
                    // 更新资产分配信息表与订单表
                    result = requestMoneyBiz.updateRequestmoneyById(bean);
                    result += updateOrderInfo(requestBean.getOrderId(), map);
                } else {
                    result = -1;
                }
            } else {
                result = -2;
            }
        }
        lock.writeLock().unlock();
        return result;
    }

    /**
     * 
     * Description:更新订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    private int updateOrderInfo(String orderId, Map<String, Long> map) {
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(orderId);
        orderBean.setLeftInstIMonth(((Long) map.get("months")).intValue());
        orderBean.setLeftInstIDay(((Long) map.get("day")).intValue());
        return orderBiz.updateOrderInfoForLeftMonthByOrderId(orderBean);
    }

    private String getRisk2Excel(Integer riskStatus, Integer wfStatus) {
        String result = "";
        if (null == riskStatus) {
            return "审核通过";
        }
        switch (riskStatus) {
            case 1:
                result = "审核拒绝";
                break;
            case 2:
                result = "审核中";
                break;
            case 3:
                result = "已分期";
                break;
            case 4:
                if (wfStatus > 4 || wfStatus == 0 || wfStatus == null) {
                    result = "待付款";
                } else if (wfStatus == 4) {
                    result = "审核中";
                }
                break;
            case 6:
                result = "已取消";
                break;
            case 21:
                result = "待估价";
                break;
            case 22:
                result = "已估价";
                break;
            case 7:
                result = "已放款";
                break;
            case 5:
                result = "待确认";
                break;
            case 10:
                result = "已结清";
                break;
            case 11:
                result = "已终止";
                break;
            default:
                result = "审核通过";
                break;
        }
        return result;
    }
}
