package com.iqb.consumer.data.layer.bean.api;

import jodd.util.StringUtil;

public class CfImagePojo {

    private Integer imgType;
    private String imgPath;

    public Integer getImgType() {
        return imgType;
    }

    public void setImgType(Integer imgType) {
        this.imgType = imgType;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean check() {
        if (imgType == null ||
                StringUtil.isEmpty(imgPath)) {
            return false;
        }
        return true;
    }

}
