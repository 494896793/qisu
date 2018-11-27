package www.qisu666.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Created by zsd on 2016/7/11.
 */
public class PermissionUtils {
    //判断应用是否允许某个运行时权限
    public static boolean hasRunningPermission(Context context, String permission){

        boolean result;

//        int targetSdkVersion = 0;
//
//        try {
//            final PackageInfo info = context.getPackageManager().getPackageInfo(
//                    context.getPackageName(), 0);
//            targetSdkVersion = info.applicationInfo.targetSdkVersion;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
//
////            if (targetSdkVersion >= Build.VERSION_CODES.M) {
////                // targetSdkVersion >= Android M, we can
////                // use Context#checkSelfPermission
////                result = context.checkSelfPermission(permission)
////                        == PackageManager.PERMISSION_GRANTED;
////            } else {
//                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
////            }
        }else{
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return  result;
//        return  PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
