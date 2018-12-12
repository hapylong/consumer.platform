package com.iqb.consumer.common.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictConvertCenter {

    WHERE_COLUMN from();

    TARGET_COLUMN to();

    DICT_TYPE_CODE type();

    public enum WHERE_COLUMN {
        DICT_NAME, DICT_CODE, DICT_VALUE;
    }

    public enum TARGET_COLUMN {
        DICT_CODE, DICT_NAME;
    }

    public enum DICT_TYPE_CODE {
        bizType2OpenId, fund_source;
    }

}
