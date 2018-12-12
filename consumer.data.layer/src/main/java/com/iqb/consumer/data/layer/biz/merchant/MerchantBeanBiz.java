package com.iqb.consumer.data.layer.biz.merchant;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.dao.MerchantBeanDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.JSONUtil;

@Component
public class MerchantBeanBiz extends BaseBiz {
    protected static Logger logger = LoggerFactory.getLogger(MerchantBeanBiz.class);

    @Resource
    private MerchantBeanDao merchantBeanDao;
    @Resource
    private RedisPlatformDao redisPlatformDao;

    /**
     * 查询所有加盟商
     * 
     * @param city
     * @return
     */
    public List<MerchantBean> getMerListByCity(JSONObject objs) {

        String city = objs.getString("city");
        if (city == null || city.equals("")) {
            city = null;
        }
        return merchantBeanDao.getMerListByCity(city);
    }

    /**
     * 查询通过商户ID所有子商户包含自己
     * 
     * @param id
     * @return
     */
    public List<MerchantBean> getAllMerByID(JSONObject objs) {
        return merchantBeanDao.getAllMerByID(objs.getString("id"));
    }

    public List<String> getAllMerchantNosByOrgCode(JSONObject objs) {
        return merchantBeanDao.getAllMerchantNosByOrgCode(objs.getString("id"));
    }

    /**
     * 通过商户号查询加盟商
     * 
     * @param merchantNo
     * @return
     */
    public List<MerchantBean> getAllMerByMerNo(JSONObject objs) {
        // 通过商户号查询ID
        MerchantBean mcb = getMerByMerNo(objs.getString("merchCode"));
        if (mcb != null) {
            objs.put("id", mcb.getId());
            return getAllMerByID(objs);
        }
        return null;
    }

    /**
     * 根据ID查询商户信息
     * 
     * @param id
     * @return
     */
    public MerchantBean getMerByID(JSONObject objs) {
        return merchantBeanDao.getMerByID(objs.getString("id"));
    }

    /**
     * 根据商户号查询商户信息
     * 
     * @param merchantNo
     * @return
     */
    public MerchantBean getMerByMerNo(String merchantNo) {
        return merchantBeanDao.getMerByMerNo(merchantNo);
    }

    /**
     * 新增商户
     * 
     * @param bean
     * @return
     */
    public int insertMerchantInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        MerchantBean bean = JSONUtil.toJavaObject(objs, MerchantBean.class);
        return merchantBeanDao.insertMerchantInfo(bean);
    }

    /**
     * 修改商户信息
     * 
     * @param bean
     */
    public void updateMerchantInfo(JSONObject objs) {
        // 暂时不实现修改
    }

    /**
     * 删除商户
     * 
     * @param bean
     */
    public void delMerchantInfo(JSONObject objs) {
        MerchantBean bean = JSONUtil.toJavaObject(objs, MerchantBean.class);
        merchantBeanDao.delMerchantInfo(bean);
    }

    /**
     * 根据商户简称查询商户号
     * 
     * @param merchantShortName
     * @return
     */
    public String getMerCodeByMerSortName(JSONObject objs) {
        return merchantBeanDao.getMerCodeByMerSortName(objs.getString("merchantShortName"));
    }

    /**
     * 根据商户简称查询商户号
     * 
     * @param merchantShortName
     * @return
     */
    public List<MerchantBean> getMerCodeByMerSortNameList(JSONObject objs) {
        String merchNames = objs.getString("merchNames");
        String[] mNames = merchNames.split(",");
        return merchantBeanDao.getMerCodeByMerSortNameList(mNames);
    }

    public List<String> getAllMerchantNosByNames(JSONObject jo) {
        String merchNames = jo.getString("merchNames");
        String[] mns = merchNames.split(",");
        return merchantBeanDao.getAllMerchantNosByNames(mns);
    }

    public List<String> getAllMerchantNoList() {
        List<String> result = merchantBeanDao.getAllMerchantNoList();
        return result;
    }

    /**
     * 根据商户号查询商户信息
     * 
     * @param merchantNo
     * @return
     */
    public MerchantBean getMerByMerName(String merchantName) {
        return merchantBeanDao.getMerByMerName(merchantName);
    }

    public IqbCustomerPermissionEntity getICPEByOrgCode(String orgCode) {
        return merchantBeanDao.getICPEByOrgCode(orgCode);
    }

    public List<MerchantBean> getMBListByATList(List<MerchantTreePojo> authorityTreeList) {
        return merchantBeanDao.getMBListByATList(authorityTreeList);
    }

}
