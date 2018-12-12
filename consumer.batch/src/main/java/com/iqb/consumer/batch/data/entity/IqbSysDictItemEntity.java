package com.iqb.consumer.batch.data.entity;

import javax.persistence.Table;

@Table(name = "iqb_sys_dict_item")
public class IqbSysDictItemEntity {
    private String dictDypeCode;
    private String dictName;
    private String dictCode;
    private String dictValue;
    private String casCadeCode;
    private String remark;
    private Integer isEnable;

    public String getDictDypeCode() {
        return dictDypeCode;
    }

    public void setDictDypeCode(String dictDypeCode) {
        this.dictDypeCode = dictDypeCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getCasCadeCode() {
        return casCadeCode;
    }

    public void setCasCadeCode(String casCadeCode) {
        this.casCadeCode = casCadeCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
