package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.entity.bean.PrivilegeInfo;
import com.nautilus.ywlfair.entity.bean.VendorLevel;

import java.io.Serializable;
import java.util.List;

/**
 * 摊主vip response
 */
public class GetVendorVipResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private VendorLevel vendorLevelInfo;

        private List<VendorLevel> vendorLevelList;

        private List<PrivilegeInfo> privilegeInfoList;

        public VendorLevel getVendorLevelInfo() {
            return vendorLevelInfo;
        }

        public void setVendorLevelInfo(VendorLevel vendorLevelInfo) {
            this.vendorLevelInfo = vendorLevelInfo;
        }

        public List<VendorLevel> getVendorLevelList() {
            return vendorLevelList;
        }

        public void setVendorLevelList(List<VendorLevel> vendorLevelList) {
            this.vendorLevelList = vendorLevelList;
        }

        public List<PrivilegeInfo> getPrivilegeInfoList() {
            return privilegeInfoList;
        }

        public void setPrivilegeInfoList(List<PrivilegeInfo> privilegeInfoList) {
            this.privilegeInfoList = privilegeInfoList;
        }
    }
}
