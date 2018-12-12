package com.iqb.consumer.data.layer.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskResultBean;
import com.iqb.consumer.data.layer.dao.RiskInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class RiskInfoBiz extends BaseBiz {

    private static final Logger logger = LoggerFactory.getLogger(RiskInfoBiz.class);
    private static final String UTF_8 = "utf-8";
    private static final String RiskCodeSucc = "10008";

    private static final String RiskCodeRefused = "10009";

    @Resource
    private RiskInfoDao riskInfoDao;

    /**
     * 保存风控信息
     * 
     * @param riskInfoBean
     * @return
     */
    public Long saveRiskInfo(RiskInfoBean riskInfoBean) {
        setDb(0, super.MASTER);
        return riskInfoDao.saveRiskInfo(riskInfoBean);
    }

    // 根据手机号获取用户信息
    public RiskInfoBean getRiskInfoByRegId(String regId, String merchType) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return riskInfoDao.getRiskInfoByRegId(regId, merchType);
    }

    /**
     * 
     * Description: 获取需要风控的信息列表
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午12:38:05
     */
    public List<RiskResultBean> getRiskResultNeedSendList(RiskResultBean riskResultBean) {
        setDb(0, super.SLAVE);
        return riskInfoDao.getRiskResultInfoList(riskResultBean);
    }

    /**
     * 
     * Description: 保存风控结果
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午12:38:05
     */
    public Integer saveRiskResultInfo(RiskResultBean riskResultBean) {
        setDb(0, super.MASTER);
        return riskInfoDao.saveRiskResultInfo(riskResultBean);
    }

    /**
     * 更新风控信息
     * 
     * @param riskInfoBean
     * @return
     */
    public int updateCheckInfo(RiskInfoBean riskInfoBean) {
        setDb(0, super.MASTER);
        return riskInfoDao.updateCheckInfo(riskInfoBean);
    }

    /**
     * 
     * Description: 更新风控结果
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午12:38:05
     */
    public Integer updateRiskResultInfo(RiskResultBean riskResultBean) {
        setDb(0, super.MASTER);
        return riskInfoDao.updateRiskResultInfo(riskResultBean);
    }

    /**
     * 
     * Description: 处理风控结果信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午2:26:11
     */
    public Integer dealRiskResultSchedule(Integer reqTimes, String noticeUrl) {
        int i = 0;
        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setReqTimes(reqTimes);
        List<RiskResultBean> riskResultList = this.getRiskResultNeedSendList(riskResultBean);

        if (CollectionUtils.isEmpty(riskResultList)) {
            return 0;
        }
        logger.info("查询到数据库中需要调用风控接口的数据:{}", JSONObject.toJSONString(riskResultList));
        for (RiskResultBean riskResultBeanE : riskResultList) {
            try {
                String result =
                        HttpsClientUtil.getInstance().doPost(riskResultBeanE.getSendUrl(),
                                riskResultBeanE.getContent(), "utf-8");// 调用风控接口发送了短信
                logger.info("正在处理风控结果，订单号为:{}，风控处理结果为:{}", riskResultBeanE.getOrderId(), result);
                Map resMap = JSONObject.parseObject(result);
                // JSONArray resList = JSONObject.parseArray(result);
                // JSONObject resMap = (JSONObject) JSONObject.parse(resList.getString(0));
                String retVal = resMap.get("return") + "";
                logger.info("调用风控CheckOrder接口返回return码 ： " + retVal);
                if (RiskCodeSucc.equals(retVal)) {
                    riskResultBeanE.setStatus(1);
                    i++;
                } else if (RiskCodeRefused.equals(retVal)) {// 拒绝
                    riskResultBeanE.setStatus(1);
                    i++;
                    // 风控直接拒绝订单，触发拒单操作
                    HttpsClientUtil.getInstance().doPost2(noticeUrl,
                            "{\"orderId\":\"" + riskResultBeanE.getOrderId() + "\",\"riskStatus\":\"" + 2 + "\"}",
                            UTF_8);
                }
            } catch (Exception e) {
                riskResultBeanE.setStatus(2);
                logger.error("调用读脉出现异常：" + e.getMessage());
            }
            setDb(0, super.MASTER);
            riskInfoDao.updateRiskResultInfo(riskResultBeanE);
        }
        return i;
    }

    public RiskInfoBean getRiskInfoByRIAndRT(String regId, Integer riskType) {
        setDb(0, super.SLAVE);
        return riskInfoDao.getRiskInfoByRIAndRT(regId, riskType);
    }

    public void updateLocalRiskInfo(OrderBean orderBean) {
        setDb(0, super.SLAVE);
        riskInfoDao.updateLocalRiskInfo(orderBean);
    }

    /**
     * 
     * 根据手机号查询比当前系统时间小于7天的风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月2日
     */
    @SuppressWarnings("rawtypes")
    public List<RiskInfoBean> getRiskListInfoByOrderId(@Param("regId") String regId, @Param("bizType") String bizType) {
        setDb(0, super.SLAVE);
        return riskInfoDao.getRiskListInfoByOrderId(regId, bizType);
    }
}
