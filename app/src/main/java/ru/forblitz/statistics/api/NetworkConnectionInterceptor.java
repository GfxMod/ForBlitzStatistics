package ru.forblitz.statistics.api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import ru.forblitz.statistics.service.ConnectivityService;

/**
 * Before each and every API call intercept(Chain chain) method will be called
 * and it will check for internet connectivity. If no network found then it
 * will interrupts the normal flow of execution and throw
 * NoConnectivityException exception
 */
public class NetworkConnectionInterceptor implements Interceptor {

    private final ConnectivityService connectivityService;

    public NetworkConnectionInterceptor(ConnectivityService connectivityService) {
        this.connectivityService = connectivityService;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Activity activity = connectivityService.getResponsibleActivity();
        if (activity != null) {

            if (!isConnected()) {
                connectivityService.showAlertDialog(activity);
                while (!isConnected()) {  }
                connectivityService.killAlertDialog(activity);
            }

            return chain.proceed(chain.request().newBuilder().build());
        }

        throw new IllegalStateException("Печаль-беда, почему-то ни одна активность сейчас не зарегана");

    }

    public boolean isConnected() {
        Activity activity = connectivityService.getResponsibleActivity();

        if (activity != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) connectivityService.getResponsibleActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        }

        throw new IllegalStateException("Печаль-беда, почему-то ни одна активность сейчас не зарегана");
    }

}

