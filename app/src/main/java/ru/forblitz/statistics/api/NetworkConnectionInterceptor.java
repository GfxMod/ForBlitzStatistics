package ru.forblitz.statistics.api;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Interceptor;
import okhttp3.Response;
import ru.forblitz.statistics.service.ConnectivityService;

/**
 * Before each and every API call {@link #intercept(Chain chain)} method will
 * be called and it will check for internet connectivity. If the network is not
 * detected, the stream will be suspended until it appears. In addition, if
 * any error occurs during the execution of the request, the request will be
 * repeated after a timeout.
 */
public class NetworkConnectionInterceptor implements Interceptor {

    private final ConnectivityService connectivityService;
    private final ConnectivityManager connectivityManager;

    public NetworkConnectionInterceptor(
            ConnectivityService connectivityService,
            ConnectivityManager connectivityManager
    ) {
        this.connectivityService = connectivityService;
        this.connectivityManager = connectivityManager;
    }

    /**
     * The number of milliseconds that is skipped before repeating the request
     */
    public static final long timeout = 1000L;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) {

        Activity activity = connectivityService.getResponsibleActivity();
        if (activity != null) {

            if (isDisconnected()) {
                connectivityService.showAlertDialog(activity);
                while (isDisconnected()) {
                    try {
                        synchronized (this) {
                            wait(timeout);
                        }
                    } catch (InterruptedException e) {
                        Log.e("Interceptor error", e.getMessage(), e);
                    }
                }
                connectivityService.killAlertDialog(activity);
            }

            try {
                return chain.proceed(chain.request().newBuilder().build());
            } catch (Throwable th) {
                Log.e("Interceptor error", th.getMessage(), th);
                // This means that the connection was lost while the response
                // was being loaded. We need to intercept again
                try {
                    synchronized (this) {
                        wait(timeout);
                    }
                } catch (InterruptedException e) {
                    Log.e("Interceptor error", e.getMessage(), e);
                }
                return intercept(chain);
            }
        }

        throw new IllegalStateException("intercept: ConnectivityService does not contain any activity");

    }

    private boolean isDisconnected() {
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return true;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(nw);
        return networkCapabilities == null || (!networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) &&
                !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

}

