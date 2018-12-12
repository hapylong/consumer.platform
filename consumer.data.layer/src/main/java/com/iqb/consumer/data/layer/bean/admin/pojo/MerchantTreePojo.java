package com.iqb.consumer.data.layer.bean.admin.pojo;

public class MerchantTreePojo {

    private Long id;
    private Integer level;// 等级
	private String parentId; //
    private Integer reRankId;
    private String merchantNo;// 商户号
    private String merchantShortName;// 商户简称
    private Boolean selected = false; // 是否选中

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getReRankId() {
        return reRankId;
    }

    public void setReRankId(Integer reRankId) {
        this.reRankId = reRankId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((merchantNo == null) ? 0 : merchantNo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MerchantTreePojo other = (MerchantTreePojo) obj;
        if (merchantNo == null) {
            if (other.merchantNo != null) return false;
        } else if (!merchantNo.equals(other.merchantNo)) return false;
        return true;
    }

}
