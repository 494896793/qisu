package www.qisu666.sdk.partner.bean;

/**
 * 717219917@qq.com 2018/4/23 20:04.
 */

import java.util.ArrayList;
import java.util.List;

/**收到分红的bean  在30天详情页面中用到*/
public class Bean_RecMoney {

     public boolean isCustomer =false;//是否消费型
     public List<Bean_MyInvest.MonthProfitList>monthProfitLists=new ArrayList<>();                //当月分红list
     public  List<Bean_MyInvest.ReceiveMonthProfitList>receiveMonthProfitLists=new ArrayList<>();  //当月未分红lsit
     public  double monthProfit;           //当月累计收益
     public  double receiveMonthProfit;    //当月未分红收益


}
