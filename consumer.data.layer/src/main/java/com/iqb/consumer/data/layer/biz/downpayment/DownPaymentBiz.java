/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午4:01:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.downpayment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.downpayment.DownPaymentBean;
import com.iqb.consumer.data.layer.dao.DownPaymentDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Repository
public class DownPaymentBiz extends BaseBiz {

    @Resource
    private DownPaymentDao downPaymentDao;

    public List<DownPaymentBean> getDownPaymentList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return downPaymentDao.getDownPaymentList(objs);
    }

    public List<DownPaymentBean> getDownPaymentListSave(JSONObject objs) {
        setDb(0, super.SLAVE);
        return downPaymentDao.getDownPaymentList(objs);
    }
}
