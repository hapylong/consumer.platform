package com.iqb.consumer.data.layer.bean.carthreehundred;

import java.util.Arrays;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年8月16日下午6:47:35 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstOrderloanRiskResultBean {
    private String vin;
    private String risk_code;
    private String[] rules;
    private String rule;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRisk_code() {
        return risk_code;
    }

    public void setRisk_code(String risk_code) {
        this.risk_code = risk_code;
    }

    public String[] getRules() {
        return rules;
    }

    public void setRules(String[] rules) {
        this.rules = rules;
    }

    public String getRule() {
        String rule = Arrays.toString(rules);
        rule = rule.substring(1, rule.length() - 1);
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
