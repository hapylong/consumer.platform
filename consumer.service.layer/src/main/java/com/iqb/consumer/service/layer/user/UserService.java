package com.iqb.consumer.service.layer.user;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.user.UserRiskInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

/**
 * 
 * Description: 客户信息(含风控信息)维护
 * 
 * @author gxy
 * @version 1.0
 */
public interface UserService {

    /**
     * 修改客户信息
     * 
     */
    int updateUserInfo(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 删除客户信息
     * 
     */
    void deleteUserByID(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 查询指定客户信息
     * 
     */
    UserRiskInfo getUserById(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据商户列表、品牌查询车系
     * 
     * @param merchantNos
     * @return
     */
    PageInfo<UserRiskInfo> getUserByMerNos(JSONObject objs) throws IqbException, IqbSqlException;

    Long saveOrUpdateUserInfo(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException;

    void carOwnerBasicInfo(JSONObject requestMessage) throws Exception;

}
