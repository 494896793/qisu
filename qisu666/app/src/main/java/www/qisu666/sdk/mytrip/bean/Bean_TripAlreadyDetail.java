package www.qisu666.sdk.mytrip.bean;

/**
 * 717219917@qq.com 2018/5/9 15:15.
 */
public class Bean_TripAlreadyDetail {

    private OneOrder oneOrder;
    public void setOneOrder(OneOrder oneOrder) {
        this.oneOrder = oneOrder;
    }
    public OneOrder getOneOrder() {
        return oneOrder;
    }
 
    public class OneOrder {

        private String updatedTime;
        private double timeConsum;
        private double deductAmount;
        private double beginMileage;
        private double parkedConsum;
        private double endMileage;
        private String returnTime;
        private double carConsum;
        private String driverType;
        private double mileageConsum;
        private double payAmount;
        private String outTradeNo;
        private double costMin;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        private String createdTime;
        private double driverConsum;
        private String orderCode;
        private String borrowType;
        private String remark;

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }

        private String borrowTime;
        private double electricConsum;
        private String status;
        private double totalConsum;
        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }
        public String getUpdatedTime() {
            return updatedTime;
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

        public void setBeginMileage(double beginMileage) {
            this.beginMileage = beginMileage;
        }
        public double getBeginMileage() {
            return beginMileage;
        }

        public void setParkedConsum(double parkedConsum) {
            this.parkedConsum = parkedConsum;
        }
        public double getParkedConsum() {
            return parkedConsum;
        }

        public void setEndMileage(double endMileage) {
            this.endMileage = endMileage;
        }
        public double getEndMileage() {
            return endMileage;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }
        public String getReturnTime() {
            return returnTime;
        }

        public void setCarConsum(double carConsum) {
            this.carConsum = carConsum;
        }
        public double getCarConsum() {
            return carConsum;
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

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setCostMin(double costMin) {
            this.costMin = costMin;
        }
        public double getCostMin() {
            return costMin;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
        public String getCreatedTime() {
            return createdTime;
        }

        public void setDriverConsum(double driverConsum) {
            this.driverConsum = driverConsum;
        }
        public double getDriverConsum() {
            return driverConsum;
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