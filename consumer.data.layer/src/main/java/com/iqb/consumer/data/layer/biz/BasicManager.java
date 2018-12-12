package com.iqb.consumer.data.layer.biz;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.iqb.etep.common.db.SubDbContextHolder;

public class BasicManager {
    protected final String MASTER = "mdb";// 主库标示
    protected final String SLAVE = "sdb";// 从库标示

    private Integer DEFAULT_PAGE_NUM = 1; // 默认页码
    private Integer DEFAULT_PAGE_SIZE = 10; // 默认页大小

    /**
     * 数据源设置注入
     */
    @Autowired
    @Qualifier("common_platform_subDbContextHolder")
    protected SubDbContextHolder subDbContextHolder;

    /**
     * 设置db实例
     * 
     * @param idNum
     * @param dbType
     */
    public void setDb(int idNum, String dbType) {
        if (MASTER.equals(dbType)) {
            subDbContextHolder.setMaster(idNum);
        } else if (SLAVE.equals(dbType)) {
            subDbContextHolder.setSlave(idNum);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Map<String, Integer> getPagePara(Map map) {
        int pageNum = DEFAULT_PAGE_NUM;
        int pageSize = DEFAULT_PAGE_SIZE;
        Map<String, Integer> pageMap = new HashMap<>();
        if (map != null) {
            String pageNumStr = ObjectUtils.toString(map.get("pageNum"));
            String pageSizeStr = ObjectUtils.toString(map.get("pageSize"));
            pageNum = StringUtils.isEmpty(pageNumStr) ? DEFAULT_PAGE_NUM : Integer.parseInt(pageNumStr);
            pageSize = StringUtils.isEmpty(pageSizeStr) ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSizeStr);
        }
        pageMap.put("pageNum", pageNum);
        pageMap.put("pageSize", pageSize);
        return pageMap;
    }
}
