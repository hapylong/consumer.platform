package com.iqb.consumer.manage.front.crm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.test.penalty.derate.util.HttpClientUtil;

public class TestCustonerStoreController {
    private static final String BASEURL = "http://localhost:8080/consumer.manage.front/unIntcpt-customerStore";
    protected static final Logger logger = LoggerFactory.getLogger(TestCustonerStoreController.class);

    /**
     * 
     * Description: response
     * 
     * {"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功","retFactInfo":"处理成功","iqbResult":{"result":{"icsi":[{"id":null,"customerCode":"1006001","creditorName":"魏东宁","creditorIdNo":"220724196910294413","creditorBankNo":"6214834314848897","creditorBankName":"招商银行","creditorPhone":"13904388496","guaranteeCorporationCode":null,"guaranteeCorporationName":"松原市轮动方程汽车租赁有限公司","hasAuthed":1,"authChannel":"徽商银行","authResult":null,"status":2,"deleteFlag":null,"creditorOtherInfo":null,"flag":1}],"cb":{"customerName":"松原市轮动方程汽车租赁有限公司","customerCode":"1006001","customerShortName":"松原轮动","customerFullName":null,"customerShortNameCode":null,"customerType":null,"businessType":null,"belongsArea":null,"province":"吉林省","city":"松原市","addressDetail":null,"customerStatus":null,"pushTime":null,"createTime":null,"imgUrl":null,"deleteFlag":null,"remark":null,"version":null,"contactUserName":null,"contactPhoneNum":null,"creditorOtherInfo":null,"flag":null,"regSerialNum":null,"regAddress":null,"regCapital":null,"establishmenDate":null,"merchantMaturity":null,"operatingPeriod":null,"corporateName":null,"corporateCertificateType":null,"corporateCertificateCode":null,"uniformCreditCode":null,"organizationCode":null,"taxCertificateCode":null,"accountPermitCode":null,"icpCode":null,"companyChop":null,"companyChopPwd":null,"companyPageId":null,"companyUrl":null,"workAddress":null,"businessLicenseNum":null,"serviceCall":null,"enterpriseMailbox":null,"legalPersonPhoneNum":null,"socialCreditCode":null,"icRegCode":null,"businessContactName":null,"businessContactIdcard":null,"holdWeixin":null,"riskManageType":null,"isFather":null,"layer":null,"installmentPlan":null,"overdueInterestRate":null,"overdueFixedFee":null,"overdueInterestModel":null,"isVirtualMerc":null,"requestType":null,"reserveField":null,"corporateImgUrl":null,"componyImgUrl":null,"creditorName":"魏东宁","creditorIdNo":"220724196910294413","creditorBankNo":"6214834314848897","creditorBankName":"招商银行","creditorPhone":"13904388496","guaranteeCorporationCode":null,"guaranteeCorporationName":"松原市轮动方程汽车租赁有限公司","guaranteeCorporationCorName":"张山","creditorStatus":"2","hasAuthed":"1","authChannel":"徽商银行","authResult":null,"higherOrgName":"消费金融","higherOrgCode":null}}}}
     * 
     * @param
     * @return void
     * @throws
     * @Author adam
     * Create Date: 2017年6月9日 下午1:51:45
     */
    @Test
    public void testGetCustomerStoreInfoByCode() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customerCode", "1006001");
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/getCustomerStoreInfoByCode", json);
        logger.info("返回结果:{}", result);
    }

    /**
     * 
     * Description:
     * 返回结果:{"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功","retFactInfo"
     * :"处理成功","iqbResult":{"result":{"merchantId":null,"realName":null,"regId":null,"orderId":null,
     * "proType"
     * :null,"idCard":null,"bankCard":null,"address":null,"companyName":null,"companyAddress"
     * :null,"companyPhone"
     * :null,"colleagues1":null,"colleagues2":null,"tel1":null,"tel2":null,"relation1"
     * :null,"sex1":null
     * ,"phone1":null,"relation2":null,"sex2":null,"phone2":null,"relation3":null,"sex3"
     * :null,"phone3"
     * :null,"creditNo":null,"creditPasswd":null,"rname1":null,"rname2":null,"rname3":null}}}
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午5:56:39
     */
    @Test
    public void getSubletInfo() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "CDHTC3001170525005");
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/get_risk", json);
        logger.info("返回结果:{}", result);
    }

    /**
     * 
     * Description:返回结果:{"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功",
     * "retFactInfo":"处理成功","iqbResult":{"result":"success"}}
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午5:57:25
     */
    @Test
    public void persistDesignPersion() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", "zhangxindan");
        params.put("text", "张馨丹(zhangxindan)");
        params.put("orderId", "20160719-769424");
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/persist_design_person", json);
        logger.info("返回结果:{}", result);
    }

    /**
     * 
     * Description:
     * 返回结果:{"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功","retFactInfo"
     * :"处理成功","iqbResult":{"result":{"id":1,"version":0,"createTime":1494929613000,"updateTime":
     * 1494929613000
     * ,"orderId":"zhangxindan","designCode":"20160719-769424","designName":"张馨丹(zhangxindan)"
     * ,"guarantorNum"
     * :null,"creditType":null,"borrowTogether":null,"borrTogetherName":null,"creditInfo"
     * :null,"type":null}}}
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月17日 下午2:08:29
     */
    @Test
    public void getDesignedPersionInfo() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "20160719-769424");
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/get_designated_person_info", json);
        logger.info("返回结果:{}", result);
    }

    @Test
    public void recalculateAmtTest() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "20160719-769424");
        params.put("planId", "130");
        params.put("orderAmt", new BigDecimal(50000));
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/recalculate_amt", json);
        logger.info("返回结果:{}", result);
    }
}
