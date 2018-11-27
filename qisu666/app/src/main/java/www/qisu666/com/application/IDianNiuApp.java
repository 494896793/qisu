package www.qisu666.com.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.asa.okvolley.LruBitmapCache;
import com.asa.okvolley.OkHttpStack;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;

import org.xutils.DbManager;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import www.qisu666.com.BuildConfig;
import www.qisu666.com.config.Config;
import www.qisu666.com.util.CrashUtil;
import www.qisu666.com.util.VerifyCodeTimer;

//import com.asa.okvolley.OkHttp2Stack;
//import com.squareup.okhttp.OkHttpClient;

public class IDianNiuApp extends Application {

    /**
     * 单例 为了直接调用函数,不声明为static
     */
    private static IDianNiuApp sInstance;
    /**
     * Volley request queue
     */
    private RequestQueue mRequestQueue;
    /**
     * Volley image cache
     */
    private LruBitmapCache mLruBitmapCache;
    /**
     * Volley image loader
     */
    private ImageLoader mImageLoader;
    /**
     * 获取验证码计时器
     */
    public static VerifyCodeTimer verifyCodeTimer;
    /**
     * xutils的数据库模块
     */
    public DbManager db;

    @Override public void onCreate() {
        super.onCreate();
        //本地崩溃处理 toast("error")
        CrashUtil.getInstance().init(this);
        x.Ext.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        x.Ext.setDebug(BuildConfig.DEBUG);
        sInstance = this;

        /** 初始化友盟分享 */
        //微信 appid appsecret
        PlatformConfig.setWeixin(Config.WECHAT_APPID, Config.WECHAT_APPSECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Config.SINA_APPKEY,Config.SINA_APPSECRET);
        //QQ空间
        PlatformConfig.setQQZone(Config.QQZONE_APPKEY, Config.QQZONE_APPSECRET);
        //设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(false);
        //初始化 JPush
        JPushInterface.init(this);
        //初始化数据库
        initDbXutil();
        CrashReport.initCrashReport(getApplicationContext(), "a65554b284", false);
    }

    /**  @return the application singleton instance */
    public static IDianNiuApp getInstance() {
        return sInstance;
    }


    /**
     * 初始化xtuisl的数据库设置
     */
    public void initDbXutil(){
       db= x.getDb(daoConfig);
        Log.i("==","==");
    }


    /** Returns a Volley request queue for creating network requests
     * @return {@link RequestQueue} */
    public RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack(new OkHttpClient.Builder().build()));
        }
        return mRequestQueue;
    }

    /** Adds a request to the Volley request queue
     * @param request to be added to the Volley requests queue */
    private static void addRequest(@NonNull final Request<?> request) {
        getInstance().getVolleyRequestQueue().add(request);
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag     tag identifying the request
     */
    public static void addRequest(@NonNull final Request<?> request, @NonNull final String tag) {
        request.setTag(tag);
        addRequest(request);
    }

    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public static void cancelAllRequests(@NonNull final String tag) {
        if (getInstance().getVolleyRequestQueue() != null) {
            getInstance().getVolleyRequestQueue().cancelAll(tag);
        }
    }

    /**
     * Returns an image loader instance to be used with Volley.
     *
     * @return {@link ImageLoader}
     */
    public ImageLoader getVolleyImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader
                    (
                            getVolleyRequestQueue(),
                            IDianNiuApp.getInstance().getVolleyImageCache()
                    );
        }

        return mImageLoader;
    }

    /**
     * Returns a bitmap cache to use with volley.
     *
     * @return {@link LruBitmapCache}
     */
    private LruBitmapCache getVolleyImageCache() {
        if (mLruBitmapCache == null) {
            mLruBitmapCache = new LruBitmapCache(sInstance);
        }
        return mLruBitmapCache;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            //设置数据库名
            .setDbName("qisu.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            // .setDbDir(new File("/sdcard"))
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

}
