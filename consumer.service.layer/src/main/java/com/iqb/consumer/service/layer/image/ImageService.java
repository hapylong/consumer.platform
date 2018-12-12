package com.iqb.consumer.service.layer.image;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.image.ImageBean;

public interface ImageService {

    /**
     * 插入图片
     */
    int insertImage(JSONObject objs);

    /**
     * 根据orderId查询图片
     */
    List<ImageBean> selectList(JSONObject objs);

    /**
     * 根据imgPath删除图片
     */
    int deleteImageByPath(JSONObject objs);

    /**
     * 批量插入图片
     */
    int batchInsertImage(JSONObject objs);

    String downLoadImage(JSONObject objs, HttpServletResponse response) throws Exception;

    /**
     * 
     * 将doc文档转换为html
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    String convertDocToHtml(JSONObject objs) throws Exception;
}
