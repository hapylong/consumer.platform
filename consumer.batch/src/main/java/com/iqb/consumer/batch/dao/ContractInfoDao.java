package com.iqb.consumer.batch.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.batch.data.pojo.ContractInfoBean;

public interface ContractInfoDao {

    List<Map<String, Object>> selectContractInfoForDownload(ContractInfoBean ecInfoBean);

    Integer updateEcDownloadTimes(String tpSignid);
}
