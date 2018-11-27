package www.qisu666.com.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 717219917@qq.com ${DATA} 10:51.
 */

//{"beginMileage":5465.0,"beginStation":"SI000001","borrowTime":"Apr 5, 2018 10:38:04 AM","borrowTimeLong":1522895884570,"borrowType":"0","car":{"batteryVoltage":"12.7","boxNumber":"E61700E90113","carBrandModels":{"brandId":"68EC8F6D019980FBE050682F756D627C","brandModelsCode":"BR00001","brandName":"江淮","carDisposition":"IEV7","createdBy":"test","createdTime":"Apr 3, 2018 02:10:16","modelNumber":"IEV","modelPrice":8000000},"carCode":"CA000003","carId":"BD63EEAFB6E84897AE5C1E91866B6748","carImgPath":"/images/car/20180123/1516641530523.jpg","carSeatNum":5,"createdTime":"Mar 30, 2018 2:09:01 PM","doorSatus":"0","electricity":"-0.2","electricizeStatus":"0","gear":"3","headingAngle":"5.14","latitude":"22.564165","locationStatus":"1","longitude":"113.969543","mileage":"5465","networkSignal":"22","oddMileage":"75","oddPower":"47","plateNumber":"粤BD78579","satelliteCount":"13","speed":"0","status":"2","updatedTime":"Apr 5, 2018 10:39:00 AM","voltage":"315.3"},"carCode":"CA000003","city":"440300","costMin":1500.0,"createdTime":"Apr 5, 2018 10:37:52 AM","createdTimeLong":1522895872216,"driverType":"0","endMileage":5465.0,"orderCode":"OI000040","orderId":"6911D37E4641FBFEE050682F756D7606","returnTime":"Apr 5, 2018 10:39:04 AM","returnTimeLong":1522895944843,"status":"3","updatedTime":"Apr 5, 2018 10:39:03 AM","updatedTimeLong":1522895943000,"userCode":"10002167"}

public class CarCompleteFinish {
    private int beginMileage;
    private String beginStation;
    private long borrowTimeLong;
    private String borrowType;
    private Car car;
    private String carCode;
    private String city;
    private int costMin;
    private long createdTimeLong;
    private String driverType;
    private int endMileage;
    private String orderCode;
    private String orderId;
    private long returnTimeLong;
    private String status;
    private long updatedTimeLong;
    private String userCode;

    public void setBeginMileage(int beginMileage) {
        this.beginMileage = beginMileage;
    }

    public int getBeginMileage() {
        return beginMileage;
    }

    public void setBeginStation(String beginStation) {
        this.beginStation = beginStation;
    }

    public String getBeginStation() {
        return beginStation;
    }


    public void setBorrowTimeLong(long borrowTimeLong) {
        this.borrowTimeLong = borrowTimeLong;
    }

    public long getBorrowTimeLong() {
        return borrowTimeLong;
    }

    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }

    public String getBorrowType() {
        return borrowType;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCostMin(int costMin) {
        this.costMin = costMin;
    }

    public int getCostMin() {
        return costMin;
    }

    public void setCreatedTimeLong(long createdTimeLong) {
        this.createdTimeLong = createdTimeLong;
    }

    public long getCreatedTimeLong() {
        return createdTimeLong;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setEndMileage(int endMileage) {
        this.endMileage = endMileage;
    }

    public int getEndMileage() {
        return endMileage;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public class CarBrandModels {

        private String brandId;
        private String brandModelsCode;
        private String brandName;
        private String carDisposition;
        private String createdBy;
        private Date createdTime;
        private String modelNumber;
        private long modelPrice;
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

        public void setCarDisposition(String carDisposition) {
            this.carDisposition = carDisposition;
        }
        public String getCarDisposition() {
            return carDisposition;
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

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }
        public String getModelNumber() {
            return modelNumber;
        }

        public void setModelPrice(long modelPrice) {
            this.modelPrice = modelPrice;
        }
        public long getModelPrice() {
            return modelPrice;
        }

    }
    public class Car {

        private String batteryVoltage;
        private String boxNumber;
        private CarBrandModels carBrandModels;
        private String carCode;
        private String carId;
        private String carImgPath;
        private int carSeatNum;
        private Date createdTime;
        private String doorSatus;
        private String electricity;
        private String electricizeStatus;
        private String gear;
        private String headingAngle;
        private String latitude;
        private String locationStatus;
        private String longitude;
        private String mileage;
        private String networkSignal;
        private String oddMileage;
        private String oddPower;
        private String plateNumber;
        private String satelliteCount;
        private String speed;
        private String status;
        private Date updatedTime;
        private String voltage;
        public void setBatteryVoltage(String batteryVoltage) {
            this.batteryVoltage = batteryVoltage;
        }
        public String getBatteryVoltage() {
            return batteryVoltage;
        }

        public void setBoxNumber(String boxNumber) {
            this.boxNumber = boxNumber;
        }
        public String getBoxNumber() {
            return boxNumber;
        }

        public void setCarBrandModels(CarBrandModels carBrandModels) {
            this.carBrandModels = carBrandModels;
        }
        public CarBrandModels getCarBrandModels() {
            return carBrandModels;
        }

        public void setCarCode(String carCode) {
            this.carCode = carCode;
        }
        public String getCarCode() {
            return carCode;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }
        public String getCarId() {
            return carId;
        }

        public void setCarImgPath(String carImgPath) {
            this.carImgPath = carImgPath;
        }
        public String getCarImgPath() {
            return carImgPath;
        }

        public void setCarSeatNum(int carSeatNum) {
            this.carSeatNum = carSeatNum;
        }
        public int getCarSeatNum() {
            return carSeatNum;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }
        public Date getCreatedTime() {
            return createdTime;
        }

        public void setDoorSatus(String doorSatus) {
            this.doorSatus = doorSatus;
        }
        public String getDoorSatus() {
            return doorSatus;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
        }
        public String getElectricity() {
            return electricity;
        }

        public void setElectricizeStatus(String electricizeStatus) {
            this.electricizeStatus = electricizeStatus;
        }
        public String getElectricizeStatus() {
            return electricizeStatus;
        }

        public void setGear(String gear) {
            this.gear = gear;
        }
        public String getGear() {
            return gear;
        }

        public void setHeadingAngle(String headingAngle) {
            this.headingAngle = headingAngle;
        }
        public String getHeadingAngle() {
            return headingAngle;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
        public String getLatitude() {
            return latitude;
        }

        public void setLocationStatus(String locationStatus) {
            this.locationStatus = locationStatus;
        }
        public String getLocationStatus() {
            return locationStatus;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
        public String getLongitude() {
            return longitude;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }
        public String getMileage() {
            return mileage;
        }

        public void setNetworkSignal(String networkSignal) {
            this.networkSignal = networkSignal;
        }
        public String getNetworkSignal() {
            return networkSignal;
        }

        public void setOddMileage(String oddMileage) {
            this.oddMileage = oddMileage;
        }
        public String getOddMileage() {
            return oddMileage;
        }

        public void setOddPower(String oddPower) {
            this.oddPower = oddPower;
        }
        public String getOddPower() {
            return oddPower;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }
        public String getPlateNumber() {
            return plateNumber;
        }

        public void setSatelliteCount(String satelliteCount) {
            this.satelliteCount = satelliteCount;
        }
        public String getSatelliteCount() {
            return satelliteCount;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }
        public String getSpeed() {
            return speed;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setUpdatedTime(Date updatedTime) {
            this.updatedTime = updatedTime;
        }
        public Date getUpdatedTime() {
            return updatedTime;
        }

        public void setVoltage(String voltage) {
            this.voltage = voltage;
        }
        public String getVoltage() {
            return voltage;
        }

    }

}


