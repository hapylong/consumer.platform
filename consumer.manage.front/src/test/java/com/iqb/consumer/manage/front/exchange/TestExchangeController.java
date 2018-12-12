package com.iqb.consumer.manage.front.exchange;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.jys.RecordAssets;
import com.iqb.consumer.manage.front.AbstractController;

public class TestExchangeController extends AbstractController {

    @Test
    public void testRecRecordAssets() {
        RecordAssets assetPushVo = new RecordAssets();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            assetPushVo.setAssetName("应收账款收益权转让");
            assetPushVo.setAssetNumber("djs-xxx-rhtsybl-015346");
            assetPushVo.setAssetKind("保理资产收益权转让");
            assetPushVo.setAssetUrl("http://jrs.china-zszy.com/index.php?m=content&c=index&a=show&catid=45&id=2263");
            assetPushVo.setIssuerName("荣汇通商业保理");
            assetPushVo.setInterestStart(dateFormat.parse("2017-06-01"));
            assetPushVo.setInterestEnd(dateFormat.parse("2017-08-10"));
            assetPushVo.setRefundMethod(0);
            assetPushVo.setTotalMoney(10000d);
            assetPushVo.setCurrency("CNY-人民币");
            assetPushVo.setRate("12%");
            assetPushVo.setDebtorName("恒通商务有限公司");
            assetPushVo.setDebtorIndustryType("贸易");
            assetPushVo.setDebtorRegAddress("北京市海淀区永泰庄");
            assetPushVo.setDebtorEconomicNature("有限责任公司");
            assetPushVo.setDebtorLegalmen("张二普");
            assetPushVo.setDebtorBusiness("电脑图文设计制作、企业形象策划、展览展示服务、市场调研");
            assetPushVo.setInnerDecision(0);
            assetPushVo.setPromotionLetter("股东提供保证担保");
            assetPushVo.setDescription("为核心企业上下游供应链的营收账款融资");
            assetPushVo.setRemarks("无");
            assetPushVo.setRiskLevel(2);
            assetPushVo.setGuaranteedMethod("1,5");
            assetPushVo.setGuaranteedDetail("中投融融资性担保公司担保，股东提供保证担保");
            String s = JSON.toJSONString(assetPushVo);
            String random = RandomStringUtils.random(20, true, true);
            Integer randomInt = (int) (Math.random() * 16 + 1);
            String ciphertext = EncryptionAlgorithm.encrypte("127.0.0.1", random, randomInt, new Date());
            String express = random + randomInt.toString();
            String uri = BASEURL + "/ex/recRecordAssets" + "?ciphertext=" + ciphertext + "&express=" + express;
            sendJsonRequest(uri, s);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelRecordAssets() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("assetNumber", "djs-xxx-rhtsybl-015346");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/ex/selRecordAssets", params);
        System.out.println(returnMap);
    }

    @Test
    public void testRecZXFData() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ListPushVo listPushVo = new ListPushVo();
        listPushVo.setListName("安系列-20170524");
        listPushVo.setListNumber("djs-xxx-rhtsybl-015346-001");
        listPushVo.setAssetNumber("djs-xxx-rhtsybl-015346");
        listPushVo.setPublishTime(dateFormat.parse("2017-06-07"));
        listPushVo.setIssuerName("荣汇通商业保理");
        listPushVo.setRecruitStart(dateFormat.parse("2017-06-07"));
        listPushVo.setRecruitEnd(dateFormat.parse("2017-06-10"));
        listPushVo.setInterestStart(dateFormat.parse("2017-06-11"));
        listPushVo.setInterestEnd(dateFormat.parse("2017-08-10"));
        listPushVo.setRefundMethod(1);
        listPushVo.setListCount(5);
        listPushVo.setTotalMoney(1000d);
        listPushVo.setRate("7.6%");
        listPushVo.setCurrency("CNY-人民币");
        listPushVo.setInvestKind("0,1,2");

        List<AmountSetting> amountSettingList = new ArrayList<>();
        AmountSetting amountSetting0 = new AmountSetting();
        amountSetting0.setAppendAmount(0.01);
        amountSetting0.setCustomKind(0);
        amountSetting0.setMinAmount(10000d);
        amountSettingList.add(amountSetting0);
        AmountSetting amountSetting1 = new AmountSetting();
        amountSetting1.setAppendAmount(0.01);
        amountSetting1.setCustomKind(1);
        amountSetting1.setMinAmount(200000d);
        amountSettingList.add(amountSetting1);
        AmountSetting amountSetting2 = new AmountSetting();
        amountSetting2.setAppendAmount(0.01);
        amountSetting2.setCustomKind(2);
        amountSetting2.setMinAmount(500000d);
        amountSettingList.add(amountSetting2);
        listPushVo.setAmountSetting(amountSettingList);

        List<ExpectRefund> expectRefundList = new ArrayList<>();
        ExpectRefund expectRefund1 = new ExpectRefund();
        expectRefund1.setRefundNumber(1);
        expectRefund1.setRefundDetailDay(dateFormat.parse("2017-07-10"));
        expectRefund1.setRefundStatu(0);
        expectRefund1.setRefundMoney(63000d);
        expectRefund1.setRefundCapital(0d);
        expectRefund1.setRefundInterest(63000d);
        expectRefundList.add(expectRefund1);

        ExpectRefund expectRefund2 = new ExpectRefund();
        expectRefund2.setRefundNumber(2);
        expectRefund2.setRefundDetailDay(dateFormat.parse("2017-08-10"));
        expectRefund2.setRefundStatu(1);
        expectRefund2.setRefundMoney(10063000d);
        expectRefund2.setRefundCapital(10000000d);
        expectRefund2.setRefundInterest(63000d);
        expectRefundList.add(expectRefund2);
        listPushVo.setRefundDetail(expectRefundList);

        List<QuestionVo> questionVoList = new ArrayList<>();
        QuestionVo questionVo1 = new QuestionVo();
        questionVo1.setQuestion("您的年龄：");
        questionVo1.setQuestionKind(0);
        questionVo1.setQuestionNumber("0111111");
        List<ItemListVo> itemListVoList1 = new ArrayList<>();
        ItemListVo itemListVo11 = new ItemListVo();
        itemListVo11.setItemName("A.18岁以下");
        itemListVo11.setPoint(1);
        itemListVoList1.add(itemListVo11);
        ItemListVo itemListVo12 = new ItemListVo();
        itemListVo12.setItemName("B.18岁-30岁");
        itemListVo12.setPoint(2);
        itemListVoList1.add(itemListVo12);
        ItemListVo itemListVo13 = new ItemListVo();
        itemListVo13.setItemName("C.31岁-50岁");
        itemListVo13.setPoint(3);
        itemListVoList1.add(itemListVo13);
        ItemListVo itemListVo14 = new ItemListVo();
        itemListVo14.setItemName("D.51岁-64岁");
        itemListVo14.setPoint(4);
        itemListVoList1.add(itemListVo14);
        ItemListVo itemListVo15 = new ItemListVo();
        itemListVo15.setItemName("E.65岁及以上");
        itemListVo15.setPoint(5);
        itemListVoList1.add(itemListVo15);
        questionVo1.setItemList(itemListVoList1);
        questionVoList.add(questionVo1);

        QuestionVo questionVo2 = new QuestionVo();
        questionVo2.setQuestion("你的家庭年收入：");
        questionVo2.setQuestionKind(0);
        questionVo2.setQuestionNumber("0222222");
        List<ItemListVo> itemListVoList2 = new ArrayList<>();
        ItemListVo itemListVo21 = new ItemListVo();
        itemListVo21.setItemName("A.5万元及以下");
        itemListVo21.setPoint(1);
        itemListVoList2.add(itemListVo21);
        ItemListVo itemListVo22 = new ItemListVo();
        itemListVo22.setItemName("B.5万元-20万元");
        itemListVo22.setPoint(2);
        itemListVoList2.add(itemListVo22);
        ItemListVo itemListVo23 = new ItemListVo();
        itemListVo23.setItemName("C.20万元-50万元");
        itemListVo23.setPoint(3);
        itemListVoList2.add(itemListVo23);
        ItemListVo itemListVo24 = new ItemListVo();
        itemListVo24.setItemName("D.50万元-100万元");
        itemListVo24.setPoint(4);
        itemListVoList2.add(itemListVo24);
        ItemListVo itemListVo25 = new ItemListVo();
        itemListVo25.setItemName("E.100万元以上");
        itemListVo25.setPoint(5);
        itemListVoList2.add(itemListVo25);
        questionVo2.setItemList(itemListVoList2);
        questionVoList.add(questionVo2);

        QuestionVo questionVo3 = new QuestionVo();
        questionVo3.setQuestion("一般情况下，在您每年的家庭收入中，可用于金融投资的比例为：");
        questionVo3.setQuestionKind(0);
        questionVo3.setQuestionNumber("033333333");
        List<ItemListVo> itemListVoList3 = new ArrayList<>();
        ItemListVo itemListVo31 = new ItemListVo();
        itemListVo31.setItemName("A.小于10%");
        itemListVo31.setPoint(1);
        itemListVoList3.add(itemListVo31);
        ItemListVo itemListVo32 = new ItemListVo();
        itemListVo32.setItemName("B.10%-25%");
        itemListVo32.setPoint(2);
        itemListVoList3.add(itemListVo32);
        ItemListVo itemListVo33 = new ItemListVo();
        itemListVo33.setItemName("C.25%-50%");
        itemListVo33.setPoint(3);
        itemListVoList3.add(itemListVo33);
        ItemListVo itemListVo34 = new ItemListVo();
        itemListVo34.setItemName("D.50%以上");
        itemListVo34.setPoint(4);
        itemListVoList3.add(itemListVo34);
        questionVo3.setItemList(itemListVoList3);
        questionVoList.add(questionVo3);

        QuestionVo questionVo4 = new QuestionVo();
        questionVo4.setQuestion("以下哪项最能说明您的投资经验");
        questionVo4.setQuestionKind(0);
        questionVo4.setQuestionNumber("044444444");
        List<ItemListVo> itemListVoList4 = new ArrayList<>();
        ItemListVo itemListVo41 = new ItemListVo();
        itemListVo41.setItemName("A.除存款以外，我几乎不投资其他理财项目");
        itemListVo41.setPoint(1);
        itemListVoList4.add(itemListVo41);
        ItemListVo itemListVo42 = new ItemListVo();
        itemListVo42.setItemName("B.大部分资金用于存款，尝试过货币基金（如余额宝）、信托等低风险理财产品");
        itemListVo42.setPoint(2);
        itemListVoList4.add(itemListVo42);
        ItemListVo itemListVo43 = new ItemListVo();
        itemListVo43.setItemName("C.大部分资金投在货币基金等理财产品，较少投资于股票、基金等高风险产品");
        itemListVo43.setPoint(3);
        itemListVoList4.add(itemListVo43);
        ItemListVo itemListVo44 = new ItemListVo();
        itemListVo44.setItemName("D.资产均衡的分布于存款、国债、银行理财产品、互联网金融产品、信托产品、股票、基金等");
        itemListVo44.setPoint(4);
        itemListVoList4.add(itemListVo44);
        ItemListVo itemListVo45 = new ItemListVo();
        itemListVo45.setItemName("E.大部分投资于股票、基金、外汇等高风险产品，较少投资于存款、国债");
        itemListVo45.setPoint(5);
        itemListVoList4.add(itemListVo45);
        questionVo4.setItemList(itemListVoList4);
        questionVoList.add(questionVo4);

        QuestionVo questionVo5 = new QuestionVo();
        questionVo5.setQuestion("你有多少年投资股票、基金、外汇、金融衍生产品等风险投资品的经验：");
        questionVo5.setQuestionKind(0);
        questionVo5.setQuestionNumber("05555555");
        List<ItemListVo> itemListVoList5 = new ArrayList<>();
        ItemListVo itemListVo51 = new ItemListVo();
        itemListVo51.setItemName("A.没有经验");
        itemListVo51.setPoint(1);
        itemListVoList5.add(itemListVo51);
        ItemListVo itemListVo52 = new ItemListVo();
        itemListVo52.setItemName("B.1年-2年");
        itemListVo52.setPoint(2);
        itemListVoList5.add(itemListVo52);
        ItemListVo itemListVo53 = new ItemListVo();
        itemListVo53.setItemName("C.3年-5年");
        itemListVo53.setPoint(3);
        itemListVoList5.add(itemListVo53);
        ItemListVo itemListVo54 = new ItemListVo();
        itemListVo54.setItemName("D.6年-10年");
        itemListVo54.setPoint(4);
        itemListVoList5.add(itemListVo54);
        ItemListVo itemListVo55 = new ItemListVo();
        itemListVo55.setItemName("E.10年以上");
        itemListVo55.setPoint(5);
        itemListVoList5.add(itemListVo55);
        questionVo5.setItemList(itemListVoList5);
        questionVoList.add(questionVo5);

        for (int i = 1; i < 3; i++) {
            QuestionVo questionVo11 = new QuestionVo();
            questionVo11.setQuestion("您的企业注册的时间：");
            questionVo11.setQuestionKind(i);
            questionVo11.setQuestionNumber("" + i + "11111111");
            List<ItemListVo> itemListVoList11 = new ArrayList<>();
            ItemListVo itemListVo111 = new ItemListVo();
            itemListVo111.setItemName("A.一年以下");
            itemListVo111.setPoint(1);
            itemListVoList11.add(itemListVo111);
            ItemListVo itemListVo112 = new ItemListVo();
            itemListVo112.setItemName("B.1-3年");
            itemListVo112.setPoint(2);
            itemListVoList11.add(itemListVo112);
            ItemListVo itemListVo113 = new ItemListVo();
            itemListVo113.setItemName("C.3-7年");
            itemListVo113.setPoint(3);
            itemListVoList11.add(itemListVo113);
            ItemListVo itemListVo114 = new ItemListVo();
            itemListVo114.setItemName("D.7-10年");
            itemListVo114.setPoint(4);
            itemListVoList11.add(itemListVo114);
            ItemListVo itemListVo115 = new ItemListVo();
            itemListVo115.setItemName("E.10年以上");
            itemListVo115.setPoint(5);
            itemListVoList11.add(itemListVo115);
            questionVo11.setItemList(itemListVoList11);
            questionVoList.add(questionVo11);

            QuestionVo questionVo12 = new QuestionVo();
            questionVo12.setQuestion("您企业的年利润：");
            questionVo12.setQuestionKind(i);
            questionVo12.setQuestionNumber("" + i + "22222222");
            List<ItemListVo> itemListVoList12 = new ArrayList<>();
            ItemListVo itemListVo121 = new ItemListVo();
            itemListVo121.setItemName("A.5万元及以下");
            itemListVo121.setPoint(1);
            itemListVoList12.add(itemListVo121);
            ItemListVo itemListVo122 = new ItemListVo();
            itemListVo122.setItemName("B.5万元-20万元");
            itemListVo122.setPoint(2);
            itemListVoList12.add(itemListVo122);
            ItemListVo itemListVo123 = new ItemListVo();
            itemListVo123.setItemName("C.20万元-50万元");
            itemListVo123.setPoint(3);
            itemListVoList12.add(itemListVo123);
            ItemListVo itemListVo124 = new ItemListVo();
            itemListVo124.setItemName("D.50万元-100万元");
            itemListVo124.setPoint(4);
            itemListVoList12.add(itemListVo124);
            ItemListVo itemListVo125 = new ItemListVo();
            itemListVo125.setItemName("E.100万元以上");
            itemListVo125.setPoint(5);
            itemListVoList12.add(itemListVo125);
            questionVo12.setItemList(itemListVoList12);
            questionVoList.add(questionVo12);

            QuestionVo questionVo13 = new QuestionVo();
            questionVo13.setQuestion("一般情况下，在您每年的企业利润中，可用于金融投资的比例为");
            questionVo13.setQuestionKind(i);
            questionVo13.setQuestionNumber("" + i + "333333");
            List<ItemListVo> itemListVoList13 = new ArrayList<>();
            ItemListVo itemListVo131 = new ItemListVo();
            itemListVo131.setItemName("A.小于10%");
            itemListVo131.setPoint(1);
            itemListVoList13.add(itemListVo131);
            ItemListVo itemListVo132 = new ItemListVo();
            itemListVo132.setItemName("B.10%-25%");
            itemListVo132.setPoint(2);
            itemListVoList13.add(itemListVo132);
            ItemListVo itemListVo133 = new ItemListVo();
            itemListVo133.setItemName("C.25%-50%");
            itemListVo133.setPoint(3);
            itemListVoList13.add(itemListVo133);
            ItemListVo itemListVo134 = new ItemListVo();
            itemListVo134.setItemName("D.50%以上");
            itemListVo134.setPoint(4);
            itemListVoList13.add(itemListVo134);
            questionVo13.setItemList(itemListVoList13);
            questionVoList.add(questionVo13);

            QuestionVo questionVo14 = new QuestionVo();
            questionVo14.setQuestion("以下哪项最能说明您的投资经验");
            questionVo14.setQuestionKind(0);
            questionVo14.setQuestionNumber("" + i + "44444444");
            List<ItemListVo> itemListVoList14 = new ArrayList<>();
            ItemListVo itemListVo141 = new ItemListVo();
            itemListVo141.setItemName("A.除存款以外，我几乎不投资其他理财项目");
            itemListVo141.setPoint(1);
            itemListVoList14.add(itemListVo141);
            ItemListVo itemListVo142 = new ItemListVo();
            itemListVo142.setItemName("B.大部分资金用于存款，尝试过货币基金（如余额宝）、信托等低风险理财产品");
            itemListVo142.setPoint(2);
            itemListVoList14.add(itemListVo142);
            ItemListVo itemListVo143 = new ItemListVo();
            itemListVo143.setItemName("C.大部分资金投在货币基金等理财产品，较少投资于股票、基金等高风险产品");
            itemListVo143.setPoint(3);
            itemListVoList14.add(itemListVo143);
            ItemListVo itemListVo144 = new ItemListVo();
            itemListVo144.setItemName("D.资产均衡的分布于存款、国债、银行理财产品、互联网金融产品、信托产品、股票、基金等");
            itemListVo144.setPoint(4);
            itemListVoList14.add(itemListVo144);
            ItemListVo itemListVo145 = new ItemListVo();
            itemListVo145.setItemName("E.大部分投资于股票、基金、外汇等高风险产品，较少投资于存款、国债");
            itemListVo145.setPoint(5);
            itemListVoList14.add(itemListVo145);
            questionVo14.setItemList(itemListVoList14);
            questionVoList.add(questionVo14);

            QuestionVo questionVo15 = new QuestionVo();
            questionVo15.setQuestion("你的企业有多少年投资股票、基金、外汇、金融衍生产品等风险投资品的经验：");
            questionVo15.setQuestionKind(0);
            questionVo15.setQuestionNumber("" + i + "5555555");
            List<ItemListVo> itemListVoList15 = new ArrayList<>();
            ItemListVo itemListVo151 = new ItemListVo();
            itemListVo151.setItemName("A.没有经验");
            itemListVo151.setPoint(1);
            itemListVoList15.add(itemListVo151);
            ItemListVo itemListVo152 = new ItemListVo();
            itemListVo152.setItemName("B.1年-2年");
            itemListVo152.setPoint(2);
            itemListVoList15.add(itemListVo152);
            ItemListVo itemListVo153 = new ItemListVo();
            itemListVo153.setItemName("C.3年-5年");
            itemListVo153.setPoint(3);
            itemListVoList15.add(itemListVo153);
            ItemListVo itemListVo154 = new ItemListVo();
            itemListVo154.setItemName("D.6年-10年");
            itemListVo154.setPoint(4);
            itemListVoList15.add(itemListVo154);
            ItemListVo itemListVo155 = new ItemListVo();
            itemListVo155.setItemName("E.10年以上");
            itemListVo155.setPoint(5);
            itemListVoList15.add(itemListVo155);
            questionVo15.setItemList(itemListVoList15);
            questionVoList.add(questionVo15);
        }
        listPushVo.setQuestionList(questionVoList);

        List<RiskVo> riskVoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            RiskVo riskVo0 = new RiskVo();
            riskVo0.setRiskBearAbility(i);
            riskVo0.setRiskKind(0);
            riskVo0.setNeedPoint(i * 10);
            riskVoList.add(riskVo0);

            RiskVo riskVo1 = new RiskVo();
            riskVo1.setRiskBearAbility(i);
            riskVo1.setRiskKind(1);
            riskVo1.setNeedPoint(i * 8);
            riskVoList.add(riskVo1);

            RiskVo riskVo2 = new RiskVo();
            riskVo2.setRiskBearAbility(i);
            riskVo2.setRiskKind(2);
            riskVo2.setNeedPoint(i * 5);
            riskVoList.add(riskVo2);
        }
        listPushVo.setRiskList(riskVoList);

        List<BidVo> bidVoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            BidVo bidVo1 = new BidVo();
            bidVo1.setBidNumber("djs-xxx-rhtsybl-015346-001-bncd-" + i);
            bidVo1.setMoney(1000000d);
            List<MaxAmountVo> maxAmountVoList1 = new ArrayList<>();
            MaxAmountVo maxAmountVo0 = new MaxAmountVo();
            maxAmountVo0.setCustomKind(0);
            maxAmountVo0.setMaxAmount(1000000d);
            maxAmountVoList1.add(maxAmountVo0);
            MaxAmountVo maxAmountVo1 = new MaxAmountVo();
            maxAmountVo1.setCustomKind(1);
            maxAmountVo1.setMaxAmount(1000000d);
            maxAmountVoList1.add(maxAmountVo1);
            MaxAmountVo maxAmountVo2 = new MaxAmountVo();
            maxAmountVo2.setCustomKind(2);
            maxAmountVo2.setMaxAmount(1000000d);
            maxAmountVoList1.add(maxAmountVo2);
            bidVo1.setMaxAmountList(maxAmountVoList1);
            bidVoList.add(bidVo1);
        }
        listPushVo.setBidList(bidVoList);
        String s = JSON.toJSONString(listPushVo);
        String random = RandomStringUtils.random(20, true, true);
        Integer randomInt = (int) (Math.random() * 16 + 1);
        String ciphertext = EncryptionAlgorithm.encrypte("127.0.0.1", random, randomInt, new Date());
        String express = random + randomInt.toString();
        String uri = BASEURL + "/ex/recZixunfanData" + "?ciphertext=" + ciphertext + "&express=" + express;
        sendJsonRequest(uri, s);
    }

    private static HttpResponse sendJsonRequest(String uri, String jsonString) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        RequestBuilder requestBuilder = RequestBuilder.post();
        requestBuilder.setUri(uri);
        StringEntity stringEntity;
        HttpResponse res = null;
        try {
            String s = new String(jsonString.getBytes("utf-8"), "ISO8859-1");
            stringEntity = new StringEntity(s);
            stringEntity.setContentEncoding("application/json; charset=utf-8");
            stringEntity.setContentType("application/json; charset=utf-8");
            requestBuilder.setEntity(stringEntity);
            res = httpClient.execute(requestBuilder.build());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
