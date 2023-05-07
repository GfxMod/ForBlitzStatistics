package ru.forblitz.statistics.api;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import ru.forblitz.statistics.ForBlitzStatisticsApplication;
import ru.forblitz.statistics.R;
import ru.forblitz.statistics.service.ConnectivityService;
import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * Before each and every API call intercept(Chain chain) method will be called
 * and it will check for internet connectivity. If no network found then it
 * will interrupts the normal flow of execution and throw
 * NoConnectivityException exception
 */
public class NetworkConnectionInterceptor implements Interceptor {

    private final ConnectivityService connectivityService;
    //private final MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private AlertDialog alertDialog = null;

    public NetworkConnectionInterceptor(ConnectivityService connectivityService) {
        this.connectivityService = connectivityService;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Activity activity = connectivityService.getResponsibleActivity();
        if (activity != null) {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = InterfaceUtils.createAlertDialog(
                    activity,
                    activity.getString(R.string.network_error),
                    activity.getString(R.string.network_error_desc),
                    activity.getString(R.string.network_error_try_again),
                    () -> showAlertDialog(activity)
            );

            if (!isConnected()) {
                showAlertDialog(activity);
                while (!isConnected()) {  }
                killAlertDialog(activity);
            }

            return chain.proceed(chain.request().newBuilder().build());
        }

        throw new IllegalStateException("Печаль-беда, почему-то ни одна активность сейчас не зарегана");

    }

    private void showAlertDialog(Activity activity) {
        activity.runOnUiThread(() -> {
            killAlertDialog(activity);
            alertDialog = materialAlertDialogBuilder.show();
        });
    }

    private void killAlertDialog(Activity activity) {
        activity.runOnUiThread(() -> {
            if (alertDialog != null) { alertDialog.dismiss(); alertDialog = null; }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}

