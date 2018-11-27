package www.qisu666.com.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.cardid.CardBackBean;
import www.qisu666.com.cardid.CardDriverBean;
import www.qisu666.com.cardid.CardFrontBean;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.FlatListFunction;
import www.qisu666.com.carshare.LoadingActivity;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.model.CarListBean;
import www.qisu666.com.model.PayMethodBean;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.TransFormUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//测试页面
public class TestActivity extends LoadingActivity {

    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_test);
        initLoadingDialog();
    }

    @OnClick(R.id.btn_test) public void onViewClicked() {
//        goMethohs();
//        goPartList();
//        photoRequest();
//        goTestIdCard();
//        goTestBank();
        goTestDriver();
    }


    //测试司机
    private void goTestDriver() {
        String url = "https://api.megvii.com/faceid/v2/ocr_driver_license";
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("api_key", "7fIb47kR1fNeoFnOZ1RvjTLZvDWgMIj7");
        requestMap.put("api_secret", "zPtVSbB5515aDIdnsFKMQADNXnh-jHVF");

        File file = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cs_car_normal),"test.png");
        MultipartBody.Part body = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadDriverPhoto(url, body, TransFormUtil.strMap2respMap(requestMap))
//                .uploadFrontPhoto(url, apiKey, apiSecret,legality, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<CardDriverBean>() {
                    @Override
                    public void onNext(CardDriverBean cardBackBean) {
                        ToastUtil.showToast("onNext");
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.showToast("onError");
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.showToast("onComplete");
                    }
                });
    }

    //测试银行卡
    private void goTestBank() {
        String url = "https://api.megvii.com/faceid/v3/ocrbankcard";
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("api_key", "7fIb47kR1fNeoFnOZ1RvjTLZvDWgMIj7");
        requestMap.put("api_secret", "zPtVSbB5515aDIdnsFKMQADNXnh-jHVF");

        File file = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cs_car_normal),"test.png");
        MultipartBody.Part body = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadBackPhoto(url, body, TransFormUtil.strMap2respMap(requestMap))
//                .uploadFrontPhoto(url, apiKey, apiSecret,legality, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<CardBackBean>() {
                    @Override
                    public void onNext(CardBackBean cardBackBean) {
                        ToastUtil.showToast("onNext");
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.showToast("onError");
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.showToast("onComplete");
                    }
                });
    }

   //测试id卡
    private void goTestIdCard() {
        String url = "https://api.faceid.com/faceid/v1/ocridcard";
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("api_key", "7fIb47kR1fNeoFnOZ1RvjTLZvDWgMIj7");
        requestMap.put("api_secret", "zPtVSbB5515aDIdnsFKMQADNXnh-jHVF");
        requestMap.put("legality", "1");

        File file = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cs_car_normal),"test.png");
        MultipartBody.Part body = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadIdPhoto(url, body, TransFormUtil.strMap2respMap(requestMap))
//                .uploadFrontPhoto(url, apiKey, apiSecret,legality, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<CardFrontBean>() {
                    @Override
                    public void onNext(CardFrontBean cardFrontBean) {
                        ToastUtil.showToast("onNext");
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.showToast("onError");
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.showToast("onComplete");
                    }
                });
    }

    //条件查询计费策略
    private void goMethohs(){
        String url = "https://api.faceid.com/faceid/v1/ocridcard";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", "U0000003");
        requestMap.put("certName", "张测试");
        requestMap.put("certNumber", "441424199812018890");

        File file = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker),"test.png");
        File file1 = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cs_car_normal), "test1.png");
        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/*"), file1);
        body = MultipartBody.Part.createFormData("frontImg", file.getName(), requestFile);
        body1 = MultipartBody.Part.createFormData("backImg", file1.getName(), requestFile1);
        MyNetwork.getMyApi()
                .uploadPhoto(url, MyMessageUtils.addBody(requestMap), body, body1)
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        Log.w("guanglog", "msg 88888 " +" data 222" );
                    }

                    @Override
                    public void onFail(Message<Object> bean) {

                    }

                });
    }

    //条件查询停车网点列表
    private void goPartList(){
        String url = "api/station/list/query";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("stationName", "罗湖区宝安北路");
        requestMap.put("label ", "0");
        normalRequest(url, requestMap);
    }

    private void photoRequest(){
        String url = "api/auth/idcard";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", "U0000003");
        requestMap.put("certName", "张测试");
        requestMap.put("certNumber", "441424199812018890");

        File file = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker),"test.png");
        File file1 = saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cs_car_normal), "test1.png");
        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/*"), file1);
        body = MultipartBody.Part.createFormData("frontImg", file.getName(), requestFile);
        body1 = MultipartBody.Part.createFormData("backImg", file1.getName(), requestFile1);
        MyNetwork.getMyApi()
                .uploadPhoto(url, MyMessageUtils.addBody(requestMap), body, body1)
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        Log.w("guanglog", "msg 88888 " +" data 222" );
                    }

                    @Override
                    public void onFail(Message<Object> bean) {

                    }

                });
    }

    //保存为位图
    public File saveBitmap(Bitmap bm, String fileName) {
        Log.e("SaveBitmap", "保存图片");
        File f = new File(Environment.getExternalStorageDirectory() + File.separator, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("SaveBitmap", "已经保存");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }

    //正常请求
    private void normalRequest(String url, HashMap<String, Object> request){
        MyNetwork.getMyApi()
                .carRequest(url, MyMessageUtils.addBody(request))
                .map(new FlatListFunction<>(CarListBean.class))
                .compose(RxNetHelper.<List<CarListBean>>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<List<CarListBean>>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override public void onSuccess(List<CarListBean> bean) {
                        Log.w("guanglog", "msg 88888 " +" data 222" + bean.get(0).createdTime);
                    }

                    @Override public void onFail(Message<List<CarListBean>> bean) {

                    }

                });
    }
}
