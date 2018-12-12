package com.iqb.consumer.manage.front.jys.assetallocation.assetallocine;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetallocine.db.entity.BreakOrderInfoEntity;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException.Reason;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetAllocinePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.AssetDivisionPojo;
import com.iqb.consumer.asset.allocation.assetallocine.pojo.OrderBreakDetailsInfoResponsePojo;
import com.iqb.consumer.asset.allocation.assetallocine.service.AssetAllocineService;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.back.IBackGroundService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------
 * 2017年3月10日    adam       1.0        1.0 Version
 * </pre>
 */
@Controller
@RequestMapping("/assetallocine")
public class AssetAllocineController extends BasicService {

    protected static final Logger logger = LoggerFactory.getLogger(AssetAllocineController.class);
    private static final String SUCCESS = "success";

    private static final int SERVICE_CODE_CGET_ASSET_BREAK_DETAILS = 1;
    private static final int SERVICE_CODE_ORDER_BREAK = 2;
    private static final int SERVICE_CODE_EXPORT_XLSX = 3;
    private static final int SERVICE_CODE_EXPORT_XLSX_ASSET_BREAK = 4;
    @Resource
    private AssetAllocineService assetAllocineServiceImpl;

    @Resource
    private AdminService adminService;
    @Resource
    private IBackGroundService backGroundService;
    @Resource
    private BillInfoService billInfoService;

    /**
     * 4.10.1查询待资产分配的子订单
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_division_assets_details"}, method = {RequestMethod.POST})
    public Object getDivisionAssetsDetails(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            PageInfo<AssetAllocinePojo> result = assetAllocineServiceImpl
                    .getDivisionAssetsDetails(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 通过订单号查询账单详情
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getAllBillByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getAllBillInfoByOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("通过订单号:{} 查询账单", objs.getString("orderId"));
        List<Map<String, Object>> billMap =
                billInfoService.getAllJysBillInfo(objs.getString("orderId"));
        return billMap;
    }

    /**
     * Description: 4.10.2资产分配详情信息展示
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月13日 下午8:13:35
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_division_assets_details/{id}"}, method = {RequestMethod.POST})
    public Object getDivisionAssetsDetailsById(@PathVariable Long id,
            HttpServletRequest request) {
        try {
            if (id == null) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            }
            AssetAllocinePojo result = assetAllocineServiceImpl
                    .getDivisionAssetsDetailsById(id);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.10.3详情信息保存
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月13日 下午8:15:06
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/persist_details"}, method = {RequestMethod.POST})
    public Object persistDetails(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            assetAllocineServiceImpl
                    .persistAssetDetails(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", SUCCESS);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (InvalidRARException e) {
            switch (e.getReason()) {
                case INVALID_B_ORDER_ID:
                    return super.returnFailtrueInfo(e);
                case INVALID_PARAMS:
                    return super.returnFailtrueInfo(e);
                case INVALID_RESPONSE:
                    return super.returnFailtrueInfo(e);
                case DB_ERROR:
                    return super.returnFailtrueInfo(e);
                default:
                    return super.returnFailtrueInfo(new InvalidRARException(
                            Reason.UNKNOWN_ERROR));
            }
        } catch (Throwable e) {
            logger.error(
                    "errorMSG：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /*** --------------------------------- ***/

    /**
     * Description: 4.9.1拆分订单查询接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月13日 下午8:15:50
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_division_assets_prepare"}, method = {RequestMethod.POST})
    public Object getDivisionAssetsPrepare(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            PageInfo<AssetDivisionPojo> result = assetAllocineServiceImpl
                    .getDivisionAssetsPrepare(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_division_assets_prepare/{id}"}, method = {RequestMethod.POST})
    public Object getDivisionAssetsPrepareById(@PathVariable Long id,
            HttpServletRequest request) {
        try {
            if (id == null) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            }
            AssetDivisionPojo result = assetAllocineServiceImpl
                    .getDivisionAssetsPrepareById(id);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.9.2拆分订单保存
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月14日 下午2:45:39
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/persist_child_assets"}, method = {RequestMethod.POST})
    public Object persistChildAssets(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            int result = assetAllocineServiceImpl
                    .persistChildAssets(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", SUCCESS);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/child_assets_prepare"}, method = {RequestMethod.POST})
    public Object childAssetsPrepare(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            logger.info("child_assets_prepare [starting] .");
            List<BreakOrderInfoEntity> result = assetAllocineServiceImpl
                    .childAssetsPrepare(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/persist_children_assets"}, method = {RequestMethod.POST})
    public Object persistChildrenAssets(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            int result = assetAllocineServiceImpl
                    .persistChildrenAssets(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", SUCCESS);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cget_asset_break_details", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CGET_ASSET_BREAK_DETAILS)
    public Object cgetAssetBreakDetails(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminService.getMerchantNos(requestMessage);
            PageInfo<AssetBreakDetailsInfoResponsePojo> result =
                    assetAllocineServiceImpl.cgetAssetBreakDetails(requestMessage);
            BigDecimal total = assetAllocineServiceImpl.cgetAssetBreakDetailsAmtTotal(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            linkedHashMap.put("amtTotal", total != null ? BigDecimalUtil.format(total) : "");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_ASSET_BREAK_DETAILS);
        }
    }

    /**
     * 
     * Description: FINANCE-1191 交易所订单分期（订单分期）
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月14日 下午4:21:27
     */
    @ResponseBody
    @RequestMapping(value = "/cget_order_break_details", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CGET_ASSET_BREAK_DETAILS)
    public Object cgetOrderBreakDetails(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminService.getMerchantNos(requestMessage);
            PageInfo<OrderBreakDetailsInfoResponsePojo> result =
                    assetAllocineServiceImpl.cgetOrderBreakDetails(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_ASSET_BREAK_DETAILS);
        }
    }

    /**
     * 
     * Description: FINANCE-1191 交易所订单分期（订单分期）分期按钮
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月14日 上午10:23:47
     */
    @ResponseBody
    @RequestMapping(value = "/order_break", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_ORDER_BREAK)
    public Object jysOrderBreak(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            Object result = backGroundService.jsyOrderBreak(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ORDER_BREAK);
        }
    }

    /**
     * 
     * Description: FINANCE-1191 交易所订单分期（订单分期）FINANCE-1257 导出Excel功能
     * 
     * @param
     * @return Map
     * @throws
     * @Author adam Create Date: 2017年6月14日 下午5:07:24
     */
    @ResponseBody
    @RequestMapping(value = {"/export_xlsx"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_EXPORT_XLSX)
    public Object exportOrderList(HttpServletRequest request, HttpServletResponse response) {
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
            JSONObject requestMessage = JSONObject.parseObject(json);
            adminService.getMerchantNos(requestMessage);
            String result = assetAllocineServiceImpl.exportXlsx(requestMessage, response);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            return generateFailResponseMessage(e, SERVICE_CODE_ORDER_BREAK);
        }
    }

    /**
     * 
     * Description: FINANCE-1190 交易所---资产拆分明细查询FINANCE-1276 资产拆分添加导出功能
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月15日 下午4:56:05
     */
    @ResponseBody
    @RequestMapping(value = {"/export_xlsx_asset_break"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_EXPORT_XLSX_ASSET_BREAK)
    public Object exportXlsxAssetBreak(HttpServletRequest request, HttpServletResponse response) {
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
            JSONObject requestMessage = JSONObject.parseObject(json);
            adminService.getMerchantNos(requestMessage);
            String result = assetAllocineServiceImpl.exportXlsxAssetBreak(requestMessage, response);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            return generateFailResponseMessage(e, SERVICE_CODE_EXPORT_XLSX_ASSET_BREAK);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_ASSET_ALLOCINE_CENTER;
    }

    /**
     * 
     * Description:资产分配到交易所-订单保存接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月20日
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-saveJysOrder", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_ORDER_BREAK)
    public Object saveJysOrder(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            long result = backGroundService.saveJysOrder(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ORDER_BREAK);
        }
    }

    /**
     * 
     * Description:交易所已推送列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/assetAllocationList"}, method = {RequestMethod.POST})
    public Object assetAllocationList(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            PageInfo<AssetAllocinePojo> result = assetAllocineServiceImpl
                    .assetAllocationList(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:删除资产分配记录
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月27日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/deleteAssetAllocationInfo"}, method = {RequestMethod.POST})
    public Object deleteAssetAllocationInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            int result = assetAllocineServiceImpl
                    .deleteAssetAllocationInfo(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result > 0 ? "success" : String.valueOf(result));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }
}
