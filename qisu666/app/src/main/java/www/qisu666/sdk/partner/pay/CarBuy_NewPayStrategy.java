package www.qisu666.sdk.partner.pay;

/**
 * 717219917@qq.com 2018/4/17 16:42.
 */
public interface CarBuy_NewPayStrategy {
        /**
         * 调用支付方法  userId;
         public static String  totalFee;
         public static String  subAmount;
         public static String  subType;
         public static String productCode;
         */
        void pay(String userId, String totalFee, String productCode, String subAmount,String subCount,String subType);
}
