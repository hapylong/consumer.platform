package com.iqb.consumer.batch.service.schedule;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.batch.biz.CheThreeHBiz;
import com.iqb.consumer.batch.biz.ContractInfoBiz;
import com.iqb.consumer.batch.biz.InstAssetStockBiz;
import com.iqb.consumer.batch.biz.InstPenaltyDerateBiz;
import com.iqb.consumer.batch.biz.InstRemindPhoneBiz;
import com.iqb.consumer.batch.biz.OrderBiz;
import com.iqb.consumer.batch.biz.ScheduleTaskManager;
import com.iqb.consumer.batch.biz.SettleApplyBeanBiz;
import com.iqb.consumer.batch.config.BuckleConfig;
import com.iqb.consumer.batch.config.ConsumerConfig;
import com.iqb.consumer.batch.config.ParamConfig;
import com.iqb.consumer.batch.config.PledgeUrlConfig;
import com.iqb.consumer.batch.data.entity.InstScheduleTaskEntity;
import com.iqb.consumer.batch.data.entity.InstSettlementResultEntity;
import com.iqb.consumer.batch.data.pojo.BuckleScheduleTaskAnalysisPojo;
import com.iqb.consumer.batch.data.pojo.BuckleSendPolicy;
import com.iqb.consumer.batch.data.pojo.CallBackResponsePojo;
import com.iqb.consumer.batch.data.pojo.ContractInfoBean;
import com.iqb.consumer.batch.data.pojo.EndProcessPojo;
import com.iqb.consumer.batch.data.pojo.InstAssetStockPojo;
import com.iqb.consumer.batch.data.pojo.InstOrderInfoEntity;
import com.iqb.consumer.batch.data.pojo.InstRemindPhoneBean;
import com.iqb.consumer.batch.data.pojo.InstallmentBillPojo;
import com.iqb.consumer.batch.data.pojo.KeyValuePair;
import com.iqb.consumer.batch.data.pojo.OrderBean;
import com.iqb.consumer.batch.data.pojo.SettleApplyBean;
import com.iqb.consumer.batch.data.pojo.SettlementCenterBuckleRequestMessage;
import com.iqb.consumer.batch.data.pojo.SpecialTimeOrderPojo;
import com.iqb.consumer.batch.data.schedule.http.FullScaleLendingIdsRequestMessage;
import com.iqb.consumer.batch.data.schedule.http.FullScaleLendingIdsResponseMessage;
import com.iqb.consumer.batch.data.schedule.http.FullScaleLendingPojosResponseMessage;
import com.iqb.consumer.batch.data.schedule.pojo.CfRequestMoneyPojo;
import com.iqb.consumer.batch.service.dict.DictService;
import com.iqb.consumer.batch.util.BuckleUtils.Chat;
import com.iqb.consumer.batch.util.CalendarUtil;
import com.iqb.consumer.batch.util.CallBackPolicyRepository;
import com.iqb.consumer.batch.util.HttpsClientUtil;
import com.iqb.consumer.batch.util.MathUtil;
import com.iqb.consumer.batch.util.encript.EncryptUtils;
import com.iqb.consumer.batch.util.encript.RSAUtils;
import com.iqb.consumer.batch.util.encript.SignUtil;
import com.iqb.etep.common.base.config.BaseConfig;
import com.iqb.etep.common.utils.DateUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.https.SendHttpsUtil;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;
import com.iqb.platform.security.client.ApplicationSecurityClient;

@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);
    @Autowired
    private ScheduleTaskManager scheduleTaskManager;

    @Autowired
    private ContractInfoBiz contractInfoBiz;
    @Autowired
    private PledgeUrlConfig pledgeUrlConfig;
    @Autowired
    private DictService dictServiceImpl;
    @Autowired
    private ConsumerConfig consumerConfig;
    @Autowired
    private ParamConfig paramConfig;
    @Autowired
    private BuckleConfig buckleConfig;
    @Autowired
    private BaseConfig config;
    @Autowired
    private InstPenaltyDerateBiz instPenaltyDerateBiz;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Autowired
    private EncryptUtils encryptUtils;

    @Autowired
    private InstRemindPhoneBiz instRemindPhoneBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private CheThreeHBiz cheThreeHBiz;

    private final int NO_EXCUTE_TASK = 0;
    private final int UNKNOWN_ERROR = -1;
    private final int ERROR_RESPONSE_1 = -2;
    private final int ERROR_RESPONSE_1_EMPTY_LIST = -6;
    private final int ERROR_RESPONSE_2 = -3;
    private final int ERROR_SYMPHONY = -5;
    private static boolean SYMPHONY = false;

    private static boolean CALLBACK = false;

    private final String STATUS_NAME = "status";
    @Autowired
    private InstAssetStockBiz instAssetStockBiz;

    @SuppressWarnings("static-access")
    @Override
    public int fullScaleLendingScheduleTask() {
        if (SYMPHONY) {
            return ERROR_SYMPHONY;
        }
        SYMPHONY = true;
        try {
            /** CLEAR CACHE **/
            FullScaleLendingIdsResponseMessage.init();
            List<String> orderids = scheduleTaskManager.getChatIds();
            if (orderids == null || orderids.isEmpty()) {
                return NO_EXCUTE_TASK;
            }
            FullScaleLendingIdsRequestMessage fsli = new FullScaleLendingIdsRequestMessage();
            if (!fsli.generatorIds(orderids)) {
                return orderids.size();
            }
            fsli.setSign(sign(fsli.getOrderIds()));
            /** FIRST REQUEST **/
            JSONArray ja = null;
            try {
                String json = HttpsClientUtil.getInstance().
                        doPost(pledgeUrlConfig.getFullScaleLendingIdsUrl(),
                                JSONObject.toJSONString(fsli), "utf-8");
                logger.info("ScheduleTaskServiceImpl[fullScaleLendingScheduleTask] POST *"
                        + JSONObject.toJSONString(fsli) + "* /r/n URL *"
                        + pledgeUrlConfig.getFullScaleLendingIdsUrl() + "*/r/n FIRST RESPONSE :" + json);
                ja = JSONObject.parseArray(json);
                logger.info("fullScaleLendingScheduleTask[RESPONSE_1] : " + json);
            } catch (Throwable e) {
                logger.error("fullScaleLendingScheduleTask[ERROR_RESPONSE_1]", e);
                return ERROR_RESPONSE_1;
            }
            List<FullScaleLendingIdsResponseMessage> fsir = new LinkedList<>();
            if (ja != null && ja.size() > 0) {
                for (int i = 0; i < ja.size(); i++) {
                    FullScaleLendingIdsResponseMessage fs =
                            JSONObject.toJavaObject(ja.getJSONObject(i), FullScaleLendingIdsResponseMessage.class);
                    /** CHECK RESPONSE **/
                    if (!fs.check()) {
                        continue;
                    }
                    InstOrderInfoEntity ioie = scheduleTaskManager.getDetailsByOid(fs.getOrderId());
                    if (ioie == null || !fs.integration(ioie)) {
                        continue;
                    }
                    fs.setOpenId(dictServiceImpl.getOpenIdByBizType(fs.getBizType()));
                    /** ADD CACHE **/
                    CfRequestMoneyPojo cf = CfRequestMoneyPojo.init();
                    if (cf.create(fs)) {
                        FullScaleLendingIdsResponseMessage.setRepository(fs.getOrderId(), cf);
                        fsir.add(fs);
                    }
                }
            } else {
                return ERROR_RESPONSE_1;
            }
            /** SECOND REQUEST **/
            JSONArray ja2 = null;
            if (fsir.isEmpty()) {
                return ERROR_RESPONSE_1_EMPTY_LIST;
            }
            try {
                String json2 = SimpleHttpUtils.
                        httpPost(pledgeUrlConfig.getFullScaleLendingPojoUrl(), encode(fsir));
                logger.info("ScheduleTaskServiceImpl[fullScaleLendingScheduleTask] POST *"
                        + JSONObject.toJSONString(fsir) + "* /r/n URL *"
                        + pledgeUrlConfig.getFullScaleLendingPojoUrl() + "*/r/n SECOND RESPONSE :" + json2);
                ja2 = JSONObject.parseArray(json2);
                logger.info("fullScaleLendingScheduleTask[RESPONSE_2] :" + json2);
            } catch (Throwable e) {
                logger.error("fullScaleLendingScheduleTask[ERROR_RESPONSE_2]", e);
                return ERROR_RESPONSE_2;
            }
            if (ja2 != null && ja2.size() > 0) {
                List<String> errorRecord = new LinkedList<>();
                for (int i = 0; i < ja.size(); i++) {
                    FullScaleLendingPojosResponseMessage fs =
                            JSONObject.toJavaObject(ja2.getJSONObject(i), FullScaleLendingPojosResponseMessage.class);
                    if (fs.check()) {
                        /** LOAD FROM CACHE **/
                        CfRequestMoneyPojo cf = FullScaleLendingIdsResponseMessage.getRepository(fs.getOrderId());
                        if (cf != null) {
                            try {
                                scheduleTaskManager.removeChatId(cf);
                            } catch (Throwable e) {
                                errorRecord.add(fs.getOrderId());
                                logger.error("ScheduleTaskServiceImpl[fullScaleLendingScheduleTask] error ", e);
                            }
                        }
                    }
                }
                logger.error(
                        "ScheduleTaskServiceImpl[fullScaleLendingScheduleTask] error update orderIds {} size [{}]",
                        JSONObject.toJSONString(errorRecord), errorRecord.size());
            } else {
                return ERROR_RESPONSE_2;
            }
            return orderids.size();
        } catch (Throwable e) {
            logger.error("ScheduleTaskServiceImpl[fullScaleLendingScheduleTask] UNKNOWN_ERROR ", e);
            return UNKNOWN_ERROR;
        } finally {
            SYMPHONY = false;
        }
    }

    private String sign(List<String> orderIds) throws NoSuchAlgorithmException {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(JSONObject.toJSONString(orderIds).getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    private Map<String, Object> encode(List<FullScaleLendingIdsResponseMessage> list) throws Exception {
        // String PUBLIC_KEY = consumerConfig.getCommonPublicKey();
        String PRIVATE_KEY = consumerConfig.getCommonPrivateKey();
        String source = JSONObject.toJSONString(list);
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, PRIVATE_KEY);
        String data1 = new String(Base64.encodeBase64(encodedData), "UTF-8");
        String sign = RSAUtils.sign(encodedData, PRIVATE_KEY);
        Map<String, Object> params = new HashMap<>();
        params.put("data", data1);
        params.put("sign", sign);
        return params;
    }

    private static int COUNT = 0;

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Object> callBackScheduleTask() {
        if (CALLBACK) {
            COUNT++; // 如果标识使程序驳回， 驳回次数 ++ ， 用以数据集 查询起始条件（start）的计算
            throw new RuntimeException("线程任务未完成不许进入");
        } else {
            COUNT = 0; // 否则，初始化该值，使查询区间不变。
        }
        CALLBACK = true;
        try {
            Date start = new Date();
            Date createTime = new Date();
            start.setTime(start.getTime() - COUNT * CalendarUtil.SCHEDULE_INTERVAL);
            createTime.setTime(createTime.getTime() - CalendarUtil.ONE_DAY);
            Date end = CalendarUtil.getEndCalendarByStartAndInterval(start, CalendarUtil.SCHEDULE_INTERVAL);
            int succ = 0;
            int fail = 0;
            Set<Long> fids = new HashSet<>();
            StringBuilder msg = new StringBuilder("失败 id");
            List<InstScheduleTaskEntity> istes = scheduleTaskManager.cgetISTE(start, end, createTime);
            for (InstScheduleTaskEntity iste : istes) {
                iste.setSendNum(iste.getSendNum() + 1);
                LinkedHashMap response = null;
                try {
                    response = SendHttpsUtil.postMsg4GetMap(iste.getUrl(), iste.getJsonData());
                } catch (Throwable e) {
                    logger.error("callBackScheduleTask http error:", e);
                    fail++;
                    continue;
                }

                if (callBackAnlyze(JSONObject.toJSONString(response), iste)) {
                    succ++;
                    iste.setState(InstScheduleTaskEntity.STATE_SUCC);
                    scheduleTaskManager.updateISTEById(iste);
                } else {
                    fail++;
                    fids.add(iste.getId());
                    scheduleTaskManager.updateISTEById(iste);
                }
            }
            msg.append(fids.toString());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("succ", succ);
            result.put("fail", fail);
            result.put("msg", msg);
            return result;
        } catch (Throwable e) {
            logger.error("callBackScheduleTask error:", e);
            throw e;
        } finally {
            CALLBACK = false;
        }

    }

    private boolean callBackAnlyze(String response, InstScheduleTaskEntity iste) {
        CallBackResponsePojo cbrp = null;
        try {
            cbrp = JSONObject.parseObject(response, CallBackResponsePojo.class);
            if (cbrp != null && cbrp.getResult_code().equals(CallBackResponsePojo.CODE_SUCCESS)) {
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("callBackAnlyze.CallBackResponsePojo error : [%s]", response, e);
            Date nextSendTime =
                    CalendarUtil.getEndCalendarByStartAndInterval(
                            iste.getNextSendTime(),
                            CallBackPolicyRepository.getIntervalBySendPolicyAndSendNum(iste.getSendPolicy(),
                                    iste.getSendNum()));
            iste.setNextSendTime(nextSendTime);
            return false;
        }
    }

    @Override
    public BuckleScheduleTaskAnalysisPojo buckleScheduleTask() {
        long startTime = System.currentTimeMillis();
        // 从划扣表中获取要划扣的
        List<InstSettlementResultEntity> isres = scheduleTaskManager.getAllISRE(
                CalendarUtil.getDayStartTime(Calendar.getInstance()),
                CalendarUtil.getDayEndTime(Calendar.getInstance()));

        if (isres == null || isres.isEmpty()) {
            return null;
        }
        try {
            useThread(startTime, isres);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("buckleScheduleTask error:", e);
        }
        return null;
    }

    /**
     * 使用线程池实现任务的执行
     * 
     * @param startTime
     * @param isres
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void useThread(long startTime, List<InstSettlementResultEntity> isres) throws InterruptedException,
            ExecutionException {
        List<List<InstSettlementResultEntity>> all = MathUtil.subListByFixedLength(isres, 100);
        ExecutorService es = Executors.newCachedThreadPool();
        CompletionService<Integer> cs = new ExecutorCompletionService<>(es);
        /**
         * List<List<InstSettlementResultEntity>> all,此处相当于二维数组； 如果List<InstSettlementResultEntity>
         * isres的长度为1000，将分为10个批次，每个线程处理100条记录
         */
        for (int i = 0; i < all.size(); i++) {
            cs.submit(new BuckleThread(startTime, all.get(i)));
        }
        es.shutdown();
        for (int i = 0; i < all.size(); i++) {
            if (i == all.size() - 1) {
                logger.info("useThread finished savely.");
            }
            cs.take();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void orderSpecialTimeScheduleTask() {
        Date caculateSpecialTime = CalendarUtil.getStartOfXDaysBefore(Calendar.getInstance(), 30);
        List<SpecialTimeOrderPojo> data = scheduleTaskManager.getSpecialTime30OrdersByCST(caculateSpecialTime);
        if (data == null || data.isEmpty()) {
            logger.info("orderSpecialTimeScheduleTask stop is empty.");
            return;
        }
        String uri = paramConfig.getEndProcessUrl();
        for (SpecialTimeOrderPojo stop : data) {
            if (stop.checkPojo()) {
                EndProcessPojo epp = EndProcessPojo.init();
                epp.setProcBizId(stop.getProcBizId());
                epp.setProcInstId(stop.getProcInstId());
                epp.setProcOrgCode(stop.getProcOrgCode());
                Map responseMessage = null;
                try {
                    responseMessage = SendHttpsUtil.postMsg4GetMap(uri, epp.createRequestMap());
                    scheduleTaskManager.updateSpecialIOIEByOid(stop.getProcBizId());
                } catch (Exception e) {
                    logger.error("orderSpecialTimeScheduleTask chat error:", e);
                } finally {
                    logger.info(JSONObject.toJSONString(responseMessage));
                }
            }
        }
    }

    class BuckleThread implements Callable<Integer> {
        private List<InstSettlementResultEntity> isres;
        private long startTime;
        private static final int TASK_EXIT = 1;

        public BuckleThread(long startTime, List<InstSettlementResultEntity> isres) {
            this.startTime = startTime;
            this.isres = isres;
        }

        @SuppressWarnings("finally")
        @Override
        public Integer call() throws Exception {
            try {
                String BUSINESS_CHANNAL = buckleConfig.getIqbBuckleChannal();
                String BUCKLE_PRIVATE_KEY = buckleConfig.getIqbBucklePrivateKey();
                String CERT_PATH = buckleConfig.getCertPath();
                String APP_SECRET = buckleConfig.getAppSecret();
                BuckleSendPolicy bsp = BuckleSendPolicy.initPolicy();
                List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> dataSend =
                        new LinkedList<>();
                for (InstSettlementResultEntity instSettlementResultEntity : isres) {
                    // 此处实时调用账户系统,查询每期账单是否已还款，如已还款则不需要发往结算中心
                    boolean repaymentStatus =
                            validateRepaymentStatus(instSettlementResultEntity.getOrderId(),
                                    instSettlementResultEntity.getRepayNo());
                    if (!repaymentStatus) {
                        SettlementCenterBuckleRequestMessage scb = null;
                        try {
                            scb = scheduleTaskManager.createBuckleBaseMsg(instSettlementResultEntity.getOrderId());
                        } catch (Exception e) {
                            logger.error(
                                    "buckleScheduleTask error inst_settlementresult id ["
                                            + instSettlementResultEntity.getId() + "]", e);
                            instSettlementResultEntity.setDescribe(e.getMessage());
                            instSettlementResultEntity.setStatus(InstSettlementResultEntity.NOT_SEND);
                            scheduleTaskManager.updateISREById(instSettlementResultEntity);
                            continue;
                        }

                        if (scb == null) {
                            instSettlementResultEntity.setDescribe("调用结算中心前的相关数据查找不全。");
                            instSettlementResultEntity.setStatus(InstSettlementResultEntity.NOT_SEND);
                            scheduleTaskManager.updateISREById(instSettlementResultEntity);
                            continue;
                        }
                        scb.setBusinessChannel(BUSINESS_CHANNAL);
                        scb.setOrderAmount(instSettlementResultEntity.getCurRepayAmt().toString());
                        scb.setNotifyUrl(buckleConfig.getCallBackUrl());
                        scb.setTradeType(SettlementCenterBuckleRequestMessage.TYPE_REFUND);
                        scb.setTradeNo(instSettlementResultEntity.generateTradeNo());
                        instSettlementResultEntity.setTradeNo(scb.getTradeNo());
                        Object checkInfo = scb.check();
                        if (checkInfo instanceof String) {
                            instSettlementResultEntity.setDescribe("结算中心代扣接口请求模型校验失败 【请求前】：" + checkInfo);
                            instSettlementResultEntity.setStatus(InstSettlementResultEntity.NOT_SEND);
                            scheduleTaskManager.updateISREById(instSettlementResultEntity);
                            continue;
                        }
                        // 将划扣信息封装,加密
                        Map<String, String> data =
                                ApplicationSecurityClient.encryption(BUSINESS_CHANNAL, APP_SECRET, CERT_PATH,
                                        BUCKLE_PRIVATE_KEY, scb.convertMap());
                        // 将待发送的数据存入list中
                        if (data != null) {
                            KeyValuePair<Map<String, String>, InstSettlementResultEntity> kvp =
                                    new KeyValuePair<>();
                            kvp.setKey(data);
                            kvp.setValue(instSettlementResultEntity);
                            dataSend.add(kvp);
                        }
                    }
                }

                bsp.chatDataPrepare(dataSend);

                for (int i = 1; i <= bsp.getMaxSendSize(); i++) {
                    logger.info("buckleScheduleTask round " + i + " start.");
                    List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> chatData = bsp.getChatData();
                    if (chatData == null || chatData.size() == 0) {
                        return null;
                    }
                    while (true) {
                        if (okToSend(startTime, i - 1)) { // 计数从0开始， 而for循环从1开始 所以 -1
                            List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> nextRoundData =
                                    new LinkedList<>();
                            for (KeyValuePair<Map<String, String>, InstSettlementResultEntity> kvp : chatData) {
                                String chatResponseMessage = chatWithBuckleCenter(kvp.getKey());
                                if (!StringUtil.isEmpty(chatResponseMessage)) {
                                    logger.info(chatResponseMessage);
                                    if (i < bsp.getMaxSendSize()) {
                                        // 放入下一轮次请求数据集 nextRoundData 里
                                        nextRoundData.add(kvp);
                                    } else {
                                        InstSettlementResultEntity isre = new InstSettlementResultEntity();
                                        isre.setId(kvp.getValue().getId());
                                        isre.setDescribe("结算中心返回消息【请求后】：" + chatResponseMessage);
                                        isre.setStatus(InstSettlementResultEntity.NOT_SEND);
                                        isre.setNumber(bsp.getMaxSendSize());
                                        scheduleTaskManager.updateISREById(isre);
                                    }
                                } else {
                                    // 成功
                                    InstSettlementResultEntity isre = kvp.getValue();
                                    isre.setStatus(InstSettlementResultEntity.SEND);
                                    isre.setNumber(i);
                                    scheduleTaskManager.updateISREById(isre);
                                }
                            }
                            bsp.chatDataPrepare(nextRoundData);
                            break;
                        }
                    }
                }
                return TASK_EXIT;
            } catch (Exception e) {
                logger.error("BuckleThread error ", e);
            } finally {
                return TASK_EXIT;
            }
        }

        /**
         * 
         * Description: 单笔请求 结算中心
         * 
         * @param
         * @return String
         * @throws
         * @Author adam Create Date: 2017年6月21日 下午2:15:22
         */
        private String chatWithBuckleCenter(Map<String, String> data) {
            logger.info("---发送代扣数据到结算中心---{}", JSONObject.toJSON(data));
            try {
                String response = Chat.post(buckleConfig.getIqbBuckleUrl(), data);
                logger.info("---结算中心返回信息---{}", response);
                JSONObject responseMessage = JSONObject.parseObject(response);
                if ("000000".equals(String.valueOf(responseMessage.get("code")))) {
                    return null;
                } else {
                    return String.valueOf(responseMessage.get("msg"));
                }
            } catch (Throwable e) {
                logger.error("chat with buckle center error.", e);
                return "调用结算中心异常:" + e.getMessage();
            }
        }

        private boolean okToSend(long startTime, int num) {
            long interval =
                    CallBackPolicyRepository.getIntervalBySendPolicyAndSendNum(CallBackPolicyRepository.BUCKLE_POLICY,
                            num);
            return (startTime + interval) < System.currentTimeMillis();
        }

    }

    @Override
    public void contractScheduleTask() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 5);
        // 获取待下载合同信息
        String urls = null;
        Integer ecSendtime = (int) calendar.getTime().getTime();
        ContractInfoBean ecInfoBean = new ContractInfoBean();
        ecInfoBean.setEcSendtime(ecSendtime);
        List<Map<String, Object>> lists = contractInfoBiz.selectContractInfoForDownload(ecInfoBean);
        if (!lists.isEmpty()) {
            String orderId = null;
            String tpSignid = null;
            String ecFileName = null;
            int ecDownloadTimes = 0;
            for (Map<String, Object> map : lists) {
                urls = (String) map.get("ecDownloadUrl");
                orderId = (String) map.get("orderId");
                ecDownloadTimes = Integer.valueOf(String.valueOf(map.get("ecDownloadTimes")));
                ecFileName = (String) map.get("ecFileName");
                String[] ecName = ecFileName.split(".");
                tpSignid = (String) map.get("tpSignid");
                if (ecDownloadTimes == 0) {
                    if (urls != null) {
                        try {
                            URL url = new URL(urls);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            // 设置超时间为3秒
                            conn.setConnectTimeout(3 * 1000);
                            // 防止屏蔽程序抓取而返回403错误
                            conn.setRequestProperty("User-Agent",
                                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                            // 获取自己数组
                            File saveDir = new File(config.getCommon_basedir() + "//contract//"
                                    + format.format(new Date()) + "//" + orderId + "//" + ecName[0] + ".pdf");
                            if (!saveDir.getParentFile().exists()) {
                                saveDir.getParentFile().mkdirs();
                                saveDir.createNewFile();
                            }
                            DataInputStream input = new DataInputStream(conn.getInputStream());
                            DataOutputStream output = new DataOutputStream(new FileOutputStream(saveDir));
                            byte[] buffer = new byte[1024 * 8];
                            int count = 0;
                            while ((count = input.read(buffer)) > 0) {
                                output.write(buffer, 0, count);
                            }
                            output.close();
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            contractInfoBiz.updateEcDownloadTimes(tpSignid);
                        }
                        logger.info(" contract files down success tpSignid：" + tpSignid + " files name :" + ecFileName);
                    } else {
                        logger.info(" no contract files need to down tpSignid：" + tpSignid + " files name :"
                                + ecFileName);
                    }
                } else {
                    logger.info(" contract files Already  downloaded tpSignid: " + tpSignid);
                }
            }
        }
    }

    /**
     * Description:校验账单还款状态,已还款返回true,其他返回false
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月30日
     */
    private boolean validateRepaymentStatus(String orderId, Integer repayNo) {
        boolean result = false;
        InstallmentBillPojo billBean = chatToGetRepayStartParams(orderId, repayNo);
        if (null != billBean) {
            return billBean.getStatus() == 3 ? true : false;
        }
        return result;
    }

    /**
     * 
     * Description:罚息减免每日作废任务
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    @SuppressWarnings("rawtypes")
    public void instPenaltyDerateScheduleTask() {
        List<SpecialTimeOrderPojo> data = instPenaltyDerateBiz.getCurrentInstPenaltyDerateInfo(formatDate());
        if (data == null || data.isEmpty()) {
            logger.info("---罚息减免每日作废任务--- is empty.");
            return;
        }
        String uri = paramConfig.getEndProcessUrl();
        for (SpecialTimeOrderPojo stop : data) {
            if (stop.checkPojo()) {
                EndProcessPojo epp = EndProcessPojo.init();
                epp.setProcBizId(stop.getProcBizId());
                epp.setProcInstId(stop.getProcInstId());
                epp.setProcOrgCode(stop.getProcOrgCode());
                Map responseMessage = null;
                try {
                    responseMessage = SendHttpsUtil.postMsg4GetMap(uri, epp.createRequestMap());
                    instPenaltyDerateBiz.updateInstPenaltyDerateByOid(stop.getProcInstId());
                } catch (Exception e) {
                    logger.error("---罚息减免每日作废任务---结束流程报错---:", e);
                } finally {
                    logger.info(JSONObject.toJSONString(responseMessage));
                }
            }
        }
    }

    private static String formatDate() {
        final String NROMAL_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NROMAL_FORMAT);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 提前结清每日作废任务 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    @SuppressWarnings("rawtypes")
    public void prepaymentEndScheduleTask() {
        JSONObject objs = new JSONObject();
        List<SettleApplyBean> data = settleApplyBeanBiz.selectPrepaymentList(objs);
        if (data == null || data.isEmpty()) {
            logger.info("---提前结清每日作废任务--- is empty.");
            return;
        }
        String uri = paramConfig.getEndProcessUrl();
        for (SettleApplyBean applyBean : data) {
            // 根据订单号 期数校验账单状态;如果逾期,则可以终止流程,如果已还款则不终止流程
            if (validateBillInfo(applyBean.getOrderId(), applyBean.getCurItems())) {
                EndProcessPojo epp = EndProcessPojo.init();
                epp.setProcBizId(applyBean.getOrderId() + "-" + applyBean.getId());
                epp.setProcInstId(applyBean.getProcInstId());
                epp.setProcOrgCode(applyBean.getOrderId() + "-" + applyBean.getId());
                Map responseMessage = null;
                try {
                    responseMessage = SendHttpsUtil.postMsg4GetMap(uri, epp.createRequestMap());
                    logger.info("-----终止提前结清流程返回---{}", JSONObject.toJSONString(responseMessage));
                    String retCode = (String) responseMessage.get("retCode");
                    if (retCode.equals("00000000")) {
                        SettleApplyBean bean = new SettleApplyBean();
                        bean.setId(applyBean.getId());
                        bean.setSettleStatus(4);
                        settleApplyBeanBiz.updateSettleBean(bean);
                    }
                } catch (Exception e) {
                    logger.error("---提前结清每日作废任务---结束流程报错---:", e);
                } finally {
                    logger.info(JSONObject.toJSONString(responseMessage));
                }
            }
        }
    }

    /**
     * 
     * Description:根据订单号查询账务数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    private InstallmentBillPojo chatToGetRepayStartParams(String orderId, int repayNo) {
        logger.debug("---调用账务系统接口getInstallmentBillInfoByOrderId---参数--{}", orderId);
        String response = "";
        InstallmentBillPojo crp = null;
        try {
            Map<String, Object> request = new HashMap<String, Object>();
            request.put("orderId", orderId);
            request.put("repayNo", repayNo);
            response = SimpleHttpUtils.httpPost(
                    consumerConfig.getInstallmentBillInfoByOrderId(),
                    SignUtil.chatEncode(JSONObject.toJSONString(request), consumerConfig.getCommonPrivateKey()));
            logger.debug("---调用账务系统接口getInstallmentBillInfoByOrderId---返回--{}" + JSONObject.toJSONString(response));

        } catch (Exception e) {
            logger.error("---调用账务系统接口getInstallmentBillInfoByOrderId---报错{}:", e);
        }
        if (!StringUtil.isNull(response)) {
            try {
                JSONObject objs = JSON.parseObject(response);
                String result = objs.getString("result");
                String retCode = objs.getString("retCode");
                if (retCode.equals("success")) {
                    crp = JSONObject.parseObject(result, InstallmentBillPojo.class);
                }
            } catch (Exception e) {
                logger.error("---调用账务系统接口getInstallmentBillInfoByOrderId---解析返回结果报错{}  :", e);
            }
        }
        return crp;
    }

    /**
     * 
     * Description:校验总罚息是否一致
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    private boolean validateBillInfo(String orderId, int repayNo) {
        boolean result = false;
        InstallmentBillPojo repayBean = chatToGetRepayStartParams(orderId, repayNo);
        if (repayBean != null) {
            if (repayBean.getStatus() == 0) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 获取距当前系统时间三天后的账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月27日
     */
    @SuppressWarnings("unchecked")
    @Override
    public long batchSelectLatelyThreeDaysBill() {
        JSONObject objs = new JSONObject();
        List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList(objs);
        long result = 0;
        // 获取所有待还款账单
        int pageNum = 1;
        int numPerPage = 300;

        objs.put("numPerPage", numPerPage);
        objs.put("pageNum", pageNum);
        Map<String, Object> retMap = selectLatelyThreeDaysBill(objs);
        if (retMap != null) {
            Map<String, Object> res = (Map<String, Object>) retMap.get("result");
            if (retMap.get("retCode").equals("success")) {
                while (pageNum <= Integer.parseInt(String.valueOf(res.get("endPageIndex")))) {
                    List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
                    List<InstRemindPhoneBean> list = converList2ListBean(recordList);

                    logger.debug("---账单数据--{}", JSONObject.toJSONString(list));
                    pageNum++;
                    if (!CollectionUtils.isEmpty(list)) {

                        if (!CollectionUtils.isEmpty(instRemindPhoneList)) {
                            for (InstRemindPhoneBean bean : list) {
                                if (bean != null) {
                                    JSONObject json = new JSONObject();
                                    json.put("orderId", bean.getOrderId());
                                    json.put("curItems", bean.getCurItems());
                                    InstRemindPhoneBean instRemindPhoneBean =
                                            instRemindPhoneBiz.selectInstRemindPhone(json);
                                    // 根据订单号查询贷后车辆信息表，如果存在,则电话提醒信息表的状态为已处理
                                    InstRemindPhoneBean remindBean =
                                            instRemindPhoneBiz.selectInstManageCarInfo(bean.getOrderId());

                                    if (instRemindPhoneBean != null) {
                                        json.put("curRepayAmt", bean.getCurRepayAmt());
                                        json.put("perOverdueAmt", bean.getPerOverdueAmt());
                                        json.put("billInfoStatus", bean.getBillInfoStatus());
                                        json.put("flag", 1);
                                        if (remindBean != null) {
                                            json.put("status", "3");
                                        }

                                        result += instRemindPhoneBiz.updateInstRemindPhoneResult(json);
                                    } else {
                                        List<InstRemindPhoneBean> instRemindList = new ArrayList<>();
                                        bean.setStatus(remindBean == null ? "1" : "3");
                                        instRemindList.add(bean);
                                        result += instRemindPhoneBiz.insertInstRemindPhoneResult(instRemindList);
                                    }
                                }
                            }
                        } else {
                            result += instRemindPhoneBiz.insertInstRemindPhoneResult(list);
                        }

                        objs.put("numPerPage", numPerPage);
                        objs.put("pageNum", pageNum);
                        retMap = selectLatelyThreeDaysBill(objs);
                        res = (Map<String, Object>) retMap.get("result");
                    }
                }
            }
        } else {
            logger.error("---调用账务系统接口--获取每日还款账单出现异常--");
        }
        // 调用账务系统接口，完成账单状态的同步更新
        doProcessInstRemindInfo(instRemindPhoneList);
        return result;
    }

    /**
     * Description:将账单状态为未还款或部分还款的的电话提醒信息发送账务系统，完成账单的同步更新
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    @SuppressWarnings("unchecked")
    private void doProcessInstRemindInfo(List<InstRemindPhoneBean> instRemindPhoneList) {
        if (!CollectionUtils.isEmpty(instRemindPhoneList)) {
            JSONObject objs = null;
            for (InstRemindPhoneBean bean : instRemindPhoneList) {
                objs = new JSONObject();
                objs.put("orderId", bean.getOrderId());
                objs.put("repayNo", bean.getCurItems());
                Map<String, Object> retMap = getInstallmentBillInfoByOrderId(objs);
                if (retMap != null) {
                    if (retMap.get("retCode").equals("success")) {
                        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
                        if ((int) res.get(STATUS_NAME) == 3) {
                            JSONObject json = new JSONObject();
                            json.put("orderId", bean.getOrderId());
                            json.put("curItems", bean.getCurItems());
                            json.put("flag", 1);
                            json.put("billInfoStatus", (int) res.get(STATUS_NAME));
                            instRemindPhoneBiz.updateInstRemindPhoneResult(json);
                        } else if ((int) res.get(STATUS_NAME) == 0) {
                            JSONObject json = new JSONObject();
                            json.put("orderId", bean.getOrderId());
                            json.put("curItems", bean.getCurItems());
                            json.put("flag", 1);
                            json.put("billInfoStatus", (int) res.get(STATUS_NAME));
                            instRemindPhoneBiz.updateInstRemindPhoneResult(json);
                        } else if ((int) res.get(STATUS_NAME) == 4) {
                            JSONObject json = new JSONObject();
                            json.put("orderId", bean.getOrderId());
                            json.put("curItems", bean.getCurItems());
                            json.put("flag", 1);
                            json.put("status", 3);
                            json.put("billInfoStatus", (int) res.get(STATUS_NAME));
                            instRemindPhoneBiz.updateInstRemindPhoneResult(json);
                        }
                    }
                }
            }
        }
    }

    // 发送账户系统查询所有待还款的账单
    private Map<String, Object> selectLatelyThreeDaysBill(JSONObject objs) {
        logger.debug("------调用账户系统获取所有待还款账单接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getGetLatelyThreeDaysBill(),
                encryptUtils.encrypt(objs));
        logger.debug("---调用账户系统获取所有待还款账单接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 将从账务系统获取的数据转化为电话提醒信息
     * 
     * @author haojinlong
     * @param recordList 从账务系统获取的符合条件的集合
     * @return 2018年4月28日
     */
    private List<InstRemindPhoneBean> converList2ListBean(List<Map<String, Object>> recordList) {
        List<InstRemindPhoneBean> list = new ArrayList<>();
        if (recordList != null) {
            for (Map<String, Object> objs : recordList) {
                String orderId = String.valueOf(objs.get("orderId"));
                InstRemindPhoneBean instRemindPhoneBean = getPreRemindPhoneBean(objs, orderId);
                list.add(instRemindPhoneBean);
            }
        }
        return list;
    }

    private InstRemindPhoneBean getPreRemindPhoneBean(Map<String, Object> objs, String orderId) {
        // 根据订单号获取订单相关信息
        OrderBean orderBean = orderBiz.getOrderAllInfoByOrderId(orderId);
        InstRemindPhoneBean instRemindPhoneBean = null;
        if (orderBean != null) {
            BigDecimal curRepayAmt = (BigDecimal) objs.get("curRepayAmt");
            BigDecimal perOverdueAmt = (BigDecimal) objs.get("monthOverdueAmt");
            instRemindPhoneBean = new InstRemindPhoneBean();
            instRemindPhoneBean.setOrderId(orderId);
            instRemindPhoneBean.setRealName(orderBean.getRealName());
            instRemindPhoneBean.setRegId(orderBean.getRegId());
            instRemindPhoneBean.setMerchantNo(orderBean.getMerchantNo());
            instRemindPhoneBean.setOrderItems(Integer.parseInt(orderBean.getOrderItems()));
            instRemindPhoneBean.setCurItems(Integer.parseInt(String.valueOf(objs.get("repayNo"))));
            instRemindPhoneBean.setFlag(1);
            instRemindPhoneBean.setVersion(1);
            instRemindPhoneBean.setStatus("1");
            long lastRepayDate = (long) objs.get("lastRepayDate");
            instRemindPhoneBean.setLastRepayDate(DateUtil.toString(DateUtil.getDate(lastRepayDate, 2).getTime(), 23));
            instRemindPhoneBean.setCurRepayAmt(curRepayAmt != null ? curRepayAmt : BigDecimal.ZERO);
            instRemindPhoneBean.setPerOverdueAmt(perOverdueAmt != null ? perOverdueAmt : BigDecimal.ZERO);
            instRemindPhoneBean.setBillInfoStatus((Integer) objs.get("status"));
        }
        return instRemindPhoneBean;
    }

    /**
     * 
     * Description:根据订单号 期数查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    private Map<String, Object> getInstallmentBillInfoByOrderId(JSONObject objs) {
        logger.info("---调用账户系统--根据订单号 期数查询账单信息接口开始---{}---", objs);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getInstallmentBillInfoByOrderId(),
                encryptUtils.encrypt(objs));
        logger.info("---调用账户系统--根据订单号 期数查询账单信息接口返回结果--{}", JSONObject.toJSON(resultStr));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        return retMap;
    }

    /**
     * 资产存量数据每日入库
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月1日
     */
    @Override
    public long everyDayAssetStock(JSONObject objs) {
        JSONObject params = new JSONObject();
        long result = 0;
        int pageNum = 1;
        int numPerPage = 500;

        params.put("pageSize", numPerPage);
        params.put("pageNum", pageNum);
        params.put("curRepayDate", objs.getString("curRepayDate"));

        String retValue = getAssetStockList(params);
        if (!StringUtil.isNull(retValue)) {
            JSONObject json = JSONObject.parseObject(retValue);
            while (pageNum <= json.getIntValue("endPageIndex")) {
                if (!CollectionUtils.isEmpty(json.getJSONArray("recordList"))) {
                    InstAssetStockPojo instAssetStockPojo = JSONObject.toJavaObject(json, InstAssetStockPojo.class);
                    pageNum++;
                    if (!CollectionUtils.isEmpty(instAssetStockPojo.getRecordList())) {
                        result += instAssetStockBiz.batchInsertInstAssetStock(instAssetStockPojo.getRecordList());
                        params.put("pageSize", numPerPage);
                        params.put("pageNum", pageNum);
                        retValue = getAssetStockList(params);
                        json = JSONObject.parseObject(retValue);
                    }
                }
            }
        } else {
            logger.error("---调用账务系统接口--获取每日还款账单出现异常--");
        }
        return result;
    }

    /**
     * 
     * Description:调用中阁获取每日存量数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月1日
     */
    @SuppressWarnings({"static-access"})
    private String getAssetStockList(JSONObject objs) {
        String retValue = null;
        String resultStr =
                HttpsClientUtil.getInstance().doPost(pledgeUrlConfig.getEveryAssetstockUrl(), objs.toString(), "UTF-8");

        JSONObject json = JSONObject.parseObject(resultStr);
        String retCode = json.getString("retCode");
        if (retCode.equals("00000000")) {
            String iqbResult = json.getString("iqbResult");
            JSONObject resultJson = JSONObject.parseObject(iqbResult);
            retValue = resultJson.getString("result");
        }
        return retValue;
    }

    /**
     * 
     * Description:将已结清并且已发送车300进行车辆监控的车辆取消监控
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    @SuppressWarnings({"static-access"})
    @Override
    public long stopMonitorOrderLoanrisk(JSONObject objs) {
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(cheThreeHBiz.selectCheThreeHWaitStopList(50));// 每次最多处理50笔订单
        if (pageInfo == null || pageInfo.getList() == null || pageInfo.getList().isEmpty())
            return 0;
        List<Map<String, Object>> waitStopMonitorList = pageInfo.getList();
        Map<String, List<String>> assetsIdOrderIdMap = new HashMap<String, List<String>>();
        for (Map<String, Object> tmpMap : waitStopMonitorList) {
            String assetsId = ObjectUtils.toString(tmpMap.get("assetsId"), "NULL").trim();
            if (assetsId.equals(""))
                assetsId = "NULL";
            if (tmpMap.get("orderId") == null || tmpMap.get("orderId").toString().trim().equals(""))
                continue;

            List<String> tmpOrderIdList = null;
            if (assetsIdOrderIdMap.get(assetsId) == null)
                tmpOrderIdList = new ArrayList<String>();
            else
                tmpOrderIdList = assetsIdOrderIdMap.get(assetsId);
            tmpOrderIdList.add(tmpMap.get("orderId").toString().trim());
            assetsIdOrderIdMap.put(assetsId, tmpOrderIdList);
        }

        if (assetsIdOrderIdMap.containsKey("NULL")) {
            List<String> nullCarNoList = assetsIdOrderIdMap.get("NULL");
            if (nullCarNoList != null && !nullCarNoList.isEmpty()) {
                String infoMsg = "assetsId is null --> orderIds:[";
                for (String tmpOrderId : nullCarNoList)
                    infoMsg += tmpOrderId + ",";
                infoMsg += "]";
                logger.info(infoMsg);
            }
            assetsIdOrderIdMap.remove("NULL");
        }

        if (assetsIdOrderIdMap.isEmpty())
            return 0;

        long reLong = 0;
        for (String assetsId_key : assetsIdOrderIdMap.keySet()) {
            List<String> tmpOrderIdList = assetsIdOrderIdMap.get(assetsId_key);
            if (tmpOrderIdList == null || tmpOrderIdList.isEmpty())
                continue;

            JSONObject sendReqJson = new JSONObject();
            sendReqJson.put("assets_id", assetsId_key);
            String resultStr =
                    HttpsClientUtil.getInstance().doPost(pledgeUrlConfig.getOrderChethreeStopMonitorUrl(),
                            sendReqJson.toJSONString(), "UTF-8");
            boolean isStopOK = false;
            String errorMsg = null;
            JSONObject json = JSONObject.parseObject(resultStr);
            String retCode = json.getString("retCode");
            if (retCode.equals("00000000")) {
                String iqbResult = json.getString("iqbResult");
                JSONObject resultJson = JSONObject.parseObject(iqbResult);
                String retValue = resultJson.getString("result");
                if (retValue == null || retValue.trim().equals("")) {
                    errorMsg = "stopResult is null";
                } else {
                    JSONObject stopResult = JSONObject.parseObject(retValue);
                    if (stopResult == null || stopResult.isEmpty())
                        errorMsg = "stopResult is null";
                    else if (stopResult.getString("status") != null
                            && stopResult.getString("status").trim().equals("1"))
                        isStopOK = true;
                    else {
                        if (stopResult.getString("message") == null)
                            errorMsg = "stopResult`message is null";
                        else
                            errorMsg = stopResult.getString("message");
                    }
                }
            } else
                errorMsg = "front have Exception";

            String logMsg = "assetsId:[" + assetsId_key + "]";
            logMsg += " --> orderIds:[";
            for (String tmpOrderId : tmpOrderIdList) {
                if (isStopOK)
                    reLong += cheThreeHBiz.stopMonitorOrderLoanrisk(tmpOrderId, Integer.valueOf(assetsId_key));
                logMsg += tmpOrderId + ",";
            }
            logMsg += "]";

            if (isStopOK) {
                logMsg += " stopMonitor is sended";
                logger.info(logMsg);
            } else {
                logMsg += " stopMonitor is failed errorMsg[" + errorMsg + "]";
                logger.info(logMsg);
                logger.error(logMsg);
            }
        }

        return reLong;
    }
}
