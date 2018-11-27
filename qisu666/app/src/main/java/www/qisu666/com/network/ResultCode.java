package www.qisu666.com.network;

import java.util.HashMap;
import java.util.Map;

/**
 * 717219917@qq.com 2018/6/14 13:55.
 */
public class ResultCode {

    /** 平台各类消息状态
     *  ASCII异常状态码
     10×× 平台内模块交互状态
     20×× 第三方模块交互状态
     30×× 重定向等请求错误
     40×× 客户端类资源错误
     50×× 服务器错误
     成功类验证码为正数，失败及异常类验证码为负数
     默认1000、2000、3000、4000、5000开始
     */

    /**
     * 用户公共状态返回码集合
     */
    public static Map<Integer,String> U_PUBMODE=new HashMap<Integer,String>();

    /**
     * 用户认证状态返回码集合
     */
    public static Map<Integer,String> U_AUTHMODE=new HashMap<Integer,String>();

    /**
     * 用户信息状态返回码集合
     */
    public static Map<Integer,String> U_USERMODE=new HashMap<Integer,String>();

    /**
     * 用户公共状态码
     */
    public static final Integer U_PUB_PHONE_ERROR = -1001;
    public static final Integer U_PUB_PHONE_NOTNULL = -1002;
    public static final Integer U_PUB_QUERY_NODATA = -1008;
    public static final Integer U_PUB_PHONEFORMAT_ERROR = -1012;
    public static final Integer U_PUB_REGID_ERROR = -1013;
    public static final Integer U_PUB_TIME_NOTNULL = -1014;
    public static final Integer U_PUB_IP_NOTNUL = -1020;
    public static final Integer U_PUB_REFRESH_FAIL = -1022;
    public static final Integer U_PUB_REFRESH_SUCCESS = 1023;
    public static final Integer U_PUB_QUERY_FAIL = -1024;
    public static final Integer U_PUB_QUERY_SUCCESS = 1025;
    public static final Integer U_PUB_OPERATION_FAIL = -1026;
    public static final Integer U_PUB_OPERATION_SUCCESS = 1027;
    public static final Integer U_PUB_DATA_ERROR = -1029;
    public static final Integer U_PUB_ADD_SUCCESS = 1030;
    public static final Integer U_PUB_ADD_FAIL = -1032;
    public static final Integer U_PUB_UPDATE_SUCCESS = 1033;
    public static final Integer U_PUB_UPDATE_FAIL = -1034;
    public static final Integer U_PUB_EXCEPTION = -1035;
    public static final Integer U_PUB_REGWAY_ERROR = -1036;
    public static final Integer U_PUB_MEGTRPE_EXCEPTION = -1039;
    public static final Integer U_PUB_PARAM_ERROR = -1040;
    public static final Integer U_PUB_PARAM_NOTNUL = -1041;
    public static final Integer U_PUB_UPLOAD_SUCCESS = 1042;
    public static final Integer U_PUB_UPLOAD_FAIL = -1043;
    public static final Integer U_PUB_AUTH_SUCCESS = 1044;
    public static final Integer U_PUB_AUTH_FAIL = -1045;

    /**
     * 用户认证状态码
     */
    public static final Integer U_AUTH_IDCARD_REPEAT = -1130;
    public static final Integer U_AUTH_IDCARD_FRONT_FAIL = -1131;
    public static final Integer U_AUTH_LICENSE_REPEAT = 1132;
    public static final Integer U_AUTH_IDCARD_BACK_FAIL = -1133;
    public static final Integer U_AUTH_LICENSE_FRONT_SUCCESS = 1134;
    public static final Integer U_AUTH_LICENSE_FRONT_FAIL = -1135;
    public static final Integer U_AUTH_LICENSE_BACK_SUCCESS = -1136;
    public static final Integer U_AUTH_LICENSE_BACK_FAIL = -1137;
    public static final Integer U_AUTH_VERIFY_SUCCESS = -1138;
    public static final Integer U_AUTH_VERIFY_FAIL = -1139;
    public static final Integer U_AUTH_IDCARD_YES = -1140;
    public static final Integer U_AUTH_LICENSE_YES = -1141;
    public static final Integer U_AUTH_IDCARD_OVERDUE = -1142;
    public static final Integer U_AUTH_IDCARD_COUNT_3 = -1143;
    public static final Integer U_AUTH_LICENSE_COUNT_3 = -1144;
    public static final Integer U_AUTH_IDCARD_NO = -1145;
    public static final Integer U_AUTH_LICENSE_NO = -1146;
    public static final Integer U_AUTH_IDCARD_FAILD=1163;
    public static final Integer U_AUTH_DRIVER_FAILD=1164;
    public static final Integer U_AUTH_IDCARD_LICENSE_NO = -1147;
    public static final Integer U_AUTH_IDCARD_FRONT_VALID_NOT = -1148;
    public static final Integer U_AUTH_IDCARD_BACK_VALID_NOT = -1149;
    public static final Integer U_AUTH_VERIFY_VALID_NOT = -1150;
    public static final Integer U_AUTH_FRONT_NOT = -1151;
    public static final Integer U_AUTH_BACK_NOT = -1152;
    public static final Integer U_AUTH_IDCARD_CERT_NOT = -1153;
    public static final Integer U_AUTH_LICENSE_CERT_NOT = -1154;
    public static final Integer U_AUTH_LICENSE_OVERDUE = -1155;
    public static final Integer U_AUTH_IDENTIFY_EXCEPTION = -1156;

    public static final Integer U_AUTH_IDCARD_HUMAN_REVIEW = -1157;
    public static final Integer U_AUTH_LICENSE_HUMAN_REVIEW = -1158;
    public static final Integer U_AUTH_IDCARD_ON_REVIEW = -1159;
    public static final Integer U_AUTH_LICENSE_ON_REVIEW = -1160;

    /**
     * 用户信息相关
     */
    public static final Integer U_USER_LOGIN_SUCCESS = 1200;
    public static final Integer U_USER_LOGIN_FAIL = -1201;
    public static final Integer U_USER_CODE_NOTNULL = -1202;

    static{
        U_PUBMODE.put(U_PUB_PHONE_ERROR, "手机号码输入有误");
        U_PUBMODE.put(U_PUB_PHONE_NOTNULL, "手机号不能为空");
        U_PUBMODE.put(U_PUB_QUERY_NODATA, "暂无查询数据");
        U_PUBMODE.put(U_PUB_PHONEFORMAT_ERROR, "手机格式错误");
        U_PUBMODE.put(U_PUB_REGID_ERROR, "推送错误");
        U_PUBMODE.put(U_PUB_TIME_NOTNULL, "时间不能为空");
        U_PUBMODE.put(U_PUB_IP_NOTNUL, "ip地址不能为空");
        U_PUBMODE.put(U_PUB_REFRESH_FAIL, "刷新失败");
        U_PUBMODE.put(U_PUB_REFRESH_SUCCESS, "刷新成功");
        U_PUBMODE.put(U_PUB_QUERY_FAIL, "查询失败");
        U_PUBMODE.put(U_PUB_QUERY_SUCCESS, "查询成功");
        U_PUBMODE.put(U_PUB_OPERATION_FAIL, "操作失败");
        U_PUBMODE.put(U_PUB_OPERATION_SUCCESS, "操作成功");
        U_PUBMODE.put(U_PUB_DATA_ERROR, "数据错误");
        U_PUBMODE.put(U_PUB_ADD_SUCCESS, "添加成功");
        U_PUBMODE.put(U_PUB_ADD_FAIL, "添加失败");
        U_PUBMODE.put(U_PUB_UPDATE_SUCCESS, "修改成功");
        U_PUBMODE.put(U_PUB_UPDATE_FAIL, "修改失败");
        U_PUBMODE.put(U_PUB_EXCEPTION, "出现异常");
        U_PUBMODE.put(U_PUB_REGWAY_ERROR, "非法链接");
        U_PUBMODE.put(U_PUB_MEGTRPE_EXCEPTION, "短信发送异常");
        U_PUBMODE.put(U_PUB_PARAM_ERROR, "参数有误");
        U_PUBMODE.put(U_PUB_PARAM_NOTNUL, "必传参数不能为空");
        U_PUBMODE.put(U_PUB_UPLOAD_SUCCESS, "上传成功");
        U_PUBMODE.put(U_PUB_UPLOAD_FAIL, "上传失败");
        U_PUBMODE.put(U_PUB_AUTH_SUCCESS, "认证成功");
        U_PUBMODE.put(U_PUB_AUTH_FAIL, "认证失败");

        U_AUTHMODE.put(U_AUTH_IDCARD_REPEAT, "您已通过身份证认证,无需再重复认证");
        U_AUTHMODE.put(U_AUTH_IDCARD_FRONT_FAIL, "身份证正面照认证失败");
        U_AUTHMODE.put(U_AUTH_LICENSE_REPEAT, "您已通过驾驶证认证,无需再重复认证");
        U_AUTHMODE.put(U_AUTH_IDCARD_BACK_FAIL, "身份证背面照认证失败");
        U_AUTHMODE.put(U_AUTH_LICENSE_FRONT_SUCCESS, "驾驶证正面照认证成功");
        U_AUTHMODE.put(U_AUTH_LICENSE_FRONT_FAIL, "驾驶证正面照认证失败");
        U_AUTHMODE.put(U_AUTH_LICENSE_BACK_SUCCESS, "驾驶证背面照认证成功");
        U_AUTHMODE.put(U_AUTH_LICENSE_BACK_FAIL, "驾驶证背面照认证失败");
        U_AUTHMODE.put(U_AUTH_VERIFY_SUCCESS, "活体检测比对成功");
        U_AUTHMODE.put(U_AUTH_VERIFY_FAIL, "活体检测比对失败,请重新识别");
        U_AUTHMODE.put(U_AUTH_IDCARD_YES, "您的身份证信息,已被认证");
        U_AUTHMODE.put(U_AUTH_LICENSE_YES, "您的驾驶证信息,已被认证");
        U_AUTHMODE.put(U_AUTH_IDCARD_OVERDUE, "身份证已过期");
        U_AUTHMODE.put(U_AUTH_IDCARD_COUNT_3, "身份证认证今日已达3次,请24小时后重新认证");
        U_AUTHMODE.put(U_AUTH_LICENSE_COUNT_3, "驾驶证认证今日已达3次,请24小时后重新认证");
        U_AUTHMODE.put(U_AUTH_IDCARD_NO, "身份证未认证");
        U_AUTHMODE.put(U_AUTH_LICENSE_NO, "驾驶证未认证");
        U_AUTHMODE.put(U_AUTH_IDCARD_LICENSE_NO, "您已认证的身份证与您的驾驶证不匹配");
        U_AUTHMODE.put(U_AUTH_IDCARD_FRONT_VALID_NOT, "身份证正面照验证不通过,请检查照片是否清晰");
        U_AUTHMODE.put(U_AUTH_IDCARD_BACK_VALID_NOT, "身份证背面照验证不通过,请检查照片是否清晰");
        U_AUTHMODE.put(U_AUTH_VERIFY_VALID_NOT, "活体检测比对验证不通过,请确认是否本人");
        U_AUTHMODE.put(U_AUTH_FRONT_NOT, "上传有误,请上传正面证件照");
        U_AUTHMODE.put(U_AUTH_BACK_NOT, "上传有误,请上传背面证件照");
        U_AUTHMODE.put(U_AUTH_IDCARD_CERT_NOT, "上传的身份证照片与输入的信息不一致");
        U_AUTHMODE.put(U_AUTH_LICENSE_CERT_NOT, "上传的驾驶证照片与输入的信息不一致");
        U_AUTHMODE.put(U_AUTH_LICENSE_OVERDUE, "驾驶证已过期");
        U_AUTHMODE.put(U_AUTH_IDENTIFY_EXCEPTION, "证件照识别异常,请重新拍摄上传");
        U_AUTHMODE.put(U_AUTH_IDCARD_HUMAN_REVIEW, "身份证认证今日已达3次,请走人工审核");
        U_AUTHMODE.put(U_AUTH_LICENSE_HUMAN_REVIEW, "驾驶证认证今日已达3次,请走人工审核");
        U_AUTHMODE.put(U_AUTH_IDCARD_ON_REVIEW, "身份证认证人工审核中");
        U_AUTHMODE.put(U_AUTH_LICENSE_ON_REVIEW, "驾驶证认证人工审核中");

        U_USERMODE.put(U_USER_LOGIN_SUCCESS, "用户验证已登录");
        U_USERMODE.put(U_USER_LOGIN_FAIL, "用户登录已失效,请重新登录");
        U_USERMODE.put(U_USER_CODE_NOTNULL, "用户唯一编号不能为空");

    }

}
