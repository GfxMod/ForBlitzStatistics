package com.example.forblitzstatistics.api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Before each and every API call intercept(Chain chain) method will be called
 * and it will check for internet connectivity. If no network found then it
 * will interrupts the normal flow of execution and throw
 * NoConnectivityException exception
 */
public class NetworkConnectionInterceptor implements Interceptor {

    private final Activity activity;

    public NetworkConnectionInterceptor(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!isConnected()) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}

