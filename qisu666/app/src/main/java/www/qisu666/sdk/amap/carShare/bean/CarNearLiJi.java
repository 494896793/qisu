package www.qisu666.sdk.amap.carShare.bean;

import java.util.List;

/**
 * 717219917@qq.com 2018/5/17 17:06.
 */
public class CarNearLiJi {
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

        private String carCode;
        private String lng;
        private double distance;
        private int count;
        private String type;
        private String lat;
        private String status;
        public void setCarCode(String carCode) {
            this.carCode = carCode;
        }
        public String getCarCode() {
            return carCode;
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

        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
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