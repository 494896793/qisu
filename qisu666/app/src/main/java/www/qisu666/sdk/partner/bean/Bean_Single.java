package www.qisu666.sdk.partner.bean;

import java.util.Date;

/**
 * 717219917@qq.com 2018/4/19 19:37.
 */
public class Bean_Single {

    private SubscribeInfo subscribeInfo=new SubscribeInfo();
    private CarInfoMap carInfoMap=new CarInfoMap();
    public void setSubscribeInfo(SubscribeInfo subscribeInfo) {
        this.subscribeInfo = subscribeInfo;
    }
    public SubscribeInfo getSubscribeInfo() {
        return subscribeInfo;
    }
    public void setCarInfoMap(CarInfoMap carInfoMap) {
        this.carInfoMap = carInfoMap;
    }
    public CarInfoMap getCarInfoMap() {
        return carInfoMap;
    }

    public class SubscribeInfo {

        private Date firstPhaseTime=new Date();
        private Date contractExpiresTime=new Date();
        private String contractStatus="";
        private String subType="";
        private Date canCancleTime=new Date();
        private double useBonusAmount=0;
        private Date subTime=new Date();
        private String subStatus="";
        public void setFirstPhaseTime(Date firstPhaseTime) {
            this.firstPhaseTime = firstPhaseTime;
        }
        public Date getFirstPhaseTime() {
            return firstPhaseTime;
        }

        public void setContractExpiresTime(Date contractExpiresTime) {
            this.contractExpiresTime = contractExpiresTime;
        }
        public Date getContractExpiresTime() {
            return contractExpiresTime;
        }

        public void setContractStatus(String contractStatus) {
            this.contractStatus = contractStatus;
        }
        public String getContractStatus() {
            return contractStatus;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }
        public String getSubType() {
            return subType;
        }

        public void setCanCancleTime(Date canCancleTime) {
            this.canCancleTime = canCancleTime;
        }
        public Date getCanCancleTime() {
            return canCancleTime;
        }

        public void setUseBonusAmount(double useBonusAmount) {
            this.useBonusAmount = useBonusAmount;
        }
        public double getUseBonusAmount() {
            return useBonusAmount;
        }

        public void setSubTime(Date subTime) {
            this.subTime = subTime;
        }
        public Date getSubTime() {
            return subTime;
        }

        public void setSubStatus(String subStatus) {
            this.subStatus = subStatus;
        }
        public String getSubStatus() {
            return subStatus;
        }

    }
    public class CarInfoMap {

        private double withdrawPeriods=0;
        private double period=0;
        private String color="";
        private String cityCode="";
        private String carImgPath="";
        private String productStatus="";
        private double productNumber=0;
        private double surplusNumber=0;
        private String plateNumber;
        private String vinNo;
        private String productTitle;
        private long totalAmount=0;
        private double subAmount=0;
        private String productCode;
        private String carCode;
        private long balance=0;
        private String cityName;
        private double subRebate=0;
        private String carWpmi;
        private String productType;
        private String productTypeCn;
        public void setWithdrawPeriods(double withdrawPeriods) {
            this.withdrawPeriods = withdrawPeriods;
        }
        public double getWithdrawPeriods() {
            return withdrawPeriods;
        }

        public void setPeriod(double period) {
            this.period = period;
        }
        public double getPeriod() {
            return period;
        }

        public void setColor(String color) {
            this.color = color;
        }
        public String getColor() {
            return color;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
        public String getCityCode() {
            return cityCode;
        }

        public void setCarImgPath(String carImgPath) {
            this.carImgPath = carImgPath;
        }
        public String getCarImgPath() {
            return carImgPath;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }
        public String getProductStatus() {
            return productStatus;
        }

        public void setProductNumber(double productNumber) {
            this.productNumber = productNumber;
        }
        public double getProductNumber() {
            return productNumber;
        }

        public void setSurplusNumber(double surplusNumber) {
            this.surplusNumber = surplusNumber;
        }
        public double getSurplusNumber() {
            return surplusNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }
        public String getPlateNumber() {
            return plateNumber;
        }

        public void setVinNo(String vinNo) {
            this.vinNo = vinNo;
        }
        public String getVinNo() {
            return vinNo;
        }

        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }
        public String getProductTitle() {
            return productTitle;
        }

        public void setTotalAmount(long totalAmount) {
            this.totalAmount = totalAmount;
        }
        public long getTotalAmount() {
            return totalAmount;
        }

        public void setSubAmount(double subAmount) {
            this.subAmount = subAmount;
        }
        public double getSubAmount() {
            return subAmount;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
        public String getProductCode() {
            return productCode;
        }

        public void setCarCode(String carCode) {
            this.carCode = carCode;
        }
        public String getCarCode() {
            return carCode;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }
        public long getBalance() {
            return balance;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
        public String getCityName() {
            return cityName;
        }

        public void setSubRebate(double subRebate) {
            this.subRebate = subRebate;
        }
        public double getSubRebate() {
            return subRebate;
        }

        public void setCarWpmi(String carWpmi) {
            this.carWpmi = carWpmi;
        }
        public String getCarWpmi() {
            return carWpmi;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }
        public String getProductType() {
            return productType;
        }

        public void setProductTypeCn(String productTypeCn) {
            this.productTypeCn = productTypeCn;
        }
        public String getProductTypeCn() {
            return productTypeCn;
        }

    }


}