package com.iqb.consumer.data.layer.biz.pro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.project.ProjectBaseInfoBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.dao.ProjectBaseInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;
import com.iqb.etep.common.utils.JSONUtil;

@Component
public class ProjectBaseInfoBiz extends BaseBiz {
    protected static Logger logger = LoggerFactory.getLogger(ProjectBaseInfoBiz.class);

    @Resource
    private ProjectBaseInfoDao projectBaseInfoDao;

    @Resource
    private MerchantBeanBiz merchantBeanBiz;

    /*
     * Description: 增加车基础信息
     */
    public int insertProBaseInfo(JSONObject objs) {
        // 权限树修改
        objs.put("merchantShortName", objs.getString("merchNames"));
        String merchantNo = merchantBeanBiz.getMerCodeByMerSortName(objs);
        objs.put("merchantNo", merchantNo);

        // ProjectBaseInfoBean projectBaseInfoBean = JSONUtil.toJavaObject(objs,
        // ProjectBaseInfoBean.class);
        ProjectBaseInfoBean projectBaseInfoBean = JSONObject.parseObject(objs.toJSONString(),
                ProjectBaseInfoBean.class);
        projectBaseInfoBean.setMerchantNo(merchantNo);
        return projectBaseInfoDao.insertProBaseInfo(projectBaseInfoBean);
    }

    /*
     * Description: 修改车基础信息
     */
    public int updateProBaseInfo(JSONObject objs) {
        ProjectBaseInfoBean projectBaseInfoBean = JSONUtil.toJavaObject(objs, ProjectBaseInfoBean.class);
        projectBaseInfoBean.setMerchantNo(objs.getString("merchantNo"));
        return projectBaseInfoDao.updateProBaseInfo(projectBaseInfoBean);
    }

    /*
     * Description: 删除车基础信息
     */
    public void deleteProBaseInfo(JSONObject objs) {
        projectBaseInfoDao.delProBaseInfoByID(Long.valueOf(objs.getLongValue("id")));
    }

    /*
     * Description: 查询指定车详细信息
     */
    public ProjectBaseInfoBean getProBaseInfoById(JSONObject objs) {
        return projectBaseInfoDao.getProBaseInfoById(Long.valueOf(objs.getLongValue("id")));
    }

    /*
     * Description: 根据商户列表、品牌查询车系
     */
    public List<ProjectBaseInfoBean> getProBaseInfoByMerNos(JSONObject objs, boolean flag) {
        // List<String> merLists = new ArrayList<String>();
        // String merCodeDef = objs.getString("merCodeDef");
        // String merCodeSel = objs.getString("merCodeSel");
        String realCommunity = objs.getString("realCommunity");

        // if (merCodeSel == null || merCodeSel.equals("")) {
        // merLists = getMerListByMerCodeList(merCodeDef);
        // } else {
        // // 权限树修改
        // objs.put("merchantShortName", objs.getString("merCodeSel"));
        // merCodeSel = merchantBeanBiz.getMerCodeByMerSortName(objs);
        // merLists.add(merCodeSel);
        // }
        if (realCommunity == null || realCommunity.equals("")) {
            realCommunity = null;
        }

        if (flag) { // 分页
            PageHelper.startPage(getPagePara(objs));
        }

        // objs.put("merchantNos", merLists);
        objs.put("realCommunity", realCommunity);
        List<ProjectBaseInfoBean> list = projectBaseInfoDao.getProBaseInfoByMerNos(objs);

        return list;
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
    private List<String> getMerListByMerCodeList(String merCode) {
        JSONObject objs = new JSONObject();
        objs.put("id", merCode);
        List<MerchantBean> list = merchantBeanBiz.getAllMerByID(objs);
        List<String> listMc = new ArrayList<String>();
        for (MerchantBean mc : list) {
            listMc.add(mc.getMerchantNo());
        }

        return listMc;
    }
}
