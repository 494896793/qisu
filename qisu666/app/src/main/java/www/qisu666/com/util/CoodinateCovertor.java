package www.qisu666.com.util;

import java.math.BigDecimal;

import www.qisu666.com.entity.LngLat;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * 717219917@qq.com 2018/9/10 8:52.
 */
public class CoodinateCovertor {

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 对double类型数据保留小数点后多少位
     *  高德地图转码返回的就是 小数点后6位，为了统一封装一下
     * @param digit 位数
     * @param in 输入
     * @return 保留小数位后的数
     */
    static double dataDigit(int digit,double in){
        
        return new BigDecimal(in).setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 将火星坐标转变成百度坐标
     * @param lngLat_gd 火星坐标（高德、腾讯地图坐标等）
     * @return 百度坐标
     */

    public static LngLat bd_encrypt(LngLat lngLat_gd)
    {
        double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLantitude();
        double z = Math.sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * cos(x *  x_pi);
        return new LngLat(dataDigit(6,z * cos(theta) + 0.0065),dataDigit(6,z * sin(theta) + 0.006));
    }


 }
