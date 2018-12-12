package com.iqb.consumer.asset.allocation.assetinfo.bean;

public class ImageBean {

    private String orderId; // 订单id
    private int imgType;// 1,借款及担保合同资料(水印版) 2,借款及担保合同资料(完整版) 3,项目现场 4,项目logo 5,支付凭证
    private int imgNo;// 图片顺序()主要合同
    private String imgName;// 图片名称
    private String imgPath;// 图片路径

    public String getOrderId() {
        return orderId;
    }

    public int getImgNo() {
        return imgNo;
    }

    public void setImgNo(int imgNo) {
        this.imgNo = imgNo;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
