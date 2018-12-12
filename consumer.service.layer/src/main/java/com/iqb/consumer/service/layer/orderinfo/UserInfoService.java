package com.iqb.consumer.service.layer.orderinfo;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.user.UserBean;

/**
 * Description: O
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年12月11日上午11:44:21 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface UserInfoService {
    /**
     * 
     * Description:批量插入用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public Map<String, Object> userInfoXlsImport(MultipartFile file);

    /**
     * 
     * Description:获取用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public PageInfo<UserBean> selectInstUserInfo(JSONObject objs);
}
