package www.qisu666.sdk.amap.carShare.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 717219917@qq.com ${DATA} 10:17.
 */

//针对还车完毕封装的bean
public class CarNowOrderBean_Comple {


    /**
     * orderId : 63E2547405EE0020E050682F756D5F27
     * orderCode : OI000001
     * city : SZ
     * borrowTime : Jan 29, 2018 10:51:59 AM
     * returnTime : Jan 29, 2018 10:54:19 AM
     * beginMileage : 2915.0
     * endMileage : 3012.0
     * mileageExpenses : 174600.0
     * timeCostCharge : 862.1
     * costMin : 1500.0
     * costFinal : 175462.1
     * beginStation : S0000005
     * driverType : 0
     * borrowType : 0
     * status : 2
     * userCode : 10000425
     * carCode : CA000001
     * driverCost : 254.0
     * beginLocationTxt : 正新鸡排
     * car : {"carId":"605E9D1FA5A8AA35E050682F756D23F6","carCode":"CA000001","brandCode":"BC0000001","brandName":"江淮1","modelNumber":"IEV4","status":"2","plateNumber":"粤61700E90113","boxNumber":"E61700E90113","carSeatNum":2,"carImgPath":"/images/car/20180123/1516641530523.jpg","doorSatus":"0","gear":"3","speed":"0","mileage":"2915","batteryVoltage":"13.0","voltage":"319.1","electricity":"-0.1","oddPower":"81","oddMileage":"129","electricizeStatus":"0","networkSignal":"29","locationStatus":"1","satelliteCount":"18","longitude":"113.903831","latitude":"22.552437","headingAngle":"91.57","createdTime":"Dec 15, 2017 5:27:47 PM","updatedTime":"Jan 29, 2018 10:51:59 AM"}
     * borrowTimeLong : 1.517194319223E12
     * createdTimeLong : 1.517194319238E12
     * updatedTimeLong : 1.517194319238E12
     * returnTimeLong : 1.517194459941E12
     * createdTime : Jan 29, 2018 10:51:59 AM
     * updatedTime : Jan 29, 2018 10:51:59 AM
     * <p> */

    public String orderId;
    public String orderCode;
    public String city;
    public Date borrowTime;
    public Date returnTime;
    public int beginMileage;
    public int endMileage;
    public int beginOddMileage;
    public int endOddMileage;
    public int costMin;             //最低花费
    public int mileageConsum;       //里程费
    public int timeConsum;          //时长
    public int electricConsum;      //电度费
    public int carConsum;
    public int totalConsum;          //总费用
    public int payAmount;            //实付款
    public int deductAmount;         //抵扣费用
    public String benefitType;
    public int benefitRate;
    public int benefitAmount;


    public int parkedAmount;         //挪车费
    public int couponConsum;
    public String beginStation;
    public String driverType;
    public String borrowType;       //日计费  1
    public String status;
    public String userCode;
    public String carCode;
    public String endLocationTxt;
    public Car car;

       public long borrowTimeLong;
       public long createdTimeLong;
       public long updatedTimeLong;
       public long returnTimeLong;
    public String remark;          //会员优惠
    public String isParkingOut;
    public Date createdTime;
    public Date updatedTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public int getBeginMileage() {
        return beginMileage;
    }

    public void setBeginMileage(int beginMileage) {
        this.beginMileage = beginMileage;
    }

    public int getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(int endMileage) {
        this.endMileage = endMileage;
    }

    public int getBeginOddMileage() {
        return beginOddMileage;
    }

    public void setBeginOddMileage(int beginOddMileage) {
        this.beginOddMileage = beginOddMileage;
    }

    public int getEndOddMileage() {
        return endOddMileage;
    }

    public void setEndOddMileage(int endOddMileage) {
        this.endOddMileage = endOddMileage;
    }

    public int getCostMin() {
        return costMin;
    }

    public void setCostMin(int costMin) {
        this.costMin = costMin;
    }

    public int getMileageConsum() {
        return mileageConsum;
    }

    public void setMileageConsum(int mileageConsum) {
        this.mileageConsum = mileageConsum;
    }

    public int getTimeConsum() {
        return timeConsum;
    }

    public void setTimeConsum(int timeConsum) {
        this.timeConsum = timeConsum;
    }

    public int getElectricConsum() {
        return electricConsum;
    }

    public void setElectricConsum(int electricConsum) {
        this.electricConsum = electricConsum;
    }

    public int getCarConsum() {
        return carConsum;
    }

    public void setCarConsum(int carConsum) {
        this.carConsum = carConsum;
    }

    public int getTotalConsum() {
        return totalConsum;
    }

    public void setTotalConsum(int totalConsum) {
        this.totalConsum = totalConsum;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(int deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public void setBenefitType(String benefitType) {
        this.benefitType = benefitType;
    }

    public int getBenefitRate() {
        return benefitRate;
    }

    public void setBenefitRate(int benefitRate) {
        this.benefitRate = benefitRate;
    }

    public int getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(int benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public int getParkedAmount() {
        return parkedAmount;
    }

    public void setParkedAmount(int parkedAmount) {
        this.parkedAmount = parkedAmount;
    }

    public int getCouponConsum() {
        return couponConsum;
    }

    public void setCouponConsum(int couponConsum) {
        this.couponConsum = couponConsum;
    }

    public String getBeginStation() {
        return beginStation;
    }

    public void setBeginStation(String beginStation) {
        this.beginStation = beginStation;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public String getEndLocationTxt() {
        return endLocationTxt;
    }

    public void setEndLocationTxt(String endLocationTxt) {
        this.endLocationTxt = endLocationTxt;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsParkingOut() {
        return isParkingOut;
    }

    public void setIsParkingOut(String isParkingOut) {
        this.isParkingOut = isParkingOut;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }


    class CarBrandModels {

        public String brandId;
        public String brandModelsCode;
        public String brandName;
        public String modelNumber;
        public String carDisposition;
        public long modelPrice;
        public String createdBy;
        public Date createdTime;
        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }
        public String getBrandId() {
            return brandId;
        }

        public void setBrandModelsCode(String brandModelsCode) {
            this.brandModelsCode = brandModelsCode;
        }
        public String getBrandModelsCode() {
            return brandModelsCode;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
        public String getBrandName() {
            return brandName;
        }

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }
        public String getModelNumber() {
            return modelNumber;
        }

        public void setCarDisposition(String carDisposition) {
            this.carDisposition = carDisposition;
        }
        public String getCarDisposition() {
            return carDisposition;
        }

        public void setModelPrice(long modelPrice) {
            this.modelPrice = modelPrice;
        }
        public long getModelPrice() {
            return modelPrice;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }
        public Date getCreatedTime() {
            return createdTime;
        }

    }
    class Car {

        public String carId;
        public String carCode;
        public String brandModelsCode;
        public String cityCode;
        public CarBrandModels carBrandModels;
        public String status;
        public String plateNumber;
        public String boxNumber;
        public int carSeatNum;
        public String color;
        public String carImgPath;
        public String doorSatus;
        public String gear;
        public String speed;
        public String mileage;
        public String batteryVoltage;
        public String voltage;
        public String electricity;
        public String oddPower;
        public String oddMileage;
        public String electricizeStatus;
        public String networkSignal;
        public String locationStatus;
        public String satelliteCount;
        public String longitude;
        public String latitude;
        public String headingAngle;
        public int enduranceMileage;
        public int chargeElectric;
        public String createdBy;
        public Date createdTime;
        public Date updatedTime;

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarCode(String carCode) {
            this.carCode = carCode;
        }

        public String getCarCode() {
            return carCode;
        }

        public void setBrandModelsCode(String brandModelsCode) {
            this.brandModelsCode = brandModelsCode;
        }

        public String getBrandModelsCode() {
            return brandModelsCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCarBrandModels(CarBrandModels carBrandModels) {
            this.carBrandModels = carBrandModels;
        }

        public CarBrandModels getCarBrandModels() {
            return carBrandModels;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setBoxNumber(String boxNumber) {
            this.boxNumber = boxNumber;
        }

        public String getBoxNumber() {
            return boxNumber;
        }

        public void setCarSeatNum(int carSeatNum) {
            this.carSeatNum = carSeatNum;
        }

        public int getCarSeatNum() {
            return carSeatNum;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setCarImgPath(String carImgPath) {
            this.carImgPath = carImgPath;
        }

        public String getCarImgPath() {
            return carImgPath;
        }

        public void setDoorSatus(String doorSatus) {
            this.doorSatus = doorSatus;
        }

        public String getDoorSatus() {
            return doorSatus;
        }

        public void setGear(String gear) {
            this.gear = gear;
        }

        public String getGear() {
            return gear;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getSpeed() {
            return speed;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public String getMileage() {
            return mileage;
        }

        public void setBatteryVoltage(String batteryVoltage) {
            this.batteryVoltage = batteryVoltage;
        }

        public String getBatteryVoltage() {
            return batteryVoltage;
        }

        public void setVoltage(String voltage) {
            this.voltage = voltage;
        }

        public String getVoltage() {
            return voltage;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
        }

        public String getElectricity() {
            return electricity;
        }

        public void setOddPower(String oddPower) {
            this.oddPower = oddPower;
        }

        public String getOddPower() {
            return oddPower;
        }

        public void setOddMileage(String oddMileage) {
            this.oddMileage = oddMileage;
        }

        public String getOddMileage() {
            return oddMileage;
        }

        public void setElectricizeStatus(String electricizeStatus) {
            this.electricizeStatus = electricizeStatus;
        }

        public String getElectricizeStatus() {
            return electricizeStatus;
        }

        public void setNetworkSignal(String networkSignal) {
            this.networkSignal = networkSignal;
        }

        public String getNetworkSignal() {
            return networkSignal;
        }

        public void setLocationStatus(String locationStatus) {
            this.locationStatus = locationStatus;
        }

        public String getLocationStatus() {
            return locationStatus;
        }

        public void setSatelliteCount(String satelliteCount) {
            this.satelliteCount = satelliteCount;
        }

        public String getSatelliteCount() {
            return satelliteCount;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setHeadingAngle(String headingAngle) {
            this.headingAngle = headingAngle;
        }

        public String getHeadingAngle() {
            return headingAngle;
        }

        public void setEnduranceMileage(int enduranceMileage) {
            this.enduranceMileage = enduranceMileage;
        }

        public int getEnduranceMileage() {
            return enduranceMileage;
        }

        public void setChargeElectric(int chargeElectric) {
            this.chargeElectric = chargeElectric;
        }

        public int getChargeElectric() {
            return chargeElectric;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setUpdatedTime(Date updatedTime) {
            this.updatedTime = updatedTime;
        }

        public Date getUpdatedTime() {
            return updatedTime;
        }

    }



}
