package com.iqb.consumer.data.layer.biz.dict;

import org.apache.ibatis.type.Alias;

import com.iqb.consumer.common.constant.DictConvertCenter.DICT_TYPE_CODE;
import com.iqb.consumer.common.constant.DictConvertCenter.TARGET_COLUMN;
import com.iqb.consumer.common.constant.DictConvertCenter.WHERE_COLUMN;

@Alias("DynamicSQL")
public class DynamicSQL {
    private TARGET_COLUMN target;
    private WHERE_COLUMN where;
    private Object value;
    private DICT_TYPE_CODE type;

    public DynamicSQL(TARGET_COLUMN to, WHERE_COLUMN from, Object value, DICT_TYPE_CODE type) {
        this.target = to;
        this.where = from;
        this.value = value;
        this.type = type;
    }

    public TARGET_COLUMN getTarget() {
        return target;
    }

    public void setTarget(TARGET_COLUMN target) {
        this.target = target;
    }

    public WHERE_COLUMN getWhere() {
        return where;
    }

    public void setWhere(WHERE_COLUMN where) {
        this.where = where;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DICT_TYPE_CODE getType() {
        return type;
    }

    public void setType(DICT_TYPE_CODE type) {
        this.type = type;
    }

}
