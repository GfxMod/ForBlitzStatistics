package ru.forblitz.statistics.service;

import android.app.Activity;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectivityService {

    private final Set<Activity> activities = new CopyOnWriteArraySet<>();

    public synchronized void subscribe(Activity activity) {
        activities.add(activity);
    }

    public synchronized void unsubscribe(Activity activity) {
        activities.remove(activity);
    }

    public synchronized @Nullable Activity getResponsibleActivity() {
        return activities.stream().findAny().orElse(null);
    }

}
