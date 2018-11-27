package www.qisu666.com.network;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by admin on 2018/1/15.
 */

public class MyClient{

    private static final String ENCRYPT_KEY = "HL1HBF6lLND721";

    public MyClient(){
    }

//    public static void response(JSONObject jsonObject, Context context, final HttpCallback callback){
//        response(jsonObject, context, true, callback);
//    }
//
//    public static void response(JSONObject jsonObject, Context context, boolean isShowDialog, final HttpCallback callback){
//        response(jsonObject, context, isShowDialog, LoadingDialog.TYPE_GIF, callback);
//    }

    public static void response(String url, final HashMap<String, Object> requestMap,  final boolean isListData,final Class type, final ClientCallback callback){
        response(url, requestMap, null, false, isListData, type,"" , callback);
    }

    public static void response(String url, final HashMap<String, Object> requestMap, Context context, boolean isShowDialog, final boolean isListData, final Class type , String dialog_type, final ClientCallback callback){

//        MyNetwork.getMyApi()
//                .carRequest(url, MyMessageUtils.addBody(requestMap))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSubscriber<HttpResult<String>>() {
//                    @Override
//                    public void onNext(HttpResult<String> bean) {
//                        if (isListData){
//                            callback.onResponse(MyMessageUtils.readMessageList(bean.data, type));
//                        }else {
//                            callback.onResponse(MyMessageUtils.readMessage(bean.data, type));
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        callback.onError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

    public interface ClientCallback{
        void onResponse(Object message);
        void onError();
    }

}
