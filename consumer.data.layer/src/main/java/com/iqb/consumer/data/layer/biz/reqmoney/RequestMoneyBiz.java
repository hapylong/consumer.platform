/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月27日 下午4:17:41
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.reqmoney;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.dao.RequestMoneyDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class RequestMoneyBiz extends BaseBiz {

    @Resource
    private RequestMoneyDao requestMoneyDao;

    public List<RequestMoneyBean> getAllRequest(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        // 开始分页,采用MyBatis分页插件分页,会在下一句查询中自动启用分页机制,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        List<RequestMoneyBean> list = requestMoneyDao.getAllRequest(objs);
        if (!CollectionUtils.isEmpty(list)) {
            for (RequestMoneyBean bean : list) {
                BigDecimal sbAmt = new BigDecimal(bean.getApplyAmt() != null ? bean.getApplyAmt() : "0");
                sbAmt = sbAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
                bean.setApplyAmt(sbAmt.toString());
            }
        }
        return list;
    }

    public long insertReqMoney(RequestMoneyBean requestMoneyBean) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return requestMoneyDao.insertReqMoney(requestMoneyBean);
    }

    /**
     * 
     * Description:查询资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月4日
     */
    public List<RequestMoneyBean> getAllotDetailByOrderId(JSONObject objs) {
        setDb(1, super.SLAVE);
        List<RequestMoneyBean> list = requestMoneyDao.getAllotDetailByOrderId(objs);
        for (RequestMoneyBean requestMoneyBean : list) {
            if (requestMoneyBean.getApplyInstIDay() != 0) {
                requestMoneyBean.setApplyItems(requestMoneyBean.getApplyItems() + 1);
            }
        }
        return list;
    }

    /**
     * 
     * Description:资产分配明细查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月5日
     */
    public List<RequestMoneyBean> getAllReqAllotMoney(JSONObject objs, boolean isPage) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        if (isPage) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<RequestMoneyBean> list = requestMoneyDao.getAllReqAllotMoney(objs);
        if (!CollectionUtils.isEmpty(list)) {
            for (RequestMoneyBean bean : list) {
                BigDecimal sbAmt = new BigDecimal(bean.getApplyAmt() != null ? bean.getApplyAmt() : "0");
                sbAmt = sbAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
                bean.setApplyAmt(sbAmt.toString());
            }
        }
        return list;
    }

    /**
     * 
     * Description:根据订单号查询资产分配来源 上标id
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月13日
     */
    public RequestMoneyBean getRequestMoneyByOrderId(Map<String, Object> params) {
        setDb(1, super.SLAVE);
        return requestMoneyDao.getRequestMoneyByOrderId(params);
    }

    @SuppressWarnings("rawtypes")
    public Map getAllRequestTotal(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return requestMoneyDao.getAllRequestTotal(objs);
    }

    @SuppressWarnings("rawtypes")
    public Map getAllReqAllotMoneyTotal(JSONObject objs, boolean b) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return requestMoneyDao.getAllReqAllotMoneyTotal(objs);
    }

    /**
     * 
     * Description:资产赎回查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    public List<RequestMoneyBean> geAllotRedemptionInfo(JSONObject objs, boolean isPage) {
        // 设置数据源为从库
        setDb(1, super.SLAVE);
        if (isPage) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<RequestMoneyBean> list = requestMoneyDao.geAllotRedemptionInfo(objs);
        for (RequestMoneyBean requestMoneyBean : list) {
            BigDecimal sbAmt =
                    new BigDecimal(requestMoneyBean.getApplyAmt() != null ? requestMoneyBean.getApplyAmt() : "0");
            sbAmt = sbAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
            requestMoneyBean.setApplyAmt(sbAmt.toString());
            if (requestMoneyBean.getApplyInstIDay() != 0) {
                requestMoneyBean.setApplyItems(requestMoneyBean.getApplyItems() + 1);
            }
        }
        return list;
    }

    /**
     * 
     * Description:更新资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    public int updateRequestmoneyById(RequestMoneyBean bean) {
        setDb(0, super.SLAVE);
        return requestMoneyDao.updateRequestmoneyById(bean);
    }

    /**
     * 
     * Description:根据id查询资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    public RequestMoneyBean getRequestMoneyById(Integer id) {
        setDb(1, super.SLAVE);
        return requestMoneyDao.getRequestMoneyById(id);
    }
}
