package www.qisu666.sdk.partner.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**  我的认购的bean
 * 717219917@qq.com 2018/4/16 19:53.
 */
public class Bean_MyInvest  {

    private List<UserSubscribeList> userSubscribeList=new ArrayList<>();
    private List<ReceiveMonthProfitList> receiveMonthProfitList=new ArrayList<>();
    private double receiveMonthProfit=-1;
    private double totalProfit=-1;
    private String subType="";

    public String getSubType() {
        return subType;
    }
    public void setSubType(String subType) {
        this.subType = subType;
    }

    private double monthProfit=-1;
    private List<MonthProfitList> monthProfitList=new ArrayList<>();
    private double totalSubscribeCount=-1;

    public void setUserSubscribeList(List<UserSubscribeList> userSubscribeList) {
        this.userSubscribeList = userSubscribeList;
    }
    public List<UserSubscribeList> getUserSubscribeList() {
        return userSubscribeList;
    }

    public void setReceiveMonthProfitList(List<ReceiveMonthProfitList> receiveMonthProfitList) {
        this.receiveMonthProfitList = receiveMonthProfitList;
    }
    public List<ReceiveMonthProfitList> getReceiveMonthProfitList() {
        return receiveMonthProfitList;
    }

    public void setReceiveMonthProfit(double receiveMonthProfit) {
        this.receiveMonthProfit = receiveMonthProfit;
    }
    public double getReceiveMonthProfit() {
        return receiveMonthProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
    public double getTotalProfit() {
        return totalProfit;
    }

    public void setMonthProfit(double monthProfit) {
        this.monthProfit = monthProfit;
    }
    public double getMonthProfit() {
        return monthProfit;
    }

    public void setMonthProfitList(List<MonthProfitList> monthProfitList) {
        this.monthProfitList = monthProfitList;
    }
    public List<MonthProfitList> getMonthProfitList() {
        return monthProfitList;
    }

    public void setTotalSubscribeCount(double totalSubscribeCount) {
        this.totalSubscribeCount = totalSubscribeCount;
    }
    public double getTotalSubscribeCount() {
        return totalSubscribeCount;
    }

    public class UserSubscribeList {

        private double countPeriods;
        private String contractStatus;
        private double useBonusAmount;
        private String userCode;
        private double totalBonusAmount;
        private String subStatus;
        private String subId;
        private String idcardNum;
        private String productTitle;
        private Date firstPhaseTime;
        private double subAmount;
        private double rechSubAmount;
        private String relName;
        private String subOrderNo;
        private Date contractExpiresTime;
        private String productCode;
        private double ROWNO;
        private String subCode;
        private String modelCode;
        private Date createdTime;
        private double subType;
        private double surplusAmount;
        public void setCountPeriods(double countPeriods) {
            this.countPeriods = countPeriods;
        }
        public double getCountPeriods() {
            return countPeriods;
        }

        public void setContractStatus(String contractStatus) {
            this.contractStatus = contractStatus;
        }
        public String getContractStatus() {
            return contractStatus;
        }

        public void setUseBonusAmount(double useBonusAmount) {
            this.useBonusAmount = useBonusAmount;
        }
        public double getUseBonusAmount() {
            return useBonusAmount;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }
        public String getUserCode() {
            return userCode;
        }

        public void setTotalBonusAmount(double totalBonusAmount) {
            this.totalBonusAmount = totalBonusAmount;
        }
        public double getTotalBonusAmount() {
            return totalBonusAmount;
        }

        public void setSubStatus(String subStatus) {
            this.subStatus = subStatus;
        }
        public String getSubStatus() {
            return subStatus;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }
        public String getSubId() {
            return subId;
        }

        public void setIdcardNum(String idcardNum) {
            this.idcardNum = idcardNum;
        }
        public String getIdcardNum() {
            return idcardNum;
        }

        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }
        public String getProductTitle() {
            return productTitle;
        }

        public void setFirstPhaseTime(Date firstPhaseTime) {
            this.firstPhaseTime = firstPhaseTime;
        }
        public Date getFirstPhaseTime() {
            return firstPhaseTime;
        }

        public void setSubAmount(double subAmount) {
            this.subAmount = subAmount;
        }
        public double getSubAmount() {
            return subAmount;
        }

        public void setRechSubAmount(double rechSubAmount) {
            this.rechSubAmount = rechSubAmount;
        }
        public double getRechSubAmount() {
            return rechSubAmount;
        }

        public void setRelName(String relName) {
            this.relName = relName;
        }
        public String getRelName() {
            return relName;
        }

        public void setSubOrderNo(String subOrderNo) {
            this.subOrderNo = subOrderNo;
        }
        public String getSubOrderNo() {
            return subOrderNo;
        }

        public void setContractExpiresTime(Date contractExpiresTime) {
            this.contractExpiresTime = contractExpiresTime;
        }
        public Date getContractExpiresTime() {
            return contractExpiresTime;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
        public String getProductCode() {
            return productCode;
        }

        public void setROWNO(double ROWNO) {
            this.ROWNO = ROWNO;
        }
        public double getROWNO() {
            return ROWNO;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }
        public String getSubCode() {
            return subCode;
        }

        public void setModelCode(String modelCode) {
            this.modelCode = modelCode;
        }
        public String getModelCode() {
            return modelCode;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }
        public Date getCreatedTime() {
            return createdTime;
        }

        public void setSubType(double subType) {
            this.subType = subType;
        }
        public double getSubType() {
            return subType;
        }

        public void setSurplusAmount(double surplusAmount) {
            this.surplusAmount = surplusAmount;
        }
        public double getSurplusAmount() {
            return surplusAmount;
        }

    }

    public class ReceiveMonthProfitList {

        private Date profitTime;
        private double profit;
        public void setProfitTime(Date profitTime) {
            this.profitTime = profitTime;
        }
        public Date getProfitTime() {
            return profitTime;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }
        public double getProfit() {
            return profit;
        }

    }

    public class MonthProfitList {

        private Date profitTime;
        private double profit;
        public void setProfitTime(Date profitTime) {
            this.profitTime = profitTime;
        }
        public Date getProfitTime() {
            return profitTime;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }
        public double getProfit() {
            return profit;
        }

    }



}