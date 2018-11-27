package www.qisu666.com.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by admin on 2018/1/26.
 */

public class CarNowOrderBean {


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
     */

    public String orderId;
    public String orderCode;
    public String city;
    public String borrowTime;
    public String returnTime;
    public BigDecimal beginMileage;
    public BigDecimal endMileage;
    public BigDecimal mileageExpenses;
    public BigDecimal timeCostCharge;
    public BigDecimal costMin;
    public BigDecimal costFinal;
    public String beginStation;
    public String driverType;
    public String borrowType;
    public String status;
    public String userCode;
    public String carCode;
    public BigDecimal driverCost;
    public String beginLocationTxt;
    public CarBean car;
    public long borrowTimeLong;
    public long createdTimeLong;
    public long updatedTimeLong;
    public long returnTimeLong;
    public DriverInfoBean driver;
    public String createdTime;
    public String updatedTime;


}
