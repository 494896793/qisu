package www.qisu666.common.utils;

import java.math.BigDecimal;

/**
 * 717219917@qq.com 2018/6/1 15:53.
 * @author layoute
 */
public class CurrencyUtil {
    /**
     * 从以分为单位的金额获取正常的金额
     * @param money
     * @return
     */
    public static String getNormalMoney (Double money) {
        BigDecimal moneyDecimal = new BigDecimal(money / 100).setScale(BigDecimal.ROUND_HALF_UP);
        return moneyDecimal.doubleValue() + "元";
    }
}
