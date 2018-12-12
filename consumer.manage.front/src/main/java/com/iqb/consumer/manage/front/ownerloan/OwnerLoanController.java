package com.iqb.consumer.manage.front.ownerloan;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.bean.ownerloan.CheckInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.DeptSignInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MortgageInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.OwnerLoanBaseInfoBean;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.creditorinfo.CreditorInfoService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.ownerloan.OwnerLoanService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:车主贷Controller
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年11月10日上午9:32:54 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/ownerloan")
public class OwnerLoanController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(OwnerLoanController.class);
    @Resource
    private OwnerLoanService ownerLoanServiceImpl;
    @Resource
    private CreditorInfoService creditorInfoServiceImpl;
    @Resource
    private DictService dictServiceImpl;

    private final static String SUCCESS_FLAG = "success";
    private final static String FAIL_FLAG = "fail";

    /**
     * 
     * Description:车主贷-车辆信息查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月10日
     */
    @ResponseBody
    @RequestMapping(value = "/getCarinfo", method = RequestMethod.POST)
    public Object getCarinfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-车辆信息查询:{}", objs);
        try {
            MortgageInfoBean carInfo = ownerLoanServiceImpl.getCarinfo(objs);
            if (carInfo != null) {
                if (!StringUtil.isNull(carInfo.getMortgageCompany()) && carInfo.getInstAmt() != null) {
                    carInfo.setMortgageFlag(1);
                } else {
                    carInfo.setMortgageFlag(2);
                }
            }
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", carInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷-查询抵押及相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月10日
     */
    @ResponseBody
    @RequestMapping(value = "/getMortgageInfo", method = RequestMethod.POST)
    public Object getMortgageInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-查询抵押及相关信息:{}", objs);
        try {
            MortgageInfoBean mortgageInfo = ownerLoanServiceImpl.selectOneByOrderId(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", mortgageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷-获取订单 人员 卡信息接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    @ResponseBody
    @RequestMapping(value = "/getBaseInfo", method = RequestMethod.POST)
    public Object getBaseInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-获取订单 人员 卡信息接口:{}", objs);
        try {
            OwnerLoanBaseInfoBean baseInfo = ownerLoanServiceImpl.getBaseInfo(objs);
            baseInfo.setAge(IdNOToAge(baseInfo.getIdNo()));
            baseInfo.setSex(IdNOToSex(baseInfo.getIdNo()));

            if (!StringUtil.isNull(baseInfo.getOrderName())) {
                String orderName = baseInfo.getOrderName();
                String[] orderNames = orderName.split("-");
                baseInfo.setCarBrand(orderNames[0]);
                baseInfo.setCarDetail(orderNames[1]);
            }
            String riskRetRemark = baseInfo.getRiskRetRemark();
            if (!StringUtil.isNull(riskRetRemark)) {
                String[] riskRetRemarks = riskRetRemark.split(":");
                baseInfo.setRiskStatus(Integer.parseInt(riskRetRemarks[0]) == 1 ? "审核通过" : "审核未通过");
                baseInfo.setRiskMessage(riskRetRemarks[1]);
            } else {
                baseInfo.setRiskStatus("");
                baseInfo.setRiskMessage("");
            }
            String checkInfo = baseInfo.getCheckInfo();
            CheckInfoBean checkInfoBean = JSON.toJavaObject(JSON.parseObject(checkInfo), CheckInfoBean.class);
            checkInfoBean.setRelation1(dictServiceImpl.getDictByDTCAndDN(DictTypeCodeEnum.SECTORS,
                    checkInfoBean.getRelation1()).getDictName());
            checkInfoBean.setRelation2(dictServiceImpl.getDictByDTCAndDN(DictTypeCodeEnum.SECTORS,
                    checkInfoBean.getRelation2()).getDictName());
            checkInfoBean.setRelation3(dictServiceImpl.getDictByDTCAndDN(DictTypeCodeEnum.SECTORS,
                    checkInfoBean.getRelation3()).getDictName());
            checkInfoBean.setRelation4(dictServiceImpl.getDictByDTCAndDN(DictTypeCodeEnum.SECTORS,
                    checkInfoBean.getRelation4()).getDictName());
            baseInfo.setCheckInfoBean(checkInfoBean);
            baseInfo.setCheckInfo(null);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", baseInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷-更新车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    @ResponseBody
    @RequestMapping(value = "/updateCarInfo", method = RequestMethod.POST)
    public Object updateCarInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-更新车辆抵押信息接口:{}", objs);
        try {
            int result = ownerLoanServiceImpl.updateCarInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result >= 1) {
                linkedHashMap.put("result", SUCCESS_FLAG);
            } else {
                linkedHashMap.put("result", FAIL_FLAG);
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:更新车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    @ResponseBody
    @RequestMapping(value = "/updateMortgageInfo", method = RequestMethod.POST)
    public Object updateMortgageInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-更新车辆抵押信息接口:{}", objs);
        try {
            int result = ownerLoanServiceImpl.updateMortgageInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result >= 1) {
                linkedHashMap.put("result", SUCCESS_FLAG);
            }
            linkedHashMap.put("result", FAIL_FLAG);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:更新车辆Gps相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    @ResponseBody
    @RequestMapping(value = "/updateGpsInfo", method = RequestMethod.POST)
    public Object updateGpsInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-更新车辆抵押信息接口:{}", objs);
        try {
            int result = ownerLoanServiceImpl.updateGpsInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result >= 1) {
                linkedHashMap.put("result", SUCCESS_FLAG);
            }
            linkedHashMap.put("result", FAIL_FLAG);

            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷-更新放款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    @ResponseBody
    @RequestMapping(value = "/updateOrderInfo", method = RequestMethod.POST)
    public Object updateOrderInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-更新车辆抵押信息接口:{}", objs);
        try {
            int result = ownerLoanServiceImpl.updateOrderInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result >= 1) {
                linkedHashMap.put("result", SUCCESS_FLAG);
            } else {
                linkedHashMap.put("result", FAIL_FLAG);
            }

            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷-风控回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-riskNotice", method = RequestMethod.POST)
    public Object ownerLoanRiskNotice(@RequestBody JSONObject objs) {
        logger.info("车主贷-风控回调通知开始:{}", objs);
        try {
            int result = ownerLoanServiceImpl.ownerLoanRiskNotice(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("车主贷-风控回调通知完成:{}", objs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车主贷金额重算
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    @ResponseBody
    @RequestMapping(value = "/recalAmt", method = RequestMethod.POST)
    public Object recalAmt(@RequestBody JSONObject objs) {
        logger.info("车主贷金额重算开始:{}", objs);
        try {
            Map<String, BigDecimal> result = ownerLoanServiceImpl.recalAmt(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("车主贷金额重算完成:{}", objs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            System.out.println("3333-----" + e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:获取门店签约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    @ResponseBody
    @RequestMapping(value = "/getDeptSignInfo", method = RequestMethod.POST)
    public Object getDeptSignInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-获取门店签约信息开始:{}", objs);
        try {
            DeptSignInfoBean result = ownerLoanServiceImpl.getDeptSignInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("车主贷-获取门店签约信息完成:{}", objs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:获取放款确认信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    @ResponseBody
    @RequestMapping(value = "/getLoanInfo", method = RequestMethod.POST)
    public Object getLoanInfo(@RequestBody JSONObject objs) {
        logger.info("车主贷-获取放款确认信息开始:{}", objs);
        try {
            DeptSignInfoBean result = ownerLoanServiceImpl.getLoanInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("车主贷-获取放款确认信息完成:{}", objs);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("ZRZX错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月10日
     */
    @Override
    public int getGroupCode() {
        return 0;
    }

    /**
     * 
     * Description:根据身份证号码计算性别
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    private String IdNOToSex(String IdNO) {
        int leh = IdNO.length();
        String sexNum = IdNO.substring(leh - 2, leh - 1);
        if (Integer.parseInt(sexNum) % 2 == 0) {
            return "女";
        } else {
            return "男";
        }
    }

    /**
     * 
     * Description:根据身份证获取年龄
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    private int IdNOToAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }
    }
}
