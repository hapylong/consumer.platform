package com.iqb.consumer.data.layer.bean.api;

import java.util.ArrayList;
import java.util.List;

public class PicInformationPojo {

    private String baseUrl;
    private String orderId;

    /** cf_image **/
    private List<String> zxbg = new ArrayList<>(); // 征信报告 [10]
    private List<String> sfzzfm = new ArrayList<>(); // 身份证正反面 [12]
    private List<String> hkb = new ArrayList<>(); // 户口本 [13]
    private List<String> jsz = new ArrayList<>(); // 驾驶证 [27]
    private List<String> gcqrd = new ArrayList<>(); // 购车确认单 [8]
    private List<String> jksy = new ArrayList<>(); // 借款及担保合同资料（水印版） [1]
    private List<String> jkwz = new ArrayList<>(); // 借款及担保合同资料（完整版） [2]
    private List<String> cl = new ArrayList<>(); // 车辆评估照片及车辆检验表 [3]
    private List<String> clqz = new ArrayList<>(); // 车辆权证资料 [8]

    /** inst_authoritycard **/
    private String cph; // 车牌号
    private String cjh; // 车架号
    private String fdjh; // 发动机号
    private String bxdh; // 保险单号

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getZxbg() {
        return zxbg;
    }

    public void setZxbg(String zxbg) {
        this.zxbg.add(zxbg);
    }

    public List<String> getSfzzfm() {
        return sfzzfm;
    }

    public void setSfzzfm(String sfzzfm) {
        this.sfzzfm.add(sfzzfm);
    }

    public List<String> getHkb() {
        return hkb;
    }

    public void setHkb(String hkb) {
        this.hkb.add(hkb);
    }

    public List<String> getJsz() {
        return jsz;
    }

    public void setJsz(String jsz) {
        this.jsz.add(jsz);
    }

    public List<String> getGcqrd() {
        return gcqrd;
    }

    public void setGcqrd(String gcqrd) {
        this.gcqrd.add(gcqrd);
    }

    public List<String> getJksy() {
        return jksy;
    }

    public void setJksy(String jksy) {
        this.jksy.add(jksy);
    }

    public List<String> getJkwz() {
        return jkwz;
    }

    public void setJkwz(String jkwz) {
        this.jkwz.add(jkwz);
    }

    public List<String> getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl.add(cl);
    }

    public List<String> getClqz() {
        return clqz;
    }

    public void setClqz(String clqz) {
        this.clqz.add(clqz);
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getCjh() {
        return cjh;
    }

    public void setCjh(String cjh) {
        this.cjh = cjh;
    }

    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }

    public String getBxdh() {
        return bxdh;
    }

    public void setBxdh(String bxdh) {
        this.bxdh = bxdh;
    }
}
