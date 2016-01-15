package pl.automatedplayground.imgurltestapp.managers;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.automatedplayground.imgurltestapp.model.ImagesList;
import pl.automatedplayground.imgurltestapp.model.Image;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by adrian on 15.01.16.
 */
public class NetworkManager {
    private static final String API_URL = "https://api.imgur.com/3/";
    private static final String CLIENT_ID = "551e335e6262097";
    private static NetworkManager instance;

    /**
     * Initialize instance
     */
    public static void init() {
        instance = new NetworkManager();
        instance.initialize();
    }

    /**
     * Get singleton instance
     *
     * @return
     */
    public static NetworkManager get() {
        if (instance == null)
            throw new RuntimeException("Wrong usage! Initialize Network Manager first!");
        return instance;
    }

    private ImgUrlApi mApi;

    /**
     * Initialize library
     */
    private void initialize() {
        // let's create a client and set it up
        OkHttpClient client = new OkHttpClient();
        // we add custom headers to each request
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(@NonNull Chain chain) throws IOException {
                Request currentRequest = chain.request();
                Request.Builder builder = currentRequest.newBuilder()
                        .addHeader("Authorization", "Client-ID " + CLIENT_ID);
                Request req = builder.build();
                return chain.proceed(req);
            }
        });
        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .baseUrl(API_URL)
                .client(client)
                .build();
        mApi = mRetrofit.create(ImgUrlApi.class);
    }

    /**
     * Get images from album
     *
     * @param returnMethod
     */
    public void getImages(final Callback<ArrayList<Image>> returnMethod) {
        mApi.getViralImages().enqueue(new retrofit.Callback<ImagesList>() {
            @Override
            public void onResponse(Response<ImagesList> response, Retrofit retrofit) {
                returnMethod.onCallback(response.body().getData());
            }

            @Override
            public void onFailure(Throwable t) {
                returnMethod.onError(t.getMessage());
            }
        });
    }

    /**
     * Interface for callbacks
     *
     * @param <T>
     */
    public interface Callback<T> {
        void onCallback(T object);

        void onError(String cause);
    }
}
