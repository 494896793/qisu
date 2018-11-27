package www.qisu666.com.model;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 */

public class CarPolicyBean {


    /**
     * driverPolicy1 : 80
     * policyList : [{"chargingOnTime":370,"chargingByMileage":1100,"timeType":"2","miniConsum":1300},{"timeEnd":23,"chargingOnTime":1800,"chargingByMileage":1200,"timeBegin":7,"timeType":"1","miniConsum":1800},{"timeEnd":19,"chargingOnTime":370,"chargingByMileage":1800,"timeBegin":8,"timeType":"1","miniConsum":1500}]
     * driverPolicy3 : 2
     * driverPolicy2 : 100
     */

    public String driverPolicy1;
    public String driverPolicy3;
    public String driverPolicy2;
    public List<PolicyListBean> policyList;

    public static class PolicyListBean {
        /**
         * chargingOnTime : 370.0
         * chargingByMileage : 1100.0
         * timeType : 2
         * miniConsum : 1300.0
         * timeEnd : 23.0
         * timeBegin : 7.0
         */

        public double chargingOnTime;
        public double chargingByMileage;
        public String timeType;
        public double miniConsum;
        public double timeEnd;
        public double timeBegin;
    }
}

