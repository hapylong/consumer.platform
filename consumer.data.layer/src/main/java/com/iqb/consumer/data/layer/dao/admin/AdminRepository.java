package com.iqb.consumer.data.layer.dao.admin;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.admin.entity.CASOrgCodeRecordEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbSysOrganizationInfoEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.admin.pojo.SpecialTableColumn;
import com.iqb.consumer.data.layer.bean.admin.pojo.TableColumnPair;

public interface AdminRepository {

    IqbSysOrganizationInfoEntity getIDIEByOrgCodeAndOrgLevel(@Param("orgCode") String orgCode);

    int casChangeOrgCodeSTC(SpecialTableColumn stc);

    int casChangeOrgCodeTCP(TableColumnPair tcp);

    int casUpdateMgLogMsg(@Param("codeA") String codeA, @Param("codeB") String codeB);

    void persistCOCRE(CASOrgCodeRecordEntity cre);

    int casUpdateISOI(CASOrgCodeRecordEntity cre);

    IqbCustomerPermissionEntity getICPEByMerchantNo(String merchantNo);

    List<MerchantTreePojo> getMerchantTreeList(Integer concatId);

    int saveOrUpdateICPE(IqbCustomerPermissionEntity icpe);

    int isMerchantNoExit(String merchantNo);

    String getMerchantNameByNo(String merchantNo);

}
