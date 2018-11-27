package www.qisu666.com.model;

/**
 * 717219917@qq.com ${DATA} 16:39.
 */


import java.util.List;

/**新的计费规则,接口地址/api/policy/driver/query 字段名查看redmine*/
public class CarPolicyBeanNew {

    /**司机套餐：10公里以内费用,元*/
    public String driverPolicy1;

    /**司机套餐：10-20公里费用,元*/
    public String driverPolicy2;

    /**司机套餐：超过20公里2元每公里费用,元*/
    public String driverPolicy3;

    /**里程计费,元/公里*/
    public double chargingByMileage;

    /**汽车编码*/
    public String carCode;

    /**自驾日租套餐费用,元*/
    public int dayCost;

    public List<PolicyList> policyList;

   /**最低消费,元*/
    public int miniConsum;

    /**电度费,元*/
    public double electricityCharge; 


    /***时间段list*/
    public class PolicyList {
        /**分时结束时段*/
        private String timeEnd;
        /**分时开始时段*/
        private String timeBegin;
        /**分时计费,元/分*/
        private double chargePrice;

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getTimeBegin() {
            return timeBegin;
        }

        public void setTimeBegin(String timeBegin) {
            this.timeBegin = timeBegin;
        }

        public double getChargePrice() {
            return chargePrice;
        }

        public void setChargePrice(double chargePrice) {
            this.chargePrice = chargePrice;
        }
    }





}
