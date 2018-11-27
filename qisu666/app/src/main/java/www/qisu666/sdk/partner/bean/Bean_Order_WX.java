package www.qisu666.sdk.partner.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 717219917@qq.com 2018/4/18 15:53.
 */
public class Bean_Order_WX {

    private String orderNo="";
    private RequestParam requestParam=new RequestParam();
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setRequestParam(RequestParam requestParam) {
        this.requestParam = requestParam;
    }
    public RequestParam getRequestParam() {
        return requestParam;
    }

    public class RequestParam {

        @SerializedName("package")
        private String packag;
        private String out_trade_no;
        private String appid;
        private String partnerid;
        private String prepayid;
        private String paysign;
        private String noncestr;
        private String timestamp;

        public void setPackage(String packag) {
            this.packag = packag;
        }
        public String getPackage() {
            return packag;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }
        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
        public String getAppid() {
            return appid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }
        public String getPartnerid() {
            return partnerid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }
        public String getPrepayid() {
            return prepayid;
        }

        public void setPaysign(String paysign) {
            this.paysign = paysign;
        }
        public String getPaysign() {
            return paysign;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }
        public String getNoncestr() {
            return noncestr;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getTimestamp() {
            return timestamp;
        }

    }


}