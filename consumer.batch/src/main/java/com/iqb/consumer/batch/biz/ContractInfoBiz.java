package com.iqb.consumer.batch.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.dao.ContractInfoDao;
import com.iqb.consumer.batch.data.pojo.ContractInfoBean;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 电子合同biz服务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年2月27日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class ContractInfoBiz extends BaseBiz {

    @Autowired
    private ContractInfoDao contractInfoDao;

    /**
     * 
     * Description: 为合同下载查询合同信息
     * 
     * @param
     * @return List<JSONObject>
     * @throws
     * @Author wangxinbang Create Date: 2017年10月12日 下午4:56:52
     */
    public List<Map<String, Object>> selectContractInfoForDownload(ContractInfoBean ecInfoBean) {
        super.setDb(0, super.SLAVE);
        return this.contractInfoDao.selectContractInfoForDownload(ecInfoBean);
    }

    /**
     * 
     * Description: 更新合同下载次数
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年10月12日 下午5:18:54
     */
    public Integer updateEcDownloadTimes(String tpSignid) {
        super.setDb(0, super.MASTER);
        return this.contractInfoDao.updateEcDownloadTimes(tpSignid);
    }

}
