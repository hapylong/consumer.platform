/*
 * 软件著作权：北京爱钱帮财富科技有限公司 项目名称：
 * 
 * NAME : WFReturnController.java
 * 
 * PURPOSE : 蒲公英流程接口回调类
 * 
 * AUTHOR : haojinlong
 * 
 * 
 * 创建日期: 2017年5月15日 HISTORY： 变更日期
 */
package com.iqb.consumer.manage.front.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.BizConstant.BizPledgeProcDefKeyConstant;
import com.iqb.consumer.common.constant.BizConstant.WFConst;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.constant.CommonConstant.CaseStatus;
import com.iqb.consumer.common.constant.CommonConstant.ProcessStatus;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;
import com.iqb.consumer.data.layer.bean.schedule.http.ApiRequestMessage;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.StatusFlowBean;
import com.iqb.consumer.data.layer.biz.CombinationQueryBiz;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.InstOrderLawBiz;
import com.iqb.consumer.data.layer.biz.carstatus.InstRemindPhoneBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.order.OverdueInfoBiz;
import com.iqb.consumer.data.layer.biz.pledge.PledgeInquiryBiz;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskAnalysisAllot;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskManager;
import com.iqb.consumer.data.layer.biz.trafficmanage.TrafficManageBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.manage.front.workflow.wfstructure.WorkFlowsStructure;
import com.iqb.consumer.service.layer.afterLoan.IAfterLoanService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.carstatus.CarStatusCenterService;
import com.iqb.consumer.service.layer.carstatus.InstRemindPhoneService;
import com.iqb.consumer.service.layer.creditorinfo.CreditorInfoService;
import com.iqb.consumer.service.layer.pledge.IPledgeSerivce;
import com.iqb.consumer.service.layer.pledge.PledgeInquiryWFService;
import com.iqb.consumer.service.layer.riskinfo.RiskInfoService;
import com.iqb.consumer.service.layer.sublet.SubletCenterService;
import com.iqb.consumer.service.layer.wfservice.StatusFlowService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * @author haojinlong 工作流共用流程接口回调类
 */
@Component
public class WFReturnController extends BaseService implements IWfProcTaskCallBackService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(WFReturnController.class);
    @Resource
    private ParamConfig paramConfig;
    @Resource
    private StatusFlowService statusFlowService;
    @Resource
    private IPledgeSerivce pledgeSerivceImpl;
    @Resource
    private CombinationQueryBiz combinationQueryBiz;
    @Autowired
    private SubletCenterService subletCenterServiceImpl;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private SysUserSession sysUserSession;
    @Autowired
    private ScheduleTaskManager scheduleTaskManager;
    @Autowired
    private ScheduleTaskAnalysisAllot scheduleTaskAnalysisAllot;
    @Autowired
    private RiskInfoService riskInfoService;
    @Autowired
    private CarStatusCenterService carStatusCenterServiceImpl;
    @Autowired
    private PledgeInquiryWFService pledgeInquiryWFService;
    @Resource
    private PledgeInquiryBiz pledgeInquiryBiz;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private CreditorInfoService creditorInfoService;
    @Autowired
    private BillInfoService billInfoService;
    @Resource
    private OverdueInfoBiz overdueInfoBiz;
    @Autowired
    private UserBeanBiz userBeanBiz;
    @Autowired
    private InstRemindPhoneBiz instRemindPhoneBiz;
    @Autowired
    private InstRemindPhoneService instRemindPhoneService;
    @Resource
    private IAfterLoanService afterLoanServiceImpl;
    @Autowired
    private InstOrderLawBiz instOrderLawBiz;
    @Autowired
    private TrafficManageBiz trafficManageBiz;

    private static final int ORDER_AMT = 150000;

    @SuppressWarnings("rawtypes")
    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        // 工作流处理后需要同步order表状态,根据约定的接口状态去维护riskStatus
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String procCurrTask = (String) procBizData.get("procCurrTask"); // 当前节点

        if (WfProcDealType.PROC_APPROVE.equalsIgnoreCase(dealType)) {
            try {
                String wfStatus = "0";
                String preAmtStatus = "0";
                String preAmt = "";
                String riskStatus = "0";
                String carStatus = "00";
                String backFlag = "0";
                String showContract = null;
                Map<String, Object> params = new HashMap<>();
                params.put("dealType", dealType);
                params.put("procCurrTask", procCurrTask);
                params.put("procApprStatus", procApprStatus);
                String processStatus = "5";// 贷后案件进度状态
                // 根据流程节点以及状态查询节点信息
                StatusFlowBean statusFlowBean = statusFlowService.selectOne(params);

                // 流程状态修改标识,0 表示默认执行流程 2项目初审并且有转租的情况下一次修改订单状态及其相关信息
                String flag = "0";

                // 通用流程设置(只涉及状态修改)
                if (statusFlowBean != null) {
                    wfStatus = statusFlowBean.getGoalWfStatus();
                    preAmtStatus = statusFlowBean.getPreAmtStatus();
                    riskStatus = statusFlowBean.getGoalRiskStatus();
                } else {
                    flag = "1";
                    if (procApprStatus.equals("1")) {
                        if (procCurrTask.equalsIgnoreCase("car_sale_finance")) {
                            carStatus = CommonConstant.CarStatus.STATUS_40;
                            processStatus = null;
                            JSONObject jo = new JSONObject();
                            jo.put("orderId", procBizId);
                            jo.put("finishTime", new Date());
                            orderService.finishBill(jo);
                        } else if (procCurrTask.equalsIgnoreCase("car_storage_gps")) {
                            carStatus = CommonConstant.CarStatus.STATUS_25;
                            processStatus = CommonConstant.ProcessStatus.ProcessStatus_10;
                        } else if (procCurrTask.equalsIgnoreCase("car_storage_confirm")) {
                            carStatus = CommonConstant.CarStatus.STATUS_30;
                            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
                        } else if (procCurrTask.equalsIgnoreCase("car_sublet_confirm")) {
                            carStatus = CommonConstant.CarStatus.STATUS_50;
                            processStatus = null;
                        } else if (procCurrTask.equalsIgnoreCase("car_return_fee")) {
                            carStatus = CommonConstant.CarStatus.STATUS_60;
                            processStatus = null;
                        } else if (procCurrTask.equalsIgnoreCase("car_out_risk")) {
                            carStatus = CommonConstant.CarStatus.STATUS_20;
                        } else if (procCurrTask.equalsIgnoreCase("loan_gps_check")) {
                            Map<String, String> map = loanGpsCheck(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        } else if (procCurrTask.equalsIgnoreCase("car_info_check")) {
                            Map<String, String> map = carInfoCheck(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        } else if (procCurrTask.equalsIgnoreCase("warrant_apply")) {
                            Map<String, String> map = warrantApply(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        } else if (procCurrTask.equalsIgnoreCase("accept_car")) {
                            Map<String, String> map = acceptCar(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        } else if (procCurrTask.equalsIgnoreCase("accept_car_check")) {
                            Map<String, String> map = acceptCarCheck(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        } else if (procCurrTask.equalsIgnoreCase("afterloan_leder_add")) {
                            flag = "2";
                            updateInstRemindPhone(procBizId);
                        } // 立案申请流程-庭前调解
                        else if (procCurrTask.equalsIgnoreCase("registerCase_mediate")) {
                            Map<String, String> map = registerCaseMediate(procBizData);
                            carStatus = null;
                            processStatus = map.get("processStatus");
                        } // 立案申请-财务确认
                        else if (procCurrTask.equalsIgnoreCase("registerCase_finance")) {
                            Map<String, String> map = registerCaseFinance(procBizData);
                            carStatus = map.get("carStatus");
                            processStatus = map.get("processStatus");
                        }
                        else {
                            flag = "2";
                        }
                    } else if (procApprStatus.equals("0")) {
                        if (procCurrTask.equalsIgnoreCase("car_return_risk")) {
                            carStatus = CommonConstant.CarStatus.STATUS_30;
                        } else if (procCurrTask.equalsIgnoreCase("car_sale_risk")) {
                            carStatus = CommonConstant.CarStatus.STATUS_30;
                        } else if (procCurrTask.equalsIgnoreCase("car_sale_comp")) {
                            carStatus = CommonConstant.CarStatus.STATUS_30;
                        }
                    } else {
                        flag = "2";
                    }
                    // 电话提醒 电催转贷后流程
                    if (procCurrTask.equalsIgnoreCase("urge_to_loan_check") ||
                            procCurrTask.equalsIgnoreCase("remind_to_loan_check")) {
                        flag = "2";
                        urgeToLoanCheck(procBizData);
                    }
                    // 车务管理-渠道审批
                    else if (procCurrTask.equalsIgnoreCase("car_insurance_channel")) {
                        carInsuranceChannel(procBizData);
                        flag = "2";
                    }
                }

                logger.debug("WFReturnController {} {}!", procCurrTask, getStringByStatus(procCurrTask));
                // 如果流程节点有特殊业务需要处理,在下面处理

                // 以租代售-门店预处理
                if (procCurrTask.equalsIgnoreCase("lease_pretreatment")) {
                    Map result2 = leasePretreatment(procBizId, procApprStatus);
                    logger.debug("调用风控结果:{}", result2);
                    // 以租代售 -人工风控or车帮风控
                } else if (procCurrTask.equalsIgnoreCase("lease_risk_department")) {
                    Map<String, String> map = leaseRiskDepartment(procBizData);
                    wfStatus = map.get("wfStatus");
                    preAmtStatus = map.get("preAmtStatus");
                    showContract = map.get("showContract");
                    riskStatus = map.get("riskStatus");
                    // 以租代售-抵质押物估价
                } else if (procCurrTask.equalsIgnoreCase("lease_price")) {
                    Map<String, String> map = leasePrice(procBizData);
                    wfStatus = map.get("wfStatus");
                    preAmtStatus = map.get("preAmtStatus");
                    backFlag = map.get("backFlag");
                    showContract = map.get("showContract");
                    riskStatus = map.get("riskStatus");
                    // 以租代售-项目初审
                } else if (procCurrTask.equalsIgnoreCase("lease_project")) {
                    flag = leaseProject(procBizData);
                    // 以租代售-门店额度确认
                } else if (procCurrTask.equalsIgnoreCase("lease_credit")) {
                    Map<String, String> map = leaseCredit(procBizData);
                    preAmtStatus = map.get("preAmtStatus");
                    // FINANCE-2867 电子合同：以租代购流程只有到项目信息维护节点在待签约合同列表页显示订单数据
                    showContract = map.get("showContract");
                    // 蒲公英行-外访组实地考察
                } else if (procCurrTask.equalsIgnoreCase("dandelion_investigate")) {
                    Map<String, String> map = dandelionInvestigate(procBizData);
                    wfStatus = map.get("wfStatus");
                    // 蒲公英行流程-人工风控
                } else if (procCurrTask.equalsIgnoreCase("dandelion_risk_department")) {
                    dandelionRiskDepartment(procBizData);
                    // 抵押车流程-门店预处理
                } else if (procCurrTask.equalsIgnoreCase("guaranty_pretreatment")) {
                    guarantyPretreatment(procBizId, procApprStatus);
                    // 抵押车流程-抵质押物估价--生成项目信息
                } else if (procCurrTask.equalsIgnoreCase("guaranty_price")) {
                    preAmtStatus = guarantyPrice(procBizId, procApprStatus);
                    // 质押车询价流程 -门店车价评估
                } else if (procCurrTask.equalsIgnoreCase("pledge_inquiry_pretreatment")) {
                    flag = "2";
                    pledgeInquiryPretreatment(procBizId, procBizData);
                    // 质押车询价流程 -车价复评
                } else if (procCurrTask.equalsIgnoreCase("pledge_inquiry_review")) {
                    flag = "2";
                    pledgeInquiryReview(procBizId, procApprStatus);
                    // 质押车进件流程 -车帮风控审批
                } else if (procCurrTask.equalsIgnoreCase(BizPledgeProcDefKeyConstant.PLEDGE_RISK_KEY)) {
                    pledgeRisk(procBizId, procApprStatus);
                    // 提前还款流程审核
                } else if (procCurrTask.startsWith("prepayment_")) {
                    flag = "3";
                    prepaymentProcess(procBizData);
                    // 车秒贷流程-项目信息维护
                } else if (procCurrTask.equalsIgnoreCase("credit_store")) {
                    creditStore(procBizData);
                 // 车主贷流程-门店风控审核
                } else if (procCurrTask.equalsIgnoreCase("carLoan_dept_risk")) {
                    carLoanDeptRisk(procBizData);
                    // 车主贷-风控部门终审
                } else if (procCurrTask.equalsIgnoreCase("carLoan_final_risk")) {
                    carLoanFinalRisk(procBizData);
                    // 提前还款结清-到账确认
                } else if (procCurrTask.equalsIgnoreCase("zg_prepayment_account_confirm_finance")
                        || procCurrTask.equalsIgnoreCase("zg_prepayment_account_confirm")
                        || procCurrTask.equalsIgnoreCase("prepayment_account_confirm")
                        || procCurrTask.equalsIgnoreCase("prepayment_account_confirm_finance")) {
                    flag = "3";
                    prepaymentProcess(procBizData);
                } else if (procCurrTask.equalsIgnoreCase("zg_prepayment_detail")
                        || procCurrTask.equalsIgnoreCase("zg_prepayment_remission_costdetails")
                        || procCurrTask.equalsIgnoreCase("zg_prepayment_control_auditfees")
                        || procCurrTask.equalsIgnoreCase("zg_prepayment_leader_auditfees")
                        || procCurrTask.equalsIgnoreCase("wechat_prepayment_pretreatment")
                        || procCurrTask.equalsIgnoreCase("prepayment_detail")
                        || procCurrTask.equalsIgnoreCase("prepayment_remission_costdetails")
                        || procCurrTask.equalsIgnoreCase("prepayment_control_auditfee")
                        || procCurrTask.equalsIgnoreCase("prepayment_leader_auditfees")) {
                    flag = "3";
                    doPrepaymentProcess(procBizData);
                }
                // 违约保证金结算流程-结算核实保证金结算明细
                else if (procCurrTask.equalsIgnoreCase("forfeit_balance_auditingdetail")) {
                    flag = "3";
                    auditingDetail(procBizData);
                }
                // 华益-周转贷-人工风控生成项目信息
                else if (procCurrTask.equalsIgnoreCase("huayi_risk_department")) {
                    huayiRiskDepartment(procBizData);
                }
                // 以租代售人车并行流程-门店预处理
                else if (procCurrTask.equalsIgnoreCase("new_lease_pretreatment")) {
                    Map<String, String> map = newLeasePretreatment(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }
                // 以租代售人车并行流程-风控部门审核2
                else if (procCurrTask.equalsIgnoreCase("new_lease_risk_department2")) {
                    Map<String, String> map = newLeaseDepartment2(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                    preAmtStatus = map.get("preAmtStatus");
                }
                // 以租代售人车并行流程-电核
                else if (procCurrTask.equalsIgnoreCase("phone_Audit")) {
                    Map<String, String> map = newPhoneAudit(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }
                // 以租代售人车并行流程-风控部门审核
                else if (procCurrTask.equalsIgnoreCase("new_lease_risk_department")) {
                    Map<String, String> map = newLeaseDepartment(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }
                // 以租代售人车并行流程-风控部车价评估-生成项目信息
                else if (procCurrTask.equalsIgnoreCase("new_lease_price")) {
                    Map<String, String> map = newLeasePrice(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                    backFlag = map.get("backFlag");
                }
                // 以租代售人车并行流程-门店额度确认
                else if (procCurrTask.equalsIgnoreCase("new_lease_credit")) {
                    Map<String, String> map = newLeaseCredit(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }

                // 以租代售人车并行流程-风控经理审核
                else if (procCurrTask.equalsIgnoreCase("new_lease_risk_manager")) {
                    Map<String, String> map = newLeaseRiskManager(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }

                // 以租代售人车并行流程-合同审核
                else if (procCurrTask.equalsIgnoreCase("new_lease_audit")) {
                    Map<String, String> map = newLeaseAudit(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }
                // 以租代售人车并行流程-收款确认
                else if (procCurrTask.equalsIgnoreCase("new_lease_finance")) {
                    Map<String, String> map = newLeaseFinance(procBizData);
                    wfStatus = map.get("wfStatus");
                    riskStatus = map.get("riskStatus");
                }
                // 以租代售人车并行流程-项目初审
                else if (procCurrTask.equalsIgnoreCase("new_lease_project")) {
                    flag = newLeaseProject(procBizData);
                }

                // 房融保流程-外访资料上传-发送资料到风控系统
                else if (procCurrTask.equalsIgnoreCase("house_loan_document")) {
                    houseLoanDocument(procBizData);
                }
                // 房融保流程-风控部门审核
                else if (procCurrTask.equalsIgnoreCase("house_loan_risk")) {
                    houseLoanRisk(procBizData);
                }

                // 修改工作流状态
                if (flag.equals("0")) {
                    updateWfStatus(procBizId, wfStatus, preAmtStatus, riskStatus, backFlag, preAmt, showContract);
                } else if (flag.equals("1")) {
                    // 修改车辆跟踪状态
                    updateCarTrack(procBizId, carStatus, processStatus);
                }
            } catch (Exception e) {
                logger.error("工作流回调更改订单状态错误", e);
            }
        } else if (WfProcDealType.PROC_COMMIT.equalsIgnoreCase(dealType)) {
            logger.debug("WFReturnController 启动工作流 {} {}!", procCurrTask, getStringByStatus(procCurrTask));
            try {
                // 车辆跟踪状态s
                String carStatus = "00";
                // 拖车入库
                if (procCurrTask.equalsIgnoreCase("car_storage_order")) {
                    carStatus = CommonConstant.CarStatus.STATUS_25;
                    // 车辆失联
                } else if (procCurrTask.equalsIgnoreCase("car_out_of_order")) {
                    carStatus = CommonConstant.CarStatus.STATUS_20;
                    // 车辆出售
                } else if (procCurrTask.equalsIgnoreCase("car_order")) {
                    carStatus = CommonConstant.CarStatus.STATUS_35;
                    // 车辆转租
                } else if (procCurrTask.equalsIgnoreCase("car_sublet_order")) {
                    carStatus = CommonConstant.CarStatus.STATUS_45;
                    // 车辆返还
                } else if (procCurrTask.equalsIgnoreCase("car_return_order")) {
                    carStatus = CommonConstant.CarStatus.STATUS_55;
                }
                updateCarTrack(procBizId, carStatus, null);
            } catch (Exception e) {
                logger.error("工作流回调更改车辆跟踪状态错误", e);
            }
        } else if (WfProcDealType.PROC_END.equalsIgnoreCase(dealType)) {
            /** 车辆跟踪流程节点信息 **/
            Map<String, String> map = new HashMap<>();
            map.put("car_storage_apply", "car_storage_apply");
            map.put("car_storage_gps", "car_storage_gps");
            map.put("car_out_apply", "car_out_apply");
            map.put("car_out_risk", "car_out_risk");
            map.put("car_storage_confirm", "car_storage_confirm");

            Map<String, String> map2 = new HashMap<>();
            map2.put("car_sale_apply", "car_sale_apply");
            map2.put("car_sale_price", "car_sale_price");
            map2.put("car_sale_operate", "car_sale_operate");
            map2.put("car_sale_risk", "car_sale_risk");
            map2.put("car_sale_comp", "car_sale_comp");
            map2.put("car_sale_finance", "car_sale_finance");
            map2.put("car_sublet_apply", "car_sublet_apply");
            map2.put("car_sublet_confirm", "car_sublet_confirm");
            map2.put("car_return_apply", "car_return_apply");
            map2.put("car_return_risk", "car_return_risk");
            map2.put("car_return_fee", "car_return_fee");
            // 提请结清流程
            Map<String, String> map3 = new HashMap<>();
            map3.put("zg_prepayment_detail", "zg_prepayment_detail");
            map3.put("zg_prepayment_remission_costdetails", "zg_prepayment_remission_costdetails");
            map3.put("zg_prepayment_control_auditfees", "zg_prepayment_control_auditfees");
            map3.put("zg_prepayment_leader_auditfees", "zg_prepayment_leader_auditfees");
            map3.put("zg_prepayment_payment", "zg_prepayment_payment");
            map3.put("zg_prepayment_account_confirm_finance", "zg_prepayment_account_confirm_finance");
            map3.put("zg_prepayment_account_confirm", "zg_prepayment_account_confirm");
            map3.put("wechat_prepayment_pretreatment", "wechat_prepayment_pretreatment");
            map3.put("prepayment_detail", "prepayment_detail");
            map3.put("prepayment_remission_costdetails", "prepayment_remission_costdetails");
            map3.put("prepayment_control_auditfee", "prepayment_control_auditfee");
            map3.put("prepayment_payment", "prepayment_payment");
            map3.put("prepayment_leader_auditfees", "prepayment_leader_auditfees");
            map3.put("prepayment_account_confirm_finance", "prepayment_account_confirm_finance");
            map3.put("prepayment_account_confirm", "prepayment_account_confirm");
            map3.put("prepayment_details_audit", "prepayment_details_audit");

            // 以租代购流程-终止操作修改支付状态为0
            Map<String, String> map4 = new HashMap<>();
            map4.put("lease_price", "lease_price");
            map4.put("lease_credit", "lease_credit");
            map4.put("lease_store", "lease_store");
            map4.put("lease_operate_audit", "lease_operate_audit");
            map4.put("lease_audit", "lease_audit");
            map4.put("lease_fee", "lease_fee");
            map4.put("lease_finance", "lease_finance");
            map4.put("lease_gps", "lease_gps");
            map4.put("lease_project", "lease_project");
            // 违约保证金结算流程-结算核实保证金结算明细
            Map<String, String> map5 = new HashMap<>();
            map5.put("forfeit_balance_auditingdetail", "forfeit_balance_auditingdetail");
            map5.put("forfeit_balance_finance", "forfeit_balance_finance");

            /** 贷后处置流程节点信息 **/
            Map<String, String> map6 = new HashMap<>();
            map6.put("loan_gps_check", "loan_gps_check");
            map6.put("car_info_check", "car_info_check");
            map6.put("warrant_apply", "warrant_apply");
            map6.put("accept_car", "accept_car");
            map6.put("accept_car_check", "accept_car_check");
            // 外包车辆回款流程退回处理
            Map<String, String> map7 = new HashMap<>();
            map7.put("received_payments_apply", "received_payments_apply");
            map7.put("received_payments_finance", "received_payments_finance");
            map7.put("received_payments_operate", "received_payments_operate");

            // 立案申请流程终止处理
            Map<String, String> map8 = new HashMap<>();
            map8.put("registerCase_mediate", "registerCase_mediate");
            map8.put("registerCase_finance", "registerCase_finance");

            // 车务流程终止处理
            Map<String, String> map9 = new HashMap<>();
            map9.put("car_insurance_apply", "car_insurance_apply");
            map9.put("car_insurance_channel", "car_insurance_channel");

            /** 车辆跟踪流程终止操作不更新主订单状态 **/
            if (!map.containsKey(procCurrTask) && !map2.containsKey(procCurrTask) && !map5.containsKey(procCurrTask)
                    && !map6.containsKey(procCurrTask) && !map7.containsKey(procCurrTask)
                    && !map8.containsKey(procCurrTask) && !map9.containsKey(procCurrTask)) {
                updateWfStatus(procBizId, CommonConstant.WFStatusConst.PENALTY_DERATE_APPLY_STATUS_0, null,
                        CommonConstant.RiskStatusConst.RiskStatus_11, null, null, null);
            }
            if (map.containsKey(procCurrTask)) {
                updateCarProcess(procBizId);
            }
            if (map3.containsKey(procCurrTask)) {
                updateSettleStatus(procBizId);
            }
            if (map4.containsKey(procCurrTask)) {
                updateWfStatus(procBizId, null, CommonConstant.RiskStatusConst.RiskStatus_0, null, null, null, null);
            }
            if (map5.containsKey(procCurrTask)) {
                auditingDetail(procBizData);
            }
            if (map6.containsKey(procCurrTask)) {
                updateCarTrack(procBizId, null, ProcessStatus.ProcessStatus_05);
            }
            if (map7.containsKey(procCurrTask)) {
                procBizId = procBizId.substring(2, procBizId.length());
                updateCarTrack(procBizId, null, ProcessStatus.ProcessStatus_15);
            }
            if (map8.containsKey(procCurrTask)) {
                updateOrderLawStatus(procBizId, CaseStatus.CaseStatus_15);
            }
            if (map9.containsKey(procCurrTask)) {
                carInsuranceEnd(procBizId);
            }
        } else if (WfProcDealType.PROC_RETRIEVE.equalsIgnoreCase(dealType)) {
            /** 中阁提前还款审批流程节点信息 **/
            Map<String, String> map = new HashMap<>();
            map.put("zg_prepayment_pretreatment", "zg_prepayment_pretreatment");

            if (map.containsKey(procCurrTask)) {
                updateSettleStatusForRetrieve(procBizId);
            }
        }

    }

    /**
     * 更新贷后车辆为待外包处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    private void updateCarProcess(String procBizId) {
        JSONObject json = new JSONObject();
        json.put("processMethod", 1);
        json.put("processStatus", 15);
        json.put("orderId", procBizId);
        afterLoanServiceImpl.updateManagerCarInfo(json);
    }

    /**
     * 并行流程汇聚节点回调类
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月19日
     */
    public void parallelAfter(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("--并行汇聚节点回调处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String procBizId = (String) procBizData.get("procBizId"); // 订单号

        String wfStatus = "5";// 待项目信息维护
        String preAmtStatus = "0";
        String showContract = "1";
        String riskStatus = "0";
        String backFlag = "0";
        String preAmt = "";
        CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
        // 启动车秒贷流程
        if ("1".equals(procApprStatus)) {
            // 车秒贷业务处理
            OrderBean obx = orderBiz.selByOrderId(procBizId + "X");
            if (obx != null) {
                if (obx != null && new BigDecimal(obx.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                    preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
                }

                BigDecimal amt = obx.getPreAmt() == null ? BigDecimal.ZERO : new BigDecimal(obx.getPreAmt());
                orderBiz.updatePledgeWfStatus(procBizId + "X", String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED),
                        null, preAmtStatus, null, null, amt.toString(),
                        null);
            }
            startWF(procBizId);
        }

        BigDecimal needAmt =
                BigDecimalUtil.sub(new BigDecimal(cqb.getPreAmt()), cqb.getReceivedPreAmt() == null
                        ? BigDecimal.ZERO
                        : new BigDecimal(cqb.getReceivedPreAmt()));
        if (needAmt.compareTo(BigDecimal.ZERO) > 0) {
            preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
            noticeSend(procBizId, 1);
        }
        this.updateWfStatus(procBizId, wfStatus, preAmtStatus, riskStatus, backFlag, preAmt, showContract);
    }

    /**
     * 对车辆追踪的流程如果终止，修改车辆状态 1.贷后处置中拖车入库/车辆失联流程拒绝或流程终止后，车辆状态为贷后处置中，订单在贷后处置中显示
     * 2.车辆出库中出售/转租/返还流程拒绝或终止后，车辆状态为已入库，订单在车辆出库中显示
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月14日
     */
    @Override
    public void before(String arg0, Map<String, Object> arg1) throws IqbException {
        String dealType = arg0;
        String procCurrTask = (String) arg1.get("procCurrTask");
        String procBizId = (String) arg1.get("procBizId");
        /** 车辆跟踪流程节点信息 **/
        Map<String, String> map = new HashMap<>();
        map.put("car_storage_apply", "car_storage_apply");
        map.put("car_storage_gps", "car_storage_gps");
        map.put("car_out_apply", "car_out_apply");
        map.put("car_out_risk", "car_out_risk");

        Map<String, String> map2 = new HashMap<>();
        map2.put("car_sale_apply", "car_sale_apply");
        map2.put("car_sale_price", "car_sale_price");
        map2.put("car_sale_operate", "car_sale_operate");
        map2.put("car_sale_risk", "car_sale_risk");
        map2.put("car_sale_comp", "car_sale_comp");
        map2.put("car_sale_finance", "car_sale_finance");
        map2.put("car_sublet_apply", "car_sublet_apply");
        map2.put("car_sublet_confirm", "car_sublet_confirm");
        map2.put("car_return_apply", "car_return_apply");
        map2.put("car_return_risk", "car_return_risk");
        map2.put("car_return_fee", "car_return_fee");
        /** 贷后处置流程节点信息 **/
        Map<String, String> map3 = new HashMap<>();
        map3.put("loan_gps_check", "loan_gps_check");
        map3.put("car_info_check", "car_info_check");
        map3.put("warrant_apply", "warrant_apply");
        map3.put("accept_car", "accept_car");
        map3.put("accept_car_check", "accept_car_check");

        if (WfProcDealType.PROC_END.equalsIgnoreCase(dealType)) {
            if (map2.containsKey(procCurrTask)) {
                updateCarTrack(procBizId, CommonConstant.CarStatus.STATUS_30, null);
            }
        }
    }

    /**
     * 修改工作流状态
     * 
     * @param flag
     * @param procBizId
     * @param wfStatus
     * @param preAmtStatus
     * @param riskStatus
     * @param backFlag
     * @param preAmt
     * @return LinkedHashMap
     */
    private void updateWfStatus(String procBizId, String wfStatus, String preAmtStatus, String riskStatus,
            String backFlag, String preAmt, String showContract) {
        orderBiz.updatePledgeWfStatus(procBizId, riskStatus, wfStatus, preAmtStatus, "", backFlag, preAmt, showContract);
    }

    /**
     * 流程节点状态转义 0 拒绝 2 同意 2 退回
     * 
     * @param status
     * @return String
     */
    private String getStringByStatus(String status) {
        String returnInfo = "";
        if (status.equals("0")) {
            returnInfo = "refused";
        } else if (status.equals("1")) {
            returnInfo = "passed";
        } else if (status.equals("2")) {
            returnInfo = "back";
        }
        return returnInfo;
    }

    /**
     * 以租代售 门店预处理
     * 
     * @param orderId
     * @return LinkedHashMap
     */
    @SuppressWarnings({"rawtypes"})
    private Map leasePretreatment(String orderId, String procApprStatus) {
        logger.debug("--以租代售流程门店预处理-分控调用开始--");
        Map<String, String> params2 = new HashMap<>();
        params2.put("orderId", orderId);
        Map result = new HashMap<>();
        if ("1".equals(procApprStatus)) {
            result = riskInfoService.send2Risk(params2);
        }
        logger.debug("--以租代售流程门店预处理分控调用结束--{}", JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 以租代售 人工风控or车帮风控
     * 
     * @param procBizData
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> leaseRiskDepartment(Map<String, Object> procBizData) {
        logger.debug("--以租代售流程人工分控调用开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
        String isSublet = (String) procBizData.get("isSublet"); // 是否转租

        String wfStatus = "0";
        String preAmtStatus = "0";
        String riskStatus = "0";
        Map<String, String> map = new HashMap<>();
        if ("1".equals(procApprStatus)) {

            if (isSublet.equalsIgnoreCase("true")) {
                /**
                 * FINANCE-3097 转租业务订单状态为审核通过，待签约合同列表无订单信息
                 */
                map.put("showContract", "1");

                /**
                 * FINANCE-3030 以租代购：转租人工风控审核通过后，生成项目名称/担保公司/担保法人及债权人信息
                 */
                Map<String, String> params = new HashMap<>();
                params.put("orderId", procBizId);
                // 生成项目信息
                LinkedHashMap result =
                        SendHttpsUtil
                                .postMsg4GetMap(
                                        paramConfig.getManagerWfreturnYZDSurl(),
                                        params);
                logger.debug("---以租代购---人工风控---生成项目信息  passed! result={}", result);

                // 转租订单,首付款大于0,待支付，否则支付完成。
                wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                if (new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                    preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
                } else {
                    preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_OVER_PAY);
                }
            } else {
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                wfStatus = WorkFlowsStructure.WF_WAIT_GOOD_EVALUATE;
            }
        } else if ("2".equals(procApprStatus)) {
            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
        } else if ("0".equals(procApprStatus)) {
            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_REFUSE);
        }
        map.put("riskStatus", riskStatus);
        map.put("wfStatus", wfStatus);
        map.put("preAmtStatus", preAmtStatus);
        logger.debug("--以租代售流程人工分控调用 结束--{}", map);
        return map;
    }

    /**
     * 以租代售-抵质押物估价
     * 
     * @param procBizData
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> leasePrice(Map<String, Object> procBizData) {
        logger.debug("--以租代售-抵质押物估价调用开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String backFlag = "0";
        CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
        Map<String, String> params = new HashMap<>();
        params.put("orderId", procBizId);

        String wfStatus = "0";
        String preAmtStatus = cqb.getPreAmtStatus() + "";
        String riskStatus = "0";
        Map<String, String> map = new HashMap<>();
        if (procApprStatus.equals("1")) {
            // 生成项目信息
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    // "http://101.201.151.38:8088/consumer.manage.front/creditorInfo/WFReturn/YZDS",
                                    paramConfig.getManagerWfreturnYZDSurl(),
                                    params);
            logger.debug("WFReturnController 生成项目信息  passed! result={}", result);
            orderBiz.updateSpecialTimeByOid(procBizId);
            // 判断是否车秒贷，如果车秒贷状态修改为44，否则修改为5
            /** useCreditLoan 信用贷 0 否 1 是 **/
            String useCreditLoan = (String) procBizData.get("useCreditLoan");
            int orderAmt = (Integer) procBizData.get("orderAmt");
            int carAmt = (Integer) procBizData.get("carAmt");
            if (useCreditLoan.equals("1")) {
                wfStatus = WorkFlowsStructure.WF_WAIT_CREDIT_CONFIRM;
                riskStatus = "2";
            } else {
                if (orderAmt != carAmt) {
                    wfStatus = WorkFlowsStructure.WF_WAIT_CREDIT_CONFIRM;
                    riskStatus = "2";
                } else {
                    wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                    // FINANCE-2867 电子合同：以租代购流程只有到项目信息维护节点在待签约合同列表页显示订单数据
                    map.put("showContract", "1");
                }

                /** task1，以租代售无车秒贷 在通过抵质押估价后，判断预支付金额大于0，将订单金额修改为待支付 **/
                // 用户应该支付的预付款金额 FINANCE-2496 以租代购：抵质押物估价节点审核通过后，用户已支付，流程主动撤回，支付状态又变成待支付
                BigDecimal needAmt =
                        BigDecimalUtil.sub(new BigDecimal(cqb.getPreAmt()), cqb.getReceivedPreAmt() == null
                                ? BigDecimal.ZERO
                                : new BigDecimal(cqb.getReceivedPreAmt()));
                if (needAmt.compareTo(BigDecimal.ZERO) > 0) {
                    preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
                    noticeSend(procBizId, 1);
                }
            }
        } else if (procApprStatus.equals("2")) {
            backFlag = "1";
            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
        } else {
            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_REFUSE);
        }

        map.put("wfStatus", wfStatus);
        map.put("backFlag", backFlag);
        map.put("preAmtStatus", preAmtStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-抵质押物估价调用结束--{}", map);
        return map;
    }

    /**
     * 以租代售-项目初审
     * 
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    private String leaseProject(Map<String, Object> procBizData) {
        logger.debug("--以租代售-项目初审调用开始--{}", procBizData);
        String flag = "0";
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        if ("1".equals(procApprStatus)) {
            String wfStatus = WorkFlowsStructure.WF_OVER_FINISHED;
            logger.debug("WFReturnController lease_project 项目初审---是否转租isSublet--{}",
                    procBizData.get("isSublet"));
            String isSublet = (String) procBizData.get("isSublet"); // 是否转租
            if (isSublet != null && isSublet.equalsIgnoreCase("true")) {
                flag = "2";
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", procBizId);
                params.put("wfStatus", wfStatus);
                Map result;
                try {
                    result = subletCenterServiceImpl.getSubletInfo(params);
                    logger.debug("转租结果:{}", result);
                } catch (GenerallyException e) {
                    logger.error("工作流回调更改订单状态错误", e);
                }
            }
            logger.debug("WFReturnController lease_project passed!");
        }
        logger.debug("--以租代售-项目初审调用结束--{}", flag);
        return flag;
    }

    /**
     * 
     * Description:以租代售-门店额度确认
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    private Map<String, String> leaseCredit(Map<String, Object> procBizData) throws IqbException {
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        /** FINANCE-2053 车秒贷状态修改 @author adam **/
        CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
        String preAmtStatus = "0";
        Map<String, String> map = new HashMap<>();
        if ("1".equals(procApprStatus)) {
            /** task2，以租代售含车秒贷，在门店金额确认通过后，判断预支付金额大于0，将订单金额修改为待支付 **/
            if (cqb != null && new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
            }
            OrderBean obx = orderBiz.selByOrderId(procBizId + "X");
            if (obx != null) {
                BigDecimal margin =
                        StringUtil.isEmpty(obx.getMargin()) ? BigDecimal.ZERO : new BigDecimal(obx.getMargin());
                BigDecimal serviceFee =
                        StringUtil.isEmpty(obx.getServiceFee()) ? BigDecimal.ZERO : new BigDecimal(obx.getServiceFee());
                BigDecimal preAmt = obx.getPreAmt() == null ? BigDecimal.ZERO : new BigDecimal(obx.getPreAmt());
                orderBiz.updatePledgeWfStatus(procBizId + "X", String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED),
                        null, preAmtStatus, null, null, preAmt.toString(),
                        null);
            }

            map.put("preAmtStatus", preAmtStatus);
            // FINANCE-2867 电子合同：以租代购流程只有到项目信息维护节点在待签约合同列表页显示订单数据
            map.put("showContract", "1");
            startWF(procBizId);
        }
        return map;
    }

    /**
     * 蒲公英行流程-外访组实地考察
     * 
     * @param orderId
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> dandelionInvestigate(Map<String, Object> procBizData) {
        logger.info("WFReturnController 蒲公英行流程-外访组实地考察  start -- {}", procBizData);
        String orderId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String orderFlag = "2"; // 订单标识 1 老订单 2 新订单
        String isBack = String.valueOf(procBizData.get("backFlag")); // 是否退回标识 0 否 1 是
        String wfStatus = "00";
        Map<String, String> map = new HashMap<>();
        if ("1".equals(procApprStatus)) {
            if (isBack.equals("0")) {
                if (orderFlag.equals("2")) {
                    dandelionRisk(orderId, procApprStatus);
                    wfStatus = WorkFlowsStructure.WF_WAIT_RISK_APPROVE;
                } else {
                    wfStatus = WorkFlowsStructure.WF_WAIT_MAN_RISK;
                }
            } else {
                wfStatus = WorkFlowsStructure.WF_WAIT_MAN_RISK;
            }
            Map<String, String> params = new HashMap<>();
            params.put("orderId", orderId);
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    // "http://101.201.151.38:8088/consumer.manage.front/creditorInfo/WFReturn/PGYH",
                                    paramConfig.getManagerWfreturnPGYHurl(),
                                    params);
            logger.info("WFReturnController dandelion_investigate passed!{}", result);
        }
        map.put("wfStatus", wfStatus);
        return map;
    }

    /**
     * 蒲公英行-客服派单-提交读脉
     * 
     * @param orderId
     * @return Map<String,Object>
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Object> dandelionRisk(String orderId, String procApprStatus) {
        logger.info("WFReturnController 蒲公英行-客服派单-提交读脉  start! {}", orderId);
        Map<String, Object> map = new HashMap<>();
        if ("1".equals(procApprStatus)) {
            Map<String, String> params2 = new HashMap<>();
            params2.put("orderId", orderId);
            Map result2 = riskInfoService.send2RiskForDandelion(params2);
            logger.debug("调用风控结果:{}", result2);
            map.put("result", result2);
            logger.info("WFReturnController 蒲公英行-客服派单-提交读脉 passed!");
        }
        return map;
    }

    /**
     * 启动工作流
     * 
     * @param orderId
     * @return
     * @throws IqbException LinkedHashMap
     */
    @SuppressWarnings("rawtypes")
    private LinkedHashMap startWF(String orderId)
            throws IqbException {
        LinkedHashMap responseMap = null;
        OrderBean orderBean = orderBiz.selByOrderId(orderId + "X");
        if (orderBean != null) {
            double amt = orderBean.getOrderAmt() == null ? 0d : Double.parseDouble(orderBean.getOrderAmt());
            if (amt <= 0) {// 不走工作流
                logger.debug("订单{}无须走车秒贷工作流", orderId);
                return null;
            }
            UserBean userBean = userBeanBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    userBean.getRealName() + ";" + merchantBean.getMerchantFullName() + ";"
                            + orderBean.getOrderName() + ";" + orderBean.getRegId() + ";" + orderBean.getOrderAmt()
                            + ";" + orderBean.getOrderItems();// 摘要添加手机号

            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), orderBean.getWfStatus());

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", orderBean.getRegId());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderId + "X");
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", orderBean.getOrderAmt());
            // hmBizData.put("baseUrl", commonParamConfig.getSelfCallURL());

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = wfConfig.getStartWfurl();
            // 发送Https请求

            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new RuntimeException("工作流接口交易失败");
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        }
        return responseMap;
    }

    /**
     * 抵押车流程--门店预处理 Description:
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map guarantyPretreatment(String orderId, String procApprStatus) {
        logger.debug("--抵押车流程--门店预处理-调用开始--orderId = {},procApprStatus = {}", orderId, procApprStatus);
        Map<String, String> params = new HashMap<>();
        Map result = new HashMap<>();
        params.put("orderId", orderId);
        if ("1".equals(procApprStatus)) {
            result = riskInfoService.send2Risk4Pledge(params);
        }
        logger.debug("--抵押车流程--门店预处理-调用结束--", result);
        return result;
    }

    /**
     * 
     * Description:抵押车流程-抵押物估价-生成项目信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String guarantyPrice(String orderId, String procApprStatus) {
        logger.debug("--抵押车流程--抵押物估价-调用开始--orderId = {},procApprStatus = {}", orderId, procApprStatus);
        String preAmtStatus = "0";
        if ("1".equals(procApprStatus)) {
            CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(orderId);
            if (new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
            }
            Map<String, String> params = new HashMap<>();
            params.put("orderId", orderId);
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getManagerWfreturnDYurl(),
                                    params);
            logger.debug("--抵押车流程--抵押物估价-生成项目信息结果 {}", result);
            // 调度处理
            orderBiz.updateSpecialTimeByOid(orderId);
            noticeSend(orderId, 1);
        }
        return preAmtStatus;
    }

    /**
     * 
     * Description:流程节点状态变更通知
     * 
     * @param objs
     * @param request
     * @return
     */
    public void noticeSend(String procBizId, int status) {
        try {
            OrderBean ob = orderBiz.selByOrderId(procBizId);

            String url = ob.getOrderRemark();
            boolean save = scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
            ApiRequestMessage data = new ApiRequestMessage();
            data.setOrderId(ob.getOrderId());
            data.setStatus(status);
            if (!StringUtil.isNull(url)) {
                scheduleTaskAnalysisAllot.send(data.toString(), url,
                        ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
            } else {
                logger.error("WFReturnController.noticeSend---订单 {} 没有可执行的url--{}", procBizId, url);
            }

        } catch (Throwable e) {
            logger.error("WFReturnController.noticeSend exception :", e);
        }
    }

    /**
     * 
     * Description:修改车辆跟踪状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月12日
     */
    public void updateCarTrack(String procBizId, String carStatus, String processStatus) {
        try {
            Map<String, String> params = new HashMap<>();
            String prefix = procBizId.substring(0, 2);
            if (prefix.equals("FW")) {
                JSONObject objs = new JSONObject();
                objs.put("caseId", procBizId);
                InstOrderLawBean instOrderLawBean = afterLoanServiceImpl.getInstOrderLawnInfoByCaseId(objs);
                procBizId = instOrderLawBean.getOrderId();
            }
            params.put("orderId", procBizId);
            params.put("processStatus", processStatus);
            params.put("status", carStatus);
            int result = carStatusCenterServiceImpl.updateStatusByOrderId(params);
            logger.debug("WFReturnController 车辆跟踪流程-修改车辆状态- {}!", result);
        } catch (Exception e) {
            logger.debug("WFReturnController 车辆跟踪流程-修改车辆状态报错- {}!", e.getMessage());
        }
    }

    /**
     * 
     * Description:蒲公英行流程 -人工风控退回 -设置订单为退回
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月17日
     */
    public void dandelionRiskDepartment(Map<String, Object> procBizData) {
        logger.info("--dandelionRiskDepartment--蒲公英行流程 -人工风控退回 -设置订单为退回--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        if (procApprStatus.equals("2")) {
            int result = orderBiz.updateInstOrderBackFlag(procBizId, "1");
            logger.info("--dandelionRiskDepartment--蒲公英行流程 -人工风控退回 -设置订单为退回结果--{}", result);
        }
    }

    /**
     * 
     * Description:质押车询价流程 -门店车价评估
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月31日
     */
    public void pledgeInquiryPretreatment(String procBizId, Map<String, Object> procBizData) throws IqbException {
        logger.info("--pledgeInquiryPretreatment--质押车询价流程 -门店车价评估 -回调开始--{}", procBizData);
        String pledgeInquiryInfo = ObjectUtils.toString(procBizData.get("pledgeInquiryInfo"));
        PledgeInfoBean pledgeInfoBean = new PledgeInfoBean();
        try {
            pledgeInfoBean = BeanUtil.toJavaObject(pledgeInquiryInfo, PledgeInfoBean.class);
        } catch (Exception e) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000002);
        }
        /** 提交成功，数据入库 **/
        JSONObject object = new JSONObject();
        object.put("orderId", procBizId);
        object.put("wfStatus", CommonConstant.WFStatusConst.PENALTY_DERATE_APPLY_STATUS_12);
        object.put("applyAmt", pledgeInfoBean.getApplyAmt().multiply(BigDecimal.valueOf(10000)));
        object.put("assessPrice", String.valueOf((new BigDecimal(pledgeInfoBean.getAssessPrice()))
                .multiply(BigDecimal.valueOf(10000))));
        object.put("remark", pledgeInfoBean.getRemark());
        pledgeInquiryWFService.after(object);
        logger.info("--pledgeInquiryPretreatment--质押车询价流程 -门店车价评估 -回调结束--");
    }

    /**
     * 
     * Description:质押车询价流程 -车价复评
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月31日
     */
    public void pledgeInquiryReview(String procBizId, String procApprStatus) {
        logger.info("--pledgeInquiryReview--质押车询价流程 -车价复评 -回调开始--{}", procBizId);
        Map<String, String> params = new HashMap<>();
        params.put("orderId", procBizId);
        params.put("status", CommonConstant.pledgeStatus.PLEDGESTATUS_2);
        // 退回状态处理
        if ("2".equals(procApprStatus)) {
            params.put("status", CommonConstant.pledgeStatus.PLEDGESTATUS_1);
        } else if ("0".equals(procApprStatus)) {
            params.put("status", CommonConstant.pledgeStatus.PLEDGESTATUS_5);
        }
        pledgeInquiryBiz.updatePledgeStatus(params);
        logger.info("--pledgeInquiryReview--质押车询价流程 -车价复评 -回调结束--");
    }

    /**
     * 
     * Description:质押车进件流程 -车帮风控审批
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月31日
     */
    public void pledgeRisk(String procBizId, String procApprStatus) throws IqbException {
        logger.info("--pledgeRisk--质押车进件流程 -车帮风控审批-回调开始--{}", procBizId);
        PledgeInfoBean bean = new PledgeInfoBean();
        bean.setOrderId(procBizId);
        if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
            logger.info("--pledgeRisk--质押车进件流程 -车帮风控审批-生成项目信息--{}");
            this.pledgeSerivceImpl.dealRiskWFReturn(procBizId);
        }
        if (WFConst.PROCAPPRSTATUS_REFUSE.equals(procApprStatus)) {
            bean.setStatus(CommonConstant.pledgeStatus.PLEDGESTATUS_2);
            this.pledgeSerivceImpl.updatePledgeStatus(bean);
        }
        logger.info("--pledgeRisk--质押车进件流程 -车帮风控审批-回调结束--{}");
    }

    /**
     * 
     * Description:提前还款流程处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月22日
     */
    public void prepaymentProcess(Map<String, Object> procBizData) {
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String procEnded = (String) procBizData.get("procEnded"); // 流程是否结束，1：是；0：否
        if (procBizId.contains("-") && procBizId.indexOf("-") > 8) {
            procBizId = procBizId.substring(0, procBizId.indexOf("-"));
        } else {
            procBizId = procBizId.substring(0, procBizId.lastIndexOf("-"));
        }

        Map<String, Object> params = null;

        // 流程审批
        if ("0".equals(procApprStatus)) { // 拒绝流程结束
            logger.info("订单:{}流程拒绝", procBizId);
            params = new HashMap<>();
            params.put("orderId", procBizId);
            params.put("settleStatus", 3);
            settleApplyBeanBiz.updateSettleStatus(params);
        }
        if ("1".equals(procApprStatus) && "1".equals(procEnded)) { // 流程通过并结束
            logger.info("订单:{}流程通过并结束", procBizId);
            params = new HashMap<>();
            params.put("orderId", procBizId);
            params.put("settleStatus", 2);
            settleApplyBeanBiz.updateSettleStatus(params);

            // 账单平账
            Map<String, Object> resultMap = allClearPay(procBizData);
            if (!CollectionUtils.isEmpty(resultMap)) {
                if (((String) resultMap.get("retCode")).equalsIgnoreCase("success")) {
                    // 修改订单状态为已结清
                    orderBiz.updateStatus(procBizId, String.valueOf(10));
                }

            }
        }
    }
    /**
     * 
     * Description:车秒贷-项目信息维护生成项目信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月20日
     */
    @SuppressWarnings("rawtypes")
    private void creditStore(Map<String, Object> procBizData) {
        logger.debug("--车秒贷-项目信息维护调用开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        Map<String, String> params = new HashMap<>();
        params.put("orderId", procBizId);
        if (procApprStatus.equals("1")) {
            // 生成项目信息
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getCmdCopyInfoUrl(),
                                    params);
            logger.debug("WFReturnController 生成项目信息  passed! result={}", result);
        }
        logger.debug("--车秒贷-项目信息维护调用结束--");
    }

    /**
     * 
     * Description:车主贷-门店风控审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    public Map<String, Object> carLoanDeptRisk(Map<String, Object> procBizData) {
        logger.debug("--车主贷-门店风控审核开始--{}", procBizData);
        Map<String, Object> result = new HashMap<>();
        Map<String, String> params2 = new HashMap<>();

        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        params2.put("orderId", procBizId);

        if ("1".equals(procApprStatus)) {
            result = riskInfoService.send3Risk(params2);
        }
        logger.debug("--车主贷-门店风控审核结束--{}", JSONObject.toJSONString(result));

        return result;
    }

    /**
     * Description:车主贷-风控终审
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    private void carLoanFinalRisk(Map<String, Object> procBizData) {
        String orderId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        logger.debug("--车主贷-风控终审-调用开始--orderId = {},procApprStatus = {}", orderId, procApprStatus);
        if ("1".equals(procApprStatus)) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("type", "DY");
            String result = creditorInfoService.createProjectName(map);
            logger.debug("--车主贷-风控终审-生成项目信息结果 {}", result);
        }
    }

    /**
     * 
     * Description:提前结清流程-拒绝处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月4日
     */
    public void doPrepaymentProcess(Map<String, Object> procBizData) {
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        if (procBizId.contains("-") && procBizId.indexOf("-") > 8) {
            procBizId = procBizId.substring(0, procBizId.indexOf("-"));
        } else {
            procBizId = procBizId.substring(0, procBizId.lastIndexOf("-"));
        }
        Map<String, Object> params = null;

        // 流程审批
        if ("0".equals(procApprStatus)) { // 拒绝流程结束
            logger.info("订单:{}流程拒绝", procBizId);
            params = new HashMap<>();
            params.put("orderId", procBizId);
            params.put("settleStatus", 3);
            settleApplyBeanBiz.updateSettleStatus(params);
        }
    }

    /**
     * 
     * Description:更新还款代扣信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public void updateSettleStatus(String orderId) {
        String id = null;
        if (orderId.contains("-") && orderId.indexOf("-") > 8) {
            // 新订单号
            id = orderId.substring(orderId.indexOf("-") + 1, orderId.length());
            orderId = orderId.substring(0, orderId.lastIndexOf("-"));
        } else {
            // 旧订单号
            id = orderId.substring(orderId.lastIndexOf("-") + 1, orderId.length());
            orderId = orderId.substring(0, orderId.lastIndexOf("-"));
        }

        SettleApplyBean bean = new SettleApplyBean();
        bean.setOrderId(orderId);
        bean.setId(Long.parseLong(id));
        bean.setFinalOverdueAmt(BigDecimal.ZERO);
        bean.setSettleStatus(4);

        settleApplyBeanBiz.updateSettleBean(bean);
    }

    /**
     * 
     * Description:中阁提前还款审批流程-门店审核岗撤回处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    @SuppressWarnings("unused")
    public void updateSettleStatusForRetrieve(String orderId) {
        String id = "";
        if (orderId.contains("-") && orderId.indexOf("-") > 8) {
            // 新订单号
            id = orderId.substring(orderId.indexOf("-") + 1, orderId.length());
            orderId = orderId.substring(0, orderId.lastIndexOf("-"));
        } else {
            // 旧订单号
            id = orderId.substring(orderId.lastIndexOf("-") + 1, orderId.length());
            orderId = orderId.substring(0, orderId.lastIndexOf("-"));
        }
        SettleApplyBean settleApplyBean = settleApplyBeanBiz.selectSettleBeanByOrderId(orderId);
        if (settleApplyBean != null) {
            if (settleApplyBean.getCutOverdueAmt().compareTo(settleApplyBean.getOverdueAmt()) == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("orderId", orderId);
                map.put("finalOverdueAmt", BigDecimal.ZERO);
                settleApplyBeanBiz.updateSettleAmtForOrderId(map);
            }
        }

    }

    /**
     * 
     * Description:提前结清平账处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月16日
     */
    public Map<String, Object> allClearPay(Map<String, Object> procBizData) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> preResultParam = new HashMap<>();
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        procBizId = procBizId.substring(0, procBizId.lastIndexOf("-"));
        String totalRepayAmt = (String) procBizData.get("receiveAmt");
        String recieveDate = (String) procBizData.get("recieveDate");
        SettleApplyBean bean = settleApplyBeanBiz.selectSettleBeanByOrderId(procBizId);
        preResultParam.put("amount", totalRepayAmt);
        preResultParam.put("repayNo", String.valueOf(bean.getCurItems() + 1));
        preResultParam.put("tranTime", recieveDate);

        resultMap = billInfoService.allClearPay(procBizId, preResultParam);
        return resultMap;
    }

    /**
     * Description:违约保证金结算流程-结算核实保证金结算明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月21日
     */
    private void auditingDetail(Map<String, Object> procBizData) {
        String batchId = (String) procBizData.get("procBizId"); // 批次号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        logger.debug("--违约保证金结算流程-结算核实保证金结算明细--拒绝处理开始--batchId = {},procApprStatus = {}", batchId, procApprStatus);
        if ("0".equals(procApprStatus) || StringUtils.isEmpty(procApprStatus)) {
            // 更新提前结清申请状态
            OverdueInfoBean overdueInfoBean = new OverdueInfoBean();
            overdueInfoBean.setBatchId(batchId);
            // 待结算
            overdueInfoBean.setStatus(1);
            int result = overdueInfoBiz.updateOverdueInfoByBatchId(overdueInfoBean);
            logger.debug("--违约保证金结算流程-结算核实保证金结算明细--拒绝处理结果 {}", result);
        }
    }

    /**
     * Description:华益-周转贷-人工风控节点生成项目信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月2日
     */
    @SuppressWarnings("rawtypes")
    private void huayiRiskDepartment(Map<String, Object> procBizData) {
        logger.info("--华益-周转贷-人工风控节点-生成项目信息--{}", procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        if (procApprStatus.equals("1")) {
            String orderId = (String) procBizData.get("procBizId"); // 订单号
            Map<String, String> params = new HashMap<>();
            params.put("orderId", orderId);
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getManagerWfreturnHYZZDurl(),
                                    params);
            logger.info("华益周转贷流程生成项目信息--{}", result);
        }
    }

    /**
     * 
     * 以租代售-人车并行流程-风控部门审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> newLeaseDepartment(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-风控部门审核调用开始--{}", procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String wfStatus = "0";
        String riskStatus = "0";
        // 是否转租 true 转租 false 不转租
        if (procApprStatus.equals("1")) {
            // 下一节点
            ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks");
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                // 风控部车价评估完成并且其他并行任务已结束
                if (procNextTasks.get(0).equals("new_lease_store")) {
                    wfStatus = "5";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED);
                } else {
                    wfStatus = "33";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                }
            } else {
                wfStatus = "33";
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
            }
        } else if (procApprStatus.equals("2")) {
            // 待门店预处理
            wfStatus = "2";
            riskStatus = "2";
        } else {
            // 流程拒绝
            wfStatus = "0";
            riskStatus = "1";
        }
        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-风控部门审核调用结束--{}", map);
        return map;
    }

    /**
     * 以租代售-人车并行流程-门店预处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月20日
     */
    private Map<String, String> newLeasePretreatment(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-门店预处理调用开始--{}", procBizData);
        String isSublet = (String) procBizData.get("isSublet"); // 是否转租 1 是 0 否

        String wfStatus = "0";
        String riskStatus = "0";
        if (isSublet.equals("1")) {
            // 待人工风控
            wfStatus = "31";
            riskStatus = "2";
        } else {
            // 待并行审批结束
            wfStatus = "33";
            riskStatus = "2";
        }
        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);

        logger.debug("--以租代售-人车并行流程-门店预处理调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-风控部门审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings("rawtypes")
    private Map<String, String> newLeaseDepartment2(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-风控部门审核调用开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        int orderAmt = (int) procBizData.get("orderAmt"); // 订单金额

        String wfStatus = "0";
        String riskStatus = "0";
        String preAmtStatus = "0";
        Map<String, String> params = new HashMap<>();
        params.put("orderId", procBizId);

        if (procApprStatus.equals("1")) {
            // 生成项目信息
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getManagerWfreturnYZDDurl(),
                                    params);
            logger.debug("WFReturnController 生成项目信息  passed! result={}", result);

            if (orderAmt > ORDER_AMT) {
                // 风控经理审核
                wfStatus = "32";
                riskStatus = "2";
                // 转租处理

            } else {
                // 待项目信息维护
                wfStatus = "5";
                riskStatus = "2";
                preAmtStatus = "2";
            }
        } else if (procApprStatus.equals("2")) {
            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
            riskStatus = "2";
        } else {
            wfStatus = "0";// 流程拒绝
            riskStatus = "1";
        }
        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        map.put("preAmtStatus", preAmtStatus);

        logger.debug("--以租代售-人车并行流程-风控部门审核调用结束--{}", map);
        return map;
    }

    /**
     * 以租代售-人车并行流程-风控部车价评估
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, String> newLeasePrice(Map<String, Object> procBizData) {
        logger.debug("--以租代售-人车并行流程-风控部车价评估调用开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String backFlag = "0";

        Map<String, String> params = new HashMap<>();
        params.put("orderId", procBizId);

        String wfStatus = "0";
        String riskStatus = "0";
        Map<String, String> map = new HashMap<>();
        if (procApprStatus.equals("1")) {
            // 生成项目信息
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getManagerWfreturnYZDDurl(),
                                    params);
            logger.debug("WFReturnController 生成项目信息  passed! result={}", result);
            orderBiz.updateSpecialTimeByOid(procBizId);
            // 下一节点
            ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks");
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                // 风控部车价评估完成并且其他并行任务已结束
                if (procNextTasks.get(0).equals("new_lease_store")) {
                    backFlag = "0";
                    wfStatus = "5";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED);
                } else {
                    backFlag = "0";
                    wfStatus = "33";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                }
            } else {
                wfStatus = "33";
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
            }
        } else if (procApprStatus.equals("2")) {
            backFlag = "1";
            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
        } else {
            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_REFUSE);
        }

        map.put("wfStatus", wfStatus);
        map.put("backFlag", backFlag);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-风控部车价评估调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-合同审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> newLeaseAudit(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-合同审核调用开始--{}", procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks"); // 下一节点

        String wfStatus = "0";
        String riskStatus = "0";
        if (procApprStatus.equals("1")) {
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                String procNextTask = procNextTasks.get(0);
                if (!StringUtil.isNull(procNextTask)) {
                    if (procNextTask.equals("new_lease_finance")) {
                        wfStatus = WorkFlowsStructure.WF_WAIT_FINANCE_VERIFY;
                        riskStatus = "0";
                    } else {
                        // 待线下费用确认
                        wfStatus = "71";
                        riskStatus = "0";
                    }
                }
            }
        } else if (procApprStatus.equals("2")) {
            // 待项目信息维护
            wfStatus = "5";
            riskStatus = "0";
        }

        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-合同审核调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-收款确认
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> newLeaseFinance(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-收款确认调用开始--{}", procBizData);
        ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks"); // 当前节点状态
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String wfStatus = "0";
        String riskStatus = "0";
        if (procApprStatus.equals("1")) {
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                String procNextTask = procNextTasks.get(0);
                if (!StringUtil.isNull(procNextTask)) {
                    if (procNextTask.equals("new_lease_gps")) {
                        // 待gps确认
                        wfStatus = "83";
                        riskStatus = "0";
                    } else {
                        // 待项目初审
                        wfStatus = "8";
                        riskStatus = "0";
                    }
                }
            }
        } else {
            // 待项目信息维护
            wfStatus = "5";
            riskStatus = "0";
        }

        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-收款确认调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-门店额度确认
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings({"unchecked"})
    private Map<String, String> newLeaseCredit(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-门店额度确认调用开始--{}", procBizData);
        ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks"); // 当前节点状态
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String wfStatus = "0";
        String riskStatus = "0";
        if (procApprStatus.equals("1")) {
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                String procNextTask = procNextTasks.get(0);
                if (!StringUtil.isNull(procNextTask)) {
                    if (procNextTasks.get(0).equals("new_lease_store")) {
                        wfStatus = "5";
                        riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED);
                    } else {
                        wfStatus = "33";
                        riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                    }
                }
            } else {
                wfStatus = "33";
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
            }
        } else if (procApprStatus.equals("0")) {
            // 流程拒绝
            wfStatus = "0";
            riskStatus = "1";
        }

        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-门店额度确认调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-风控经理审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> newLeaseRiskManager(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售-人车并行流程-风控经理审核调用开始--{}", procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String wfStatus = "0";
        String riskStatus = "0";
        if (procApprStatus.equals("1")) {
            // 下一节点
            ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks");
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                // 风控部车价评估完成并且其他并行任务已结束
                if (procNextTasks.get(0).equals("new_lease_store")) {
                    wfStatus = "5";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED);
                } else {
                    wfStatus = "33";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                }
            } else {
                wfStatus = "33";
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
            }
        }
        else if (procApprStatus.equals("2")) {
            // 待门店预处理
            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
        } else {
            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_REFUSE);
        }
        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售-人车并行流程-风控经理审核调用结束--{}", map);
        return map;
    }

    /**
     * 
     * 以租代售-人车并行流程-项目初审
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    @SuppressWarnings("rawtypes")
    private String newLeaseProject(Map<String, Object> procBizData) {
        logger.debug("--以租代售-人车并行流程-项目初审调用开始--{}", procBizData);
        String flag = "0";
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        if ("1".equals(procApprStatus)) {
            String wfStatus = WorkFlowsStructure.WF_OVER_FINISHED;
            logger.debug("WFReturnController lease_project 项目初审---是否转租isSublet--{}",
                    procBizData.get("isSublet"));
            String isSublet = (String) procBizData.get("isSublet"); // 是否转租
            if (isSublet != null && isSublet.equalsIgnoreCase("true")) {
                flag = "2";
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", procBizId);
                params.put("wfStatus", wfStatus);
                Map result;
                try {
                    result = subletCenterServiceImpl.getSubletInfo(params);
                    logger.debug("转租结果:{}", result);
                } catch (GenerallyException e) {
                    logger.error("工作流回调更改订单状态错误", e);
                }
            }
            logger.debug("WFReturnController lease_project passed!");
        }
        logger.debug("--以租代售-人车并行流程-项目初审调用结束--{}", flag);
        return flag;
    }

    /**
     * 
     * Description:贷后处置流程-GPS信号核实
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    private Map<String, String> loanGpsCheck(Map<String, Object> procBizData) throws GenerallyException,
            DevDefineErrorMsgException {
        logger.debug("--贷后处置流程-GPS信号核实-开始--{}", procBizData);
        Map<String, String> returnMap = new HashMap<>();
        String carStatus = "";
        String processStatus = CommonConstant.ProcessStatus.ProcessStatus_10;
        int isRepayment = Integer.parseInt((String) procBizData.get("isRepayment")); // 是否还款 1 是 2 否
        if (isRepayment == 1) {
            carStatus = CommonConstant.CarStatus.STATUS_70;
            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
        }
        logger.debug("--贷后处置流程-GPS信号核实调用结束--");
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        return returnMap;
    }

    /**
     * 
     * Description:贷后处置流程-车辆状况核实
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     * @throws DevDefineErrorMsgException
     * @throws GenerallyException
     */
    private Map<String, String> carInfoCheck(Map<String, Object> procBizData) throws GenerallyException,
            DevDefineErrorMsgException {
        Map<String, String> returnMap = new HashMap<>();
        logger.debug("--贷后处置流程-车辆状况核实-开始--{}", procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String carStatus = "";
        String processStatus = "";
        int isRepayment = Integer.parseInt((String) procBizData.get("isRepayment")); // 是否还款 1 是 2 否
        String processMethod = (String) procBizData.get("processMethod"); // 处理途径 1 转外包 2 转法务

        if (isRepayment == 2) {
            int isLost = Integer.parseInt((String) procBizData.get("isLost")); // 是否失联 1 是 2 否
            if (1 == isLost) {
                carStatus = CommonConstant.CarStatus.STATUS_20;
                if (!StringUtil.isNull(processMethod) && processMethod.equals("1")) {
                    processStatus = CommonConstant.ProcessStatus.ProcessStatus_15;
                }
                if (!StringUtil.isNull(processMethod) && processMethod.equals("2")) {
                    processStatus = CommonConstant.ProcessStatus.ProcessStatus_25;
                    // 保存法务案件信息
                    JSONObject objs = new JSONObject();
                    objs.put("orderId", procBizId);
                    objs.put("operateFlag", "1");
                    afterLoanServiceImpl.saveOutSourceOrder(objs);
                }
            } else if (2 == isLost) {
                carStatus = CommonConstant.CarStatus.STATUS_70;
            }
        } else if (isRepayment == 1) {
            carStatus = CommonConstant.CarStatus.STATUS_70;
            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
        }
        logger.debug("--贷后处置流程-车辆状况核实调用结束--");
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        return returnMap;
    }

    /**
     * 
     * Description:贷后处置流程-申请权证资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    private Map<String, String> warrantApply(Map<String, Object> procBizData) throws GenerallyException,
            DevDefineErrorMsgException {
        logger.debug("--贷后处置流程-申请权证资料-开始--{}", procBizData);
        Map<String, String> returnMap = new HashMap<>();
        String carStatus = "";
        String processStatus = CommonConstant.ProcessStatus.ProcessStatus_10;
        int isRepayment = Integer.parseInt((String) procBizData.get("isRepayment")); // 是否还款 1 是 2 否
        if (isRepayment == 1) {
            carStatus = CommonConstant.CarStatus.STATUS_70;
            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
        }
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        logger.debug("--贷后处置流程-申请权证资料调用结束--");
        return returnMap;
    }

    /**
     * 
     * Description:贷后处置流程-收车
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    private Map<String, String> acceptCar(Map<String, Object> procBizData) throws GenerallyException,
            DevDefineErrorMsgException {
        logger.debug("--贷后处置流程-收车-开始--{}", procBizData);
        Map<String, String> returnMap = new HashMap<>();
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String carStatus = "";
        String processStatus = CommonConstant.ProcessStatus.ProcessStatus_10;
        int isRepayment = Integer.parseInt((String) procBizData.get("isRepayment")); // 是否还款 1 是 2 否
        int isRetrieve = Integer.parseInt((String) procBizData.get("isRetrieve")); // 车辆是否收回 1 是 2 否
        if (isRepayment == 2) {
            if (isRetrieve == 1) {
                carStatus = CommonConstant.CarStatus.STATUS_25;
                // 启动车辆入库子流程
                startCarStorageWF(procBizId);
            } else {
                carStatus = CommonConstant.CarStatus.STATUS_70;
            }
        } else if (isRepayment == 1) {
            carStatus = CommonConstant.CarStatus.STATUS_70;
            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
        }
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        logger.debug("--贷后处置流程-收车调用结束--");
        return returnMap;
    }

    /**
     * 
     * Description:贷后处置流程-收车情况核实
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    private Map<String, String> acceptCarCheck(Map<String, Object> procBizData) {
        logger.debug("--贷后处置流程-收车情况核实-开始--{}", procBizData);
        Map<String, String> returnMap = new HashMap<>();
        String procBizId = (String) procBizData.get("procBizId"); // 订单号
        String carStatus = "";
        String processStatus = CommonConstant.ProcessStatus.ProcessStatus_10;
        int isRepayment = Integer.parseInt((String) procBizData.get("isRepayment")); // 是否还款 1 是 2 否
        String processMethod = (String) procBizData.get("processMethod"); // 处理途径 1 转外包 2 转法务
        if (isRepayment == 2) {
            int isLost = Integer.parseInt((String) procBizData.get("isLost")); // 是否失联 1 是 2 否
            if (isLost == 1) {
                carStatus = CommonConstant.CarStatus.STATUS_20;
            } else {
                carStatus = CommonConstant.CarStatus.STATUS_65;
            }
            // 转外包 转法务处理
            if (!StringUtil.isNull(processMethod) && processMethod.equals("1")) {
                processStatus = CommonConstant.ProcessStatus.ProcessStatus_15;
            }
            if (!StringUtil.isNull(processMethod) && processMethod.equals("2")) {
                processStatus = CommonConstant.ProcessStatus.ProcessStatus_25;
                // 保存法务案件信息
                JSONObject objs = new JSONObject();
                objs.put("orderId", procBizId);
                objs.put("operateFlag", "1");
                afterLoanServiceImpl.saveOutSourceOrder(objs);
            }
        } else if (isRepayment == 1) {
            carStatus = CommonConstant.CarStatus.STATUS_70;
            processStatus = CommonConstant.ProcessStatus.ProcessStatus_30;
        }
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        logger.debug("--贷后处置流程-收车情况核实调用结束--");
        return returnMap;
    }

    /**
     * 
     * Description:车辆失联子流程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    @SuppressWarnings({"rawtypes", "unused"})
    private LinkedHashMap startCarOutofContactWF(String orderId)
            throws IqbException {
        LinkedHashMap responseMap = null;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            UserBean userBean = userBeanBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    userBean.getRealName() + ";" + merchantBean.getMerchantShortName() + ";" + orderBean.getOrderName()
                            + ";" + "车辆失联";

            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 40);

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", orderBean.getRegId());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderId);
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", orderBean.getOrderAmt());

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = wfConfig.getStartWfurl();
            // 发送Https请求

            logger.info("调用工作流接口传入信息：{}", reqData);
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                logger.error("---启动车辆失联子流程异常,---()", e);
                throw new RuntimeException("工作流接口交易失败");
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}", responseMap);
            logger.info("工作流接口交互花费时间，{}", (endTime - startTime));
        }
        return responseMap;
    }

    /**
     * 
     * Description:启动拖车入库子流程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    @SuppressWarnings("rawtypes")
    private LinkedHashMap startCarStorageWF(String orderId)
            throws IqbException {
        LinkedHashMap responseMap = null;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            UserBean userBean = userBeanBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    userBean.getRealName() + ";" + merchantBean.getMerchantShortName() + ";" + orderBean.getOrderName()
                            + ";" + "拖车入库";

            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 50);

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", orderBean.getRegId());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderId);
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", orderBean.getOrderAmt());

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = wfConfig.getStartWfurl();
            // 发送Https请求

            logger.info("调用工作流接口传入信息：{}", reqData);
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new RuntimeException("工作流接口交易失败");
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}", responseMap);
            logger.info("工作流接口交互花费时间，{}", (endTime - startTime));
        }
        return responseMap;
    }

    /**
     * 
     * Description:补充客户资料-贷后经理处理后将电话、电催数据的状态修改为未处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    public void updateInstRemindPhone(String orderId) {
        logger.debug("--补充客户资料-贷后经理-开始--{}", orderId);
        if (null != orderId) {
            String[] orderIds = orderId.split("-");
            InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
            instRemindPhoneBean.setOrderId(orderIds[0]);
            instRemindPhoneBean.setCurItems(Integer.parseInt(orderIds[1]));
            instRemindPhoneBean.setStatus("1");
            instRemindPhoneBean.setFlag(Integer.parseInt(orderIds[2]));
            instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
        }
        logger.debug("--补充客户资料-贷后经理-结束--");
    }

    /**
     * Description:房融保-风控部门审核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    @SuppressWarnings("rawtypes")
    private void houseLoanRisk(Map<String, Object> procBizData) {
        logger.info("--房融保-风控部门审核开始--{}", procBizData);
        String orderId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        if (procApprStatus.equals("1")) {
            Map<String, String> params = new HashMap<>();
            params.put("orderId", orderId);
            LinkedHashMap result =
                    SendHttpsUtil
                            .postMsg4GetMap(
                                    paramConfig.getManagerWfreturnFRBurl(),
                                    params);
            logger.info("房融保-风控部门审核生成项目信息--{}", result);
        }
    }

    /**
     * 
     * Description:房融保-外访资料上传-发送个人信息到风控系统
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月15日
     */
    private void houseLoanDocument(Map<String, Object> procBizData) {
        logger.info("--房融保-外访资料上传-发送个人信息到风控系统开始--{}", procBizData);
        String orderId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        if (procApprStatus.equals("1")) {
            Map<String, String> params = new HashMap<>();
            params.put("orderId", orderId);
            Map<String, Object> map = riskInfoService.send2Risk(params);
            logger.info("--房融保-外访资料上传-发送个人信息到风控系统完成--{}", JSONObject.toJSON(map));
        }
    }

    /**
     * 
     * Description:以租代售并行流程-电核
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月4日
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> newPhoneAudit(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        logger.debug("--以租代售并行流程-电核调用开始--{}", procBizData);
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        String wfStatus = "0";
        String riskStatus = "0";
        // 是否转租 true 转租 false 不转租
        if (procApprStatus.equals("1")) {
            // 下一节点
            ArrayList<String> procNextTasks = (ArrayList<String>) procBizData.get("procNextTasks");
            if (!CollectionUtils.isEmpty(procNextTasks)) {
                // 风控部车价评估完成并且其他并行任务已结束
                if (procNextTasks.get(0).equals("new_lease_store")) {
                    wfStatus = "5";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_OVER_PASSED);
                } else {
                    wfStatus = "33";
                    riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
                }
            } else {
                wfStatus = "33";
                riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
            }
        } else if (procApprStatus.equals("2")) {
            // 待门店预处理
            wfStatus = "2";
            riskStatus = "2";
        } else {
            // 流程拒绝
            wfStatus = "33";
            riskStatus = String.valueOf(WorkFlowsStructure.RISK_IN_REVIEW);
        }
        map.put("wfStatus", wfStatus);
        map.put("riskStatus", riskStatus);
        logger.debug("--以租代售并行流程-电核结束--{}", map);
        return map;
    }

    /**
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    private void urgeToLoanCheck(Map<String, Object> procBizData) {
        logger.info("--电催转贷后流程-电催提醒审核开始--{}", procBizData);
        String orderId = (String) procBizData.get("procBizId"); // 订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态

        String[] orderIds = orderId.split("-");
        InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
        instRemindPhoneBean.setOrderId(orderIds[0]);
        instRemindPhoneBean.setCurItems(Integer.parseInt(orderIds[1]));

        instRemindPhoneBean.setFlag(Integer.parseInt(orderIds[2]));
        if (procApprStatus.equals("1")) {
            instRemindPhoneBean.setStatus("3");
            instRemindPhoneService.saveInstManagerCarInfo(orderIds[0]);
        } else if (procApprStatus.equals("0")) {
            instRemindPhoneBean.setStatus("1");
        }
        instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
        logger.info("--电催转贷后流程-电催提醒审核完成--");
    }

    /**
     * 立案申请流程-庭前调解
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月24日
     */
    private Map<String, String> registerCaseMediate(Map<String, Object> procBizData) {
        Map<String, String> map = new HashMap<>();
        String caseId = (String) procBizData.get("procBizId"); // 案件号
        // 是否庭前调解 1 是 2 否
        String composeFlag = (String) procBizData.get("composeFlag");
        // 和解标识 1 是 2 否
        String compromiseFlag = (String) procBizData.get("compromiseFlag");
        String processStatus = null;
        if ((composeFlag.equals("1") && compromiseFlag.equals("2")) || composeFlag.equals("2")) {
            JSONObject objs = new JSONObject();
            objs.put("caseId", caseId);
            objs.put("caseStatus", CaseStatus.CaseStatus_25);
            instOrderLawBiz.updateInstOrderLawnInfo(objs);
        }
        // 如果该案件由外包转入，则同步更改其外包状态
        JSONObject objs = new JSONObject();
        objs.put("caseId", caseId);
        List<InstOrderLawBean> lawList = instOrderLawBiz.selectInstOrderLawnInfoByOrderId(objs);
        if (!CollectionUtils.isEmpty(lawList)) {
            InstOrderLawBean lawBean = lawList.get(0);
            if (lawBean.getCaseSource().equals("1")) {
                processStatus = ProcessStatus.ProcessStatus_25;
            }
        }
        map.put("processStatus", processStatus);
        return map;
    }

    /**
     * 
     * 立案申请-财务确认
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月27日
     */
    private Map<String, String> registerCaseFinance(Map<String, Object> procBizData) {
        Map<String, String> returnMap = new HashMap<>();
        String caseId = (String) procBizData.get("procBizId"); // 案件号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        logger.debug("--立案申请-财务确认-开始--{}", procBizData);
        String carStatus = null;
        String processStatus = "";
        if (procApprStatus.equals("1")) {
            JSONObject objs = new JSONObject();
            objs.put("caseId", caseId);
            objs.put("caseStatus", CommonConstant.CaseStatus.CaseStatus_35);
            instOrderLawBiz.updateInstOrderLawnInfo(objs);
            // 如果该案件由外包转入，则同步更改其外包状态
            List<InstOrderLawBean> lawList = instOrderLawBiz.selectInstOrderLawnInfoByOrderId(objs);
            if (!CollectionUtils.isEmpty(lawList)) {
                InstOrderLawBean lawBean = lawList.get(0);
                if (lawBean.getCaseSource().equals("1")) {
                    processStatus = ProcessStatus.ProcessStatus_30;
                }
            }
        }
        returnMap.put("carStatus", carStatus);
        returnMap.put("processStatus", processStatus);
        logger.debug("--立案申请-财务确认-结束--{}", procBizData);
        return returnMap;
    }

    /**
     * 
     * Description:更新案件状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月31日
     */
    public void updateOrderLawStatus(String caseId, String caseStatus) {
        logger.debug("--更新案件状态-开始--");
        JSONObject objs = new JSONObject();
        objs.put("caseId", caseId);
        objs.put("caseStatus", caseStatus);
        int result = instOrderLawBiz.updateInstOrderLawnInfo(objs);
        logger.debug("--更新案件状态-结束--{}", result);
    }

    /**
     * Description:车务管理-渠道审批
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    private void carInsuranceChannel(Map<String, Object> procBizData) {
        Map<String, Object> map = new HashMap<>();
        String procBizId = (String) procBizData.get("procBizId");
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 当前节点状态
        map.put("procBizId", procBizId);
        if (procApprStatus.equals("1")) {
            map.put("checkStatus", 2);
            map.put("procResult", 1);
        } else if (procApprStatus.equals("2")) {
            map.put("procResult", 3);
        }
        trafficManageBiz.updateAdditionalStatus(map);
    }

    /**
     * Description:车务管理-流程终止
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    private void carInsuranceEnd(String procBizId) {
        Map<String, Object> map = new HashMap<>();
        map.put("procBizId", procBizId);
        map.put("checkStatus", 7);
        trafficManageBiz.updateAdditionalStatus(map);
    }
}
