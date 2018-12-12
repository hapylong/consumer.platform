/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月16日下午2:07:30 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseAssetBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseOrderBean;
import com.iqb.consumer.asset.allocation.assetinfo.dao.HouseAssetDao;
import com.iqb.consumer.asset.allocation.assetinfo.dao.HouseOrderDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Component
public class HouseAssetBiz extends BaseBiz {
    @Resource
    private HouseAssetDao houseAssetDao;
    @Resource
    private HouseOrderDao houseOrderDao;

    /**
     * 
     * Description:根据条件查询房贷资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public List<HouseAssetBean> selectHouseAssetByParams(JSONObject objs) {
        setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return houseAssetDao.selectHouseAssetByParams(objs);
    }

    /**
     * 
     * Description:根据条件查询房贷资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public HouseAssetBean selectHouseAssetDetailByParams(JSONObject objs) {
        setDb(1, super.MASTER);
        return houseAssetDao.selectHouseAssetDetailByParams(objs);
    }

    /**
     * 
     * Description:根据订单号查询房贷订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月18日
     */
    public HouseOrderBean selectHouseOrderByParams(JSONObject objs) {
        setDb(1, super.MASTER);
        return houseOrderDao.selectHouseOrderByParams(objs);
    }

    /**
     * 更新房贷订单资产分配信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月22日
     */
    public int updateHouseOrderByOrderNo(JSONObject objs) {
        setDb(0, super.MASTER);
        return houseOrderDao.updateHouseOrderByOrderNo(objs);
    }
}
