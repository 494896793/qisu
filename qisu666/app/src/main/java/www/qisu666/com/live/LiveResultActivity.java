package www.qisu666.com.live;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import www.qisu666.com.R;
import www.qisu666.com.activity.GestureSettingActivity;
import www.qisu666.com.activity.PinSettingActivity;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.SPUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.constant.Constant;
import www.qisu666.common.utils.ImageUtil;
import www.qisu666.common.utils.ToastUtil;

import static android.os.Build.VERSION_CODES.M;

/**
 * 刷脸认证等
 *
 * @author lp
 */
public class LiveResultActivity extends BaseActivity {

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    private static final int PAGE_INTO_LIVENESS = 100;
    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.img_result)
//    ImageView imgResult;
    private String uuid;
    private LoadingDialog mLoadingDialog;
    /**
     * Intent 传值
     */
    private String extraValue;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    ToastUtil.showToast("联网授权失败");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_live_result);
        initView();
        init();
        netWorkWarranty();
    }

    private void initView() {
        tvTitle.setText("刷脸认证");
        mLoadingDialog = DialogHelper.loadingAletDialog(this, "正在上传中");
    }

    private void init() {
        uuid = ConUtil.getUUIDString(this);
        extraValue = getIntent().getStringExtra("ONE_VALUE");
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {
        // TODO 需要进行优化
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.v("rx--create ", Thread.currentThread().getName());
                subscriber.onNext("dd");
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v("rx--subscribe ", Thread.currentThread().getName());
                        Manager manager = new Manager(LiveResultActivity.this);
                        LivenessLicenseManager licenseManager = new LivenessLicenseManager(LiveResultActivity.this);
                        manager.registerLicenseManager(licenseManager);
                        manager.takeLicenseFromNetwork(uuid);
                        // 联网授权失败与包名相关
                        if (licenseManager.checkCachedLicense() > 0) {
                            mHandler.sendEmptyMessage(1);
                        } else {
                            mHandler.sendEmptyMessage(2);
                        }
                    }
                });
    }

    private void requestCameraPerm() {
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                enterNextPage();
            }
        } else {
            enterNextPage();
        }
    }

    private void enterNextPage() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
            if (f.exists()) {
                f.delete();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 跳转识别页面
        startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            String delta = data.getStringExtra("delta");
            Serializable imagesExtra = data.getSerializableExtra("images");
            LogUtil.e("guanglog" + "result" + result);
            LogUtil.e("guanglog" + "delta" + delta);

            Gson g = new Gson();
            LiveResult liveResult = g.fromJson(result, LiveResult.class);


            if (!TextUtils.isEmpty(liveResult.result)) {
                boolean isSuccess = liveResult.result.equals(getResources().getString(R.string.verify_success));
                if (isSuccess) {
                    LogUtil.e("进入人脸验证成功页面");
                    Map<String, byte[]> images = (Map<String, byte[]>) imagesExtra;
                    if (images.containsKey("image_best")) {
                        byte[] bestImg = images.get("image_best");
                        if (bestImg != null && bestImg.length > 0) {

                            Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
                            File file = ImageUtil.saveBitmap2File_p10(bestBitMap, "face.jpg", LiveResultActivity.this);
                            try {
                                File f = new File(Environment.getExternalStorageDirectory() + "/temp/", "face.jpg");
                                photoRequest(f);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }

                        }
                    }
                    if (images.containsKey("image_env")) {
//                    byte[] envImg = images.get("image_env");
//                    if (envImg != null && envImg.length > 0) {
//                        Bitmap envBitMap = BitmapFactory.decodeByteArray(envImg, 0, envImg.length);
//                        File file = ImageUtil.saveBitmap2File(bestBitMap, "face.jpg");
//                        Picasso.with(mContext).load(file).into(imgResult);
//                    }
                    }
                } else {
                    ToastUtil.showToast("刷脸失败，请重试！");
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                        if (f.exists()) {
                            f.delete();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } else {
                ToastUtil.showToast("刷脸失败，请重试。");
                try {
                    File f = new File(Environment.getDownloadCacheDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                    if (f.exists()) {
                        f.delete();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

//            ll_result_image.setVisibility(View.VISIBLE);

//            Bundle bundle = data.getExtras();

//            String resultOBJ = bundle.getString("result");
//            try {
//                JSONObject result = new JSONObject(resultOBJ);
//                ToastUtil.showToast(result.getString("result"));
////                textView.setText(result.getString("result"));
////
////                int resID = result.getInt("resultcode");
////                if (resID == R.string.verify_success) {
////                    doPlay(R.raw.meglive_success);
////                } else if (resID == R.string.liveness_detection_failed_not_video) {
////                    doPlay(R.raw.meglive_failed);
////                } else if (resID == R.string.liveness_detection_failed_timeout) {
////                    doPlay(R.raw.meglive_failed);
////                } else if (resID == R.string.liveness_detection_failed) {
////                    doPlay(R.raw.meglive_failed);
////                } else {
////                    doPlay(R.raw.meglive_failed);
////                }
//
//                boolean isSuccess = result.getString("result").equals(
//                        getResources().getString(R.string.verify_success));
////                mImageView.setImageResource(isSuccess ? R.drawable.result_success
////                        : R.drawable.result_failded);
//                if (isSuccess) {
//                    ToastUtil.showToast(result.getString("success"));
////                    String delta = bundle.getString("delta");
////                    Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
////                    if (images.containsKey("image_best")) {
////                        byte[] bestImg = images.get("image_best");
////                        if (bestImg != null && bestImg.length > 0) {
////                            Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
////                            bestImage.setImageBitmap(bestBitMap);
////                        }
////                    }
////                    if (images.containsKey("image_env")) {
////                        byte[] envImg = images.get("image_env");
////                        if (envImg != null && envImg.length > 0) {
////                            Bitmap envBitMap = BitmapFactory.decodeByteArray(envImg, 0, envImg.length);
////                            envImage.setImageBitmap(envBitMap);
////                        }
////                    }
////                    ll_result_image.setVisibility(View.VISIBLE);
////                imageVerify(images,delta);
//                } else {
//                    ToastUtil.showToast(result.getString("fault"));
////                    ll_result_image.setVisibility(View.GONE);
//                }
////                doRotate(isSuccess);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ResultActivity.startActivity(this, bundle);
        } else {
            try {
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                if (f.exists()) {
                    f.delete();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 开始验证文件
     */
    private void photoRequest(File file) {
        String url = "api/auth/verify";
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        LogUtil.w("认证参数 liveImg" + file.getName());
        MultipartBody.Part body = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("liveImg", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadLive(url, MyMessageUtils.addBody(requestMap), body)
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(www.qisu666.com.carshare.Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        LogUtil.w("guanglog" + "msg 88888 " + " data 222");
                        ToastUtil.showToast("认证成功");
                        
                        if (extraValue == null) {
                            EventBus.getDefault().post("活体验证成功");
                        }
                        if ("数字重置密码".equals(extraValue)) {
                            SPUtil.remove(mContext, Constant.ERROR_COUNT_KEY);
                            // TODO 重置密码下一步操作
                            ActivityUtil.startActivity(mContext, PinSettingActivity.class);
                        }
                        if ("手势重置密码".equals(extraValue)) {
                            SPUtil.remove(mContext, Constant.ERROR_GESTURE_KEY);
                            // TODO 重置密码下一步操作
                            ActivityUtil.startActivity(mContext, GestureSettingActivity.class);
                        }
                        finish();
                    }

                    @Override
                    public void onFail(www.qisu666.com.carshare.Message<Object> bean) {
                        LogUtil.w("认证失败" + bean.msg);
                        ToastUtil.showToast(bean.msg);
                        try {//清空缓存
                            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                            if (f.exists()) {
                                f.delete();
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.img_title_left, R.id.fl_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
//                EventBus.getDefault().post("活体验证成功");
                LiveResultActivity.this.finish();
                break;
            case R.id.fl_face:
                requestCameraPerm();
                break;
            default:
                break;
        }
    }

}
