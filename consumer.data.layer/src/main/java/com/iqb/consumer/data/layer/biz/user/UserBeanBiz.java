package com.iqb.consumer.data.layer.biz.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.dao.UserBeanDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class UserBeanBiz extends BaseBiz {

    @Resource
    private UserBeanDao userBeanDao;

    // 根据手机号查询用户
    public UserBean selectOne(String regId) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return userBeanDao.selectOneByRegId(regId);
    }

    // 根据id查询用户
    public UserBean getUserInfo(long id) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return userBeanDao.getUserInfo(id);
    }

    // 批量插入用户信息
    public int batchInsertJysUser(List<JYSUserBean> list) {
        setDb(0, super.MASTER);
        return userBeanDao.batchInsertJysUser(list);
    }

    // 插入用户信息返回key值
    public long insertJysUser(JYSUserBean bean) {
        setDb(0, super.MASTER);
        JYSUserBean jysUserBean = userBeanDao.selectJysUser(bean);
        if (jysUserBean != null) {
            return jysUserBean.getId();
        }
        userBeanDao.insertJysUser(bean);
        return bean.getId();
    }

    public long saveUserBean(UserBean userBean) {
        setDb(0, super.MASTER);
        userBeanDao.saveUserBean(userBean);
        return userBean.getId();
    }

    public String getRegIdByName(String realName) {
        setDb(0, super.SLAVE);
        return userBeanDao.getRegIdByName(realName);
    }

    /**
     * 查询实际借款人全部数据
     * 
     * @param regId
     * @return
     */
    public UserBean selectAll(String regId) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return userBeanDao.selectAllByRegId(regId);
    }

    /**
     * 
     * Description: JUB means JYSUserBean
     * 
     * @param
     * @return JYSUserBean
     * @throws
     * @Author adam Create Date: 2017年6月14日 上午11:31:28
     */
    public JYSUserBean getJUBByRegId(String regId) {
        return userBeanDao.getJUBByRegId(regId);
    }

    /**
     * 
     * Description:
     * 
     * @param
     * @return Map<String,Object>
     * @throws @Author adam Create Date: 2017年6月28日 下午3:07:04
     */
    public Map<String, Object> getInfoByRegId(String regId) {
        return userBeanDao.getInfoByRegId(regId);
    }

    /**
     * 
     * Description:更新inst_userInfo
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    public long updateUserInfo(UserBean userBean) {
        setDb(0, super.MASTER);
        return userBeanDao.updateUserInfo(userBean);
    }

    /**
     * 
     * Description:根据手机号码获取系统用户id
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月17日
     */
    public UserBean getSysUserByRegId(Map<String, Object> params) {
        setDb(1, super.MASTER);
        return userBeanDao.getSysUserByRegId(params);
    }

    /**
     * 
     * Description:批量插入用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public int batchInsertUser(List<UserBean> list) {
        setDb(0, super.MASTER);
        return userBeanDao.batchInsertUser(list);
    }

    /**
     * 
     * Description:获取用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public List<UserBean> selectInstUserInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return userBeanDao.selectInstUserInfo(objs);
    }

    /**
     * 
     * Description:根据身份证号码手机号码查询用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public UserBean selectInstUser(Map<String, Object> params) {
        setDb(1, super.MASTER);
        return userBeanDao.selectInstUser(params);
    }
}
