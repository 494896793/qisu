package www.qisu666.com.model;

import java.util.Date;

/**
 * Created by admin on 2018/1/25.
 */
//共享汽车模块 单个汽车点击之后
public class CarBean {
    public String carId;
    public String carCode;
    public String brandCode;
    public String brandName;
    public String modelNumber;
    public String status;
    public String plateNumber;
    public String boxNumber;
    public double carSeatNum;
    public String carImgPath;
    public String doorSatus;
    public String gear;
    public String speed;
    public String mileage;
    public String batteryVoltage;
    public String voltage;
    public String electricity;
    public String oddPowerForNE;
    public String oddMileage;
    public String electricizeStatus;
    public String networkSignal;
    public String locationStatus;
    public String satelliteCount;
    public String longitude;
    public String latitude;
    public String headingAngle;
    public String createdTime;
    public String updatedTime;
    public carBrandModels carBrandModels;
    public StationInfo stationInfo;
    public String bootType;//纯动力
    public String powerStatus;//开关动力 状态



    public static class carBrandModels {
        public String brandId;
        public String brandModelsCode;
        public String brandName;
        public String modelNumber;
        public String carDisposition;
        public long modelPrice;
        public String createdBy;
        public Date createdTime;
    } 

    public  carBrandModels getCarBrandModels() {
        return carBrandModels;
    }

    public StationInfo getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(StationInfo stationInfo) {
        this.stationInfo = stationInfo;
    }

    public void setCarBrandModels(carBrandModels carBrandModels) {
        this.carBrandModels = carBrandModels;
    }

 
      public class StationInfo {

          public String stationId;
          public String stationCode;
          public String lng;
          public String lat;
          public String parkingLot;
          public String useArkingLot;
          public String label;
          public String status;
          public Date createdTime;
      }
      
      
      
    
}
