package www.qisu666.sdk.mytrip.bean;

import java.util.List;

/**
 * 717219917@qq.com 2018/5/8 20:27.
 */
public class Bean_TripAlready  {


    private List<MyOrderList> myOrderList;

    public List<MyOrderList> getMyOrderList() {
        return myOrderList;
    }

    public void setMyOrderList(List<MyOrderList> myOrderList) {
        this.myOrderList = myOrderList;
    }

    public static class MyOrderList {
        /**
         * updatedTime : 2018-07-20 14:38:54
         * payAmount : 450
         * ROWNO : 1
         * borrowTime : 2018-07-20 14:35:58
         * endLocationTxt : 金谷创业园
         * outTradeNo : 180720143522500048
         * createdTime : 2018-07-20 14:35:25
         * beginLocationTxt : 金谷创业园
         * orderCode : OI001078
         * returnTime : 2018-07-20 14:38:51
         * status : 3
         */

        private String updatedTime;
        private double payAmount;
        private int ROWNO;
        private String borrowTime;
        private String endLocationTxt;
        private String outTradeNo;
        private String createdTime;
        private String beginLocationTxt;
        private String orderCode;
        private String returnTime;
        private String status;

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
        }

        public int getROWNO() {
            return ROWNO;
        }

        public void setROWNO(int ROWNO) {
            this.ROWNO = ROWNO;
        }

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }

        public String getEndLocationTxt() {
            return endLocationTxt;
        }

        public void setEndLocationTxt(String endLocationTxt) {
            this.endLocationTxt = endLocationTxt;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getBeginLocationTxt() {
            return beginLocationTxt;
        }

        public void setBeginLocationTxt(String beginLocationTxt) {
            this.beginLocationTxt = beginLocationTxt;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}