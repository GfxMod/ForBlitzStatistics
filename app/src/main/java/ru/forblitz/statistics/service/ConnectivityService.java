package ru.forblitz.statistics.service;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.InterfaceUtils;

public class ConnectivityService {

    private final Set<Activity> activities = new CopyOnWriteArraySet<>();

    private AlertDialog networkAlertDialog;

    public synchronized void subscribe(Activity activity) {
        activities.add(activity);
    }

    public synchronized void unsubscribe(Activity activity) {
        activities.remove(activity);
    }

    public synchronized @Nullable Activity getResponsibleActivity() {
        return activities.stream().findAny().orElse(null);
    }

    public void showAlertDialog(@NonNull Activity activity) {
        activity.runOnUiThread(() -> {

            killAlertDialog(activity);

            networkAlertDialog = InterfaceUtils.createAlertDialog(
                    activity,
                    activity.getString(R.string.network_error),
                    activity.getString(R.string.network_error_desc),
                    activity.getString(R.string.network_error_try_again),
                    () -> showAlertDialog(activity))
                    .show();

        });
    }

    public void killAlertDialog(@NonNull Activity activity) {
        activity.runOnUiThread (() -> {

            if (networkAlertDialog != null) {
                networkAlertDialog.dismiss();
                networkAlertDialog = null;
            }

        });
    }

}
