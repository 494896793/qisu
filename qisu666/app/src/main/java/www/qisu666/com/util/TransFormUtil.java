package www.qisu666.com.util;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by admin on 2018/1/23.
 */

public class TransFormUtil {

    public static Map<String, RequestBody> strMap2respMap(Map<String,String> map){
        Map<String,RequestBody> temp = new HashMap<>();
        for (String key:map.keySet()){
            RequestBody body = str2requ(map.get(key));
            temp.put(key,body);
        }
        return temp;
    }

    public static RequestBody str2requ(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    //分变元
    public static String fen2yuan(BigDecimal money){
        if (money == null)
            return "--";
        DecimalFormat df = new DecimalFormat("#.00");
        BigDecimal result = money.divide(new BigDecimal(100));
        if (result.intValue() == 0){
            return "0";
        }else {
            return df.format(result);
        }
    }

    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
    public static String changeF2Y(String amount) throws Exception{
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    //分变元
    public static String fen2yuanPattern(BigDecimal money, String pattern){
        DecimalFormat df = new DecimalFormat(pattern);
        BigDecimal result = money.divide(new BigDecimal(100));
        return df.format(result);
    }

    //秒变分
    public static String second2minute(long time){
        return ((time / 60 / 1000)+1) + "";//向上取整
    }

    public static long TimeStamp2Date(String timestampString){
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:m:s aa",Locale.ENGLISH);
        Date d2 = null;
        try {
            d2 = sdf.parse(timestampString);
            return d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
