package www.qisu666.com.network;

import android.content.Context;

import www.qisu666.com.carshare.utils.MyMessageUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2018/1/15.
 */

public class MyNetManager<T> {

    private Class<T> typeParameterClass;

    public MyNetManager(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }


    public void response(String url, final HashMap<String, Object> requestMap, Context context, boolean isShowDialog, final boolean isListData, final Class<T> type , String dialog_type){

        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(requestMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
//                .subscribe(new DisposableSubscriber<HttpResult<String>>() {
//                    @Override
//                    public void onNext(HttpResult<String> bean) {
////                        if (isListData){
////                            MyMessageUtils.readMessage(bean.data, typeParameterClass);
////                            callback.onResponse(MyMessageUtils.readMessageList(bean.data, typeParameterClass));
////                        }else {
////                            callback.onResponse(MyMessageUtils.readMessage(bean.data, type));
////                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
////                        callback.onError();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

//    public interface ClientCallback{
//        void onResponse(Class<T> message);
//        void onError();
//    }
}
