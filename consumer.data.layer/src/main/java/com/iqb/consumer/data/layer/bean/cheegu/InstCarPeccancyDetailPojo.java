package com.iqb.consumer.data.layer.bean.cheegu;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月29日上午8:32:46 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstCarPeccancyDetailPojo {
    @JSONField(name = "data")
    private List<InstCarPeccancyDetailBean> instCarPeccancyDetailBeanList;

    public List<InstCarPeccancyDetailBean> getInstCarPeccancyDetailBeanList() {
        return instCarPeccancyDetailBeanList;
    }

    public void setInstCarPeccancyDetailBeanList(List<InstCarPeccancyDetailBean> instCarPeccancyDetailBeanList) {
        this.instCarPeccancyDetailBeanList = instCarPeccancyDetailBeanList;
    }
}
