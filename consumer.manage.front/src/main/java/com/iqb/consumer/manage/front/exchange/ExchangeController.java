package com.iqb.consumer.manage.front.exchange;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.data.layer.bean.jys.ExchangeData;
import com.iqb.consumer.data.layer.bean.jys.Iqb_customer_store_info;
import com.iqb.consumer.data.layer.bean.jys.JYSDictInfo;
import com.iqb.consumer.data.layer.bean.jys.RecordAssets;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.order.IqbCustomerStoreBiz;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.exchange.ExchangeDataService;
import com.iqb.consumer.service.layer.exchange.IAssetService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.config.BaseConfig;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

@Controller
@RequestMapping
public class ExchangeController extends BaseService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Resource
    private IAssetService assetService;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private IqbCustomerStoreBiz iqbCustomerStoreBiz;
    @Resource(name = "baseConfig")
    private BaseConfig config;
    @Resource
    private UrlConfig urlConfig;
    @Resource
    private ExchangeDataService exchangeDataService;
    @Resource
    private CalculateService calculateService;
    @Resource
    private YMForeignConfig ymForeignConfig;

    /**
     * 文件下载
     * 
     * @Description:
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/assetXlsDownload")
    public String assetXlsDownload(HttpServletRequest request, HttpServletResponse response) {
        String realPath = config.getCommon_basedir();
        String fileName = "assetTemplate.xlsx";
        File file = new File(realPath, fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                logger.error("资产导入模板文件异常:{}", e);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        logger.error("关闭流异常:{}", e);
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        logger.error("关闭流异常:{}", e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 交易所项目接口：备案资产要素
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ex/recRecordAssets", method = RequestMethod.POST)
    public Object recRecordAssets(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        logger.info("接收到{}备案资产要素为:{}", param, objs);
        boolean flag;
        try {
            // 合法性效验
            flag = decode(param.get("ciphertext"), param.get("express"), urlConfig.getGetZxfIp());
        } catch (Exception e) {
            result.put("code", 0);
            result.put("msg", "验证发生异常");
            return result;
        }
        if (flag) {
            // 业务逻辑处理
            // ExchangeData
            RecordAssets recordAssets = JSONObject.toJavaObject(objs, RecordAssets.class);
            result = exchangeDataService.saveRecordAssets(recordAssets);
        } else {
            result.put("code", 0);
            result.put("msg", "验证发生错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/ex/selRecordAssets", method = RequestMethod.POST)
    public Object selRecordAssets(@RequestBody JSONObject objs, HttpServletRequest request) {
        RecordAssets recordAssets;
        try {
            recordAssets = exchangeDataService.selRecordAssets(objs.getString("assetNumber"));
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (recordAssets != null) {
            // 不存在对应的备案编号
            params.put("assetName", recordAssets.getAssetName());
            params.put("assetFax", recordAssets.getAssetFax());
            params.put("assetUrl", recordAssets.getAssetUrl());
        }
        linkedHashMap.put("result", params);
        return super.returnSuccessInfo(linkedHashMap);
    }

    /**
     * 交易所项目接口：接收咨询范提供的数据
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ex/recZixunfanData", method = RequestMethod.POST)
    public Object recZixunfanData(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        logger.info("接收到{}咨询范数据为:{}", param, objs);
        boolean flag;
        try {
            // 合法性效验
            flag = decode(param.get("ciphertext"), param.get("express"), urlConfig.getGetZxfIp());
        } catch (Exception e) {
            result.put("code", 0);
            result.put("msg", "验证发生异常");
            return result;
        }
        if (flag) {
            // 业务逻辑处理
            // ExchangeData
            ExchangeData exchangeData = JSONObject.toJavaObject(objs, ExchangeData.class);
            result = exchangeDataService.saveExchangeData(exchangeData);
        } else {
            result.put("code", 0);
            result.put("msg", "验证发生错误");
        }
        return result;
    }

    /**
     * 咨询范认证方式
     * 
     * @param ciphertext
     * @param express
     * @param zxfIP
     * @return
     */
    private static boolean decode(String ciphertext, String express, String zxfIP) {
        String number = "";
        if (express.length() == 21) {
            number = express.substring(20, 21);
        }
        if (express.length() == 22) {
            number = express.substring(20, 22);
        }
        int randomNumber = Integer.parseInt(number);
        String[] ds = ciphertext.split("d");
        byte[] bytes = new byte[40];
        int count = 0;
        for (String s : ds) {
            int xor = Integer.parseInt(s);
            Integer xorMid = xor ^ randomNumber;
            byte b = xorMid.byteValue();
            bytes[count] = b;
            count++;
        }
        String s1 = new String(bytes);

        String[] ds1 = s1.split("d");
        String ip = ds1[0];
        if (zxfIP.equals(ip))
            return true;
        return false;
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/assetXlsImport"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map assetXlsImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            logger.debug("开始上传xls文件...");
            Map<String, Object> result = assetService.assetXlsImport(file);
            logger.debug("上传xls文件结束...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求：打包信息保存
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/savePackInfo"}, method = {RequestMethod.POST})
    public Object savePackInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("pack info params:{}", objs);
            // 避免客户客户双击造成数据重复
            Redisson redisson =
                    RedisUtils.getInstance()
                            .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
            RLock rLock = null;
            int resultValue = 0;
            try {
                rLock = RedisUtils.getInstance().getRLock(redisson, "savePackInfoFromRedis");
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    resultValue = assetService.insertPackInfo(objs);
                } else {
                    throw new IqbException(ConsumerReturnInfo.GET_PROJECT_CREATE_NAME_LOCK);
                }
            } catch (Exception e) {
                throw new IqbException(ConsumerReturnInfo.PROJECT_CREATE_NAME);
            } finally {
                rLock.unlock();
            }
            try {
                if (redisson != null) {
                    RedisUtils.getInstance().closeRedisson(redisson);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("success", resultValue);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getGuaranteeInfo"}, method = {RequestMethod.POST})
    public Object getGuaranteeInfo(@RequestBody JSONObject objs, HttpServletRequest request) {

        try {
            logger.info("pack info params:{}", objs);
            List<Iqb_customer_store_info> list = iqbCustomerStoreBiz
                    .queryAllInfo(objs.getString("guaranteeInstitution"));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求：通过Key查询Dict表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getDictByKey"}, method = {RequestMethod.POST})
    public Object getDictByKey(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("pack info params:{}", objs);
            List<JYSDictInfo> list = assetService.queryAllByKey(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求：通过接口计算订单金额
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/calOrderInfo"}, method = {RequestMethod.POST})
    public Object calOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("pack info params:{}", objs);
            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(objs.getString("planId")));
            Map<String, BigDecimal> detailMap =
                    calculateService.calculateAmt(planBean, new BigDecimal(objs.getString("orderAmt")));// getDetail(new
                                                                                                        // BigDecimal(objs.getString("orderAmt")),
                                                                                                        // planBean);
            detailMap.put("orderItems", new BigDecimal(planBean.getInstallPeriods()));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", detailMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
