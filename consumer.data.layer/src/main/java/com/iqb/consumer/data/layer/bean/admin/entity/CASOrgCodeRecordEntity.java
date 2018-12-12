package com.iqb.consumer.data.layer.bean.admin.entity;

import javax.persistence.Table;

import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

/**
 * 
 * Description: Compare And Swap Organization Code
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年9月1日    adam       1.0        1.0 Version 
 * </pre>
 */
@Table(name = "cas_org_code_record")
public class CASOrgCodeRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String codeA;
    private String codeB;
    private String describe;
    private String snapshot;
    private Integer orgLevel;
    private Integer parentId; // 修改后父节点id
    private Integer lastParentId; // 记录的是上次父节点id
    private Long lastCasId; // 对应的上次 cas_org_code_record 表中 id

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public String getCodeB() {
        return codeB;
    }

    public void setCodeB(String codeB) {
        this.codeB = codeB;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Long getLastCasId() {
        return lastCasId;
    }

    public void setLastCasId(Long lastCasId) {
        this.lastCasId = lastCasId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Integer getLastParentId() {
        return lastParentId;
    }

    public void setLastParentId(Integer lastParentId) {
        this.lastParentId = lastParentId;
    }

    public boolean checkEntity() {
        print();
        if (StringUtil.isEmpty(codeA) || StringUtil.isEmpty(codeB) || codeA.equals(codeB)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CASOrgCodeRecordEntity [codeA=" + codeA + ", codeB=" + codeB + ", describe=" + describe + ", snapshot="
                + snapshot + ", orgLevel=" + orgLevel + ", parentId=" + parentId + ", lastParentId=" + lastParentId
                + ", lastCasId=" + lastCasId + "]";
    }

}
