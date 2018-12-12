package com.iqb.consumer.batch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.data.pojo.LocalRiskInfoBean;
import com.iqb.consumer.batch.data.pojo.RiskInfoBean;
import com.iqb.consumer.batch.data.pojo.RiskResultBean;

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

    /**
     * 
     * Description: 获取发往风控风控结果列表
     * 
     * @param
     * @return List<RiskInfoBean>
     * @throws
     * @Author chengzhen Create Date: 2018年3月2日 15:18:52
     */
    public List<RiskResultBean> getRiskResultInfoList2(RiskResultBean riskResultBean);

    /**
     * 
     * Description: 保存编号到inst_riskresult中
     * 
     * @param
     * @return RiskInfoBean
     * @throws
     * @Author chengzhen Create Date: 2018年3月2日 15:46:53
     */
    public void saveReportNo(LocalRiskInfoBean rrb);

    public void updateLocalRiskInfo(LocalRiskInfoBean localBean);

}
