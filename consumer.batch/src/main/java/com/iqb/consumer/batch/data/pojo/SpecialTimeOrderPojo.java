package com.iqb.consumer.batch.data.pojo;

import com.iqb.consumer.batch.data.BasicEntity;

import jodd.util.StringUtil;

public class SpecialTimeOrderPojo extends BasicEntity {

    private String procInstId; // 流程id
    private String procBizId; // orderId
    private String procOrgCode; // merchantId

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcBizId() {
        return procBizId;
    }

    public void setProcBizId(String procBizId) {
        this.procBizId = procBizId;
    }

    public String getProcOrgCode() {
        return procOrgCode;
    }

    public void setProcOrgCode(String procOrgCode) {
        this.procOrgCode = procOrgCode;
    }

    public boolean checkPojo() {
        print();
        if (StringUtil.isEmpty(procInstId) ||
                StringUtil.isEmpty(procBizId) ||
                StringUtil.isEmpty(procOrgCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SpecialTimeOrderPojo [procInstId=" + procInstId + ", procBizId=" + procBizId + ", procOrgCode="
                + procOrgCode + "]";
    }
}
