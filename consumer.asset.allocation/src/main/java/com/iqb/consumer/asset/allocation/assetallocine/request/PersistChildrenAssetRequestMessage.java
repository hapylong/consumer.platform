package com.iqb.consumer.asset.allocation.assetallocine.request;

import java.util.List;

import com.github.pagehelper.StringUtil;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;

public class PersistChildrenAssetRequestMessage {
    private List<BreakOrderInfoEntity> children;
    private String proName; // 项目名称
    private String bakOrgan; // 备案机构
    private String url; // 摘牌网址
    private String proDetail; // 项目概况
    private String tranCondition; // 转让条件transferConditions
    private String safeWay; // SafeguardWay

    public List<BreakOrderInfoEntity> getChildren() {
        return children;
    }

    public void setChildren(List<BreakOrderInfoEntity> children) {
        this.children = children;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getBakOrgan() {
        return bakOrgan;
    }

    public void setBakOrgan(String bakOrgan) {
        this.bakOrgan = bakOrgan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProDetail() {
        return proDetail;
    }

    public void setProDetail(String proDetail) {
        this.proDetail = proDetail;
    }

    public String getTranCondition() {
        return tranCondition;
    }

    public void setTranCondition(String tranCondition) {
        this.tranCondition = tranCondition;
    }

    public String getSafeWay() {
        return safeWay;
    }

    public void setSafeWay(String safeWay) {
        this.safeWay = safeWay;
    }

    public boolean checkRequest() {
        if (children == null
                || children.isEmpty()
                || StringUtil.isEmpty(bakOrgan)
                || StringUtil.isEmpty(url)
                || StringUtil.isEmpty(proDetail)
                || StringUtil.isEmpty(tranCondition)
                || StringUtil.isEmpty(safeWay)) {
            return false;
        }
        return true;
    }
}
