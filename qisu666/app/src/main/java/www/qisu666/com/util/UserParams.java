package www.qisu666.com.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.BuildConfig;
import www.qisu666.com.R;
import www.qisu666.com.activity.LoginActivity;

/**
 * Created by Administrator on 2016/4/11.
 */
public enum  UserParams {

    INSTANCE;

    /**
     * SharedPreferences文件名
     */
    public static final String DEFULE = "userInfo";

    /**
     * SharedPreferences文件中存储的属性名
     */
    public static final String CUST_ALIAS = "cust_alias";
    public static final String S_TOKEN = "s_token";
    public static final String CUST_NO = "cust_no";
    public static final String TEL_NO = "tel_no";
    public static final String CUST_NAME = "cust_name";

    /**
     * token
     */
    private String s_token;
    /**
     * 客户号
     */
    private String cust_no;
    /**
     * 用户名/昵称
     */
    private String cust_alias;
    /**
     * 电话号
     */
    private String tel_no;
    /**
     * 性别
     */
    private String sex;
    /**
     * 用户id
     */
    private String user_id;
    /**
     * 用户最新的订单号
     */
    private String out_trade_no;
    /**
     * 账户余额
     */
    private String balance;
    /**
     * 头像id
     */
    private int picture = -1;
    /**
     * 是否Vip
     */
    private boolean isVip = false;

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getS_token() {
        return s_token;
    }

    public void setS_token(String s_token) {
        this.s_token = s_token;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getCust_alias() {
        return cust_alias;
    }

    public void setCust_alias(String cust_alias) {
        this.cust_alias = cust_alias;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void clear(){
        s_token = null;
        cust_no = null;
        cust_alias = null;
        tel_no = null;
        sex = null;
        user_id = null;
        out_trade_no = null;
        balance = null;
        picture = -1;
        isVip = false;
    }

    @Override
    public String toString() {
        return "user_id:" + user_id + ",s_token:" + s_token + ",user_name:" + cust_alias + ",phone_no:" + tel_no + ",sex:" + sex;
    }

    public boolean checkLogin(Activity context){
        // 跳过验证是否登陆
//        if (BuildConfig.DEBUG){
//            return true;
//        }
        boolean flag = !TextUtils.isEmpty(user_id);
        if(!flag){
            /*ToastUtil.showToast(R.string.toast_prompt_login);
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivityForResult(i, ConstantCode.REQ_LOGIN);*/
        }
        return flag;
    }
}
