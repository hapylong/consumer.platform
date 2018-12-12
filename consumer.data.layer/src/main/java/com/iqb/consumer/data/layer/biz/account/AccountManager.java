package com.iqb.consumer.data.layer.biz.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.dao.UserBeanDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class AccountManager extends BaseBiz {

    @Autowired
    private UserBeanDao userBeanDao;

    public Long saveOrUpdateUserInfo(UserBean ub) {
        setDb(0, super.SLAVE);
        UserBean ubdb = userBeanDao.getUBByRegIdOrIdNo(ub);
        setDb(0, super.MASTER);
        if (ubdb != null) {
            ub.setId(ubdb.getId());
            userBeanDao.updateUserInfo(ub);
            return ub.getId();
        } else {
            return userBeanDao.saveUserBean(ub);
        }
    }
}
