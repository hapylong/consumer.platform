package com.iqb.consumer.common.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEmptyCheck {

    CheckGroup[] groupCode();

    public enum CheckGroup {
        A, B, C;
    }

}
