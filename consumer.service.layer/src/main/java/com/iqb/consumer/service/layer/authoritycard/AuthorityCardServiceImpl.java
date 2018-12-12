/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:21:08
 * @version V1.0
 */
package com.iqb.consumer.service.layer.authoritycard;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.biz.authoritycard.AuthorityCardBiz;
import com.iqb.consumer.service.layer.base.BaseService;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Service("authorityCardService")
public class AuthorityCardServiceImpl extends BaseService implements AuthorityCardService {

    protected static Logger logger = LoggerFactory.getLogger(AuthorityCardServiceImpl.class);

    public final static int RET_SUCC = 0; // 处理成功
    public final static int RET_FAIL = 1; // 处理失败
    public final static int TRAN_FAIL = 2; // 数据转换错误

    @Resource
    private AuthorityCardBiz authorityCardBiz;

    @Override
    public int insertAuthorityCard(JSONObject objs) {
        logger.debug("IQB信息---【服务层】插入权证信息，开始...");
        AuthorityCardBean authorityCardBean = new AuthorityCardBean();
        try {
            authorityCardBean = JSONObject.parseObject(JSON.toJSONString(objs), AuthorityCardBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】插入权证信息,装换AuthorityCardBean类出错...");
            return TRAN_FAIL;
        }
        authorityCardBean.setUploadStatus(1);
        int result = authorityCardBiz.insertAuthorityCard(authorityCardBean);
        logger.debug("IQB信息---【服务层】插入权证信息，结束...");
        return result;
    }

    @Override
    public AuthorityCardBean selectOneByOrderId(JSONObject objs) {
        logger.debug("IQB信息---【服务层】查询权证信息，开始...");
        AuthorityCardBean authorityCardBean = authorityCardBiz.selectOneByOrderId(objs.getString("orderId"));
        logger.debug("IQB信息---【服务层】查询权证信息，结束...");
        return authorityCardBean;
    }

    @Override
    public int updateAuthorityCard(JSONObject objs) {
        logger.debug("IQB信息---【服务层】更新权证信息，开始...");
        AuthorityCardBean authorityCardBean = new AuthorityCardBean();
        try {
            authorityCardBean = JSONObject.parseObject(JSON.toJSONString(objs), AuthorityCardBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】更新权证信息,转换AuthorityCardBean类出错...");
            return TRAN_FAIL;
        }
        int result = authorityCardBiz.updateAuthorityCard(authorityCardBean);
        logger.debug("IQB信息---【服务层】更新权证信息，结束...");
        return result;
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
    public int updateAuthorityCardStatus(JSONObject objs) {
        logger.debug("IQB信息---【服务层】更新车辆权证信息表状态，开始...");
        AuthorityCardBean authorityCardBean = new AuthorityCardBean();
        try {
            authorityCardBean = JSONObject.parseObject(JSON.toJSONString(objs), AuthorityCardBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】更新车辆权证信息表状态,转换AuthorityCardBean类出错...");
            return TRAN_FAIL;
        }
        int result = authorityCardBiz.updateAuthorityCardStatus(authorityCardBean);
        logger.debug("IQB信息---【服务层】更新车辆权证信息表状态，结束...");
        return result;
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
    public int updateAuthorityCardInfo(JSONObject objs) {
        logger.debug("IQB信息---【服务层】更新车辆权证信息表颜色、里程，开始...");
        AuthorityCardBean authorityCardBean = new AuthorityCardBean();
        try {
            authorityCardBean = JSONObject.parseObject(JSON.toJSONString(objs), AuthorityCardBean.class);
        } catch (Exception e) {
            logger.error("IQB信息---【服务层】更新车辆权证信息表颜色、里程,转换AuthorityCardBean类出错...");
            return TRAN_FAIL;
        }
        int result = authorityCardBiz.updateAuthorityCardInfo(authorityCardBean);
        logger.debug("IQB信息---【服务层】更新车辆权证信息表颜色、里程，结束...");
        return result;
    }
}
