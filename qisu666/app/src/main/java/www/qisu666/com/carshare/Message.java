package www.qisu666.com.carshare;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/1/12.
 */

public class Message<T> implements Serializable
{
    public String msg;
    public int code;
    public T data;
    public String token;
    public String key;
}
