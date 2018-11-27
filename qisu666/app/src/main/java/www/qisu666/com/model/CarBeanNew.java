package www.qisu666.com.model;

import java.util.List;

/**
 * 717219917@qq.com 2018/7/18 10:04.
 */
public class CarBeanNew {


    /**
     * stationInfo : {"useArkingLot":"523","stationCode":"SI000001","parkingLot":"200","lng":"113.968971","distance":1.2519,"stationName":"金谷创业园","label":"金谷创业园","lat":"22.564318"}
     * carList : [{"stationCode":"SI000001","brandName":"比亚迪","color":"白","modelPrice":1.5E7,"adcode":"440305","cityCode":"440305","carImgPath":"/images/car/20180509/yc_52@3x.png","latitude":"22.563982","oddPowerForNE":"0","boxNumber":"E61800100165","carSeatNum":5,"plateNumber":"粤BD93926","oddMileage":"288","lastLocationTxt":"金谷创业园","carCode":"CA000003","cityName":"深圳市","areaName":"南山区","carDisposition":"E5","bootType":"1","modelNumber":"E5","stationName":"金谷创业园","brandModelsCode":"BR00002","status":"1","longitude":"113.969673"},{"stationCode":"SI000001","brandName":"比亚迪","color":"白","modelPrice":1.5E7,"adcode":"440305","cityCode":"440305","carImgPath":"/images/car/20180509/yc_52@3x.png","latitude":"22.564821","oddPowerForNE":"0","boxNumber":"E61800100164","carSeatNum":5,"plateNumber":"粤BDB2928","oddMileage":"357","lastLocationTxt":"金谷创业园","carCode":"CA000001","cityName":"深圳市","areaName":"南山区","carDisposition":"E5","bootType":"1","modelNumber":"E5","stationName":"金谷创业园","brandModelsCode":"BR00002","status":"1","longitude":"113.968605"},{"stationCode":"SI000001","brandName":"比亚迪","color":"白","modelPrice":1.5E7,"adcode":"440305","cityCode":"440305","carImgPath":"/images/car/20180509/yc_52@3x.png","latitude":"22.564346","oddPowerForNE":"0","boxNumber":"E61800100180","carSeatNum":5,"plateNumber":"粤BDB6306","oddMileage":"375","lastLocationTxt":"金谷创业园","carCode":"CA000004","cityName":"深圳市","areaName":"南山区","carDisposition":"E5","bootType":"1","modelNumber":"E5","stationName":"金谷创业园","brandModelsCode":"BR00002","status":"1","longitude":"113.969452"}]
     */

    private StationInfo stationInfo;
    private List<CarList> carList;

    public StationInfo getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(StationInfo stationInfo) {
        this.stationInfo = stationInfo;
    }

    public List<CarList> getCarList() {
        return carList;
    }

    public void setCarList(List<CarList> carList) {
        this.carList = carList;
    }

    public static class StationInfo {
        /**
         * useArkingLot : 523
         * stationCode : SI000001
         * parkingLot : 200
         * lng : 113.968971
         * distance : 1.2519
         * stationName : 金谷创业园
         * label : 金谷创业园
         * lat : 22.564318
         */

        private String useArkingLot;
        private String stationCode;
        private String parkingLot;
        private String lng;
        private double distance;
        private String stationName;
        private String label;
        private String lat;

        public String getUseArkingLot() {
            return useArkingLot;
        }

        public void setUseArkingLot(String useArkingLot) {
            this.useArkingLot = useArkingLot;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getParkingLot() {
            return parkingLot;
        }

        public void setParkingLot(String parkingLot) {
            this.parkingLot = parkingLot;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }

    public static class CarList {
        /**
         * stationCode : SI000001
         * brandName : 比亚迪
         * color : 白
         * modelPrice : 1.5E7
         * adcode : 440305
         * cityCode : 440305
         * carImgPath : /images/car/20180509/yc_52@3x.png
         * latitude : 22.563982
         * oddPowerForNE : 0
         * boxNumber : E61800100165
         * carSeatNum : 5
         * plateNumber : 粤BD93926
         * oddMileage : 288
         * lastLocationTxt : 金谷创业园
         * carCode : CA000003
         * cityName : 深圳市
         * areaName : 南山区
         * carDisposition : E5
         * bootType : 1
         * modelNumber : E5
         * stationName : 金谷创业园
         * brandModelsCode : BR00002
         * status : 1
         * longitude : 113.969673
         */

        private String stationCode;
        private String brandName;
        private String color;
        private double modelPrice;
        private String adcode;
        private String cityCode;
        private String carImgPath;
        private String latitude;
        private String oddPowerForNE;
        private String boxNumber;
        private int carSeatNum;
        private String plateNumber;
        private String oddMileage;
        private String lastLocationTxt;
        private String carCode;
        private String cityName;
        private String areaName;
        private String carDisposition;
        private String bootType;
        private String modelNumber;
        private String stationName;
        private String brandModelsCode;
        private String status;
        private String longitude;

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public double getModelPrice() {
            return modelPrice;
        }

        public void setModelPrice(double modelPrice) {
            this.modelPrice = modelPrice;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCarImgPath() {
            return carImgPath;
        }

        public void setCarImgPath(String carImgPath) {
            this.carImgPath = carImgPath;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getOddPowerForNE() {
            return oddPowerForNE;
        }

        public void setOddPowerForNE(String oddPowerForNE) {
            this.oddPowerForNE = oddPowerForNE;
        }

        public String getBoxNumber() {
            return boxNumber;
        }

        public void setBoxNumber(String boxNumber) {
            this.boxNumber = boxNumber;
        }

        public int getCarSeatNum() {
            return carSeatNum;
        }

        public void setCarSeatNum(int carSeatNum) {
            this.carSeatNum = carSeatNum;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getOddMileage() {
            return oddMileage;
        }

        public void setOddMileage(String oddMileage) {
            this.oddMileage = oddMileage;
        }

        public String getLastLocationTxt() {
            return lastLocationTxt;
        }

        public void setLastLocationTxt(String lastLocationTxt) {
            this.lastLocationTxt = lastLocationTxt;
        }

        public String getCarCode() {
            return carCode;
        }

        public void setCarCode(String carCode) {
            this.carCode = carCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCarDisposition() {
            return carDisposition;
        }

        public void setCarDisposition(String carDisposition) {
            this.carDisposition = carDisposition;
        }

        public String getBootType() {
            return bootType;
        }

        public void setBootType(String bootType) {
            this.bootType = bootType;
        }

        public String getModelNumber() {
            return modelNumber;
        }

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getBrandModelsCode() {
            return brandModelsCode;
        }

        public void setBrandModelsCode(String brandModelsCode) {
            this.brandModelsCode = brandModelsCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
