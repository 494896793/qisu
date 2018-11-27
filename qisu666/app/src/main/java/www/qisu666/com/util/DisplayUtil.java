package www.qisu666.com.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by admin on 2018/1/3.
 */

public class DisplayUtil {

    public static int dp2px(Context context, int dpSize){
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize ,context.getResources().getDisplayMetrics());
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
