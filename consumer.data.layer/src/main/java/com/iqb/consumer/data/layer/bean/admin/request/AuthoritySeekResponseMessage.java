package com.iqb.consumer.data.layer.bean.admin.request;

import java.util.List;

import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;

public class AuthoritySeekResponseMessage {

    private IqbCustomerPermissionEntity seek;
    private List<MerchantTreePojo> all;

    public IqbCustomerPermissionEntity getSeek() {
        return seek;
    }

    public void setSeek(IqbCustomerPermissionEntity seek) {
        this.seek = seek;
    }

    public List<MerchantTreePojo> getAll() {
        return all;
    }

    public void setAll(List<MerchantTreePojo> all) {
        this.all = all;
    }
}
