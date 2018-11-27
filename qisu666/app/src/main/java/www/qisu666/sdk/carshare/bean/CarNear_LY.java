package www.qisu666.sdk.carshare.bean;

import java.util.Date;
import java.util.List;

/**
 * 717219917@qq.com 2018/5/17 20:42.
 */
public class CarNear_LY  {

    private String lng;
    private String distance;
    private String stationName;
    private String label;
    private List<CarList> carList;
    private String lat;
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getLng() {
        return lng;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDistance() {
        return distance;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getStationName() {
        return stationName;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

    public void setCarList(List<CarList> carList) {
        this.carList = carList;
    }
    public List<CarList> getCarList() {
        return carList;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }

    public class CarList {

        private String brandName;
        private String color;
        private String lng;
        private double distance;
        private String carImgPath;
        private String oddPowerForNE;
        private double count;
        private double carSeatNum;
        private String plateNumber;
        private String type;
        private String oddMileage;
        private String carId;
        private String carCode;
        private String createdTime;
        private String modelNumber;
        private double RM;
        private String lat;
        private String status;
        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }
        public String getBrandName() {
            return brandName;
        }

        public void setColor(String color) {
            this.color = color;
        }
        public String getColor() {
            return color;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
        public String getLng() {
            return lng;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
        public double getDistance() {
            return distance;
        }

        public void setCarImgPath(String carImgPath) {
            this.carImgPath = carImgPath;
        }
        public String getCarImgPath() {
            return carImgPath;
        }

        public String getOddPowerForNE() {
            return oddPowerForNE;
        }

        public void setOddPowerForNE(String oddPowerForNE) {
            this.oddPowerForNE = oddPowerForNE;
        }
        public void setCount(double count) {
            this.count = count;
        }
        public double getCount() {
            return count;
        }

        public void setCarSeatNum(double carSeatNum) {
            this.carSeatNum = carSeatNum;
        }
        public double getCarSeatNum() {
            return carSeatNum;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }
        public String getPlateNumber() {
            return plateNumber;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setOddMileage(String oddMileage) {
            this.oddMileage = oddMileage;
        }
        public String getOddMileage() {
            return oddMileage;
        }

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

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
        public String getCreatedTime() {
            return createdTime;
        }

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }
        public String getModelNumber() {
            return modelNumber;
        }

        public void setRM(double RM) {
            this.RM = RM;
        }
        public double getRM() {
            return RM;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
        public String getLat() {
            return lat;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

    }


}