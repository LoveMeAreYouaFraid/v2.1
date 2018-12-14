package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ShippingAddressInfo;

import java.io.Serializable;
import java.util.List;

/**收货地址列表
 * Created by lp on 2016/1/2.
 */
public class GetShippingAddressList extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;


    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private List<ShippingAddressInfo> userAddressList;

        public List<ShippingAddressInfo> getShippingAddressInfoList() {
            return userAddressList;
        }

        public void setShippingAddressInfoList(List<ShippingAddressInfo> shippingAddressInfoList) {
            userAddressList = shippingAddressInfoList;
        }
    }
}
