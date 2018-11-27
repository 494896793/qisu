package www.qisu666.com.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by zhang on 2017/3/7.
 */

public class ActivityUtil {

    private static final String ONE_VALUE = "ONE_VALUE";
    private static final String TWO_VALUE = "TWO_VALUE";

    public static void startActivity(Context context,Class<?> cls)
    {
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }

    public static void startActivityClearTop(Context context,Class<?> cls)
    {
        Intent intent = new Intent(context,cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startActivityWithOne(Context context,Class<?> cls,String extraValue)
    {
        Intent intent = new Intent(context,cls);
        intent.putExtra(ONE_VALUE,extraValue);
        context.startActivity(intent);
    }

    public static void startActivityWithOneClearTop(Context context,Class<?> cls,String extraValue)
    {
        Intent intent = new Intent(context,cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ONE_VALUE,extraValue);
        context.startActivity(intent);
    }

    public static void startActivityWithMulti(Context context,Class<?> cls,String[] extraValue)
    {
        Intent intent = new Intent(context,cls);
        intent.putExtra(TWO_VALUE,extraValue);
        context.startActivity(intent);
    }

    public static String getOneExtra(Intent intent)
    {
        String text;
        if (intent != null){
            text = intent.getStringExtra(ONE_VALUE);
        }else {
            text = "";
        }
        return text;
    }

    public static String[] getMultiExtra(Intent intent)
    {
        String[] texts;
        if (intent != null){
            texts = intent.getStringArrayExtra(TWO_VALUE);
        }else {
            texts = null;
        }
        return texts;
    }


}
