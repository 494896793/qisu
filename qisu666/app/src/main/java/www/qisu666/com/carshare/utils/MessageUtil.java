package www.qisu666.com.carshare.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.model.CarListBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/12.
 */

public class MessageUtil
{


    public static Message<LinkedTreeMap<String, Object>> readMessage(String msg, String word)
    {
        Type objectType = new TypeToken() {}.getType();
        return readMessage(msg, word, objectType);
    }

    public static <T> Message<List<T>> readMessageList(String msg, Class<T> clazz)
    {
        return readMessageList(msg, null, clazz);
    }

    public static <T> Message<T> readMessage(String msg, Class<T> clazz)
    {
        return readMessage(msg, null, clazz);
    }

    public static <T> Message<List<T>> readMessageList(String msg, String word, Class<T> clazz)
    {
        Gson g = new Gson();

        Message<String> r_ = g.fromJson(msg, new TypeToken<Message<String>>(){}.getType());
        String dataStr = (String)r_.data;
        if ((word != null) && (word.length() > 0)) {
            try
            {
                DesTool des = new DesTool(word);
                dataStr = des.decrypt((String)r_.data);
                r_.msg = des.decrypt(r_.msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Message<List<T>> r = new Message();
        String s = MessageEncrypt.Token(dataStr);
        if (s.equals(r_.token))
        {
            JsonElement d = new JsonParser().parse(dataStr);
            List<T> lst = new ArrayList();
            if (d.isJsonArray())
            {
                JsonArray array = d.getAsJsonArray();
                for (JsonElement elem : array) {
                    lst.add(new Gson().fromJson(elem, clazz));
                }
            }
            else
            {
                lst.add(new Gson().fromJson(dataStr, clazz));
            }
            r.data = lst;

            r.code = r_.code;
            r.key = r_.key;
            r.msg = r_.msg;
            r.token = r_.token;
            return r;
        }
        return null;
    }

    public static <T> Message<T> readMessage(String data, String word, Type clazz)
    {
        Gson g = new Gson();
        String dataStr = data;
        if ((word != null) && (word.length() > 0) && (dataStr != null)) {
            try
            {
                DesTool des = new DesTool(word);
                dataStr = des.decrypt(data);
                Log.w("guanglog", "dataString    " +dataStr);
                Type messageType = new TypeToken<Message<List<CarListBean>>>(){}.getType();
                Message<List<CarListBean>> result = g.fromJson(dataStr, messageType);
                Log.w("guanglog", "result   msg " + result.msg + " data" + result.data.get(0).toString());
//                return result;
//                Message<T> result = g.fromJson(dataStr, clazz);
//                return result;
//                Message<T> r =  g.fromJson(dataStr, new TypeToken<T>(){}.getType());
//                return  r;
//                r_.msg = des.decrypt(r_.msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Message<T> result = new Message<>();
        System.out.println("2:" + dataStr);
        if (!TextUtils.isEmpty(dataStr))
        {
            T a = g.fromJson(dataStr, clazz);
            result.data = a;
            return result;
        }
        return null;
    }

//    public static <T> Message<T> readMessageList(String data, String word, Type clazz)
//    {
//        Gson g = new Gson();
//        String dataStr = data;
//        if ((word != null) && (word.length() > 0) && (dataStr != null)) {
//            try
//            {
//                DesTool des = new DesTool(word);
//                dataStr = des.decrypt(data);
//                Log.w("guanglog", "dataString    " +dataStr);
//                Type messageType = new TypeToken<Message<List<CarListBean>>>(){}.getType();
//                Message<List<CarListBean>> result = g.fromJson(dataStr, messageType);
//                Log.w("guanglog", "result   msg " + result.msg + " data" + result.data.get(0).toString());
////                return result;
////                Message<T> result = g.fromJson(dataStr, clazz);
////                return result;
////                Message<T> r =  g.fromJson(dataStr, new TypeToken<T>(){}.getType());
////                return  r;
////                r_.msg = des.decrypt(r_.msg);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        Message<T> result = new Message<>();
//        System.out.println("2:" + dataStr);
//        if (!TextUtils.isEmpty(dataStr))
//        {
//            T a = g.fromJson(dataStr, clazz);
//            result.data = a;
//            return result;
//        }
//        return null;
//    }

    public static <T> String writeMessage(T data)
    {
        return writeMessage(data, null);
    }

    public static <T> String writeMessage(T data, String word)
    {
        return writeMessage(data, "", word);
    }

    public static <T> String writeMessage(T data, String key, String word)
    {
        return writeMessage(data, 0, "", key, word);
    }

    public static <T> String writeMessage(T data, int code, String msg)
    {
        return writeMessage(data, code, msg, null);
    }

    public static <T> String writeMessage(T data, int code, String msg, String word)
    {
        return writeMessage(data, code, msg, "", word);
    }

    public static <T> String writeMessage(T data, int code, String msg, String key, String word)
    {
        Gson g = new Gson();

        String dataStr = g.toJson(data);
        Log.w("guanglog", "dataStr   `````   " + dataStr);

        Message<String> r = new Message<>();
        r.code = code;
        r.msg = msg;
        r.data = dataStr;
        r.key = key;

        System.out.println("1:" + dataStr);
        r.token = MessageEncrypt.Token(dataStr);

        System.out.println("1:" + r.token);
        if ((word != null) && (word.length() > 0)) {
            try
            {
                DesTool des = new DesTool(word);
                r.data = des.encrypt(dataStr);
                r.msg = des.encrypt(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return g.toJson(r);
    }

    public static <T> List<T> readRequestList(String request, Class<T> clazz)
    {
        return readRequestList(request, null, clazz);
    }


    public static LinkedTreeMap<String, Object> readRequest(String request, String word)
    {
        return (LinkedTreeMap)readMessage(request, word).data;
    }

    public static <T> List<T> readRequestList(String request, String word, Class<T> clazz)
    {
        Message<List<T>> msg = readMessageList(request, word, clazz);
        return msg != null ? (List)msg.data : null;
    }

    public static Message<String> getMessage(int code, String msg)
    {
        Message<String> m = new Message();
        m.code = code;
        m.msg = msg;
        return m;
    }

    public static <T> Message<T> getMessage(T data, int code, String msg, String key)
    {
        Gson g = new Gson();
        Message<T> rr = new Message();
        String dataStr = g.toJson(data);
        rr.code = code;
        rr.msg = msg;
        rr.key = key;
        rr.token = MessageEncrypt.Token(dataStr);
        rr.data = data;
        return rr;
    }

    public static <T> T readRequest(String request, Class<T> clazz)
    {
        return (T)readRequest(request, null, clazz);
    }

    public static <T> T readRequest(String request, String word, Class<T> clazz)
    {
        Message<T> msg = readMessage(request, word, clazz);
        return (T)(msg != null ? msg.data : null);
    }

    public static <T> String writeRequest(T data)
    {
        return writeRequest(data, null);
    }

    public static <T> String writeRequest(T data, String word)
    {
        return writeMessage(data, word);
    }

    public static <T> Message<List<T>> readResultList(String result, Class<T> clazz)
    {
        return readMessageList(result, null, clazz);
    }

    public static <T> Message<T> readResult(String result, Class<T> clazz)
    {
        return readMessage(result, null, clazz);
    }

    public static <T> Message<List<T>> readResultList(String result, String word, Class<T> clazz)
    {
        return readMessageList(result, word, clazz);
    }

    public static <T> Message<T> readResult(String result, String word, Class<T> clazz)
    {
        return readMessage(result, word, clazz);
    }

    public static <T> String writeResult(T data, int code, String msg)
            throws Exception
    {
        return writeMessage(data, code, msg, "", null);
    }
}
