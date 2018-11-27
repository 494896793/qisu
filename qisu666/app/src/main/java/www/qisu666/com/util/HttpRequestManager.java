package www.qisu666.com.util;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonStringRequest;

import www.qisu666.com.BuildConfig;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.com.logic.ResponseListener;
import www.qisu666.common.utils.ToastUtil;

/**
 * Created by Administrator on 2016/3/23.
 */
public class HttpRequestManager {
    private static final int REQUEST_TIMEOUT_MS = 30*1000;
    /**
     * 发送Json的Get请求
     * @param url
     * @param listener
     */
    public static JsonStringRequest newGetStringRequest(String url, final ResponseListener listener){
        JsonStringRequest stringRequest = new JsonStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null){
                    LogUtils.e(error.getMessage());
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    /**
     * 发送Json的Post请求
     * @param url
     * @param jsonRequest
     * @param listener
     */
    public static JsonStringRequest newPostStringRequest(String url, String jsonRequest, final ResponseListener listener){
        JsonStringRequest stringRequest = new JsonStringRequest(Request.Method.POST, url, jsonRequest,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(BuildConfig.DEBUG){
                    ToastUtil.showToast("err:"+error.getLocalizedMessage());
                } 

               listener.onError(error);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

}