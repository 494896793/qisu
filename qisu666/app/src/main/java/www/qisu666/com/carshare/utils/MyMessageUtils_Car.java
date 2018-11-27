package www.qisu666.com.carshare.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import ikidou.reflect.TypeBuilder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import www.qisu666.com.carshare.Message;

/**
 * Created by admin on 2018/1/15.
 */

public class MyMessageUtils_Car {

    private static final String ENCRYPT_KEY = "HL1HBF6lLND721";//des密钥

    public static <T> String writeMessage(T data, String word) {
        Gson g = new Gson();
        String dataStr = g.toJson(data);
        LogUtil.e("  请求参数:" + dataStr);
        String result = "";
        if ((word != null) && (word.length() > 0)) {
            try {
                DesTool des = new DesTool(word);
                result = des.encrypt(dataStr);
            } catch (Exception e) { e.printStackTrace();  }
        }
        LogUtil.e("  请求参数:" + result);
        return result;
    }

    public static <T> Message<List<T>> readMessageList(String data, Class<T> clazz){
        return readMessageList(data, ENCRYPT_KEY , clazz);
    }


    public static <T> Message<List<T>> readMessageList(String data, String word, Class<T> clazz)
    {
        Gson g = new Gson();
        String dataStr = data;
        if ((word != null) && (word.length() > 0) && (dataStr != null)) {
            try
            {
                DesTool des = new DesTool(word);
                dataStr = des.decrypt(data);
                LogUtil.e("guanglog 请求 结果:" +dataStr);
                Type type = TypeBuilder
                        .newInstance(Message.class)
                        .beginSubType(List.class)
                        .addTypeParam(clazz)
                        .endSubType()
                        .build();
                return g.fromJson(dataStr, type);
//                return JSON.parseObject(dataStr,type);
            }
            catch (JsonSyntaxException e){
                 e.printStackTrace();
                //防止sb后端返回的空数组变成空字符串或者null
                int headIndex = dataStr.indexOf("\"data\":");
                int lastIndex = dataStr.length();
                StringBuilder sb = new StringBuilder(dataStr);
                sb.replace(headIndex, lastIndex, "\"data\": []}");
                LogUtil.e("请求 进入异常:"+ "String after JsonSyntaxException change:   " + sb.toString());
                Type type = TypeBuilder
                        .newInstance(Message.class)
                        .beginSubType(List.class)
                        .addTypeParam(Object.class)
                        .endSubType()
                        .build();
                return g.fromJson(sb.toString(), type);
//                return  JSON.parseObject(dataStr,type);
            }
            catch (Exception e)
            {
                LogUtil.e("guanglog 进入异常："+"readMessageList error");
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> Message<T> readMessage(String data, Class<T> clazz){
        return readMessage(data, ENCRYPT_KEY, clazz);
    }

    public static <T> Message<T> readMessage(String data, String word, Class<T> clazz)
    {
        Gson g = new Gson();
        String dataStr = data;
        if ((word != null) && (word.length() > 0) && (dataStr != null)) {
            try
            {
                DesTool des = new DesTool(word);
                dataStr = des.decrypt(data);
                L.e("x_log请求结果"+dataStr);
                Type type = TypeBuilder
                        .newInstance(Message.class)
                        .addTypeParam(clazz)
                        .build();
                return g.fromJson(dataStr, type);
//                return JSON.parseArray(dataStr,type);
            }
            catch (JsonSyntaxException e){
                e.printStackTrace();
                //防止sb后端返回的空数组变成空字符串或者null
                int headIndex = dataStr.indexOf("\"data\":");
                int lastIndex = dataStr.length();
                StringBuilder sb = new StringBuilder(dataStr);
                sb.replace(headIndex, lastIndex, "\"data\": {}}");
                L.e("x_log guanglog"+"String after JsonSyntaxException change   " + sb.toString());
                Type type = TypeBuilder
                        .newInstance(Message.class)
                        .addTypeParam(clazz)
                        .build();
                return g.fromJson(sb.toString(), type);
//                return JSON.parseObject(dataStr,type);
            }
            catch (Exception e)
            {
                L.e("x_log guanglog"+ "readMessage error");
                e.printStackTrace();
            }
        }
        return null;
    }

    public static RequestBody addBody(HashMap<String, Object> requestMap) {
        String reqSend = MyMessageUtils_Car.writeMessage(requestMap, ENCRYPT_KEY);
        return RequestBody.create(MediaType.parse("text/plain"), reqSend);
    }

    public static String addEncrypt(HashMap<String, Object> requestMap) {
        return MyMessageUtils_Car.writeMessage(requestMap, ENCRYPT_KEY);
    }
}
