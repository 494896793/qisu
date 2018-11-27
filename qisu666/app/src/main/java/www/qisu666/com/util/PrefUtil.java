package www.qisu666.com.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2018/1/30.
 */

public class PrefUtil {

    public static final String UID_KEY = "uid";

    public static final String ORDER_CODE = "orderCode";
    public static final String CAR_CODE = "carCode";
    public static final String DRIVER_TYPE = "driverType";

    private static final String PREFERENCE_NAME = "qisuchong";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }


    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context,String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static void saveBoolean(Context context,int keyResId, Boolean value) {
        String key = context.getString(keyResId);
        saveBoolean(context,key, value);
    }


    public static void saveBoolean(Context context,String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static Boolean getBoolean(Context context,String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }


    public static Boolean getBoolean(Context context,String key, boolean def) {
        return getSharedPreferences(context).getBoolean(key, def);
    }


    public static Boolean getBoolean(Context context,int keyResId, boolean def) {
        String key = context.getString(keyResId);
        return getSharedPreferences(context).getBoolean(key, def);
    }


    public static int getInt(Context context,String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }


    public static void saveInt(Context context,String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

}
