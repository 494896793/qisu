package www.qisu666.common.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用fastJson解析json字符串
 * 
 * @author Zed
 *
 */
public class JsonUtils {

	/**
	 * 将json转成Map
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static Map jsonToMap(String jsonStr) {
				
		Map resMap = new HashMap();
		if(null!=jsonStr){
			resMap = (Map)JSON.parse(jsonStr);
		}
		
		return resMap;
	}

	public static boolean isBadJson(String json) {
		return !isGoodJson(json);
	}

	public static boolean isGoodJson(String json) {
		if (TextUtils.isEmpty(json)) {
			return false;
		}

		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonSyntaxException e) {
			return false;
		} catch (JsonParseException e) {
			return false;
		}
	}
	
	/**
	 * 将json转成List
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static List jsonToList(String jsonStr) {
		
		List resList = new ArrayList();
		if(null != jsonStr){
			resList = (List)JSON.parse(jsonStr);
		}
		
		return resList;
	}
	
	/**
	 * 将对象转成json
	 * 
	 * @param ob
	 * @return
	 * @throws Exception
	 */
	public static String objectToJson(Object ob) {
		
		if(null != ob){
			return JSON.toJSONString(ob);
		}
		
		return null;
	}
	
	
}
