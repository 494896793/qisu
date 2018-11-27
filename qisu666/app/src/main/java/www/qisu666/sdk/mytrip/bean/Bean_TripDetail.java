package www.qisu666.sdk.mytrip.bean;


/**
 * 717219917@qq.com 2018/5/8 10:48.
 */
public class Bean_TripDetail  {

    private OneOrder oneOrder;
    public void setOneOrder(OneOrder oneOrder) {
        this.oneOrder = oneOrder;
    }
    public OneOrder getOneOrder() {
        return oneOrder;
    }

    public class OneOrder {

        private String upStringdTime;
        private double timeConsum;
        private double deductAmount;
        private double parkedConsum;
        private String returnTime;
        private String driverType;
        private double mileageConsum;
        private double payAmount;
        private String borrowTime;
        private String orderCode;
        private String borrowType;
        private double electricConsum;
        private String status;
        private double totalConsum;
        private String outTradeNo="-1";

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public void setUpStringdTime(String upStringdTime) {
            this.upStringdTime = upStringdTime;
        }
        public String getUpStringdTime() {
            return upStringdTime;
        }

        public void setTimeConsum(double timeConsum) {
            this.timeConsum = timeConsum;
        }
        public double getTimeConsum() {
            return timeConsum;
        }

        public void setDeductAmount(double deductAmount) {
            this.deductAmount = deductAmount;
        }
        public double getDeductAmount() {
            return deductAmount;
        }

        public void setParkedConsum(double parkedConsum) {
            this.parkedConsum = parkedConsum;
        }
        public double getParkedConsum() {
            return parkedConsum;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }
        public String getReturnTime() {
            return returnTime;
        }

        public void setDriverType(String driverType) {
            this.driverType = driverType;
        }
        public String getDriverType() {
            return driverType;
        }

        public void setMileageConsum(double mileageConsum) {
            this.mileageConsum = mileageConsum;
        }
        public double getMileageConsum() {
            return mileageConsum;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
        }
        public double getPayAmount() {
            return payAmount;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }
        public String getBorrowTime() {
            return borrowTime;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }
        public String getOrderCode() {
            return orderCode;
        }

        public void setBorrowType(String borrowType) {
            this.borrowType = borrowType;
        }
        public String getBorrowType() {
            return borrowType;
        }

        public void setElectricConsum(double electricConsum) {
            this.electricConsum = electricConsum;
        }
        public double getElectricConsum() {
            return electricConsum;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setTotalConsum(double totalConsum) {
            this.totalConsum = totalConsum;
        }
        public double getTotalConsum() {
            return totalConsum;
        }

    }
    
}