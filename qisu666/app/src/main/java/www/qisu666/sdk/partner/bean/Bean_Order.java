package www.qisu666.sdk.partner.bean;

/**
 * 717219917@qq.com 2018/4/18 10:52.
 */
/**支付订单bean*/
public class Bean_Order  {

    private String orderNo="";
    private String requestParam="";
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }
    public String getRequestParam() {
        return requestParam;
    }

}
