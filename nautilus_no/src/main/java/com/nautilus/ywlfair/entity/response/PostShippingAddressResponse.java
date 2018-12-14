package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 收货地址实体
 * Created by lp on 2016/1/2.
 */
public class PostShippingAddressResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private String id;
        private String provinceCity;
        private String address;
        private String consignee;
        private String postCode;
        private String defaultFlag;
        private String telephone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvinceCity() {
            return provinceCity;
        }

        public void setProvinceCity(String provinceCity) {
            this.provinceCity = provinceCity;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getDefaultFlag() {
            return defaultFlag;
        }

        public void setDefaultFlag(String defaultFlag) {
            this.defaultFlag = defaultFlag;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
