package ru.forblitz.statistics.service;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * The {@link ConnectivityService} class provides functionality for managing operations
 * related to network connectivity. It stores the current activity and an
 * {@link AlertDialog} about the absence of a network
 */
public class ConnectivityService {

    private final Set<Activity> activities = new CopyOnWriteArraySet<>();

    private AlertDialog networkAlertDialog;

    /**
     * @param activity {@link Activity} to be subscribed
     */
    public synchronized void subscribe(Activity activity) {
        if (activities.size() != 0) {
            unsubscribe(getResponsibleActivity());
        }
        activities.add(activity);
    }

    /**
     * @param activity {@link Activity} to be unsubscribed
     */
    public synchronized void unsubscribe(Activity activity) {
        activities.remove(activity);
    }

    /**
     * @return responsible {@link Activity}
     */
    public synchronized @Nullable Activity getResponsibleActivity() {
        return activities.stream().findAny().orElse(null);
    }

    /**
     * Shows network alert dialog
     * @param activity activity when {@link AlertDialog} will be created
     */
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

    /**
     * Dismiss network alert dialog
     * @param activity activity when {@link AlertDialog} will be removed
     */
    public void killAlertDialog(@NonNull Activity activity) {
        activity.runOnUiThread (() -> {

            if (networkAlertDialog != null) {
                networkAlertDialog.dismiss();
                networkAlertDialog = null;
            }

        });
    }

}
