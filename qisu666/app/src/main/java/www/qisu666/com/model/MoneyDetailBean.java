package www.qisu666.com.model;

import java.util.List;

/**
 * 717219917@qq.com 2018/8/8 10:19.
 */
public class MoneyDetailBean {

    public MoneyDetailBean() {
    }

    private String feeGift;
    private String acctTotal;
    private String operationDate;
    private String sourceNo;
    private String chargeMoney;

    public MoneyDetailBean(String feeGift, String acctTotal, String operationDate, String sourceNo, String chargeMoney) {
        this.feeGift = feeGift;
        this.acctTotal = acctTotal;
        this.operationDate = operationDate;
        this.sourceNo = sourceNo;
        this.chargeMoney = chargeMoney;
    }

    public String getFeeGift() {
        return feeGift;
    }

    public void setFeeGift(String feeGift) {
        this.feeGift = feeGift;
    }

    public String getAcctTotal() {
        return acctTotal;
    }

    public void setAcctTotal(String acctTotal) {
        this.acctTotal = acctTotal;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
}
                                    