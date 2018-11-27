package www.qisu666.com.carshare.utils;

/**
 * Created by admin on 2018/1/12.
 */

public class MessageEncrypt
{
    public static String Token(String s)
    {
        if (s == null) {
            return "";
        }
        String tmp = MD5Util.getMD5String(s);

        return tmp;
    }
}
