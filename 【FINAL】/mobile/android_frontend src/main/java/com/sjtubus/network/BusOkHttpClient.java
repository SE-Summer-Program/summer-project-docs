package com.sjtubus.network;

import com.sjtubus.App;
import com.sjtubus.network.cookie.BusCookieJar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Allen on 2018/7/5.
 */
public class BusOkHttpClient {

    private static final long SIZE_OF_CACHE = 10 * 1024 * 1024;

    private static OkHttpClient client;

    public static OkHttpClient getInstance() {
        if (client == null) {
            //Cache cache = new Cache(new File(App.getInstance().getCacheDir(), "tqcache"), SIZE_OF_CACHE);
            CookieJar cookieJar = BusCookieJar.getInstance();

            client = new OkHttpClient.Builder()
                    //.cache(cache)
                    .cookieJar(cookieJar)
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(3,TimeUnit.SECONDS)
                    .addInterceptor(userAgentInterceptor)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
        return client;
    }

    private static final Interceptor userAgentInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();

            String userAgent = ((App) App.getInstance()).getUserAgent();
            builder.header("User-Agent", userAgent);

            request = builder.build();
            return chain.proceed(request);
        }
    };

}
