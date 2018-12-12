package com.iqb.consumer.data.layer.biz.image;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.image.ImageBean;
import com.iqb.consumer.data.layer.dao.ImageDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Repository
public class ImageBiz extends BaseBiz {

    @Resource
    private ImageDao imageDao;

    public int batchInsertImage(JSONObject objs) {
        setDb(0, super.MASTER);
        return imageDao.batchInsertImage(objs);
    }

    public int insertImage(ImageBean imageBean) {
        setDb(0, super.MASTER);
        return imageDao.insertImage(imageBean);
    }

    @SuppressWarnings("rawtypes")
    public List<ImageBean> selectList(Map objs) {
        setDb(0, super.SLAVE);
        return imageDao.selectList(objs);
    }

    public int deleteImageByPath(String imgPath) {
        setDb(0, super.MASTER);
        return imageDao.deleteImageByPath(imgPath);
    }

    public ImageBean selectImageInfo(JSONObject objs) {
        setDb(0, super.SLAVE);
        return imageDao.selectImageInfo(objs);
    }
}
