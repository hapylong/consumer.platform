package com.iqb.consumer.data.layer.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;

public class EntityUtil {

    private int saveOrUpdate;

    protected static final Logger log = LoggerFactory.getLogger(BaseEntity.class);

    public void print() {
        log.info(toString());
    }

    public int getSaveOrUpdate() {
        return saveOrUpdate;
    }

    public void setSaveOrUpdate(int saveOrUpdate) {
        this.saveOrUpdate = saveOrUpdate;
    }

    public final boolean check(CheckGroup groupCode)
            throws Exception {
        print();
        Class<? extends EntityUtil> c = this.getClass();
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            IsEmptyCheck dcc = fields[i].getAnnotation(IsEmptyCheck.class);
            if (dcc == null) {
                continue;
            }
            for (int n = 0; n < dcc.groupCode().length; n++) {
                if (dcc.groupCode()[n].equals(groupCode)) {
                    PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), c);
                    Method mget = pd.getReadMethod();// 获得写方法
                    Object value = mget.invoke(this);
                    if (value == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
