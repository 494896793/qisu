package www.qisu666.com.entity;

import java.util.List;

/**
 * 717219917@qq.com 2018/8/15 15:12.
 */
public class MyInverstEntity {

    private List<InverstOrderEntity> list;
    private String receiveMonthProfit;
    private String totalProfit;
    private String monthProfit;
    private String totalSubscribeCount;

    public MyInverstEntity() {
    }

    public List<InverstOrderEntity> getList() {
        return list;
    }

    public void setList(List<InverstOrderEntity> list) {
        this.list = list;
    }

    public String getReceiveMonthProfit() {
        return receiveMonthProfit;
    }

    public void setReceiveMonthProfit(String receiveMonthProfit) {
        this.receiveMonthProfit = receiveMonthProfit;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getMonthProfit() {
        return monthProfit;
    }

    public void setMonthProfit(String monthProfit) {
        this.monthProfit = monthProfit;
    }

    public String getTotalSubscribeCount() {
        return totalSubscribeCount;
    }

    public void setTotalSubscribeCount(String totalSubscribeCount) {
        this.totalSubscribeCount = totalSubscribeCount;
    }
}
