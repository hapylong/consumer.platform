package com.iqb.consumer.data.layer.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.dao.QrCodeAndPlanDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Component
public class QrCodeAndPlanBiz extends BaseBiz {

    @Resource
    private QrCodeAndPlanDao qrCodeAndPlanDao;
    @Resource
    private RedisPlatformDao redisPlatformDao;

    // 插入计划，返回计划编码
    public int insertPlan(PlanBean planBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return qrCodeAndPlanDao.insertPlan(planBean);
    }

    // 修改计划
    public int updatePlan(PlanBean planBean) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        return qrCodeAndPlanDao.updatePlan(planBean);
    }

    // 根据商户号查询分期计划
    public List<PlanBean> getPlanByMerNo(String merchantNo) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return qrCodeAndPlanDao.getPlanByMerNo(merchantNo);
    }

    // 根据商户号和子类型查询分期计划
    public List<PlanBean> getPlByMerAndBType(String merchantNo, String bizType) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return qrCodeAndPlanDao.getPlByMerAndBType(merchantNo, bizType);
    }

    // 根据商户号列表、期数查询分期计划
    public List<PlanBean> getPlanByMerNos(Map<String, Object> map) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return qrCodeAndPlanDao.getPlanByMerNos(map);
    }

    // 根据计划号查询出计划
    public PlanBean getPlanByID(long id) {
        String key = "Plan." + id;
        String value = redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(value) || value.equals("null")) {// 从数据库中取
            // 设置数据源为从库
            setDb(0, super.SLAVE);
            PlanBean bean = qrCodeAndPlanDao.getPlanByID(id);
            value = JSON.toJSONString(bean);
            redisPlatformDao.setKeyAndValueTimeout(key, value, 1000 * 60 * 30);
        }
        PlanBean planBean = JSONObject.parseObject(value, PlanBean.class);

        return planBean;
    }

    // 根据ID删除计划
    public void deletePlanByID(long id) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        qrCodeAndPlanDao.deletePlanByID(id);
    }

    // 获取主键id列表
    public List<Long> getAllPlanIdList() {
        String key = "AllPlanId";
        String value = redisPlatformDao.getValueByKey(key);
        if (value == null) {// 从数据库中取
            setDb(0, super.SLAVE);
            List<Long> list = qrCodeAndPlanDao.getAllPlanIdList();
            value = JSON.toJSONString(list);
            redisPlatformDao.setKeyAndValue(key, value);
        }
        List<Long> result = JSONArray.parseArray(value, Long.class);
        return result;
    }
}
