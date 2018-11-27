package www.qisu666.sdk.times3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import www.qisu666.com.R;
import www.qisu666.com.activity.MyPhotoActivity;
import www.qisu666.com.cardid.PhotoPopupHelper;
import www.qisu666.com.carshare.Message;
import www.qisu666.com.carshare.utils.FlatFunction;
import www.qisu666.com.carshare.utils.MyMessageUtils;
import www.qisu666.com.carshare.utils.ProgressSubscriber;
import www.qisu666.com.carshare.utils.RxNetHelper;
import www.qisu666.com.live.LiveResult;
import www.qisu666.com.network.MyNetwork;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.ClearEditText;
import www.qisu666.com.widget.LoadingDialog;
import www.qisu666.common.utils.ImageUtil;
import www.qisu666.common.utils.ToastUtil;

import static android.os.Build.VERSION_CODES.M;

/**
 * 717219917@qq.com 2018/5/29 14:45.
 * 身份证认证3次  人工
 */
public class Activity_ShenFen2JiaShi extends MyPhotoActivity implements View.OnClickListener{
    ImageView takephoto;
    ImageView img_title_left;
    Button btn_submit;
    TextView tvTitle;
    private String uuid;
    private LoadingDialog mLoadingDialog;

    private TakePhoto takePhoto;
    private PhotoPopupHelper photoPopupHelper;
    private Uri imageUri;
    ImageView img_front_img;
    ImageView img_back_img;
    private Map<String,Object> map=new HashMap<>();
    private Map<String,Object> fileMap=new HashMap<>();
    private int nowPhoto=1;
    private ClearEditText et_identity_name;
    private ClearEditText et_identity_num;
    private String from;
    private TextView idcard_title;
    private TextView idcard_title2;
    private TextView person_title;


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    break;//                    ToastUtil.showToast("成功");
                case 2:
                    ToastUtil.showToast("联网授权失败");
                    break;
                default:
                    break;
            }
        }
    };

    private void init() {
        takePhoto = getTakePhoto();
        uuid = ConUtil.getUUIDString(this);
        photoPopupHelper = PhotoPopupHelper.of(this);
        photoPopupHelper.setOnPhotoPopListener(new PhotoPopupHelper.OnPhotoPopListener() {
            @Override
            public void onTakePhoto() {
//                photoPopupHelper.dismiss();
//                takePhoto();
                Log.i("","");
                photoPopupHelper.dismiss();
                takePhoto(nowPhoto);

            }

            @Override
            public void onGetPhoto() {
                Log.i("","");
                photoPopupHelper.dismiss();
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_CODE);

            }
        });
    }


    private final int IMAGE_CODE=1020;

    private void takePhoto(int type) {
        final File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        if (imageUri == null) {
            return;
        }
//        if(rgCrop.getCheckedRadioButtonId()==R.id.rbCropYes){
//            takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
//        }else {
        takePhoto.onPickFromCapture(imageUri);
//        }
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        face = new File(result.getImage().getCompressPath());
        map.put(nowPhoto+"",result.getImage().getCompressPath());
        fileMap.put(nowPhoto+"",face);
        switch (nowPhoto){
            case 1:
                img_front_img.setImageBitmap(BitmapFactory.decodeFile(map.get(nowPhoto+"").toString()));
                break;
            case 2:
                img_back_img.setImageBitmap(BitmapFactory.decodeFile(map.get(nowPhoto+"").toString()));
                break;
            case 3:
                takephoto.setImageBitmap(BitmapFactory.decodeFile(map.get(nowPhoto+"").toString()));
                break;
        }
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


    /**
     * 联网授权
     *///需要进行优化
    private void netWorkWarranty() {

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
                        Manager manager = new Manager(Activity_ShenFen2JiaShi.this);
                        LivenessLicenseManager licenseManager = new LivenessLicenseManager(Activity_ShenFen2JiaShi.this);
                        manager.registerLicenseManager(licenseManager);
                        manager.takeLicenseFromNetwork(uuid);
                        if (licenseManager.checkCachedLicense() > 0) {
                            mHandler.sendEmptyMessage(1);
                        } else {
                            mHandler.sendEmptyMessage(2);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_shenfen2jiashi);
        EventBus.getDefault().register(this);
        idcard_title=findViewById(R.id.idcard_title);
        person_title=findViewById(R.id.person_title);
        idcard_title2=findViewById(R.id.idcard_title2);
        et_identity_num=findViewById(R.id.et_identity_num);
        et_identity_name=findViewById(R.id.et_identity_name);
        takephoto = (ImageView) findViewById(R.id.takephoto);//拍照
        img_front_img=findViewById(R.id.img_front_img);
        img_back_img=findViewById(R.id.img_back_img);
        img_title_left=findViewById(R.id.img_title_left);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e("点击事件：" + view);
                nowPhoto=3;
                photoPopupHelper.show();
            }
        });

        img_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileMap.size()<3){
                    ToastUtil.showToast("请先补全照片");
                    return;
                }
                if(et_identity_name.getText()==null||et_identity_name.getText().toString().trim().equals("")){
                    ToastUtil.showToast("请输入姓名");
                    return;
                }
                if(et_identity_num.getText()==null||et_identity_num.getText().toString().trim().equals("")){
                    if(from!=null){
                        if(from.equals("idcard")){
                            ToastUtil.showToast("请输入您的身份证号");
                        }else{
                            ToastUtil.showToast("请输入您的驾驶证号");
                        }
                    }
                    return;
                }
                photoRequest();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("人工审核");

        img_back_img.setOnClickListener(this);
        img_front_img.setOnClickListener(this);
        from=getIntent().getStringExtra("from");
        if(from.equals("idcard")){
            idcard_title.setText("1、身份证人像面");
            idcard_title2.setText("2、身份证国徽面");
            person_title.setText("3、个人正面照");
            et_identity_num.setHint("请输入身份证号");
        }else{
            idcard_title.setText("1、驾驶证正页");
            idcard_title2.setText("2、驾驶证副页");
            person_title.setText("3、个人正面照");
            et_identity_num.setHint("请输入驾驶证号");
        }
        init();
//        netWorkWarranty();


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

    Bean_ShenFen beanShenFen;
    Bean_JiaShi beanJiaShi;
    File face = null;

    String what = "";//区别身份证还是驾驶证

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShenFenEvent(Bean_ShenFen bean_shenFen) {
        what = "shenfen";
        beanShenFen = bean_shenFen;
        LogUtil.e("接收到身份证信息：" + bean_shenFen.userCode);
        LogUtil.e("接收到身份证信息：" + bean_shenFen.certNumber);
        LogUtil.e("接收到身份证信息：" + bean_shenFen.certName);
        LogUtil.e("接收到身份证信息：" + bean_shenFen.frontFile.getPath());
        LogUtil.e("接收到身份证信息：" + bean_shenFen.backFile.getPath());
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShenFenEvent(Bean_JiaShi bean_jiaShi) {
        what = "jiashi";
        beanJiaShi = bean_jiaShi;
        LogUtil.e("接收到驾驶证信息：" + bean_jiaShi.userCode);
        LogUtil.e("接收到驾驶证信息：" + bean_jiaShi.certNumber);
        LogUtil.e("接收到驾驶证信息：" + bean_jiaShi.certName);
        LogUtil.e("接收到驾驶证信息：" + bean_jiaShi.frontFile.getPath());
        LogUtil.e("接收到驾驶证信息：" + bean_jiaShi.backFile.getPath());
    }


    private void photoRequest() {
        String url = "api/auth/review/audit";
        HashMap<String, Object> requestMap = new HashMap<>();
        File file = null, file1 = null;
        if (what.equals("jiashi")) {
            requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
            requestMap.put("certName",et_identity_name.getText().toString() );
            requestMap.put("certNumber", et_identity_num.getText().toString());
            requestMap.put("authType", "2");
//            file = beanJiaShi.frontFile;
//            file1 = beanJiaShi.backFile;
        } else if (what.equals("shenfen")) {
            requestMap.put("userCode", UserParams.INSTANCE.getUser_id());
            requestMap.put("certName", et_identity_name.getText().toString());
            requestMap.put("certNumber", et_identity_num.getText().toString());
            requestMap.put("authType", "1");
//            file = beanShenFen.frontFile;
//            file1 = beanShenFen.backFile;
        }

//        if (face == null) {
//            ToastUtil.showToast("请先拍摄实体照片！");
//            return;
//        }

        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;
        MultipartBody.Part body2 = null;
        try{
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), (File) fileMap.get("1"));
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/*"), (File) fileMap.get("2"));
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("image/*"), (File) fileMap.get("3"));
            body = MultipartBody.Part.createFormData("frontImg", ((File) fileMap.get("1")).getName(), requestFile);
            body1 = MultipartBody.Part.createFormData("backImg", ((File) fileMap.get("2")).getName(), requestFile1);
            body2 = MultipartBody.Part.createFormData("headImg", ((File) fileMap.get("3")).getName(), requestFile2);
        }catch (Exception e){
            e.printStackTrace();
        }
        MyNetwork.getMyApi()
                .uploadPhoto(url, MyMessageUtils.addBody(requestMap), body, body1, body2)
                .map(new FlatFunction<>(Object.class))
                .compose(RxNetHelper.<Object>io_main(mLoadingDialog))
                .subscribe(new ProgressSubscriber<Object>(mLoadingDialog) {
                    @Override
                    public void onSuccessCode(Message object) {

                    }

                    @Override
                    public void onSuccess(Object bean) {
                        ToastUtil.showToast("操作成功");
                        LogUtil.e("结果" + bean);
                        finish();
//                      ActivityUtil.startActivity(mContext, LiveResultActivity.class);
                    }

                    @Override
                    public void onFail(Message<Object> bean) {
                        ToastUtil.showToast(bean.msg);
                    }
                });
    }


    public void takephoto() {
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 563);//
            } else {
                enterNextPage();
            }
        } else {
            enterNextPage();
        }
    }


    private void enterNextPage() {
//        Picasso.with(mContext).load(R.color.bg_white).into(imgResult);
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
            if (f.exists()) {
                f.delete();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        startActivityForResult(new Intent(this, LivenessActivity.class), 564);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 564 && resultCode == RESULT_OK) {
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
                    LogUtil.e("进入身份证3次  人脸验证成功页面");
                    Map<String, byte[]> images = (Map<String, byte[]>) imagesExtra;
                    if (images.containsKey("image_best")) {
                        byte[] bestImg = images.get("image_best");
                        if (bestImg != null && bestImg.length > 0) {
                            Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
                            face = ImageUtil.saveBitmap2File_p10(bestBitMap, "face.jpg", Activity_ShenFen2JiaShi.this);
                            try {
                                face = new File(Environment.getExternalStorageDirectory() + "/temp/", "face.jpg");
//                                photoRequest();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }

                        }
                    }
                    if (images.containsKey("image_env")) {
                    }
                } else {
//                    Picasso.with(mContext).load(R.color.bg_white).into(imgResult);
                    ToastUtil.showToast("刷脸失败，请重试！");//加载失败  清空缓存文件
                    try {
                        face = new File(Environment.getExternalStorageDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                        if (face.exists()) {
                            face.delete();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } else {
//                Picasso.with(mContext).load(R.color.bg_white).into(imgResult);
                ToastUtil.showToast("刷脸失败，请重试。");
                try {
                    face = new File(Environment.getDownloadCacheDirectory() + File.separator + "temp" + File.separator, "face.jpg");
                    if (face.exists()) {
                        face.delete();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        }else if(requestCode==IMAGE_CODE){
            //获取图片路径
            if(data!=null&&data.getData()!=null){
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
                fileMap.put(""+nowPhoto,new File(imagePath));
                map.put(nowPhoto+"",imagePath);
                switch (nowPhoto){
                    case 1:
                        img_front_img.setImageBitmap(bitmap);
                        break;
                    case 2:
                        img_back_img.setImageBitmap(bitmap);
                        break;
                    case 3:
                        takephoto.setImageBitmap(bitmap);
                        break;
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_front_img:
                nowPhoto=1;
                photoPopupHelper.show();
                break;
            case R.id.img_back_img:
                nowPhoto=2;
                photoPopupHelper.show();
                break;
        }
    }


}
