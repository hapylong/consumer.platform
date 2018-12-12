package com.iqb.consumer.service.layer.api;

import java.util.HashSet;
import java.util.Set;

import com.iqb.consumer.data.layer.bean.dandelion.pojo.EmergencySummaryPojo;

import jodd.util.StringUtil;

/**
 * 
 * Description: inst_riskinfo risktype = 3 checkinfo pojo
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月28日    adam       1.0        1.0 Version 
 * </pre>
 */
public class InstRiskInfoColumnRT3CheckInfoPojo {

    private String addprovince;
    private String contactname1;
    private String contactname2;
    private String contactphone1;
    private String contactphone2;
    private String marriedstatus;

    public String getAddprovince() {
        return addprovince;
    }

    public void setAddprovince(String addprovince) {
        this.addprovince = addprovince;
    }

    public String getContactname1() {
        return contactname1;
    }

    public void setContactname1(String contactname1) {
        this.contactname1 = contactname1;
    }

    public String getContactname2() {
        return contactname2;
    }

    public void setContactname2(String contactname2) {
        this.contactname2 = contactname2;
    }

    public String getContactphone1() {
        return contactphone1;
    }

    public void setContactphone1(String contactphone1) {
        this.contactphone1 = contactphone1;
    }

    public String getContactphone2() {
        return contactphone2;
    }

    public void setContactphone2(String contactphone2) {
        this.contactphone2 = contactphone2;
    }

    public String getMarriedstatus() {
        return marriedstatus;
    }

    public void setMarriedstatus(String marriedstatus) {
        this.marriedstatus = marriedstatus;
    }

    public Set<EmergencySummaryPojo> getEmergencySummary() {
        Set<EmergencySummaryPojo> esps = new HashSet<>();
        if (!StringUtil.isEmpty(contactname1) && !StringUtil.isEmpty(contactphone1)) {
            EmergencySummaryPojo esp = new EmergencySummaryPojo();
            esp.setName(contactname1);
            esp.setPhone(contactphone1);
            esp.setPriority(1);
            esps.add(esp);
        }
        if (!StringUtil.isEmpty(contactname2) && !StringUtil.isEmpty(contactphone2)) {
            EmergencySummaryPojo esp = new EmergencySummaryPojo();
            esp.setName(contactname2);
            esp.setPhone(contactphone2);
            esp.setPriority(2);
            esps.add(esp);
        }
        return esps;
    }
}
