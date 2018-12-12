package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.user.UserRiskBaseInfo;

public interface UserRiskDao {

    /**
     * 修改客户信息
     * 
     */
    int updateUserInfo(Map<String, String> params);

    /**
     * 删除客户信息
     * 
     */
    void deleteUserByID(String Id);

    /**
     * 修改客户信息
     * 
     */
    int updateUserRiskInfo(Map<String, String> params);

    /**
     * 删除客户信息
     * 
     */
    void deleteUserRiskByID(String Id);

    /**
     * 查询指定客户信息
     * 
     */
    UserRiskBaseInfo getUserById(String Id);

    /**
     * 根据商户列表、品牌查询客户信息
     * 
     * @param merchantNos
     * @return
     */
    List<UserRiskBaseInfo> getUserByMerNos(JSONObject objs);
}
