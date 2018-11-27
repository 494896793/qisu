package www.qisu666.sdk.mytrip.bean;

import java.util.List;

/**
 * 717219917@qq.com 2018/5/7 14:45.
 */
//行程列表bean
public class Bean_TripNo {


    private List<MyOrderList> myOrderList;

    public List<MyOrderList> getMyOrderList() {
        return myOrderList;
    }

    public void setMyOrderList(List<MyOrderList> myOrderList) {
        this.myOrderList = myOrderList;
    }

    public static class MyOrderList {
        /**
         * updatedTime : 2018-07-16 09:55:07
         * payAmount : 450
         * ROWNO : 1
         * outTradeNo : 180716092317764044
         * createdTime : 2018-07-16 09:23:19
         * beginLocationTxt : 金谷创业园
         * orderCode : OI000968
         * returnTime : 2018-07-16 09:53:19
         * status : 5
         * borrowTime : 2018-07-09 18:50:21
         */

        private String updatedTime;
        private int payAmount;
        private int ROWNO;
        private String outTradeNo;
        private String createdTime;
        private String beginLocationTxt;
        private String orderCode;
        private String returnTime;
        private String status;
        private String borrowTime;

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public int getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public int getROWNO() {
            return ROWNO;
        }

        public void setROWNO(int ROWNO) {
            this.ROWNO = ROWNO;
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

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }
    }
}