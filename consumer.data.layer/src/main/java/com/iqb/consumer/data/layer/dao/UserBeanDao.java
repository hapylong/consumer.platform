package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.ownerloan.CheckInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;

/**
 * 用户Dao
 */
public interface UserBeanDao {

    // 根据手机号查询用户
    UserBean selectOneByRegId(String regId);

    // 根据id查询用户
    UserBean getUserInfo(long id);

    // 批量插入用户信息
    int batchInsertJysUser(List<JYSUserBean> list);

    // 插入用户信息
    long insertJysUser(JYSUserBean bean);

    // 保存注册用户信息
    long saveUserBean(UserBean userBean);

    // 查询交易所用户信息
    JYSUserBean selectJysUser(JYSUserBean bean);

    String getRegIdByName(String realName);

    UserBean selectAllByRegId(String regId);

    JYSUserBean getJUBByRegId(String regId);

    /**
     * 
     * Description: FINANCE-1374 惠掏车以租代购数据查询FINANCE-1375 客户信息
     * （根据用户手机号查询：姓名、身份证、手机号、紧急联系人1、紧急联系人2、住址、婚姻状态。）
     * 
     * @param
     * @return Map<String,Object>
     * @throws @Author adam Create Date: 2017年6月28日 下午3:08:41
     */
    Map<String, Object> getInfoByRegId(String regId);

    /**
     * 
     * Description:更新inst_userInfo
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    Integer updateUserInfo(UserBean userBean);

    /**
     * 
     * Description: 通过regId 或者 idNo 查账户信息
     * 
     * @param
     * @return UserBean
     * @throws @Author adam Create Date: 2017年11月9日 下午4:51:10
     */
    UserBean getUBByRegIdOrIdNo(UserBean ub);

    /**
     * 
     * Description:根据手机号码获取系统用户id
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月17日
     */
    UserBean getSysUserByRegId(Map<String, Object> params);

    /**
     * 
     * Description:批量插入用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    int batchInsertUser(List<UserBean> list);

    /**
     * 
     * Description:获取用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    List<UserBean> selectInstUserInfo(Map<String, Object> params);

    /**
     * 
     * Description:根据身份证号码手机号码查询用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    UserBean selectInstUser(Map<String, Object> params);

    /**
     * FINANCE-2784 FINANCE-2690 蒲公英个人客户信息查询
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return List<UserBean>
     * @author chengzhen
     * @data
     */
    List<UserBean> getDandelionCustomerList(JSONObject objs);

    /**
     * FINANCE-2784 FINANCE-2690 蒲公英个人客户信息明细查询
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return CheckInfoBean
     * @author chengzhen
     * @data 2018年1月8日 17:40:36
     */
    UserBean getDandelionCustomerDetail(JSONObject objs);
}
