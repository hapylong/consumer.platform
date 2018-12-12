package com.iqb.consumer.data.layer.biz.dealer;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.dealer.DealerManage;
import com.iqb.consumer.data.layer.dao.dealer.DealerManageDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class DealerManageBiz extends BaseBiz {

    @Resource
    private DealerManageDao dealerManageDao;

    /**
     * 保存车商数据信息
     * 
     * @return
     */
    public int saveDealerManageInfo(DealerManage dealerManage) {
        setDb(0, super.MASTER);
        return dealerManageDao.saveDealerManageInfo(dealerManage);
    }

    /**
     * 查询黑名单信息By客户渠道名称
     * 
     * @param params
     * @return
     */
    public int judgeBlackInfo(Map<String, Object> params) {
        setDb(0, super.SLAVE);
        return dealerManageDao.judgeBlackInfo(params);
    }

    /**
     * 修改车商信息
     * 
     * @param dealerManage
     * @return
     */
    public int updateDealerManageInfo(DealerManage dealerManage) {
        setDb(0, super.MASTER);
        return dealerManageDao.updateDealerManageInfo(dealerManage);
    }

    /**
     * 通过查询条件查询车商
     * 
     * @param params
     * @return
     */
    public DealerManage getDealerInfo(Map<String, Object> params) {
        setDb(0, super.SLAVE);
        return dealerManageDao.getDealerInfo(params);
    }

    /**
     * 根据车商id批量修改车商状态(黑名单)
     * 
     * @param objs
     * @param request
     * @returnO
     */
    public int batchUpdateDealerManageInfoStatus(JSONObject objs) {
        String id = objs.getString("ids");
        String[] ids = id.split(",");
        objs.put("ids", ids);
        return dealerManageDao.batchUpdateDealerManageInfoStatus(objs);
    }

    /**
     * 通过车商名称查询订单列表
     * 
     * @param sourceCarName
     * @return
     */
    public List<String> getAllOrderIds(String sourceCarName) {
        setDb(0, super.SLAVE);
        return dealerManageDao.getAllOrderIds(sourceCarName);
    }
}
