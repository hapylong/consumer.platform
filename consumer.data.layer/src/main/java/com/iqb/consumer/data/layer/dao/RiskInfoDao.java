package com.iqb.consumer.data.layer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskResultBean;

@Component("riskInfoDao")
public interface RiskInfoDao {

    /**
     * 根据手机号获取用户信息
     */
    public RiskInfoBean getRiskInfoByRegId(@Param("regId") String regId, @Param("riskType") String merchType);

    /**
     * 
     * Description: 保存风控结果
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午12:38:21
     */
    public Integer saveRiskResultInfo(RiskResultBean riskResultBean);

    /**
     * 保存风控信息
     * 
     * @param riskInfoBean
     * @return
     */
    Long saveRiskInfo(RiskInfoBean riskInfoBean);

    /**
     * 修改风控信息
     * 
     * @param riskInfoBean
     * @return
     */
    int updateCheckInfo(RiskInfoBean riskInfoBean);

    /**
     * 
     * Description: 更新风控结果状态
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午12:43:45
     */
    public Integer updateRiskResultInfo(RiskResultBean riskResultBean);

    /**
     * 
     * Description: 获取风控结果列表
     * 
     * @param
     * @return List<RiskInfoBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午2:17:49
     */
    public List<RiskResultBean> getRiskResultInfoList(RiskResultBean riskResultBean);

    public RiskInfoBean getRiskInfoByRIAndRT(@Param("regId") String regId, @Param("riskType") Integer riskType);

    public void updateLocalRiskInfo(OrderBean orderBean);

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
    public List<RiskInfoBean> getRiskListInfoByOrderId(@Param("regId") String regId, @Param("bizType") String bizType);
}
