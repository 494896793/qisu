package www.qisu666.common.utils;

import java.math.BigDecimal;

/**
 *
 */
public class NullUtils {

	/**
	 * 空对象转空字符串
	 *
	 * @param object
	 * @return
	 */
	public static String nullToEmptyStr(Object object) {
		if (object == null) {
			return "";
		} else
			return object.toString();
	}


	/**
	 * 空对象转整数零
	 *
	 * @param object
	 * @return
	 */
	public static int null2Zero(Object object) {

		if (object == null || "".equals(object.toString())) {
			return 0;
		}

		return Integer.valueOf(object.toString());
	}

	/**
	 * 空对象转浮点数零
	 *
	 * @param object
	 * @return
	 */
	public static Double null2DoubleZero(Object object) {

		if (object == null || "".equals(object.toString())) {
			return 0.0;
		}else{
			String resultStr = object.toString();
			BigDecimal bd = new BigDecimal(resultStr);
			resultStr = bd.toPlainString();
			int dotIndex = resultStr.indexOf(".");
			if(dotIndex != -1 && dotIndex+3<=resultStr.length()){
				resultStr = resultStr.substring(0, dotIndex+3);
			}
			double result = Double.valueOf(resultStr);
			return result;
		}
	}


	public static String toPlainString(String input){
		String resultStr = "" ;
		if(input != null && !input.equals("")){
			BigDecimal bd = new BigDecimal(input);
			resultStr = bd.toPlainString();
		}
		return resultStr;
	}

}
