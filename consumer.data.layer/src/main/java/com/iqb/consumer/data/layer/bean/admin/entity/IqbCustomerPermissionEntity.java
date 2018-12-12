package com.iqb.consumer.data.layer.bean.admin.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.EntityUtil;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;

import jodd.util.StringUtil;

@Table(name = "iqb_customer_permission")
public class IqbCustomerPermissionEntity extends EntityUtil {

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String merchantNo;
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String merchantName;
    private String authorityTree;
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private List<MerchantTreePojo> authorityTreeList;
    private Date createTime;
    private Date updateTime;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAuthorityTree() {
        return authorityTree;
    }

    public void setAuthorityTree(String authorityTree) {
        this.authorityTree = authorityTree;
        this.authorityTreeList = StringUtil.isEmpty(authorityTree)
                ? new ArrayList<MerchantTreePojo>()
                : JSONObject.parseArray(authorityTree, MerchantTreePojo.class);
    }

    public List<MerchantTreePojo> getAuthorityTreeList() {
        return authorityTreeList;
    }

    public void setAuthorityTreeList(List<MerchantTreePojo> authorityTreeList) {
        this.authorityTreeList = authorityTreeList;
        this.authorityTree = authorityTreeList != null ? JSONObject.toJSONString(authorityTreeList) : "{}";
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
