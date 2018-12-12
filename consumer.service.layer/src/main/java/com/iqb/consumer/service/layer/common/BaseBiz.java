package com.iqb.consumer.service.layer.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

public class BaseBiz {

    protected final String MASTER = "mdb";// 主库标示
    protected final String SLAVE = "sdb";// 从库标示

    private Integer DEFAULT_PAGE_NUM = 1; // 默认页码
    private Integer DEFAULT_PAGE_SIZE = 10; // 默认页大小

    @SuppressWarnings("rawtypes")
    protected Map<String, Integer> getPagePara(Map map) {
        int pageNum = DEFAULT_PAGE_NUM;
        int pageSize = DEFAULT_PAGE_SIZE;
        Map<String, Integer> pageMap = new HashMap<String, Integer>();
        if (map != null) {
            @SuppressWarnings("deprecation")
            String pageNumStr = ObjectUtils.toString(map.get("pageNum"));
            @SuppressWarnings("deprecation")
            String pageSizeStr = ObjectUtils.toString(map.get("pageSize"));
            pageNum = StringUtils.isEmpty(pageNumStr) ? DEFAULT_PAGE_NUM : Integer.parseInt(pageNumStr);
            pageSize = StringUtils.isEmpty(pageSizeStr) ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSizeStr);
        }
        pageMap.put("pageNum", pageNum);
        pageMap.put("pageSize", pageSize);
        return pageMap;
    }

}
