package com.amtee.friendsfinder.retofitwork;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * Created by Amit Kumar Tiwar on 25/07/16.
 */
public class Service_Generator {

    public Service_Generator() {
    }

    public static <S> S createService(Class<S>  serviceclass, String baseurl){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(defaultClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceclass);

    }

    static OkHttpClient defaultClient() {

        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(40, TimeUnit.SECONDS);
        client.setReadTimeout(40, TimeUnit.SECONDS);
        client.setWriteTimeout(40, TimeUnit.SECONDS);
        return client;
    }
}
