package www.qisu666.com.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
import www.qisu666.com.cardid.CardFrontBean;
import www.qisu666.com.cardid.PhotoPopupHelper;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.OtherProgressSubscriber;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.config.Config;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.TransFormUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.ClearEditText;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.ImageUtil;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.times3.Activity_ShenFen2JiaShi;
import www.qisu666.sdk.times3.Bean_ShenFen;

import static android.os.Build.VERSION_CODES.M;


/**
 * 实名认证
 */
public class CarShareConfirmIdentityActivity extends MyPhotoActivity {


    @BindView(R.id.et_identity_name)
    ClearEditText etIdentityName;
    @BindView(R.id.et_identity_num)
    ClearEditText etIdentityNum;
    @BindView(R.id.img_front_img)
    ImageView imgFrontImg;
    @BindView(R.id.img_back_img)
    ImageView imgBackImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private TakePhoto takePhoto;
    @BindView(R.id.tx_artificial)
    TextView tx_artificial;

    private PhotoPopupHelper photoPopupHelper;

    private static final int ID_FRONT = 0;
    private static final int ID_BACK = 1;

    int mSide = 0;
    private static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    private static final int INTO_IDCARDSCAN_PAGE = 100;

    private LoadingDialog mLoadingDialog;

    private String frontPath;
    private String backPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_confirm_identity);
        initViews();
        network();
        takePhoto = getTakePhoto();
    }

    private void initViews() {

        try {
            File file_tmp = new File(Environment.getExternalStorageDirectory(), "/temp/" + "back.jpg");
            if (file_tmp.exists()) {
                file_tmp.delete();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            File file_tmp = new File(Environment.getExternalStorageDirectory(), "/temp/" + "front.jpg");
            if (file_tmp.exists()) {
                file_tmp.delete();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        tvTitle.setText("身份认证");
        mLoadingDialog = DialogHelper.loadingAletDialog(this, "正在上传中");
        photoPopupHelper = PhotoPopupHelper.of(this);
        photoPopupHelper.setOnPhotoPopListener(new PhotoPopupHelper.OnPhotoPopListener() {
            @Override
            public void onTakePhoto() {
                photoPopupHelper.dismiss();
                requestCameraPerm();
            }

            @Override
            public void onGetPhoto() {
                photoPopupHelper.dismiss();
                pickFromPhoto();
            }
        });
        tx_artificial.setVisibility(View.VISIBLE);
        tx_artificial.setText(Html.fromHtml("<font color='#6E717B'>身份验证失败，请走</font><font color='#51E7D3'>人工审核</font>"));
        tx_artificial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(beanShenFen);
                Intent intent=new Intent(CarShareConfirmIdentityActivity.this, Activity_ShenFen2JiaShi.class);
                intent.putExtra("from","idcard");
                startActivity(intent);
                finish();
            }
        });
    }


    private void network() {

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
                        Manager manager = new Manager(mContext);
                        IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(mContext);
                        manager.registerLicenseManager(idCardLicenseManager);
                        String uuid = "13213214321424";
                        manager.takeLicenseFromNetwork(uuid);
                        String contextStr = manager.getContext(uuid);
                        Log.w("ceshi", "contextStr====" + contextStr);
                        Log.w("ceshi", "idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
                        if (idCardLicenseManager.checkCachedLicense() > 0) {
                            UIAuthState(true);
                        } else {
                            UIAuthState(false);
                        }
                    }
                });


    }

    private void UIAuthState(final boolean isSuccess) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                authState(isSuccess);
            }
        });
    }

    private void authState(boolean isSuccess) {
        if (!isSuccess) {
            ToastUtil.showToast("联网授权失败！请检查网络或找服务商");
        }
    }

    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                enterNextPage(mSide);
            }
        } else {
            enterNextPage(mSide);
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            if (msg.what == 0x10) {
                Intent intent = new Intent(CarShareConfirmIdentityActivity.this, IDCardScanActivity.class);
                intent.putExtra("side", (Integer) msg.obj);
                intent.putExtra("isvertical", false);
                startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
            }
            return false;
        }
    });


    private void enterNextPage(int side) {
        LogUtil.e("进入拍照----");
        android.os.Message m = new android.os.Message();
        m.what = 0x10;
        m.obj = side;
        handler.sendMessage(m);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {// Permission Granted
                Util.showToast(this, "获取相机权限失败");
            } else {
                enterNextPage(mSide);
            }
        }
    }

    private void pickFromPhoto() {
        LogUtil.e("选择图片----");
//        int limit= Integer.parseInt(etLimit.getText().toString());
//        if(limit>1){
//            if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//                takePhoto.onPickMultipleWithCrop(limit,getCropOptions());
//            }else {
        takePhoto.onPickMultiple(1);
//            }
//            return;
//        }
//        if(rgFrom.getCheckedRadioButtonId()==R.id.rbFile){
//            if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//                takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions());
//            }else {
//                takePhoto.onPickFromDocuments();
//            }
//            return;
//        }else {
//            if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//                takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
//            }else {
//                takePhoto.onPickFromGallery();
//            }
//        }
    }

    private void takePhoto(Uri imageUri) {
//        if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//            takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
//        }else {
        takePhoto.onPickFromCapture(imageUri);
//        }
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 2 * 1024 * 1024;
        int width = 2000;
        int height = 2000;
        boolean showProgressBar = true;
        boolean enableRawFile = false;
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
//        if(rgPickTool.getCheckedRadioButtonId()==R.id.rbPickWithOwn){
        //拍照后是否保存原图
        builder.setWithOwnGallery(false);
//        }
//        if(rgCorrectTool.getCheckedRadioButtonId()==R.id.rbCorrectYes){
        //是否纠正拍照角度
        builder.setCorrectImage(true);
//        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            takePhoto = getTakePhoto();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("相机 获取成功-----");
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            //正面
//            if (mSide == 0){
//                {
////                    byte[] portraitImgData = data.getByteArrayExtra("portraitImg");
////                    Bitmap img = BitmapFactory.decodeByteArray(portraitImgData, 0, portraitImgData.length);
////                    imgFrontImg.setImageBitmap(img);
//                    byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
//                    Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0,
//                            idcardImgData.length);
//                    imgFrontImg.setImageBitmap(idcardBmp);
//                }
//            }else if (mSide == 1){

            try {
                byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
                Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);

                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE}; //验证是否许可权限
                    for (String str : permissions) {
                        if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) { //申请权限
                            this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                            return;
                        }
                    }
                }

                if (mSide == ID_FRONT) {
                    File frontImg = ImageUtil.saveBitmap2File(idcardBmp, "front.jpg");
                    frontPath = frontImg.getAbsolutePath();
                    Thread.sleep(50);
                    imgFrontImg.setImageBitmap(idcardBmp);
                    uploadIdCard(frontImg);
                } else if (mSide == ID_BACK) {
                    File backImg = ImageUtil.saveBitmap2File(idcardBmp, "back.jpg");
                    Thread.sleep(50); //防止dialog 弹出位置偏移
                    backPath = backImg.getAbsolutePath();
                    uploadIdCard(backImg);
                    imgBackImg.setImageBitmap(idcardBmp);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                ToastUtil.showToast("没有身份信息！");
            }
//            }
        }
    }

    private void setImg(File file) {
        if (mSide == ID_FRONT) {
            Glide.with(this).load(file).into(imgFrontImg);
            frontPath = file.getAbsolutePath();
        } else if (mSide == ID_BACK) {
            Glide.with(this).load(file).into(imgBackImg);
            backPath = file.getAbsolutePath();
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        LogUtil.e("takeSuccess" + result.getImages().size());

        File file = new File(result.getImage().getCompressPath());
        uploadIdCard(file);
    }


    private void showImg(ArrayList<TImage> images) {
        if (images.size() % 2 == 1) {
            File file = new File(images.get(images.size() - 1).getCompressPath());
            Toast.makeText(mContext, getFileSize(file) + "mb", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileSize(File file) {
        double size = (double) file.length() / 1024 / 1024;
        return String.valueOf(size);
    }

    private void uploadIdCard(final File file) {
        String url = "https://api.faceid.com/faceid/v1/ocridcard";
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("api_key", Config.FACE_KEY);
        requestMap.put("api_secret", Config.FACE_SECRET);
        requestMap.put("legality", "1");

        MultipartBody.Part body = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadIdPhoto(url, body, TransFormUtil.strMap2respMap(requestMap))
                .compose(RxNetHelper.<CardFrontBean>io_other_main(mLoadingDialog))
                .subscribe(new OtherProgressSubscriber<CardFrontBean>(mLoadingDialog) {
                    @Override
                    public void onSuccess(CardFrontBean bean) {
                        if (TextUtils.isEmpty(bean.side)) {
                            onFail();
                            return;
                        }
                        if (bean.side.equals("front")
                                && mSide == ID_BACK) {
                            ToastUtil.showToast("需要反面，请确认后重试");
                            return;
                        } else if (bean.side.equals("back")
                                && mSide == ID_FRONT) {
                            ToastUtil.showToast("需要正面，请确认后重试");
                            return;
                        }
                        CardFrontBean.LegalityBean legalityBean = bean.legality;
                        if (legalityBean != null) {
                            if (!(legalityBean.IDPhoto > 0.7)) {
                                onFail();
                                return;
                            }
//                            }else if (legalityBean.TemporaryIDPhoto > 0.7){
//                                ToastUtil.showToast("临时身份证照片");
//                            }else if (legalityBean.Photocopy > 0.7){
//                                ToastUtil.showToast("正式身份证的复印件");
//                            }else if (legalityBean.Screen > 0.7){
//                                ToastUtil.showToast("手机或电脑屏幕翻拍的照片");
//                            }else if (legalityBean.Edited > 0.7){
//                                ToastUtil.showToast("用工具合成或者编辑过的身份证图片");
//                            }
                        } else {
                            ToastUtil.showToast("身份证合法性不明确");
                            return;
                        }
                        if (mSide == ID_FRONT) {
                            if (TextUtils.isEmpty(bean.name) || TextUtils.isEmpty(bean.id_card_number)) {
                                onFail();
                                return;
                            }
                            etIdentityName.setText(bean.name);
                            etIdentityNum.setText(bean.id_card_number);
                            setImg(file);
                        } else if (mSide == ID_BACK) {
                            if (TextUtils.isEmpty(bean.valid_date) || TextUtils.isEmpty(bean.issued_by)) {
                                onFail();
                                return;
                            }
                            setImg(file);
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showToast("识别失败，请上传清晰图片");
                    }
                });
    }


    Bean_ShenFen beanShenFen = new Bean_ShenFen();

    private void photoRequest() {
        String url = "api/auth/idcard";
        String name = etIdentityName.getText().toString().trim();
        String num = etIdentityNum.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(num)) {
            ToastUtil.showToast("身份证号不能为空");
            return;
        }
        if (TextUtils.isEmpty(frontPath)) {
            ToastUtil.showToast("请上传正面照");
            return;
        }
        if (TextUtils.isEmpty(backPath)) {
            ToastUtil.showToast("请上传反面照");
            return;
        }
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        requestMap.put("certName", name);
        requestMap.put("certNumber", num);

        File file = new File(frontPath);
        File file1 = new File(backPath);
        try {
            beanShenFen.backFile = file1;
            beanShenFen.frontFile = file;
            beanShenFen.certName = name;
            beanShenFen.certNumber = num;
            beanShenFen.userCode = UserParams.INSTANCE.getUser_id();
        } catch (Throwable t) {
            t.printStackTrace();
        }
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
                        ToastUtil.showToast("身份证认证成功！");
                        ActivityUtil.startActivity(mContext, CarShareConfirmDriverActivity.class);//跳转到驾驶证认证页面
                        finish();
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        if (bean.code == -1157) {

                            /*DialogHelper.confirmDialog(CarShareConfirmIdentityActivity.this, "身份证认证今日已达3次,请走人工审核！", new AlertDialog.OnDialogButtonClickListener() {
                                @Override
                                public void onConfirm() {
                                    EventBus.getDefault().postSticky(beanShenFen);
                                    startActivity(new Intent(CarShareConfirmIdentityActivity.this, Activity_ShenFen2JiaShi.class));
                                    finish();
                                }

                                @Override
                                public void onCancel() {
                                }
                            });*/
                        }
                    }
                });
    }


    @OnClick({R.id.fl_front, R.id.fl_back, R.id.img_title_left, R.id.btn_submit})
    public void onViewClicked(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.fl_front:
                mSide = 1;
                photoPopupHelper.show();
                break;
            case R.id.fl_back:
                mSide = 0;
                photoPopupHelper.show();
                break;
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                photoRequest();
//                ActivityUtil.startActivity(mContext, CarShareConfirmDriverActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mLoadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadingDialog.dismiss();
    }


}
