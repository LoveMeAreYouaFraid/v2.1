package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.DebitRecordsInfo;
import com.nautilus.ywlfair.entity.bean.event.DepositInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 6 .5.1 摊主交押金记录明细
 * Created by lipeng on 2016/3/29.
 */
public class GetDebitRecordsResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private DepositInfo depositInfo;
        private DepositLogList depositLogList;

        public DepositInfo getDepositInfo() {
            return depositInfo;
        }

        public void setDepositInfo(DepositInfo depositInfo) {
            this.depositInfo = depositInfo;
        }

        public DepositLogList getDepositLogList() {
            return depositLogList;
        }

        public void setDepositLogList(DepositLogList depositLogList) {
            this.depositLogList = depositLogList;
        }
    }

    public class DepositLogList implements Serializable {
        private List<DebitRecordsInfo> deductionsLog;

        private List<DebitRecordsInfo> refundLog;

        public List<DebitRecordsInfo> getDeductionsLog() {
            return deductionsLog;
        }

        public void setDeductionsLog(List<DebitRecordsInfo> deductionsLog) {
            this.deductionsLog = deductionsLog;
        }

        public List<DebitRecordsInfo> getRefundLog() {
            return refundLog;
        }

        public void setRefundLog(List<DebitRecordsInfo> refundLog) {
            this.refundLog = refundLog;
        }
    }
}
