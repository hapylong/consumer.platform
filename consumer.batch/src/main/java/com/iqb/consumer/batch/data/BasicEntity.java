/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月16日 上午10:23:41
 * @version V1.0
 */
package com.iqb.consumer.batch.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BasicEntity {
    public static final Logger logger = LoggerFactory.getLogger(BasicEntity.class);

    public void print() {
        logger.info(toString());
    }
}
