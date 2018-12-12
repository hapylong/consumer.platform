package com.iqb.consumer.manage.front.contract.sign;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.eatep.ec.contract.ssq.sign.bean.EcGetImageGroupByTypeRequestMessage;
import com.iqb.eatep.ec.contract.ssq.sign.db.pojo.OrgInfo;
import com.iqb.eatep.ec.contract.ssq.sign.service.EcSignFactorService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.front.base.FrontBaseService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping(value = "/ec_participant")
// unIntcpt-
public class EcSignerController extends FrontBaseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EcSignFactorService ecSignFactorServiceImpl;

    /**
     * 
     * Description:查询已经注册的线上借款人信息 *********************** params *********************** [group*]
     * 线上借款人 |sign_type 签署类型[（1.个人;2.企业）] |phone_num| |name| |sign_state 是否上传签名图章 1是 0否 |org_code
     * 机构简称|
     * 
     * 门店 |sign_type 签署类型[（1.个人;2.企业）] |org_code 机构简称| params
     * 
     * |org_code 机构简称| short 机构简称 | short_num 机构简称编码 |
     * **********************************************
     * 
     * @param
     * @return Map
     * @throws
     * @Author adam Create Date: 2017年2月21日 下午5:05:48
     */
    @ResponseBody
    @RequestMapping(value = {"/get_group_by_type"}, method = {RequestMethod.POST})
    public Map getGroupByType(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        try {
            logger.debug("**********  EcSignerController[getAllSignerRegistered] start **************:{}",
                    JSONObject.toJSONString(objs));
            PageInfo<OrgInfo> pageInfo = ecSignFactorServiceImpl
                    .getGroupByType(objs);
            logger.info("**********  EcSignerController[getAllSignerRegistered] finished **************");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error(
                    "EcSignerController[getAllSignerRegistered_IqbException_]:"
                            + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Throwable e) {
            logger.error(
                    "EcSignerController[getAllSignerRegistered_Throwable_]:"
                            + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 上传签名图章
     * 
     * @param
     * @return Map
     * @throws IOException
     * @throws
     * @Author adam Create Date: 2017年2月21日 下午8:57:12
     */
    private static final String IQB_PERSIST_SIGNATURE_STAMP_SUCCESS_RESPONSE_MESSAGE = "{\"Status\":\"OK\"}";
    private static final String IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE = "{\"Status\":\"FAIL\"}";

    @ResponseBody
    @RequestMapping(
            value = {"/persist_signature_stamp"},
            method = {RequestMethod.POST})
    // ,
    /*
     * consumes = "", produces = "")
     */
    public Object persistSignatureStamp(
            @RequestParam("ecSignerCertNo") String ecSignerCertNo,
            @RequestParam("ecSignerPhone") String ecSignerPhone,
            @RequestParam("file") CommonsMultipartFile file,
            @RequestParam("size") int size,
            HttpServletRequest request) {

        try {
            if (ecSignerCertNo == null || file == null || file.getBytes().length == 0) {
                return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
            }
            int update = ecSignFactorServiceImpl.persistSignatureStamp(ecSignerCertNo,
                    ecSignerPhone,
                    file.getOriginalFilename(),
                    file.getBytes());
            if (update <= 0) {
                logger.error("EcSignerController[persistSignatureStamp_update]:"
                        + update);
                return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
            }
            return IQB_PERSIST_SIGNATURE_STAMP_SUCCESS_RESPONSE_MESSAGE;
        } catch (IqbException iqbe) {
            logger.error("EcSignerController[persistSignatureStamp_throwable]:" + iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Throwable e) {
            logger.error("EcSignerController[persistSignatureStamp_throwable]:"
                    + e);
            return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
        }
    }

    /**
     * 
     * Description: 上传机构签章信息
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年4月27日 上午11:51:15
     */
    @ResponseBody
    @RequestMapping(value = {"/persist_org_signature_stamp"}, method = {RequestMethod.POST})
    // ,
    public Object persistOrgSignatureStamp(@RequestParam("ecSignerCode") String ecSignerCode,
            @RequestParam("file") CommonsMultipartFile file, @RequestParam("size") int size, HttpServletRequest request) {
        try {
            if (ecSignerCode == null || file == null || file.getBytes().length == 0) {
                return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
            }
            int update =
                    ecSignFactorServiceImpl.persistOrgSignatureStamp(ecSignerCode, file.getOriginalFilename(),
                            file.getBytes());
            if (update <= 0) {
                logger.error("EcSignerController[persistSignatureStamp_update]:" + update);
                return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
            }
            return IQB_PERSIST_SIGNATURE_STAMP_SUCCESS_RESPONSE_MESSAGE;
        } catch (IqbException iqbe) {
            logger.error("EcSignerController[persistSignatureStamp_throwable]:" + iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("EcSignerController[persistSignatureStamp_throwable]:" + e);
            return IQB_PERSIST_SIGNATURE_STAMP_FAIL_RESPONSE_MESSAGE;
        }
    }

    /**
     * Description: 查看签名图章
     * 
     * @param *********************************************** |sign_type 签署类型[（1.个人;2.企业）]
     * 
     *        |id ] *********************** params ***********************
     * @return String
     * @throws
     * @Author adam Create Date: 2017年2月27日 上午9:33:47
     */
    @RequestMapping(
            value = {"/get_image_group_by_type"},
            method = {RequestMethod.POST})
    // ,
    /*
     * consumes = "", produces = "")
     */
    public Map getImageGroupByType(
            @RequestBody EcGetImageGroupByTypeRequestMessage requestMessage,
            HttpServletRequest request) {

        try {
            if (requestMessage == null || requestMessage.getId() == null
                    || requestMessage.getSign_type() == null) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            }
            OrgInfo ese = ecSignFactorServiceImpl.getImageGroupByType(
                    requestMessage.getId(), requestMessage.getSign_type());
            if (ese == null) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090003));
            }
            if (ese.getEcSignerImgDataBlob() != null
                    && ese.getEcSignerImgDataBlob().length > 0) {
                ese.setEcSignerImgDataBlobStr("data:image/png;base64,"
                        + Base64.encodeBase64String(ese
                                .getEcSignerImgDataBlob()));
            }
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", ese);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException e) {
            logger.error("EcSignerController[getImageGroupByType_IqbException]:"
                    + e);
            return super.returnFailtrueInfo(e);
        } catch (Throwable e) {
            logger.error("EcSignerController[getImageGroupByType_throwable]:"
                    + e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000002));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getById"}, method = {RequestMethod.POST})
    public Map getSignatureStamp(@RequestBody JSONObject objs,
            HttpServletRequest reqeust) {
        try {
            logger.debug("**********  EcSignerController[getParticipatorMenu] start **************");
            ecSignFactorServiceImpl.getSignatureStampById(objs);
            logger.info("**********  EcSignerController[persistSignatureStamp] finished **************");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000,
                    new String[] {});
        } catch (IqbException iqbe) {
            logger.error(
                    "EcSignerController[getSignerRegisteredByCondition_IqbException_]:"
                            + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Throwable e) {
            logger.error(
                    "EcSignerController[getAllSignerRegistered_Throwable_]:"
                            + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/update_group_by_type"}, method = {RequestMethod.POST})
    public Map updateGroupByType(@RequestBody JSONObject objs,
            HttpServletRequest reqeust) {
        try {
            logger.debug("**********  EcSignerController[getAllSignerRegistered] start **************");
            ecSignFactorServiceImpl.updateGroupByType(objs);
            logger.info("**********  EcSignerController[getAllSignerRegistered] finished **************");
            return super.returnSuccessInfo(new LinkedHashMap<String, Object>());
        } catch (IqbException iqbe) {
            logger.error(
                    "EcSignerController[getAllSignerRegistered_IqbException_]:"
                            + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Throwable e) {
            logger.error(
                    "EcSignerController[getAllSignerRegistered_Throwable_]:"
                            + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }
    
    /**
     * 
     * Description: 删除签署方信息
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang
     * Create Date: 2017年6月29日 下午2:59:57
     */
    @ResponseBody
    @RequestMapping(value = { "/deleteSignFactor" }, method = { RequestMethod.GET, RequestMethod.POST })
    public Map deleteSignFactor(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("deleteSignFactor--" + JSONObject.toJSONString(objs));
            ecSignFactorServiceImpl.deleteSignFactor(objs);
            logger.info("IQB信息--删除数据完成");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("IQB错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "lanlan");
        map.put("telp", "13020079696");
        JSONObject obj = new JSONObject(map);
        obj.get("name");
        System.out.println(obj.get("na111me"));
    }

    /**
     * 
     16:24:15 1324 2017/2/24 16:24:15
     * 
     * @POST
     * @Path("/idcard")
     * @Consumes(MediaType.MULTIPART_FORM_DATA)
     * @Produces({ MediaType.APPLICATION_JSON })
     * @ServiceCode(SERVICE_IDCARD_SUBMIT) public AccountDetailsUpdateResponseMessage idcard(
     * @FormDataParam("file") InputStream file,
     * @FormDataParam("file") FormDataContentDisposition fileDisposition,
     * @FormDataParam("phone") String phone,
     * @FormDataParam("size") int size) { AccountDetailsUpdateResponseMessage message = new
     *                        AccountDetailsUpdateResponseMessage(); String fileFullName =
     *                        fileDisposition.getFileName(); Calendar updateTime =
     *                        Calendar.getInstance(); try { AccountDetailsEntity accountDetails =
     *                        getAccountDetailsManager().find(phone); if (null == accountDetails) {
     *                        accountDetails = createNewAccountDetailsEntity(phone); } else {
     *                        if(getBlackListManager().isBlackPhone(phone)) {
     *                        message.setStatus(StatusEnum.FAIL); message.addError
     *                        (generateErrorCode(SERVICE_IDCARD_SUBMIT,
     *                        Constant.ERROR_IN_BLACK_LIST)); return message; } } if(size >
     *                        Constant.MAX_IMAGE_SIZE){ message.setStatus(StatusEnum.FAIL);
     *                        message.addError (generateErrorCode(SERVICE_IDCARD_SUBMIT,
     *                        ERROR_FILE_TOO_LARGE)); return message; }
     * 
     *                        File outputFile = ImageUtils.prepareOutputFile(Constant
     *                        .PICTURE_PATH_IDCARD, fileFullName, phone);
     * 
     *                        OutputStream outputStream = new FileOutputStream(outputFile); int
     *                        length = 0;
     * 
     *                        byte[] buff = new byte[256];
     * 
     *                        while (-1 != (length = file.read(buff))) { outputStream.write(buff, 0,
     *                        length); } file.close(); outputStream.close();
     * 
     *                        String filePath = DIR_ROOT + GooCoinSettingsFactory
     *                        .getInstance().getProperty(GooCoinSettingsFactory
     *                        .KEY_IMAGE_FILE_BASE_PATH,"image") + Constant.PICTURE_PATH_IDCARD +
     *                        DIR_ROOT + outputFile.getName(); upyun.writeFile(filePath, outputFile,
     *                        true); ImageUtils.deleteFile(outputFile);
     * 
     *                        accountDetails.setIdPhoto(outputFile.getName());
     *                        accountDetails.setUpdateTime(updateTime);
     *                        getAccountDetailsManager().update(accountDetails);
     * 
     *                        } catch (FileNotFoundException e) {
     *                        log.error("Unexpected exception in head", e);
     *                        message.setStatus(StatusEnum.FAIL); message.addError
     *                        (generateErrorCode(SERVICE_IDCARD_SUBMIT, ERROR_FILE_NOT_FOUND));
     *                        return message; } catch (IOException e) {
     *                        log.error("Unexpected exception in head", e);
     *                        message.setStatus(StatusEnum.FAIL); message.addError
     *                        (generateErrorCode(SERVICE_IDCARD_SUBMIT, ERROR_IO_EXCEPTION)); return
     *                        message; } message.setUpdateTime(updateTime);
     *                        message.setStatus(StatusEnum.OK); return message; }
     */
}
