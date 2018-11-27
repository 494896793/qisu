package www.qisu666.com.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.util.PermissionUtil;

/**
 * Created by zsd on 2016/6/2.
 * 获取手机相关信息的工具类
 */
public class PhoneParams {

    public static String getAppVersion(Context context){
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    @SuppressLint({"HardwareIds", "MissingPermission" })
    public static String getDeviceUUID(Activity context){
        String IMEI = "404";
        try {
            IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }catch (Exception e){
            LogUtils.e("权限受制：读取手机uuid失败");
        }
        return IMEI;
    }

    public static String getOsVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

}
