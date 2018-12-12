package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.image.ImageBean;

public interface ImageDao {

    int batchInsertImage(JSONObject objs);

    int insertImage(ImageBean imageBean);

    @SuppressWarnings("rawtypes")
    List<ImageBean> selectList(Map objs);

    int deleteImageByPath(String imgPath);

    ImageBean selectImageInfo(JSONObject objs);
}
