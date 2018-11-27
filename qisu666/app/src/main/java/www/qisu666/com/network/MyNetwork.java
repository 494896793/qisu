package www.qisu666.com.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import www.qisu666.com.config.Config;
import www.qisu666.com.config.JsonConverterFactory;
import www.qisu666.com.util.UserParams;

/**
 * Created by zhang on 2016/12/9.
 */

public class MyNetwork
{

    private static MyApi MyApi;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static JsonConverterFactory jsonConverterFactory=JsonConverterFactory.create();
    private static CallAdapter.Factory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();


    public static MyApi getMyApi()
    {
        if (MyApi == null)
        {
            Interceptor mTokenInterceptor = new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    // 拦截请求，全局添加服务端需要的请求密钥
                                                     String aa=UserParams.INSTANCE.getS_token();
                    Request authorised = originalRequest.newBuilder()
                            .header("token", UserParams.INSTANCE.getS_token() == null ? "" : UserParams.INSTANCE.getS_token())
                            .build();
                    return chain.proceed(authorised);
                }
            };

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(mTokenInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Config.BASE)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build();
            MyApi = retrofit.create(MyApi.class);
        }
        return MyApi;
    }


}
