package com.iqb.consumer.manage.front.crm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.crm.customer.service.ICustomerService;
import com.iqb.consumer.data.layer.bean.ownerloan.MyCheckInfoBean;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

/**
 * 
 * Description: crm客户信息维护
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/customer")
public class CustomerRestController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    @Autowired
    private ICustomerService customerServiceImpl;

    /**
     * 
     * Description: 保存客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午4:23:30
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-saveCustomerInfoFromEtep", method = RequestMethod.POST)
    public Object saveCustomerInfoFromEtep(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---保存来自etep推送的客户信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.saveCustomerServiceFromEtep(objs);
            logger.info("IQB调试信息---保存来自etep推送的客户信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 更新客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午3:13:55
     */
    @ResponseBody
    @RequestMapping(value = "/updateCustomerInfoFromEtep", method = RequestMethod.POST)
    public Object updateCustomerInfoFromEtep(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---更新来自etep推送的客户信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.updateCustomerInfoFromEtep(objs);
            logger.info("IQB调试信息---更新来自etep推送的客户信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 推送客户信息指消费金融
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午9:12:13
     */
    @ResponseBody
    @RequestMapping(value = "/pushCustomerInfoToXFJR", method = RequestMethod.POST)
    public Object pushCustomerInfoToXFJR(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---推送客户信息指消费金融开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.pushCustomerInfoToXFJR(objs);
            logger.info("IQB调试信息---推送客户信息指消费金融结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 查询客户信息列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午3:50:13
     */
    @ResponseBody
    @RequestMapping(value = "/queryCustomerList", method = RequestMethod.POST)
    public Object queryCustomerList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始客户信息列表..." + objs.toJSONString());
            PageInfo pageInfo = this.customerServiceImpl.queryCustomerList(objs);
            logger.info("IQB信息---用户查询客户信息列表结束.");

            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 删除客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午4:13:04
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCustomerInfo", method = RequestMethod.POST)
    public Object deleteCustomerInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---删除客户信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.deleteCustomerInfo(objs);
            logger.info("IQB调试信息---删除客户信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 插入客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:01:16
     */
    @ResponseBody
    @RequestMapping(value = "/insertCustomerInfo", method = RequestMethod.POST)
    public Object insertCustomerInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---插入客户信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.insertCustomerInfo(objs);
            logger.info("IQB调试信息---插入客户信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 更新客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:01:16
     */
    @ResponseBody
    @RequestMapping(value = "/updateCustomerInfo", method = RequestMethod.POST)
    public Object updateCustomerInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---更新客户信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.updateCustomerInfo(objs);
            logger.info("IQB调试信息---更新客户信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 根据客户code获取客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午3:09:29
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerInfoByCustomerCode", method = RequestMethod.POST)
    public Object getCustomerInfoByCustomerCode(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---更新客户信息开始" + JSONUtil.objToJson(objs));
            Map m = this.customerServiceImpl.getCustomerInfoByCustomerCode(objs);
            logger.info("IQB调试信息---更新客户信息结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", m);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

    /**
     * 
     * Description: 根据客户类型获取客户信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月27日 下午6:02:04
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerInfoByCustomerType", method = RequestMethod.POST)
    public Object getCustomerInfoByCustomerType(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---根据客户类型获取客户信息开始" + JSONUtil.objToJson(objs));
            List l = this.customerServiceImpl.getCustomerInfoByCustomerType(objs);
            logger.info("IQB调试信息---根据客户类型获取客户信息结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", l);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 上传客户图片信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:31:59
     */
    @ResponseBody
    @RequestMapping(value = "/uploadCustomerImg", method = RequestMethod.POST)
    public Object uploadCustomerImg(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---上传客户图片信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.uploadCustomerImg(objs);
            logger.info("IQB调试信息---上传客户图片信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 删除客户图片信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:31:59
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCustomerImg", method = RequestMethod.POST)
    public Object deleteCustomerImg(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---删除客户图片信息开始" + JSONUtil.objToJson(objs));
            this.customerServiceImpl.deleteCustomerImg(objs);
            logger.info("IQB调试信息---删除客户图片信息结束");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 根据客户编码获取客户门店信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月11日 下午2:04:19
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerStoreInfoByCode", method = RequestMethod.POST)
    public Object getCustomerStoreInfoByCode(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---根据客户编码获取客户门店信息" + JSONUtil.objToJson(objs));
            Map m = this.customerServiceImpl.getCustomerStoreInfoByCode(objs);
            logger.info("IQB调试信息---根据客户编码获取客户门店信息");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", m);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 获取市下拉框集合
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月14日 下午12:07:41
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerCityListByProvince", method = RequestMethod.POST)
    public Object getCustomerCityListByProvince(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---获取市下拉框集合" + JSONUtil.objToJson(objs));
            List<Map<String, Object>> list = this.customerServiceImpl.getCustomerCityListByProvince(objs);
            logger.info("IQB调试信息---获取市下拉框集合");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-2784 FINANCE-2690 蒲公英个人客户信息查询
     * 
     * @param
     * @return Object
     * @throws
     * @Author chengzhen Create Date: 2018年1月8日 15:02:16
     */
    @ResponseBody
    @RequestMapping(value = "/getDandelionCustomerList", method = RequestMethod.POST)
    public Object getDandelionCustomerList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---蒲公英个人客户集合入参" + JSONUtil.objToJson(objs));
            PageInfo<MyCheckInfoBean> list = this.customerServiceImpl.getDandelionCustomerList(objs);
            logger.info("IQB调试信息---蒲公英个人客户集合" + list);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-2784 FINANCE-2690 蒲公英个人客户信息查询
     * 
     * @param
     * @return Object
     * @throws
     * @Author chengzhen Create Date: 2018年1月8日 15:02:16
     */
    @ResponseBody
    @RequestMapping(value = "/getDandelionCustomerDetail", method = RequestMethod.POST)
    public Object getDandelionCustomerDetail(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---蒲公英个人客户明细入参" + JSONUtil.objToJson(objs));
            MyCheckInfoBean checkBean = this.customerServiceImpl.getDandelionCustomerDetail(objs);
            logger.info("IQB调试信息---蒲公英个人客户明细" + checkBean);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", checkBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
