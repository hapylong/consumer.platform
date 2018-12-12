package com.iqb.consumer.service.layer.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.CommonUtil;
import com.iqb.consumer.common.utils.encript.RSAUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.admin.entity.CASOrgCodeRecordEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.InstOperateEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbSysOrganizationInfoEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.NewRoverdueEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.RoverdueEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.FinanceGenerallyPageResponsePojo;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.admin.pojo.SpecialTableColumn;
import com.iqb.consumer.data.layer.bean.admin.pojo.TableColumnPair;
import com.iqb.consumer.data.layer.bean.admin.request.AuthoritySeekRequestMessage;
import com.iqb.consumer.data.layer.bean.admin.request.AuthoritySeekResponseMessage;
import com.iqb.consumer.data.layer.bean.admin.request.ChangePlanRequestMessage;
import com.iqb.consumer.data.layer.bean.contract.InstOrderContractEntity;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.merchant.MerchantKeypair;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.admin.AdminManager;
import com.iqb.consumer.data.layer.biz.carstatus.CarStatusManager;
import com.iqb.consumer.data.layer.biz.merchant.MerchantKeypairBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.page.PageMsg;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.SysUserSession;

import jodd.util.StringUtil;

@Service
public class AdminServiceImpl extends BaseService implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private static final Set<TableColumnPair> TABLE_COLUMN_PAIR = new HashSet<>();
    static {
        /** 机构调整 - 工作流组织 **/
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_wf_proc", "proc_orgcode"));
        /** 机构调整 - 客户信息 **/
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_common_push_record", "common_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_base_info", "customer_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_base_info", "org_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_deposit_rate", "customer_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_push_record", "customer_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_store_info", "customer_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_store_info", "guarantee_corporation_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_customer_xfjr_info", "customer_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_enterprise_customer_info", "customer_code"));
        /** 机构调整 - 电子合同 **/
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_ec_biz_config", "org_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_ec_info", "org_code"));
        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_ec_info_signer", "org_code"));

        TABLE_COLUMN_PAIR.add(new TableColumnPair("iqb_ec_signer", "ec_signer_code"));
        /** 业务系统 inst_merchantinfo **/
        TABLE_COLUMN_PAIR.add(new TableColumnPair("inst_merchantinfo", "id"));
    }

    private static final Set<SpecialTableColumn> SPECIAL_TABLE_COLUMN_PAIR = new HashSet<>();
    static {
        SPECIAL_TABLE_COLUMN_PAIR.add(new SpecialTableColumn("act_hi_varinst", "TEXT_", "NAME_", "procOrgCode"));
        SPECIAL_TABLE_COLUMN_PAIR.add(new SpecialTableColumn("act_ru_variable", "TEXT_", "NAME_", "procOrgCode"));
    }

    private static final String RECORD_PARTTERN = "表名【%s】, 更新条数【%d】 \r";

    private static final String CODE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final ReentrantLock lock = new ReentrantLock();

    private static final String IP_PREFIX =
            "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    private static final String ACCOUNT_EXIT = "00";
    private static final String ACCOUNT_NOT_EXIT = "01";

    private static Pattern pattern;

    @Autowired
    private AdminManager adminManager;
    @Autowired
    private SysUserSession sysUserSession;
    @Autowired
    private MerchantKeypairBiz merchantKeypairBiz;

    @Autowired
    private DictService dictServiceImpl;

    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private CarStatusManager carStatusManager;
    @Autowired
    private OrderBiz orderBiz;

    private Pattern getPatternInstance() {
        return pattern == null ? Pattern.compile(IP_PREFIX) : pattern;
    }

    private boolean checkIp(String ip) {
        Pattern p = getPatternInstance();
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    @Override
    public InstOrderContractEntity getIOCEByOid(String orderId) {
        return adminManager.getIOCEByOid(orderId);
    }

    @Override
    public String saveOrUpdateIOCE(InstOrderContractEntity ioce) throws GenerallyException {
        return adminManager.saveOrUpdateIOCE(ioce);
    }

    @Override
    public String generateKey(JSONObject requestMessage) throws GenerallyException {
        try {
            int i = 0;
            int e = 0;
            StringBuilder em = new StringBuilder();
            getMerchLimitObject(requestMessage);
            @SuppressWarnings("unchecked")
            List<MerchantBean> merList = (List<MerchantBean>) requestMessage.get("merList");
            if (merList.size() > 10) {
                return "一次性绑定密钥超过上限.";
            }
            for (MerchantBean ml : merList) {
                MerchantKeypair mk = merchantKeypairBiz.queryKeyPair(ml.getMerchantNo());
                if (mk != null) {
                    e++;
                    em.append(ml.getMerchantNo()).append(";");
                    continue;
                }
                Map<String, Object> keyMap = RSAUtils.genKeyPair();
                MerchantKeypair mkp = new MerchantKeypair();
                mkp.setMerchantNo(ml.getMerchantNo());
                mkp.setPrivateKey(RSAUtils.getPrivateKey(keyMap));
                mkp.setPublicKey(RSAUtils.getPublicKey(keyMap));
                merchantKeypairBiz.addKeyPair(mkp);
                i++;
            }
            return "密钥生成请求【" + merList.size() + "】 成功 【" + i + "】失败 【" + e + "】 失败商户 【" + em.toString() + "】";
        } catch (Throwable e) {
            logger.error("AdminServiceImpl.generateKey error:", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }
    }

    @Override
    public String updateKey(JSONObject requestMessage) throws GenerallyException {
        try {
            int i = 0;
            int e = 0;
            StringBuilder em = new StringBuilder();
            getMerchLimitObject(requestMessage);
            @SuppressWarnings("unchecked")
            List<MerchantBean> merList = (List<MerchantBean>) requestMessage.get("merList");
            if (merList.size() > 10) {
                return "一次性更新密钥超过上限.";
            }
            for (MerchantBean ml : merList) {
                MerchantKeypair mk = merchantKeypairBiz.queryKeyPair(ml.getMerchantNo());
                if (mk == null) {
                    e++;
                    em.append(ml.getMerchantNo()).append(";");
                    continue;
                }
                Map<String, Object> keyMap = RSAUtils.genKeyPair();
                MerchantKeypair mkp = new MerchantKeypair();
                mkp.setMerchantNo(ml.getMerchantNo());
                mkp.setPrivateKey(RSAUtils.getPrivateKey(keyMap));
                mkp.setPublicKey(RSAUtils.getPublicKey(keyMap));
                mkp.setVersion(mk.getVersion() == null ? 1 : mk.getVersion() + 1);
                merchantKeypairBiz.updateKeyPair(mkp);
                i++;
            }
            return "密钥更新请求【" + merList.size() + "】 成功 【" + i + "】失败 【" + e + "】 失败商户 【" + em.toString() + "】";
        } catch (Throwable e) {
            logger.error("AdminServiceImpl.updateKey error:", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }
    }

    @Override
    public void updateIpsById(JSONObject requestMessage) throws GenerallyException {
        String ips1 = requestMessage.getString("whiteList");
        if (StringUtil.isEmpty(ips1)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        String[] ips2 = ips1.split(",");
        for (String ip : ips2) {
            if (!checkIp(ip)) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
            }
        }
        merchantKeypairBiz.updateIpsById(requestMessage);
    }

    @Override
    public Object getKeyPairsByCondition(JSONObject requestMessage) throws GenerallyException {
        getMerchLimitObject(requestMessage);
        return merchantKeypairBiz.getKeyPairList(requestMessage);
    }

    @Override
    public String changePlan(JSONObject requestMessage) throws GenerallyException {
        ChangePlanRequestMessage cprm = null;
        try {
            cprm = JSONObject.toJavaObject(requestMessage, ChangePlanRequestMessage.class);
        } catch (Throwable e) {
            logger.error("AdminServiceImpl.changePlan error:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (cprm == null) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        cprm.setMerchantId(sysUserSession.getOrgCode());
        cprm.setHandler(sysUserSession.getRealName());
        // 保存
        InstOperateEntity ioe = new InstOperateEntity();
        ioe.createEntity(cprm);
        adminManager.persistIOE(ioe);
        if (!cprm.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        // 同步更新订单表中的分期时间
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(cprm.getOrderId());
        orderBean.setStageDate(cprm.getBeginDate());
        orderBiz.updateOrderInfo(orderBean);

        String openId = dictServiceImpl.getOpenIdByOrderId(cprm.getOrderId());
        return adminManager.chatToChangePlan(cprm, openId);
    }

    @Override
    public void getMerchantNos(JSONObject requestMessage) {
        getMerchLimitObject(requestMessage);
    }

    @Override
    public void merchantNos(JSONObject requestMessage) {
        getMerchantNosNew(requestMessage);
    }

    @Override
    public boolean isAccountExit(String openId, String regId) throws GenerallyException {
        Map<String, Object> requestMessage = new HashMap<>();
        if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(regId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        requestMessage.put("regId", regId);
        requestMessage.put("openId", openId);

        String responseMessage = null;
        try {
            responseMessage = SimpleHttpUtils.httpPost(
                    consumerConfig.getFinanceAccountQueryAccUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(requestMessage), urlConfig.getCommonPrivateKey()));
        } catch (Exception e) {
            logger.error("query isAccountExit error", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }

        JSONObject jo = JSONObject.parseObject(responseMessage);
        String code = jo.getString("retCode");

        switch (code) {
            case ACCOUNT_EXIT:
                return true;
            case ACCOUNT_NOT_EXIT:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void applyNewAccount(String regId, String openId, String bankCard, String realName, String idCard)
            throws DevDefineErrorMsgException, GenerallyException {
        Map<String, Object> requestMessage = new HashMap<>();
        requestMessage.put("regId", regId);
        requestMessage.put("openId", openId);
        requestMessage.put("bankCardNo", bankCard);
        requestMessage.put("realName", realName);
        requestMessage.put("idNo", idCard);
        String checkInfo = CommonUtil.checkMap(requestMessage);
        if (!StringUtil.isEmpty(checkInfo)) {
            throw new DevDefineErrorMsgException(String.format("开户失败 %s 为空", checkInfo));
        }
        String responseMessage = null;
        JSONObject jo = null;
        try {
            responseMessage = SimpleHttpUtils.httpPost(
                    consumerConfig.getFinanceAccountOpenAccUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(requestMessage), urlConfig.getCommonPrivateKey()));

            logger.info("query isAccountExit responseStr: {}", responseMessage);
            jo = JSONObject.parseObject(responseMessage);
        } catch (Exception e) {
            logger.error("query isAccountExit error", e);
            throw new DevDefineErrorMsgException("开户失败");
        }
        String code = jo.getString("retCode");
        if (jo == null || !FinanceConstant.SUCCESS.equals(code)) {
            throw new DevDefineErrorMsgException(String.format("开户失败 finance code [%s]", jo.getString("msg")));
        }
    }

    @Override
    public void casOrgCode(JSONObject requestMessage) throws DevDefineErrorMsgException, GenerallyException {
        final ReentrantLock lock = this.lock;
        if (lock.isLocked()) {
            throw new DevDefineErrorMsgException("该接口正在被占用，请稍后重试");
        }

        try {
            lock.lock();

            CASOrgCodeRecordEntity cre = JSONObject.toJavaObject(requestMessage, CASOrgCodeRecordEntity.class);
            if (cre == null || !cre.checkEntity()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }

            IqbSysOrganizationInfoEntity snapshot = null;
            IqbSysOrganizationInfoEntity isoieB = null;
            IqbSysOrganizationInfoEntity isoieParent = null;
            try {
                /** 合法条件：codeA 有且仅有一条 codeB 查询不到 **/
                snapshot = adminManager.getIDIEByOrgCodeAndOrgLevel(cre.getCodeA());
                isoieB = adminManager.getIDIEByOrgCodeAndOrgLevel(cre.getCodeB());
                isoieParent = adminManager
                        .getIDIEByOrgCodeAndOrgLevel(cre.getCodeB().substring(0, cre.getCodeB().length() - 3));
            } catch (Exception e) {
                logger.error("casOrgCode error:", e);
                throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
            }

            if (isoieB != null) {
                throw new DevDefineErrorMsgException("组织机构代码codeB已被占用，请换值后重试");
            }

            if (isoieParent == null) {
                throw new DevDefineErrorMsgException("未找到codeB对应的上级机构，请换值后重试");
            }
            if (snapshot == null) {
                throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
            } else {
                cre.setSnapshot(JSONObject.toJSONString(snapshot));
                cre.setLastCasId(snapshot.getLastCasId());
                cre.setLastParentId(snapshot.getParentId());
                cre.setParentId(isoieParent.getId());
                cre.setOrgLevel(isoieParent.getOrgLevel() + 1);
            }

            /** 全部合法后，开始更新 **/

            casOrgCodeInterval(cre);
        } finally {
            lock.unlock();
        }

    }

    // @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor =
    // {Exception.class})
    private void casOrgCodeInterval(CASOrgCodeRecordEntity cre) throws GenerallyException {
        try {
            logger.info("casOrgCodeInterval start :" + cre.toString());
            StringBuilder sb = new StringBuilder("组织机构【数据更新统计】 开始...\r");
            final String codeA = cre.getCodeA();
            final String codeB = cre.getCodeB();
            for (TableColumnPair tcp : TABLE_COLUMN_PAIR) {
                tcp.setCodeA(codeA);
                tcp.setCodeB(codeB);
                int update = adminManager.casChangeOrgCodeTCP(tcp);
                sb.append(String.format(RECORD_PARTTERN, tcp.getTable(), update));
            }
            for (SpecialTableColumn stc : SPECIAL_TABLE_COLUMN_PAIR) {
                stc.setCodeA(codeA);
                stc.setCodeB(codeB);
                int update = adminManager.casChangeOrgCodeSTC(stc);
                sb.append(String.format(RECORD_PARTTERN, stc.getTable(), update));
            }

            int u1 = adminManager.casUpdateMgLogMsg(codeA, codeB);
            sb.append(String.format(RECORD_PARTTERN, "iqb_ec_mq_log", u1));
            cre.setDescribe(sb.toString());
            adminManager.persistCOCRE(cre);

            int u2 = adminManager.casUpdateISOI(cre);
            sb.append(String.format(RECORD_PARTTERN, "iqb_sys_organization_info", u2));
            sb.append("组织机构【数据更新统计】 结束...\r");
            logger.info(sb.toString());
        } catch (Exception e) {
            logger.error("casOrgCodeInterval error :", e);
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }
    }

    @Override
    public void authorityHandlerChange(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException {

        IqbCustomerPermissionEntity icpe = null;
        try {
            icpe = JSONObject.toJavaObject(requestMessage, IqbCustomerPermissionEntity.class);
            if (icpe == null || !icpe.check(CheckGroup.A)) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("authorityHandlerChange error :", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        if (!adminManager.isMerchantNoExit(icpe.getMerchantNo())) {
            throw new DevDefineErrorMsgException("商户检索异常，请联系管理员。");
        }
        int count = adminManager.saveOrUpdateICPE(icpe);
        if (count != 1) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }

    }

    @Override
    public synchronized AuthoritySeekResponseMessage authorityHandlerSeek(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException {

        AuthoritySeekResponseMessage responseMessage = new AuthoritySeekResponseMessage();
        AuthoritySeekRequestMessage asrm = null;
        try {
            asrm = JSONObject.toJavaObject(requestMessage, AuthoritySeekRequestMessage.class);
            if (asrm == null || !asrm.check(CheckGroup.A)) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Exception e) {
            logger.error("authorityHandlerSeek error :", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }

        if (!adminManager.isMerchantNoExit(asrm.getMerchantNo())) {
            throw new DevDefineErrorMsgException("商户检索异常，请联系管理员。");
        }

        IqbCustomerPermissionEntity icpe = adminManager.getICPEByMerchantNo(asrm.getMerchantNo());
        List<MerchantTreePojo> mtps = adminManager.getMerchantTreeList(asrm.getConcatId());

        if (icpe == null) {
            icpe = new IqbCustomerPermissionEntity();
            icpe.setMerchantNo(asrm.getMerchantNo());
            icpe.setMerchantName(asrm.getMerchantName());
            responseMessage.setSeek(icpe);
            responseMessage.setAll(mtps);
            return responseMessage;
        }

        if (icpe.getAuthorityTreeList() != null && !icpe.getAuthorityTreeList().isEmpty()) {
            if (mtps != null && !mtps.isEmpty()) {
                for (MerchantTreePojo mtp : mtps) {
                    if (icpe.getAuthorityTreeList().contains(mtp)) {
                        mtp.setSelected(true);
                    }
                }
            }
        }

        responseMessage.setSeek(icpe);
        responseMessage.setAll(mtps);
        return responseMessage;

    }

    @Override
    public List<MerchantTreePojo> authorityHandlerTree1() {
        return adminManager.getMerchantTreeList(null);
    }

    @Override
    public IqbCustomerPermissionEntity authorityHandlerQuery(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException {
        if (!adminManager.isMerchantNoExit(requestMessage.getString("merchantNo"))) {
            return null;
        }

        return adminManager.getICPEByMerchantNo(requestMessage.getString("merchantNo"));
    }

    @Override
    public void getMerchantListByOrgCode(JSONObject jo) throws DevDefineErrorMsgException {
        getMerchantList(jo);
    }

    /**
     * 
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object analysisData(JSONObject requestMessage, Boolean isInside, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException {

        Map result = new HashMap();
        try {
            result = SendHttpsUtil.postMsg4GetMap(getAnalysisUrl(isPage),
                    JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(requestMessage),
                            consumerConfig.getCommonPrivateKey())));
            logger.info("analysisInside response:", JSONObject.toJSONString(result));
            if (result != null && "OK".equals(result.get("status").toString())) {
                if (!isPage) {
                    /** 如果不分页 **/
                    return result.get("result");
                } else {
                    /** 如果分页 **/
                    FinanceGenerallyPageResponsePojo<JSONObject> response = JSONObject
                            .parseObject(JSONObject.toJSONString(result), FinanceGenerallyPageResponsePojo.class);
                    if (response.getResult() != null) {
                        List<JSONObject> list = response.getResult().getRecordList();
                        List<RoverdueEntity> res = new ArrayList<>();
                        if (list == null || list.isEmpty()) {
                            list = new ArrayList<>();
                        } else {
                            for (JSONObject jo : list) {
                                RoverdueEntity re = JSONObject.toJavaObject(jo, RoverdueEntity.class);
                                re.setMerchantNo(adminManager.convertMerchantName(re.getMerchantNo()));
                                if (isInside) {
                                    re.setOverdueRateMd5(BigDecimalUtil.format(re.getOverdueMonthMd5().divide(
                                            re.getCurRepayAmtMd5(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb1(BigDecimalUtil.format(re.getOverduePrincipalMb1().divide(
                                            re.getStockAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverduePrincipalMb2().divide(
                                            re.getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverduePrincipalMb3().divide(
                                            re.getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverduePrincipalMb4().divide(
                                            re.getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                } else {
                                    re.setOverdueRateMd5(BigDecimalUtil.format(re.getOverdueMonthMd5().divide(
                                            re.getCurRepayAmtMd5(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOutOverdueRateMb1(BigDecimalUtil.format(re.getOverdueMonthMb1().divide(
                                            re.getCurRepayAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverduePrincipalMb2().divide(
                                            re.getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverduePrincipalMb3().divide(
                                            re.getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverduePrincipalMb4().divide(
                                            re.getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                }
                                res.add(re);
                            }
                        }
                        return new PageMsg<>(res, 8,
                                response.getResult().getCurrentPage(),
                                response.getResult().getTotalCount(),
                                response.getResult().getNumPerPage());
                    }
                }
            } else {
                throw new DevDefineErrorMsgException("【账务系统返回】" + result.get("errorMsg"));
            }
        } catch (Exception e) {
            logger.info("analysisInside error:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        return null;
    }

    /**
     * chengzhen 2017年12月6日 10:26:42 轮动逾期率total
     */
    @SuppressWarnings({"rawtypes"})
    @Override
    public Object analysisDataNewTotal(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException {
        Map result = new HashMap();
        try {
            result = SendHttpsUtil.postMsg4GetMap(getNewAnalysisTotalInsideUrl(isPage),
                    JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(requestMessage),
                            consumerConfig.getCommonPrivateKey())));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // to do 逾期率
        String json = JSON.toJSONString(result);
        JSONObject jsonObj = JSON.parseObject(json);
        JSONArray jsonArray = jsonObj.getJSONArray("result");
        NewRoverdueEntity newRoverdueEntity = null;
        if (jsonArray != null && jsonArray.get(0) != null) {
            newRoverdueEntity =
                    JSON.parseObject(jsonArray.get(0).toString(), NewRoverdueEntity.class);
            if ("isInside".equals(type) && newRoverdueEntity != null) {// 表示内部
                try {
                    if (newRoverdueEntity.getTotalOverduePrincipal() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRate(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipal().divide(// 设置转换率
                                        newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRate() == null
                                || newRoverdueEntity.getTotalOverdueRate().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRate(BigDecimal.ZERO);
                        }
                    }
                    if (newRoverdueEntity.getTotalOverduePrincipalMb5() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRateMb5(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipalMb5()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRateMb5() == null
                                || newRoverdueEntity.getTotalOverdueRateMb5().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRateMb5(BigDecimal.ZERO);
                        }
                    }
                    if (newRoverdueEntity.getTotalOverduePrincipalMb4() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRateMb4(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipalMb4()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRateMb4() == null
                                || newRoverdueEntity.getTotalOverdueRateMb4().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRateMb4(BigDecimal.ZERO);
                        }
                    }
                    if (newRoverdueEntity.getTotalOverduePrincipalMb3() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRateMb3(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipalMb3()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRateMb3() == null
                                || newRoverdueEntity.getTotalOverdueRateMb3().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRateMb3(BigDecimal.ZERO);
                        }
                    }
                    if (newRoverdueEntity.getTotalOverduePrincipalMb2() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRateMb2(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipalMb2()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRateMb2() == null
                                || newRoverdueEntity.getTotalOverdueRateMb2().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRateMb2(BigDecimal.ZERO);
                        }
                    }
                    if (newRoverdueEntity.getTotalOverduePrincipalMb1() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueRateMb1(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverduePrincipalMb1()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                        if (newRoverdueEntity.getTotalOverdueRateMb1() == null
                                || newRoverdueEntity.getTotalOverdueRateMb1().compareTo(BigDecimal.ZERO) == 0) {
                            newRoverdueEntity.setTotalOverdueRateMb1(BigDecimal.ZERO);
                        }
                    }

                    return newRoverdueEntity;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("noInside".equals(type) && newRoverdueEntity != null) {// 表示外部
                try {

                    if (newRoverdueEntity.getTotalOverdueMonth() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRate(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonth().divide(// 设置转换率
                                        newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }
                    if (newRoverdueEntity.getTotalOverdueMonthMb5() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRateMb5(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonthMb5()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }
                    if (newRoverdueEntity.getTotalOverdueMonthMb4() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRateMb4(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonthMb4()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }
                    if (newRoverdueEntity.getTotalOverdueMonthMb3() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRateMb3(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonthMb3()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }
                    if (newRoverdueEntity.getTotalOverdueMonthMb2() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRateMb2(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonthMb2()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }
                    if (newRoverdueEntity.getTotalOverdueMonthMb1() != null
                            && newRoverdueEntity.getTotalStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                        newRoverdueEntity.setTotalOverdueMonthRateMb1(BigDecimalUtil.format(newRoverdueEntity
                                .getTotalOverdueMonthMb1()
                                .divide(// 设置转换率
                                newRoverdueEntity.getTotalStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(100))));
                    }

                    return newRoverdueEntity;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("int".equals(type) && newRoverdueEntity != null) {
                try {

                    if (newRoverdueEntity.getTotalOverdueNum() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumRate(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNum() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    if (newRoverdueEntity.getTotalOverdueNumMb5() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumRateMb5(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNumMb5() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    if (newRoverdueEntity.getTotalOverdueNumMb4() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumRateMb4(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNumMb4() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    if (newRoverdueEntity.getTotalOverdueNumMb3() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumRateMb3(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNumMb3() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    if (newRoverdueEntity.getTotalOverdueNumMb2() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumRateMb2(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNumMb2() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    if (newRoverdueEntity.getTotalOverdueNumMb1() != 0
                            && newRoverdueEntity.getTotalOverdueStockNum() != 0) {
                        newRoverdueEntity.setTotalOverdueNumhRateMb1(BigDecimalUtil.formatInt((float) newRoverdueEntity
                                .getTotalOverdueNumMb1() / newRoverdueEntity.getTotalOverdueStockNum() * 100));
                    }
                    return newRoverdueEntity;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * chengzhen 2017年12月6日 10:26:42 轮动逾期率报表接口
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object analysisDataNewElx(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException {

        Map result = null;
        try {
            result = SendHttpsUtil.postMsg4GetMap(getNewAnalysisUrl(isPage),
                    JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(requestMessage),
                            consumerConfig.getCommonPrivateKey())));

            logger.info("analysisInside response:", JSONObject.toJSONString(result));
            if (result != null && "OK".equals(result.get("status").toString())) {
                if (!isPage) {
                    /** 如果不分页 **/
                    return result.get("result");
                } else {
                    /** 如果分页 **/
                    FinanceGenerallyPageResponsePojo<JSONObject> response = JSONObject
                            .parseObject(JSONObject.toJSONString(result), FinanceGenerallyPageResponsePojo.class);

                    if (response.getResult() != null) {
                        List<JSONObject> list = response.getResult().getRecordList();
                        List<NewRoverdueEntity> res = new ArrayList<>();
                        if (list == null || list.isEmpty()) {
                            list = new ArrayList<>();
                        } else {
                            for (JSONObject jo : list) {
                                NewRoverdueEntity re = JSONObject.toJavaObject(jo, NewRoverdueEntity.class);
                                re.setMerchantNo(adminManager.convertMerchantName(re.getMerchantNo()));// 简写转换名称
                                if ("isInside".equals(type)) {
                                    re.setOverdueRate(BigDecimalUtil.format(re.getOverduePrincipal().divide(// 设置转换率
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb1(BigDecimalUtil.format(re.getOverduePrincipalMb1().divide(
                                            re.getStockAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverduePrincipalMb2().divide(
                                            re.getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverduePrincipalMb3().divide(
                                            re.getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverduePrincipalMb4().divide(
                                            re.getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb5(BigDecimalUtil.format(re.getOverduePrincipalMb5().divide(
                                            re.getStockAmtMb5(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                } else if ("noInside".equals(type)) {
                                    re.setOverdueRate(BigDecimalUtil.format(re.getOverdueMonth().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb1(BigDecimalUtil.format(re.getOverdueMonthMb1().divide(
                                            re.getStockAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverdueMonthMb2().divide(
                                            re.getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverdueMonthMb3().divide(
                                            re.getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverdueMonthMb4().divide(
                                            re.getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb5(BigDecimalUtil.format(re.getOverdueMonthMb5().divide(
                                            re.getStockAmtMb5(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                } else {
                                    re.setOverdueRate(BigDecimalUtil.formatInt((float) re.getOverdueNum()
                                            / re.getOverdutStockNum()));
                                    re.setOverdueRateMb1(BigDecimalUtil.formatInt((float) re.getOverdueNumMb1()
                                            / re.getOverdutStockNum()));
                                    re.setOverdueRateMb2(BigDecimalUtil.formatInt((float) re.getOverdueNumMb2()
                                            / re.getOverdutStockNum()));
                                    re.setOverdueRateMb3(BigDecimalUtil.formatInt((float) re.getOverdueNumMb3()
                                            / re.getOverdutStockNum()));
                                    re.setOverdueRateMb4(BigDecimalUtil.formatInt((float) re.getOverdueNumMb4()
                                            / re.getOverdutStockNum()));
                                    re.setOverdueRateMb5(BigDecimalUtil.formatInt((float) re.getOverdueNumMb5()
                                            / re.getOverdutStockNum()));
                                }
                                res.add(re);
                            }
                        }
                        PageMsg pageIgnfo = new PageMsg<>(res, 8,
                                response.getResult().getCurrentPage(),
                                response.getResult().getTotalCount(),
                                response.getResult().getNumPerPage());

                        result.put("result", pageIgnfo);
                        result.put("resultInside", analysisDataNewTotal(requestMessage, type, true));
                        return result;
                    }
                }
            } else {
                throw new DevDefineErrorMsgException("【账务系统返回】" + result.get("errorMsg"));
            }
        } catch (Exception e) {
            logger.info("analysisInside error:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        return null;
    }

    /**
     * chengzhen 2017年12月6日 10:26:42 轮动逾期率
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public LinkedHashMap<String, Object> analysisDataNew(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException {

        LinkedHashMap<String, Object> result = new LinkedHashMap();
        try {
            result = SendHttpsUtil.postMsg4GetMap(getNewAnalysisUrl(isPage),
                    JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(requestMessage),
                            consumerConfig.getCommonPrivateKey())));

            logger.info("analysisInside response:", JSONObject.toJSONString(result));
            if (result != null && "OK".equals(result.get("status").toString())) {
                if (!isPage) {
                    /** 如果不分页 **/
                    return (LinkedHashMap<String, Object>) result.get("result");
                } else {
                    /** 如果分页 **/
                    FinanceGenerallyPageResponsePojo<JSONObject> response = JSONObject
                            .parseObject(JSONObject.toJSONString(result), FinanceGenerallyPageResponsePojo.class);

                    if (response.getResult() != null) {
                        List<JSONObject> list = response.getResult().getRecordList();
                        List<NewRoverdueEntity> res = new ArrayList<>();
                        if (list == null || list.isEmpty()) {
                            list = new ArrayList<>();
                        } else {
                            for (JSONObject jo : list) {
                                NewRoverdueEntity re = JSONObject.toJavaObject(jo, NewRoverdueEntity.class);
                                re.setMerchantNo(adminManager.convertMerchantName(re.getMerchantNo()));// 简写转换名称
                                if ("isInside".equals(type) && re.getStockAmt() != null
                                        && re.getStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                                    re.setOverdueRate(BigDecimalUtil.format(re.getOverduePrincipal().divide(// 设置转换率
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb1(BigDecimalUtil.format(re.getOverduePrincipalMb1().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverduePrincipalMb2().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverduePrincipalMb3().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverduePrincipalMb4().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb5(BigDecimalUtil.format(re.getOverduePrincipalMb5().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                } else if ("noInside".equals(type) && re.getStockAmt() != null
                                        && re.getStockAmt().compareTo(BigDecimal.ZERO) != 0) {
                                    re.setOverdueRate(BigDecimalUtil.format(re.getOverdueMonth().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb1(BigDecimalUtil.format(re.getOverdueMonthMb1().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb2(BigDecimalUtil.format(re.getOverdueMonthMb2().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb3(BigDecimalUtil.format(re.getOverdueMonthMb3().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb4(BigDecimalUtil.format(re.getOverdueMonthMb4().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                    re.setOverdueRateMb5(BigDecimalUtil.format(re.getOverdueMonthMb5().divide(
                                            re.getStockAmt(), 4, BigDecimal.ROUND_HALF_UP)
                                            .multiply(new BigDecimal(100))));
                                } else {
                                    if (re.getOverdutStockNum() != 0) {
                                        re.setOverdueRate(BigDecimalUtil.formatInt((float) re.getOverdueNum()
                                                / re.getOverdutStockNum() * 100));
                                        re.setOverdueRateMb1(BigDecimalUtil.formatInt((float) re.getOverdueNumMb1()
                                                / re.getOverdutStockNum() * 100));
                                        re.setOverdueRateMb2(BigDecimalUtil.formatInt((float) re.getOverdueNumMb2()
                                                / re.getOverdutStockNum() * 100));
                                        re.setOverdueRateMb3(BigDecimalUtil.formatInt((float) re.getOverdueNumMb3()
                                                / re.getOverdutStockNum() * 100));
                                        re.setOverdueRateMb4(BigDecimalUtil.formatInt((float) re.getOverdueNumMb4()
                                                / re.getOverdutStockNum() * 100));
                                        re.setOverdueRateMb5(BigDecimalUtil.formatInt((float) re.getOverdueNumMb5()
                                                / re.getOverdutStockNum() * 100));
                                    }
                                }
                                res.add(re);
                            }
                        }
                        PageMsg pageIgnfo = new PageMsg<>(res, 8,
                                response.getResult().getCurrentPage(),
                                response.getResult().getTotalCount(),
                                response.getResult().getNumPerPage());

                        result.put("result", pageIgnfo);
                        result.put("resultInside", analysisDataNewTotal(requestMessage, type, true));
                        return result;
                    }
                }
            } else {
                throw new DevDefineErrorMsgException("【账务系统返回】" + result.get("errorMsg"));
            }
        } catch (Exception e) {
            logger.info("analysisInside error:", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        return null;
    }

    @Override
    public void analysisXlsx(JSONObject requestMessage, Boolean isInside, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException {
        List<RoverdueEntity> xlsx = null;
        try {
            xlsx = JSONObject.parseArray(JSONObject.toJSONString(analysisData(requestMessage, isInside, false)),
                    RoverdueEntity.class);
        } catch (Exception e) {
            response.setStatus(500);
            return;
        }
        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = null;
            String fileName = null;
            if (isInside) {
                workbook = adminManager.convertInsideXlsx(xlsx);
                fileName = "attachment;filename=statisticsI-" + DateTools.getYmdhmsTime() + ".xls";
            } else {
                workbook = adminManager.convertOutSideXlsx(xlsx);
                fileName = "attachment;filename=statisticsO-" + DateTools.getYmdhmsTime() + ".xls";
            }
            response.setContentType("application/vnd.ms-excel");

            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
        }
    }

    /**
     * chengzhen 2017年12月7日 19:48:51
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void riskManagementXlsx(JSONObject requestMessage, String isInside, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException {
        Map result = new HashMap();
        try {

            result = SendHttpsUtil.postMsg4GetMap(getNewAnalysisUrl(false),
                    JSONObject.toJSONString(SignUtil.chatEncode(JSONObject.toJSONString(requestMessage),
                            consumerConfig.getCommonPrivateKey())));
        } catch (Exception e) {
            response.setStatus(500);
            return;
        }
        try {

            List<NewRoverdueEntity> newRoverdueEntityList =
                    JSONObject.parseArray(JSONObject.toJSONString(result.get("result")),
                            NewRoverdueEntity.class);

            Object analysisDataNewTotal = analysisDataNewTotal(requestMessage, isInside, true);
            String string = JSON.toJSONString(analysisDataNewTotal);

            NewRoverdueEntity newRoverdueEntityTotal = JSON.parseObject(string, NewRoverdueEntity.class);

            // 2.导出excel表格
            HSSFWorkbook workbook = null;
            String fileName = null;
            if ("isInside".equals(isInside)) {
                workbook = adminManager.riskInsideXlsx(newRoverdueEntityList, newRoverdueEntityTotal);
                fileName = "attachment;filename=statisticsIn-" + DateTools.getYmdhmsTime() + ".xls";
            } else if ("noInside".equals(isInside)) {
                workbook = adminManager.riskNOInsideXlsx(newRoverdueEntityList, newRoverdueEntityTotal);
                fileName = "attachment;filename=statisticsNo-" + DateTools.getYmdhmsTime() + ".xls";
            } else {
                workbook = adminManager.riskIntXlsx(newRoverdueEntityList, newRoverdueEntityTotal);
                fileName = "attachment;filename=statisticsInt-" + DateTools.getYmdhmsTime() + ".xls";
            }
            response.setContentType("application/vnd.ms-excel");

            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();

        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
        }
    }

    private String getAnalysisUrl(Boolean isPage) {
        return urlConfig.getAnalysisUrl() + "/" + (isPage == null ? true : isPage);
    }

    private String getNewAnalysisUrl(Boolean isPage) {
        return urlConfig.getNewAnalysisUrl() + "/" + (isPage == null ? true : isPage);
    }

    private String getNewAnalysisTotalInsideUrl(Boolean isPage) {
        return urlConfig.getTotalNewAnalysisUrl() + "/" + (isPage == null ? true : isPage);
    }

    @SuppressWarnings("unused")
    private String getNewAnalysisTotalNoInsideUrl(Boolean isPage) {
        return urlConfig.getNoinsidetotalNewAnalysisUrl() + "/" + (isPage == null ? true : isPage);
    }

    @Override
    public Map<String, String> getApproveHtml(JSONObject objs) {
        String orderId = objs.getString("orderId");
        // 根据status 获取 对应的流程Key,
        int status = 0;
        String procKey = "";
        if (StringUtils.isNoneEmpty(objs.getString("procKey"))) {
            procKey = objs.getString("procKey");
        } else {
            status = carStatusManager.getCarStatusByOrderId(orderId);
            procKey = getStrByStatus(status);
        }
        if (StringUtils.isNoneEmpty(procKey)) {
            // 根据ProcKey 和 procId 获取对应的Html
            Map<String, String> result = carStatusManager.getProcHtml(orderId, procKey);
            return result;
        }
        return null;
    }

    private String getStrByStatus(int status) {
        // 失联流程 STATUS = 15 20
        // 入库 25 30
        // 待出售 35 40
        // 转租 45 50
        // 返还 55 60
        switch (status) {
            case 15:
                return "car_out_of_contact";
            case 20:
                return "car_out_of_contact";
            case 25:
                return "car_storage";
            case 30:
                return "car_storage";
            case 35:
                return "car_sale";
            case 40:
                return "car_sale";
            case 45:
                return "car_sublet";
            case 50:
                return "car_sublet";
            case 55:
                return "car_return";
            case 60:
                return "car_return";
            default:
                return "";
        }
    }
}
