package www.qisu666.sdk.amap.carShare.bean;

/**
 * 717219917@qq.com 2018/5/16 11:45.
 */
public class CarNear {

    private String carCode;
    private String lng;
    private double distance;
    private double count;
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

    public void setCount(double count) {
        this.count = count;
    }
    public double getCount() {
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