package com.iqb.consumer.data.layer.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.dao.MerchantBeanDao;
import com.iqb.consumer.data.layer.dao.QrCodeAndPlanDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;
import com.iqb.etep.common.utils.JSONUtil;

@Component
public class ProductBiz extends BaseBiz {
    protected static Logger logger = LoggerFactory.getLogger(ProductBiz.class);

    @Resource
    private QrCodeAndPlanDao qrCodeAndPlanDao;
    @Resource
    private MerchantBeanDao merchantBeanDao;

    @Resource
    private MerchantBeanBiz merchantBiz;

    /*
     * Description: 新增商户产品信息
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public int insertPlan(JSONObject objs) {
        // // 权限树修改
        // objs.put("merchantShortName", objs.getString("merchantNo"));
        // String merchantNo = merchantBiz.getMerCodeByMerSortName(objs);
        // objs.put("merchantNo", merchantNo);
        // // objs.put("merchantNo", merchantNo);
        // objs.remove("merchantShortName");
        // objs.put("version", 1);
        // objs.put("createTime", new Date());
        // objs.put("updateTime", null);
        // PlanBean planBean = JSONObject.parseObject(objs.toJSONString(),
        // PlanBean.class);
        // // PlanBean planBean = JSONUtil.toJavaObject(objs, PlanBean.class);
        // planBean = addShortAndFullName(planBean); // 添加计划简称、全称
        // // planBean.setPlanId(1);
        // return qrCodeAndPlanDao.insertPlan(planBean);

        int result = 0;

        /* 商户号 */
        // JSONObject objs4merchantNo = new JSONObject();
        // objs4merchantNo.put("merchantShortName", objs.getString("merchantNo"));
        String merchantNo = objs.getString("merchantNo");
        /* 业务类型 */
        String bizType = objs.getString("bizType");
        /* 方案列表 */
        List<Map> list = (List<Map>) objs.get("list");
        result = list.size();
        for (int i = 0; i < list.size(); i++) {
            PlanBean planBean = JSONObject.parseObject(JSON.toJSONString(list.get(i)), PlanBean.class);

            planBean.setMerchantNo(merchantNo);
            planBean.setBizType(bizType);
            planBean.setVersion(1);
            planBean.setCreateTime(new Date());
            planBean.setUpdateTime(null);
            planBean = addShortAndFullName(planBean); // 添加计划简称、全称
            setDb(0, super.MASTER);
            result = result - qrCodeAndPlanDao.insertPlan(planBean);
        }

        return result;
    }

    /*
     * Description: 修改商户产品信息
     */
    public int updatePlan(JSONObject objs) {
        PlanBean planBean = JSONUtil.toJavaObject(objs, PlanBean.class);
        planBean = addShortAndFullName(planBean); // 更新计划简称、全称
        return qrCodeAndPlanDao.updatePlan(planBean);
    }

    /*
     * Description: 删除商户产品信息
     */
    public void deletePlan(JSONObject objs) {
        qrCodeAndPlanDao.deletePlanByID(Long.valueOf(objs.getLongValue("id")));
    }

    /*
     * Description: 根据计划号查询计划
     */
    public PlanBean getPlanByID(JSONObject objs) {
        return qrCodeAndPlanDao.getPlanByID(Long.valueOf(objs.getLongValue("id")));
    }

    /*
     * Description: 获取商户产品信息
     */
    public List<PlanBean> getPlanByMerNos(JSONObject objs, boolean flag) throws IqbSqlException, IqbException {
        // List<String> merLists = new ArrayList<String>();
        // String merCodeDef = objs.getString("merCodeDef");
        // String merCodeSel = objs.getString("merCodeSel");
        String installPeriods = objs.getString("installPeriods");

        // if (merCodeSel == null || merCodeSel.equals("")) {
        // merLists = getMerListByMerCodeList(merCodeDef);
        // } else {
        // // 权限树修改
        // objs.put("merchantShortName", objs.getString("merCodeSel"));
        // merCodeSel = merchantBiz.getMerCodeByMerSortName(objs);
        //
        // merLists.add(merCodeSel);
        // }
        if (installPeriods == null || installPeriods.equals("")) {
            installPeriods = null;
        }

        if (flag) { // 分页
            PageHelper.startPage(getPagePara(objs));
        }

        // objs.put("merchantNos", merLists);
        objs.put("installPeriods", installPeriods);
        objs.put("status", objs.getString("status"));
        objs.put("bizType", objs.getString("bizType1") != null ? objs.getString("bizType1") : objs.getString("bizType"));
        List<PlanBean> list = qrCodeAndPlanDao.getPlanByMerNos(objs);

        return list;
    }

    /**
     * 
     * Description: 拼接商户计划简称、全称
     * 
     * @param
     * @return
     * @throws @Author
     */

    private PlanBean addShortAndFullName(PlanBean planBean) {
        String shortName = "期数:" + planBean.getInstallPeriods() + "期";
        String fullName = "";
        if (planBean.getDownPaymentRatio() != 0) {
            fullName += "首付" + planBean.getDownPaymentRatio() + "%";
        }
        if (planBean.getFeeYear() > 0) {
            if ("".equals(fullName)) {
                fullName += "上收息(" + planBean.getFeeYear() + "个月)";
            } else {
                fullName += "+上收息(" + planBean.getFeeYear() + "个月)";
            }
        }
        if (planBean.getServiceFeeRatio() != 0) {
            if ("".equals(fullName)) {
                fullName += "服务费" + planBean.getServiceFeeRatio() + "%";
            } else {
                fullName += "+服务费" + planBean.getServiceFeeRatio() + "%";
            }
        }
        if (planBean.getMarginRatio() != 0) {
            if ("".equals(fullName)) {
                fullName += "保证金" + planBean.getMarginRatio() + "%";
            } else {
                fullName += "+保证金" + planBean.getMarginRatio() + "%";
            }
        }
        if (planBean.getTakePayment() == 1) {// 收月供
            if ("".equals(fullName)) {
                fullName += "上收月供";
            } else {
                fullName += "+上收月供";
            }
        }
        if (StringUtil.isEmpty(fullName)) {
            fullName = shortName;
        }
        planBean.setPlanShortName(shortName);
        planBean.setPlanFullName(fullName);
        if (planBean.getRemark() == null || "".equals(planBean.getRemark())) {
            planBean.setRemark(shortName);
        }
        return planBean;
    }

    /**
     * 
     * Description: 获取商户所有子商户（包含商户本身）
     * 
     * @param
     * @return String
     * @throws IqbException
     * @throws IqbSqlException
     * @throws @Author
     */

    @SuppressWarnings("unused")
    private String getMerListByMerCode(String merCode) throws IqbSqlException, IqbException {
        JSONObject objs = new JSONObject();
        objs.put("merCode", merCode);
        List<MerchantBean> list = merchantBiz.getAllMerByMerNo(objs);
        List<Map<String, String>> listMc = new ArrayList<Map<String, String>>();
        for (MerchantBean mc : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", mc.getMerchantNo());
            map.put("value", mc.getMerchantShortName());
            listMc.add(map);
        }
        String listJson = JSON.toJSONString(listMc, true);

        return listJson;
    }

    /**
     * 
     * Description: 获取商户所有子商户（包含商户本身）
     * 
     * @param
     * @return List
     * @throws IqbException
     * @throws IqbSqlException
     * @throws @Author
     */
    @SuppressWarnings("unused")
    private List<String> getMerListByMerCodeList(String merCode) throws IqbSqlException, IqbException {
        JSONObject objs = new JSONObject();
        objs.put("id", merCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        List<String> listMc = new ArrayList<String>();
        for (MerchantBean mc : list) {
            listMc.add(mc.getMerchantNo());
        }

        return listMc;
    }

    public int updateStatus(List<String> ids, String status) {
        setDb(0, super.MASTER);
        return qrCodeAndPlanDao.updateStatus(ids, status);
    }

    /**
     * Description:不同商户配置不同的支付主体,insert操作 2018年1月18日 10:34:23 chengzhen
     */
    public int addPayConf(JSONObject objs) {
        setDb(0, super.MASTER);
        int result = 0;
        result = qrCodeAndPlanDao.addPayConf(objs);
        return result;
    }

    /**
     * Description:不同商户配置不同的支付主体,del操作 2018年1月18日 11:15:10 chengzhen
     */
    public void delPayConf(JSONObject objs) {
        setDb(0, super.MASTER);
        qrCodeAndPlanDao.delPayConf(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,update操作 2018年1月18日 11:22:41 chengzhen
     */
    public void updatePayConf(JSONObject objs) {
        setDb(0, super.MASTER);
        qrCodeAndPlanDao.updatePayConf(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,单查询操作 2018年1月18日 11:33:01 chengzhen
     */
    public PayConfBean getPayConfByMno(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PayConfBean payConfBean = qrCodeAndPlanDao.getPayConfByMno(objs);
        MerchantBean merByMerBean = merchantBeanDao.getMerByMerNo(payConfBean.getMerchantNo());
        payConfBean.setMerchantName(merByMerBean.getMerchantShortName());
        return payConfBean;
    }
    /**
     * 根据商户号查询支付主体信息
     * 
     * @param objs
     * @return
     */
    public PayConfBean getPayConfByMno2(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return qrCodeAndPlanDao.getPayConfByMno2(objs);
    }
    
    /**
     * 根据ID查询支付主体信息
     * 
     * @param objs
     * @return
     */
    public PayConfBean getPayConfById(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return qrCodeAndPlanDao.getPayConfByMno(objs);
    }
    
    /**
     * Description:不同商户配置不同的支付主体,列表查询操作 2018年1月18日 11:33:01 chengzhen
     */
    public List<PayConfBean> queryPayConfList(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        List<PayConfBean> queryPayConfList = qrCodeAndPlanDao.queryPayConfList(objs);
        for (PayConfBean payConfBean : queryPayConfList) {
            String merchantNo = payConfBean.getMerchantNo();
            MerchantBean merByMerBean = merchantBeanDao.getMerByMerNo(merchantNo);
            payConfBean.setMerchantName(merByMerBean.getMerchantShortName());
            String bizOwner = payConfBean.getBizOwner();
            String payWay = payConfBean.getPayWay();
            if (!StringUtil.isEmpty(bizOwner)) {
                String chBizOwner = qrCodeAndPlanDao.getChinByCode("BIZ_OWNER", bizOwner);
                payConfBean.setBizOwner(chBizOwner);
            }
            if (!StringUtil.isEmpty(payWay)) {
                String chPayWay = qrCodeAndPlanDao.getChinByCode("PAY_WAY", payWay);
                payConfBean.setPayWay(chPayWay);
            }

        }
        return queryPayConfList;
    }
}
