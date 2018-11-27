package www.qisu666.sdk.carshare.bean;

/**
 * 押金查询的bean
 * 717219917@qq.com 2018/5/15 16:07.
 */
public class Bean_deposit {

    private String user_id;
    private String deposit_id;

    public String getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(String depositMoney) {
        this.depositMoney = depositMoney;
    }

    public String getDespositStatus() {
        return despositStatus;
    }

    public void setDespositStatus(String despositStatus) {
        this.despositStatus = despositStatus;
    }

    private String depositMoney;
    private String despositStatus;

    public String getDepositMoneyOn() {
        return depositMoneyOn;
    }

    public void setDepositMoneyOn(String depositMoneyOn) {
        this.depositMoneyOn = depositMoneyOn;
    }

    private String depositMoneyOn;

    private String deposit_type;
    private String update_time;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setDeposit_id(String deposit_id) {
        this.deposit_id = deposit_id;
    }

    public String getDeposit_id() {
        return deposit_id;
    }


    public void setDeposit_type(String deposit_type) {
        this.deposit_type = deposit_type;
    }

    public String getDeposit_type() {
        return deposit_type;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

}
