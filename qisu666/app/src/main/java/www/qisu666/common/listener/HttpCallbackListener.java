package www.qisu666.common.listener;

/**
 * 网络访问监听
 * 
 * @author Zed
 *
 */
public interface HttpCallbackListener {

	/**
	 * 响应成功时调用
	 * @param response
	 */
	public void onFinish(String response);
	
	/**
	 * 失败时调用
	 * @param e
	 */
	public void onError(Exception e);
       
}
