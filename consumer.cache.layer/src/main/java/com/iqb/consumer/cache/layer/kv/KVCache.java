/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午4:18:34
 * @version V1.0
 */

package com.iqb.consumer.cache.layer.kv;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface KVCache<V> {

    V get(String key, Class<V> clazz);

    // 默认时间存放对象
    void put(String key, V value);

    // 灵活时间存放对象
    void put(String key, V value, int ttl);

    // 移除
    void remove(String key);

    // 默认时间存放Str
    void put(String key, String value);

    // 灵活时间存放对象
    void put(String key, String value, int ttl);

    String get(String key);
}
