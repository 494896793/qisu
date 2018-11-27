package www.qisu666.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/7/9.
 * 版本工具
 */
public class VersionUtils {

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 比较版本号
     * @param currentVersion @require
     * @param targetVersion @require
     * @return int
     * @throws Exception
     */
    public static int checkVersion(String currentVersion, String targetVersion) throws Exception{

        String[] current = currentVersion.split("\\.");
        String[] target = targetVersion.split("\\.");
        for(int i = 0; i < current.length; i++){
            if(Integer.valueOf(current[i]) < Integer.valueOf(target[i])){
                return 1;
            } else if(Integer.valueOf(current[i]) > Integer.valueOf(target[i])){
                break;
            }
        }
        return -1;
    }


}
