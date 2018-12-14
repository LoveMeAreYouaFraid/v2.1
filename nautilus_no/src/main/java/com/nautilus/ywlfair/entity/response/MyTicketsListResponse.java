package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyTicketsListResponse extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private String count;
        private List<info> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<info> getList() {
            return list;
        }

        public void setList(List<info> list) {
            this.list = list;
        }
    }


    public class info implements Serializable {
        private String ticketType;
        private String actId;
        private String startdate;
        private String price;
        private String status;
        private String name;
        private String orderTime;
        private String enddate;
        private String imgUrl;
        private String address;
        private String orderId;
        private String num;
        private String activityStatus;
        private String countdown;
        private String ticketTypeName;

        public String getTicketTypeName() {
            return ticketTypeName;
        }

        public void setTicketTypeName(String ticketTypeName) {
            this.ticketTypeName = ticketTypeName;
        }

        public String getCountdown() {
            return countdown;
        }

        public void setCountdown(String countdown) {
            this.countdown = countdown;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getActivityStatus() {
            return activityStatus;
        }

        public void setActivityStatus(String activityStatus) {
            this.activityStatus = activityStatus;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }
    }
}
