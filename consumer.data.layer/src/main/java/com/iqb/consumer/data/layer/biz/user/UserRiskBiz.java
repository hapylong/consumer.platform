package com.iqb.consumer.data.layer.biz.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.user.UserRiskBaseInfo;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.dao.UserRiskDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

@Component
public class UserRiskBiz extends BaseBiz {

    @Resource
    private UserRiskDao userRiskDao;

    @Resource
    private MerchantBeanBiz merchantBiz;

    // 根据手机号查询用户
    public UserRiskBaseInfo getUserById(String Id) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return userRiskDao.getUserById(Id);
    }

    // 根据商户列表、姓名、手机号 查询用户
    public List<UserRiskBaseInfo> getUserByMerNos(JSONObject objs) throws IqbSqlException, IqbException {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return userRiskDao.getUserByMerNos(objs);
    }

    // 修改用户
    public int updateUserInfo(Map<String, String> params) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        Map<String, String> mapCheckInfo = new HashMap<>();
        mapCheckInfo.put("addprovince", params.get("permanent_address"));
        mapCheckInfo.put("contactname1", params.get("link_man_first"));
        mapCheckInfo.put("contactname2", params.get("link_man_second"));
        mapCheckInfo.put("contactphone1", params.get("link_man_first_phone"));
        mapCheckInfo.put("contactphone2", params.get("link_man_second_phone"));
        mapCheckInfo.put("marriedstatus", params.get("matital_status"));
        mapCheckInfo.put("phone", params.get("reg_id"));

        String checkInfo = JSONObject.toJSONString(mapCheckInfo);

        params.put("checkInfo", checkInfo);
        int result = 0;
        result = userRiskDao.updateUserInfo(params);
        if (0 == result) {
            return result;
        } else {
            result = userRiskDao.updateUserRiskInfo(params);
        }
        return result;
    }

    // 删除用户
    public void deleteUserByID(String Id) {
        // 设置数据源为主库
        setDb(0, super.MASTER);
        userRiskDao.deleteUserByID(Id);
        userRiskDao.deleteUserRiskByID(Id);
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
    private List<String> getMerListByMerCodeList(String merCode) throws IqbSqlException, IqbException {
        JSONObject objs = new JSONObject();
        objs.put("id", merCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        List<String> listMc = new ArrayList<>();
        for (MerchantBean mc : list) {
            listMc.add(mc.getMerchantNo());
        }

        return listMc;
    }
}
