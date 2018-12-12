/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午4:01:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.authoritycard;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.api.CfImagePojo;
import com.iqb.consumer.data.layer.bean.api.PicInformationPojo;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.dao.AuthorityCardDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Repository
public class AuthorityCardBiz extends BaseBiz {

    @Resource
    private AuthorityCardDao authorityCardDao;

    public int insertAuthorityCard(AuthorityCardBean authorityCardBean) {
        setDb(0, super.MASTER);
        return authorityCardDao.insertAuthorityCard(authorityCardBean);
    }

    public AuthorityCardBean selectOneByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        return authorityCardDao.selectOneByOrderId(orderId);
    }

    public int updateAuthorityCard(AuthorityCardBean authorityCardBean) {
        setDb(0, super.MASTER);
        return authorityCardDao.updateAuthorityCard(authorityCardBean);
    }

    public PicInformationPojo getPIPByOid(String orderId) {
        setDb(0, super.SLAVE);
        return authorityCardDao.getPIPByOid(orderId);
    }

    public List<CfImagePojo> getCFPByOid(String orderId) {
        setDb(0, super.SLAVE);
        return authorityCardDao.getCFPByOid(orderId);
    }

    /**
     * 
     * 更新车辆权证信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    public int updateAuthorityCardStatus(AuthorityCardBean authorityCardBean) {
        setDb(0, super.MASTER);
        return authorityCardDao.updateAuthorityCardStatus(authorityCardBean);
    }

    /**
     * 
     * 更新车辆权证信息表颜色、里程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    public int updateAuthorityCardInfo(AuthorityCardBean authorityCardBean) {
        setDb(0, super.MASTER);
        return authorityCardDao.updateAuthorityCardInfo(authorityCardBean);
    }
}
