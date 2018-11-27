package www.qisu666.sdk.partner.bean;

/**
 * 717219917@qq.com 2018/4/17 16:59.
 *///认购的支付请求bean
public class Event_PayRequest {

    public  Event_PayRequest(){}
    public Event_PayRequest( String  userId_,String totalFee_,String subAmount_,String subType_,String productCode_){
         userId=userId_;
         totalFee=totalFee_;
         subAmount=subAmount_;
         subType=subType_;
         productCode=productCode_;
    }
    public   String  userId="";      //用户id
    public   String  totalFee="'";    //总认购金额
    public   String  subAmount="";   //认购金额
    public   String  subType="";     //认购类型
    public   String productCode="";  //认购产品编码
    public   String  subCount="";    //认购份数
    public   String  carbuy_model="";//汽车型号

}
