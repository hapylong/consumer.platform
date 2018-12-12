package com.iqb.consumer.service.layer.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.bean.conf.SysSmsConfig;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderManageResult;
import com.iqb.consumer.data.layer.biz.conf.SysSmsConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.service.layer.ServiceParaConfig;
import com.iqb.consumer.service.layer.dto.repay.UserPaymentInfoDto;
import com.iqb.etep.common.utils.StringUtil;

@Service
public class SmsServiceImpl implements SmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Resource
    private ServiceParaConfig serviceParaConfig;
    @Resource
    private SysSmsConfigBiz sysSmsConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;

    /**
     * 
     * @param url 应用地址，类似于http://ip:port/msg/
     * @param account 账号
     * @param pswd 密码
     * @param mobile 手机号码，多个号码使用","分割
     * @param msg 短信内容
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<String> SendSms4Pay(List<Map> list) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        List<String> failList = new ArrayList<String>();// 发送失败的手机号
        SysSmsConfig sysSmsConfig = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                UserPaymentInfoDto upi = JSONObject.parseObject(JSON.toJSONString(list.get(i)),
                        UserPaymentInfoDto.class);
                sysSmsConfig = getSmsChannelByMerchNo(upi.getMerchantNo());
                URI base = new URI(sysSmsConfig.getSmsUrl(), false);
                method.setURI(new URI(base, "HttpBatchSendSM", false));
                method.setQueryString(
                        new NameValuePair[] {new NameValuePair("account", sysSmsConfig.getSmsName()),
                                new NameValuePair("pswd", sysSmsConfig.getSmsPswd()),
                                new NameValuePair("mobile", upi.getRegId()),
                                new NameValuePair("needstatus", sysSmsConfig.getSmsNeedStatus() + ""),
                                new NameValuePair("msg", getSmsContent4Repay(upi)), new NameValuePair("product", null),
                                new NameValuePair("extno", null),});
                int result = client.executeMethod(method);
                if (result != HttpStatus.SC_OK) {
                    failList.add(upi.getRegId());
                }
            }
            return failList;
        } catch (Exception e) {
            logger.error("还款催收短信发送过程出现异常:{}", e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<String> SendSms4Order(List<Map> list) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        List<String> failList = new ArrayList<String>();// 发送失败的手机号
        SysSmsConfig sysSmsConfig = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                OrderManageResult omr = JSONObject.parseObject(JSON.toJSONString(list.get(i)), OrderManageResult.class);
                sysSmsConfig = getSmsChannelByMerchNo(omr.getMerchantNo());
                URI base = new URI(sysSmsConfig.getSmsUrl(), false);
                method.setURI(new URI(base, "HttpBatchSendSM", false));
                method.setQueryString(
                        new NameValuePair[] {new NameValuePair("account", sysSmsConfig.getSmsName()),
                                new NameValuePair("pswd", sysSmsConfig.getSmsPswd()),
                                new NameValuePair("mobile", omr.getRegId()),
                                new NameValuePair("needstatus", sysSmsConfig.getSmsNeedStatus() + ""),
                                new NameValuePair("msg", getSmsContent4Order(omr)), new NameValuePair("product", null),
                                new NameValuePair("extno", null),});
                int result = client.executeMethod(method);
                if (result != HttpStatus.SC_OK) {
                    failList.add(omr.getRegId());
                }
            }
            return failList;
        } finally {
            method.releaseConnection();
        }
    }

    // 获取短信通道配置
    private SysSmsConfig getSmsChannelByMerchNo(String merchantNo) {
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(merchantNo);
        int publicNo = merchantBean.getPublicNo();
        String wechatNo = null;
        switch (publicNo) {
            case 1:
                wechatNo = "2";
                break;
            case 2:
                wechatNo = "1";
                break;
            case 7:
                wechatNo = "7";
                break;
        }
        SysSmsConfig sysSmsConfig = sysSmsConfigBiz.getSmsChannelByWechatNo(wechatNo);
        return sysSmsConfig;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object sendSmsByStatus(JSONObject objs) throws Exception {
        String flag = objs.getString("flag");// 根据是否有订单id判断是预付款还是还款过来
        List<String> result = null;
        if ("1".equals(flag)) {// 还款
            List<Map> list = (List<Map>) objs.get("list");
            result = SendSms4Pay(list);
        } else if ("2".equals(flag)) {// 划扣
            List<Map> list = (List<Map>) objs.get("list");
            return SendSms4Deduct(list);
        } else {// 预付款
            List<Map> list = (List<Map>) objs.get("list");
            result = SendSms4Order(list);
        }

        return result;
    }

    private String getSmsContent4Order(OrderBean orderBean) {
        try {
            String msg = null;// 发送短信的内容
            msg = serviceParaConfig.getSendSms_preAmount_urge();
            msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
            msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");// 因为某种特别的原因，要转2遍，待优化
            msg = msg.replaceAll("preAmount", orderBean.getPreAmt())
                    .replaceAll("downPayment", orderBean.getDownPayment()).replaceAll("margin", orderBean.getMargin())
                    .replaceAll("serviceFee", orderBean.getServiceFee())
                    .replaceAll("feeAmount", orderBean.getFeeAmount() == null ? "0"
                            : orderBean.getFeeAmount() + "")
                    .replaceAll("monthInterest", orderBean.getMonthInterest() + "");
            logger.info("---发送短信内容--{}", msg);
            return msg;
        } catch (Exception e) {
            logger.error("获取短信内容出现异常", e);
        }
        return null;
    }

    // 获取发送短信的内容
    private String getSmsContent4Repay(UserPaymentInfoDto userPaymentInfo) {
        try {
            String msg = null;// 发送短信的内容
            double curOverdueFee = Double.parseDouble(userPaymentInfo.getCurRepayOverdueInterest());// 滞纳金金额
            if (curOverdueFee > 0) {// 逾期
                msg = serviceParaConfig.getSendSms_overdue();
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");// 因为某种特别的原因，要转2遍，待优化
                msg = msg.replaceAll("regId", subRegId(userPaymentInfo.getRegId()))
                        .replaceAll("currcShouldPayAmt", userPaymentInfo.getCurRepayAmt() + "")
                        .replaceAll("lastDate", this.formartDate(userPaymentInfo.getLastRepayDate(), "MMdd"))
                        .replaceAll("currEstimateLatefeeIn", userPaymentInfo.getCurRepayOverdueInterest());
            } else {// 还款
                msg = serviceParaConfig.getSendSms_repay();
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");// 因为某种特别的原因，要转2遍，待优化
                msg = msg.replaceAll("regId", subRegId(userPaymentInfo.getRegId()))
                        .replaceAll("currcShouldPayAmt", userPaymentInfo.getCurRepayAmt())
                        .replaceAll("lastDate", this.formartDate(userPaymentInfo.getLastRepayDate(), "MMdd"));
            }

            return msg;
        } catch (Exception e) {
            logger.error("获取短信内容出现异常", e);
        }
        return null;
    }

    /**
     * 转换日期格式，例如：将20150810转换为2015年08月10日 param 日期（20150810） return 日期（2015年08月10日）
     */
    private String formartDate(Date date, String pattern) {
        if (date == null || date.equals("")) {
            return "";
        } else {
            String dateString = DateUtil.getDateString(date, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            StringBuffer ss = new StringBuffer(dateString);
            ss.insert(4, "年");
            ss.insert(7, "月");
            ss.insert(10, "日");
            String result = null;
            if ("MM".equals(pattern)) {
                result = ss.substring(5, 8);
            } else if ("MMdd".equals(pattern)) {
                result = ss.substring(5, 11);
            } else {
                result = ss.toString();
            }
            return result;
        }
    }

    private String subRegId(String regId) {
        int len = regId.length();
        regId = regId.substring(len - 4, len);
        return regId;
    }

    /**
     * 
     * @param url 应用地址，类似于http://ip:port/msg/
     * @param account 账号
     * @param pswd 密码
     * @param mobile 手机号码，多个号码使用","分割
     * @param msg 短信内容
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<UserPaymentInfoDto> SendSms4Deduct(List<Map> list) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        List<UserPaymentInfoDto> successList = new ArrayList<>();// 发送失败的手机号
        SysSmsConfig sysSmsConfig = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                UserPaymentInfoDto upi = JSONObject.parseObject(JSON.toJSONString(list.get(i)),
                        UserPaymentInfoDto.class);
                sysSmsConfig = getSmsChannelByMerchNo(upi.getMerchantNo());
                String url = sysSmsConfig.getSmsUrl();
                if (!StringUtil.isNull(url)) {
                    url = url.substring(0, url.lastIndexOf("msg"));
                }
                URI base = new URI(url, false);
                method.setURI(new URI(base, "HttpBatchSendSM", false));
                method.setQueryString(
                        new NameValuePair[] {new NameValuePair("account", sysSmsConfig.getSmsName()),
                                new NameValuePair("pswd", sysSmsConfig.getSmsPswd()),
                                new NameValuePair("mobile", upi.getRegId()),
                                new NameValuePair("needstatus", sysSmsConfig.getSmsNeedStatus() + ""),
                                new NameValuePair("msg", getSmsContent4Deduct(upi)),
                                new NameValuePair("product", null),
                                new NameValuePair("extno", null),});
                int result = client.executeMethod(method);
                if (result == HttpStatus.SC_OK) {
                    successList.add(upi);
                }
            }
            return successList;
        } catch (Exception e) {
            logger.error("还款催收短信发送过程出现异常:{}", e);
        }
        return null;
    }

    // 获取发送短信的内容
    private String getSmsContent4Deduct(UserPaymentInfoDto userPaymentInfo) {
        try {
            String msg = null;// 发送短信的内容
            msg = serviceParaConfig.getSendSms_deduct();
            msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
            msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");// 因为某种特别的原因，要转2遍，待优化
            return msg;
        } catch (Exception e) {
            logger.error("获取短信内容出现异常", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "http://smssh1.253.com/msg/send/json";
        System.out.println(url.substring(0, url.lastIndexOf("msg")));
    }
}
