package com.iqb.consumer.manage.front.image;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.image.ImageBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.pledge.PledgeInquiryBiz;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.image.ImageService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

import jodd.util.StringUtil;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/image")
public class ImageController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private PledgeInquiryBiz pledgeInquiryBiz;

    /**
     * 批量上传图片
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/multiUploadImage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map multiUploadImage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("开始批量上传图片数据...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            int res = imageService.batchInsertImage(objs);
            if (res >= 1) {
                linkedHashMap.put("result", "success");
            } else {
                linkedHashMap.put("result", "fail");
            }
            logger.debug("批量上传图片数据完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 上传单个图片
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/uploadImage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map uploadImage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始上传图片数据...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            // String imgName = UUID.randomUUID().toString().replaceAll("-",
            // "").toUpperCase();
            // String path = paramConfig.getImage_upload_folder() +
            // File.separator + imgName;
            // FileUtils.copyInputStreamToFile(file.getInputStream(), new
            // File(path));// 图片上传
            int res = imageService.insertImage(objs);
            if (res == 1) {
                linkedHashMap.put("result", objs.get("imgPath"));
            } else {
                linkedHashMap.put("result", "fail");
            }
            logger.debug("IQB信息---上传图片数据完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getImageList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getImage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---查询图片数据,参数:{}", objs);
            String orderId = objs.getString("orderId");
            // 如果车秒贷订单需要将主订单风控图片返回前端,单独查询车秒贷的图片
            List<ImageBean> list = null;
            List<ImageBean> result = imageService.selectList(objs);
            boolean contains = false;
            // 解决获取图片的BUG
            if (objs.getJSONArray("imgType") != null) {
                contains = objs.getJSONArray("imgType").contains(15);
            }
            if (orderId.endsWith("X") && contains) {
                Integer[] riskImgType = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
                JSONObject obj = new JSONObject();
                obj.put("imgType", riskImgType);
                obj.put("orderId", orderId.substring(0, orderId.length() - 1));
                list = imageService.selectList(obj);
                if (list != null) {
                    result.addAll(list);
                }
            }
            // 通过业务类型判断车主贷/周转贷
            OrderBean orderBean = orderBiz.selByOrderId(orderId);
            if (orderBean != null
                    && ("2400".equals(orderBean.getBizType()) || "3002".equals(orderBean.getBizType()) || "4001"
                            .equals(orderBean.getBizType()))) { // 车主贷图片特殊处理
                JSONObject obj = new JSONObject();
                obj.put("orderId", orderId);
                OrderBean ob = orderService.selectOne(obj);
                obj.put("imgType", objs.getJSONArray("imgType"));
                obj.put("orderId", ob.getOrderNo());
                list = imageService.selectList(obj);
                if (list != null) {
                    result.addAll(list);
                }
            }
            logger.debug("IQB信息---查询图片数据完成.");
            OrderOtherInfo orderOtherInfo = orderService.selectOtherOne(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("projectName", orderOtherInfo == null ? "" : orderOtherInfo.getProjectName());
            linkedHashMap.put("guarantee", orderOtherInfo == null ? "" : orderOtherInfo.getGuarantee());
            linkedHashMap.put("guaranteeName", orderOtherInfo == null ? "" : orderOtherInfo.getGuaranteeName());
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getImageListByUUid"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getImageUUid(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---查询图片数据,参数:{}", objs);
            String uuid = pledgeInquiryBiz.getUuidByOid(objs.getString("orderId"));
            if (StringUtil.isEmpty(uuid)) {
                throw new Exception("uuid 为空");
            }
            objs.put("orderId", uuid);
            List<ImageBean> result = imageService.selectList(objs);
            logger.debug("IQB信息---查询图片数据完成.");
            OrderOtherInfo orderOtherInfo = orderService.selectOtherOne(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("projectName", orderOtherInfo == null ? "" : orderOtherInfo.getProjectName());
            linkedHashMap.put("guarantee", orderOtherInfo == null ? "" : orderOtherInfo.getGuarantee());
            linkedHashMap.put("guaranteeName", orderOtherInfo == null ? "" : orderOtherInfo.getGuaranteeName());
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/deleteImage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map deleteImage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---删除图片数据...");
            int result = imageService.deleteImageByPath(objs);
            logger.debug("IQB信息---删除图片数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            if (result == 1) {
                linkedHashMap.put("result", "success");
            } else {
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/downLoadImage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map downLoadImage(HttpServletRequest request
            , HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            logger.debug("IQB信息---下载数据:{}", objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", imageService.downLoadImage(objs, response));
            logger.debug("IQB信息---下载数据完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * word文档预览功能(只支持到word2007)
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    @ResponseBody
    @RequestMapping(value = {"/convertDocToHtml"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map convertDocToHtml(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---word文档预览功能...");
            String result = imageService.convertDocToHtml(objs);
            logger.debug("IQB信息---word文档预览完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
