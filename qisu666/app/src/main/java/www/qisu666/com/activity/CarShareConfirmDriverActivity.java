package www.qisu666.com.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import www.qisu666.com.R;
import www.qisu666.com.cardid.CardDriverBean;
import www.qisu666.com.cardid.PhotoPopupHelper;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.OtherProgressSubscriber;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.live.LiveResultActivity;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.ActivityUtil;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.TransFormUtil;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.ClearEditText;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.sdk.times3.Activity_ShenFen2JiaShi;
import www.qisu666.sdk.times3.Bean_JiaShi;

//驾驶证认证页面
public class CarShareConfirmDriverActivity extends MyPhotoActivity {

    @BindView(R.id.et_identity_name)
    ClearEditText etIdentityName;
    @BindView(R.id.et_driver_num)
    ClearEditText etDriverNum;
    @BindView(R.id.img_front_img)
    ImageView imgFrontImg;
    @BindView(R.id.img_back_img)
    ImageView imgBackImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_title_right)
    TextView btnTitleRight;
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

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_car_share_confirm_driver);
        initViews();
        takePhoto = getTakePhoto();
    }

    private void initViews() {
        tvTitle.setText("驾驶证认证");
        btnTitleRight.setVisibility(View.GONE);//不能跳过
        btnTitleRight.setText("跳过");
        mLoadingDialog = DialogHelper.loadingAletDialog(this, "正在上传中");
        photoPopupHelper = PhotoPopupHelper.of(this);
        photoPopupHelper.setOnPhotoPopListener(new PhotoPopupHelper.OnPhotoPopListener() {
            @Override
            public void onTakePhoto() {
                photoPopupHelper.dismiss();
                takePhoto();
            }

            @Override
            public void onGetPhoto() {
                photoPopupHelper.dismiss();
                pickFromPhoto();
            }
        });
        tx_artificial.setText(Html.fromHtml("<font color='#6E717B'>身份验证失败，请走</font><font color='#51E7D3'>人工审核</font>"));
        tx_artificial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(bean_jiaShi);
                Intent intent=new Intent(CarShareConfirmDriverActivity.this, Activity_ShenFen2JiaShi.class);
                intent.putExtra("from","driver");
                startActivity(intent);
                finish();
            }
        });
    }


    private void pickFromPhoto() {
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

    private void takePhoto() {
        if (imageUri == null) {
            return;
        }
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
        takePhoto = getTakePhoto();
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
        File file = new File(result.getImage().getCompressPath());
        if (mSide == ID_FRONT) {
            uploadDriver(file);
        } else if (mSide == ID_BACK) {
            setImg(file);
        }
    }

    //    private void showImg(ArrayList<TImage> images) {
//        if (images.size() % 2 == 1) {
//            File file = new File(images.get(images.size() - 1).getCompressPath());
//            Toast.makeText(mContext, getFileSize(file) + "mb", Toast.LENGTH_SHORT).show();
//        }
//    }
//
    private String getFileSize(File file) {
        double size = (double) file.length() / 1024 / 1024;
        return String.valueOf(size);
    }

    private void uploadDriver(final File file) {
        String url = "https://api.megvii.com/faceid/v2/ocr_driver_license";
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("api_key", "7fIb47kR1fNeoFnOZ1RvjTLZvDWgMIj7");
        requestMap.put("api_secret", "zPtVSbB5515aDIdnsFKMQADNXnh-jHVF");

        MultipartBody.Part body = null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        MyNetwork.getMyApi()
                .uploadDriverPhoto(url, body, TransFormUtil.strMap2respMap(requestMap))
                .compose(RxNetHelper.<CardDriverBean>io_other_main(mLoadingDialog))
                .subscribe(new OtherProgressSubscriber<CardDriverBean>(mLoadingDialog) {
                    @Override
                    public void onSuccess(CardDriverBean bean) {
                        
                        if (!TextUtils.isEmpty(bean.error_message)) {
                            onFail();
                            return;
                        }
                        // || TextUtils.isEmpty(bean.valid_date) 注释掉 验证驾照的时间  新型驾照无法抓取
                        if (TextUtils.isEmpty(bean.name)
                                || TextUtils.isEmpty(bean.issued_by)
                                ) {
                            onFail();
                            return;
                        }
                        etIdentityName.setText(bean.name);
                        setImg(file);
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showToast("识别失败，请上传清晰图片");
                    }
                });
    }

    Bean_JiaShi bean_jiaShi = new Bean_JiaShi();

    private void photoRequest() {
        String url = "api/auth/license";
        String name = etIdentityName.getText().toString().trim();
        String num = etDriverNum.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(num)) {
            ToastUtil.showToast("档案编号不能为空");
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
        if (num.length() < 12) {
            ToastUtil.showToast("您输入的驾驶证号,长度不足12位！");
            return;
        }

        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
        requestMap.put("certName", name);
        requestMap.put("certNumber", num);

        File file = new File(frontPath);
        File file1 = new File(backPath);
        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;

        try {
            bean_jiaShi.backFile = file1;
            bean_jiaShi.frontFile = file;
            bean_jiaShi.userCode = UserParams.INSTANCE.getUser_id();
            bean_jiaShi.certName = name;
            bean_jiaShi.certNumber = num;
        } catch (Throwable t) {
            t.printStackTrace();
        }


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
                        ToastUtil.showToast("认证成功");
//                      ActivityUtil.startActivity(mContext, LiveResultActivity.class);
                        finish();
//                      Log.w("guanglog", "msg 88888 " +" data 222" );
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                        if (bean.code == -1158) {

                            /*DialogHelper.confirmDialog(CarShareConfirmDriverActivity.this, "驾驶证认证今日已达3次,请走人工审核!", new AlertDialog.OnDialogButtonClickListener() {
                                @Override
                                public void onConfirm() {
                                    EventBus.getDefault().postSticky(bean_jiaShi);
                                    startActivity(new Intent(CarShareConfirmDriverActivity.this, Activity_ShenFen2JiaShi.class));
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


    @OnClick({R.id.fl_front, R.id.fl_back, R.id.img_title_left, R.id.btn_submit, R.id.btn_title_right})
    public void onViewClicked(View view) {
        final File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.fl_front:
                mSide = 0;
                photoPopupHelper.show();
                break;
            case R.id.fl_back:
                mSide = 1;
                photoPopupHelper.show();
                break;
            case R.id.img_title_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                photoRequest();
//                ActivityUtil.startActivity(mContext, LiveResultActivity.class);
                break;
            case R.id.btn_title_right:
                DialogHelper.confirmDialog(mContext, "分时租赁需要驾驶证认证哦，如果不进行驾驶证认证，则只能使用代驾功能", new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtil.startActivity(mContext, LiveResultActivity.class);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            default:
                break;
        }
    }

}
