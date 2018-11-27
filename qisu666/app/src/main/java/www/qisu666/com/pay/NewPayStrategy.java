package www.qisu666.com.pay;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface NewPayStrategy {

    /**
     * 调用支付方法
     * @param charge_pile_seri 充电桩序列号
     * @param payAmount 支付金额
     * @param charge_pile_num
     * @param type 支付类型
     */
    void pay(String charge_pile_seri, String payAmount, String charge_pile_num, String type, String fee_gift);
}
