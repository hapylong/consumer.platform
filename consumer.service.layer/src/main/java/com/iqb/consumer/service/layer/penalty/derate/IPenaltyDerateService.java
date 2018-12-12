package com.iqb.consumer.service.layer.penalty.derate;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 罚息减免服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public interface IPenaltyDerateService {

    /**
     * 
     * Description: 获取罚息减免申请列表
     * 
     * @param
     * @return PageInfo<PenaltyDerateBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午3:05:08
     */
    public PageInfo<PenaltyDerateBean> listPenaltyDerateApply(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 获取可减免列表
     * 
     * @param
     * @return PageInfo<Map<String,Object>>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午3:58:05
     */
    public PageInfo<Map<String, Object>> listPenaltyDeratable(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 保存罚息减免申请(并处理工作流)
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午2:52:47
     */
    public Integer savePenaltyDeratableAndStartWF(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 保存罚息减免申请
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:12:21
     */
    public Integer savePenaltyDeratable(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 更新罚息减免申请状态
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:43:45
     */
    public Integer updatePenaltyDeratableApplyStatus(PenaltyDerateBean penaltyDerateBean) throws IqbException;

    /**
     * 
     * Description: 执行罚息减免(更新数据库此次申请状态并调用账户系统减免接口)
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月14日 上午11:15:04
     */
    public Integer doPenaltyDerate(PenaltyDerateBean penaltyDerateBean) throws IqbException;

    /**
     * 
     * Description: 根据主键id获取减免记录信息
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月15日 下午2:29:34
     */
    public PenaltyDerateBean getPenaltyDerateById(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据uuid获取罚息减免信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午3:02:27
     */
    public PenaltyDerateBean getPenaltyDerateByUUId(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据installDetailId获取罚息减免信息
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午8:58:56
     */
    public PenaltyDerateBean getPenaltyDerateByInstallDetailId(JSONObject objs) throws IqbException;

    public void retrievePenaltyDerate(String procBizId) throws IqbException;

    /**
     * 
     * Description:提前申请流程校验订单在罚息减免流程中是否发起
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月29日
     */
    public PenaltyDerateBean getPenaltyDerateBeanByOrderId(String orderId);

    /**
     * 
     * Description:按格式生成自增序列
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月10日
     */
    public String getSeqFromRedis(String key, boolean isSub);
}
