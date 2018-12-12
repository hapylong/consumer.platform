package com.iqb.consumer.data.layer.biz.sys;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;
import com.iqb.consumer.data.layer.dao.sys.SysCoreDictItemDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class SysCoreDictItemBiz extends BaseBiz {

    @Resource
    private SysCoreDictItemDao sysCoreDictItemDao;

    public List<SysDictItem> selDictItem(String dictDypeCode) {
        setDb(0, super.SLAVE);
        return sysCoreDictItemDao.selDictItem(dictDypeCode);
    }

    public List<SysDictItem> selBizType(String dictName) {
        setDb(0, super.SLAVE);
        return sysCoreDictItemDao.selBizType(dictName);
    }

    public SysDictItem getDictByDTCAndDC(DictTypeCodeEnum dtc, String dc) {
        setDb(0, super.SLAVE);
        return sysCoreDictItemDao.getDictByDTCAndDC(dtc, dc);
    }

    public SysDictItem getDictByDTCAndDN(DictTypeCodeEnum dtc, String dName) {
        setDb(0, super.SLAVE);
        return sysCoreDictItemDao.getDictByDTCAndDN(dtc, dName);
    }
}
