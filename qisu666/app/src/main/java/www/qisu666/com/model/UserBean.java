package www.qisu666.com.model;

/**
 * 717219917@qq.com 2018/7/26 10:07.
 */
public class UserBean {


    /**
     * userinfo : {"userStatus":"0","loginPWD":"4607e782c4d86fd5364d7e4508bb10d9","sex":"1","depositMoney":"150000","updateTime":"2018-06-28 15:14:16","mobileNo":"15986835876","isAdmin":0,"userName":"15986835876","VIPEnddate":"0","userId":10003070,"orgId":0,"picture":"7","createTime":"2018-06-28 15:14:16","depositStatus":"0","userType":"0","validity":"2018-06-28 15:14:16","invitationCode":"5E5C63"}
     * token : 201807260951490881
     */

    private Userinfo userinfo;
    private String token;

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Userinfo {
        /**
         * userStatus : 0
         * loginPWD : 4607e782c4d86fd5364d7e4508bb10d9
         * sex : 1
         * depositMoney : 150000
         * updateTime : 2018-06-28 15:14:16
         * mobileNo : 15986835876
         * isAdmin : 0
         * userName : 15986835876
         * VIPEnddate : 0
         * userId : 10003070
         * orgId : 0
         * picture : 7
         * createTime : 2018-06-28 15:14:16
         * depositStatus : 0
         * userType : 0
         * validity : 2018-06-28 15:14:16
         * invitationCode : 5E5C63
         */

        private String userStatus;
        private String loginPWD;
        private String sex;
        private String depositMoney;
        private String updateTime;
        private String mobileNo;
        private int isAdmin;
        private String userName;
        private String VIPEnddate;
        private String userId;
        private int orgId;
        private String picture;
        private String createTime;
        private String depositStatus;
        private String userType;
        private String validity;
        private String invitationCode;

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getLoginPWD() {
            return loginPWD;
        }

        public void setLoginPWD(String loginPWD) {
            this.loginPWD = loginPWD;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDepositMoney() {
            return depositMoney;
        }

        public void setDepositMoney(String depositMoney) {
            this.depositMoney = depositMoney;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public int getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(int isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getVIPEnddate() {
            return VIPEnddate;
        }

        public void setVIPEnddate(String VIPEnddate) {
            this.VIPEnddate = VIPEnddate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDepositStatus() {
            return depositStatus;
        }

        public void setDepositStatus(String depositStatus) {
            this.depositStatus = depositStatus;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }
    }
}
