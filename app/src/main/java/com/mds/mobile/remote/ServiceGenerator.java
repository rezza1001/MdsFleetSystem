package com.mds.mobile.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pho0890910 on 8/4/2018.
 */
public class ServiceGenerator {


//    public static final String API_BASE_URL = "https://cmapp.mncleasing.com/middleware/core-services/";
    public static final String API_BASE_URL = "https://dev.rentas.co.id/FleetMgmtSystem/middleware/core-services/";
//    public static final String API_BASE_URL = " https://dev.rentas.co.id/mncleasing-carservices/production/middleware/";
    public static final String API_PHOTO_URL = "https://dev.rentas.co.id/FleetMgmtSystem/middleware/";

//    public static final String API_URL = "https://dev.rentas.co.id/mncleasing-carservices/production/middleware/";
//    public static final String API_SECRET_KEY = "FtMQVYZ-4#z26gzBY#_@y8RzP";

//        public static final String API_URL = "https://cmapp.mncleasing.com/doc/api/";
//    public static final String API_SECRET_KEY = "FtMQVYZ-4#z26gzBY#_@y8RzP";

    public static final String API_URL = "http://103.41.205.42/FleetMgmtSystem/api-middleware/";
    public static final String API_SECRET_KEY = "!@%23123qweASDzxc";
    public static final String API_SECRET_KEY2 = "!@#123qweASDzxc";

    public static final String ABSENT_PARAM = "?page=Presence-Parameter";
    public static final String ABSENT_IN = "?page=Presence-In";
    public static final String ABSENT_OUT = "?page=Presence-Out";
    public static final String ABSENT_STATUS = "?page=Presence-Status";
    public static final String HANDOVER_LIST_CAR = "?page=Data-Fleet";
    public static final String HANDOVER_TAKING = "?page=Key-Taking";
    public static final String HANDOVER_RETURN = "?page=Key-Return";
    public static final String HANDOVER_STATUS = "?page=Key-Status";






    private static HttpLoggingInterceptor getInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(getInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);

    private static Retrofit.Builder builder =   new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder() .setLenient() .create()));



    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    public static <S> S createService(Class<S> serviceClass) {

        return retrofit.create(serviceClass);
    }

}
