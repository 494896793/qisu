package www.qisu666.com.event;

/**
 * 717219917@qq.com 2018/9/6 15:58.
 */
public class CarOrderLationEvent {
    public String lat;
    public String lon;

    public String getLat() {
        return lat;
    }

    public CarOrderLationEvent(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
