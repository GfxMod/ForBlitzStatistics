package ru.forblitz.statistics.data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.icu.util.TimeZone;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AnimatedRadioButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.R.dimen;
import ru.forblitz.statistics.R.string;
import ru.forblitz.statistics.R.drawable;

public class Utils {

    public static String parseTime(String timestamp) {
        long offset = TimeZone.getDefault().getRawOffset() / 1000L; // насколько в секундах отличается часовой пояс в большую сторону относительно обычного Timestamp
        String time = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Long.parseLong(timestamp) + offset));
        time = time.substring(0, 4) +  "." + time.substring(5, 7) + "." + time.substring(8, 10) + " " + time.substring(11, time.length() - 1);
        return time;
    }

    /**
     * @param activity required to get resources
     * @return width of app window
     */
    public static int getX(Activity activity) {

        View activityMain = activity.findViewById(id.activity_main);

        return activityMain.getWidth();

    }

    /**
     * @param activity required to get resources
     * @return height of app window
     */
    public static int getY(Activity activity) {

        View activityMain = activity.findViewById(id.activity_main);

        return activityMain.getHeight();

    }

    public static ScaleAnimation getAnimTo() {
        ScaleAnimation animTo = new ScaleAnimation(
                1f, 0.85f, 1f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animTo.setDuration(60);
        animTo.setFillAfter(true);
        return animTo;
    }

    public static ScaleAnimation getAnimFrom() {
        ScaleAnimation animFrom = new ScaleAnimation(
                0.85f, 1f, 0.85f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animFrom.setDuration(60);
        animFrom.setFillAfter(true);
        return animFrom;
    }

    public static void allActivatedCheck(Activity activity) {

        AppCompatImageButton lt = activity.findViewById(id.tanks_type_lt);
        AppCompatImageButton mt = activity.findViewById(id.tanks_type_mt);
        AppCompatImageButton ht = activity.findViewById(id.tanks_type_ht);
        AppCompatImageButton at = activity.findViewById(id.tanks_type_at);

        AppCompatImageButton cn = activity.findViewById(id.cn);
        AppCompatImageButton eu = activity.findViewById(id.eu);
        AppCompatImageButton fr = activity.findViewById(id.fr);
        AppCompatImageButton gb = activity.findViewById(id.gb);
        AppCompatImageButton de = activity.findViewById(id.de);
        AppCompatImageButton jp = activity.findViewById(id.jp);
        AppCompatImageButton other = activity.findViewById(id.other);
        AppCompatImageButton us = activity.findViewById(id.us);
        AppCompatImageButton su = activity.findViewById(id.su);

        if (
                lt.isActivated() &&
                        mt.isActivated() &&
                        ht.isActivated() &&
                        at.isActivated()
        ) {
            lt.performClick();
            mt.performClick();
            ht.performClick();
            at.performClick();
        }

        if (
                cn.isActivated() &&
                eu.isActivated() &&
                fr.isActivated() &&
                gb.isActivated() &&
                de.isActivated() &&
                jp.isActivated() &&
                other.isActivated() &&
                us.isActivated() &&
                su.isActivated()
        ) {
            cn.performClick();
            eu.performClick();
            fr.performClick();
            gb.performClick();
            de.performClick();
            jp.performClick();
            other.performClick();
            us.performClick();
            su.performClick();
        }

    }

    /**
     * Sets displayed child for {@link ru.forblitz.statistics.R.layout#fragment_random base statistics}
     * @param activity required to get resources
     * @param v visibility value to be set
     */
    public static void setBaseStatisticsVisibility(Activity activity, boolean v) {

        ViewFlipper randomFlipper = activity.findViewById(id.fragment_random);
        ViewFlipper tanksFlipper = activity.findViewById(id.fragment_tanks);

        if (v) {
            randomFlipper.setDisplayedChild(0);
            tanksFlipper.setDisplayedChild(0);
        } else {
            randomFlipper.setDisplayedChild(1);
            tanksFlipper.setDisplayedChild(1);
        }

    }

    /**
     * Sets displayed child for {@link ru.forblitz.statistics.R.layout#fragment_rating rating statistics}
     * @param activity required to get resources
     * @param v visibility value to be set
     */
    public static void setRatingStatisticsVisibility(Activity activity, boolean v) {

        ViewFlipper ratingFlipper = activity.findViewById(id.fragment_rating);

        if (v) {
            ratingFlipper.setDisplayedChild(0);
        } else {
            ratingFlipper.setDisplayedChild(1);
        }

    }

    public static AlertDialog.Builder createNetworkAlertDialog(Activity activity, Runnable r) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getString(string.network_error));
        alertDialog.setMessage(activity.getString(string.network_error_desc));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(activity.getString(string.network_error_try_again), (d, w) -> r.run());
        activity.runOnUiThread(alertDialog::show);
        return alertDialog;

    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    public static void setStateListDrawable(Activity activity, int checkedDrawableId, int uncheckedDrawableId, int viewId) {
        
        Drawable checkedDrawable = AppCompatResources.getDrawable(activity, checkedDrawableId);
        Drawable uncheckedDrawable = AppCompatResources.getDrawable(activity, uncheckedDrawableId);
        AnimatedRadioButton aRB = activity.findViewById(viewId);

        if (aRB.getWidth() > 0 && aRB.getHeight() > 0) {

            Bitmap checked = Bitmap.createBitmap(aRB.getWidth(), aRB.getHeight(), Bitmap.Config.ARGB_8888);
            new Canvas(checked).drawBitmap(
                    Bitmap.createScaledBitmap(
                            Utils.drawableToBitmap(checkedDrawable),
                            Math.min(aRB.getWidth(), aRB.getHeight()),
                            Math.min(aRB.getWidth(), aRB.getHeight()),
                            true
                    ),
                    (aRB.getWidth() - Math.min(aRB.getWidth(), aRB.getHeight())) * 0.5f,
                    (aRB.getHeight() - Math.min(aRB.getWidth(), aRB.getHeight())) * 0.5f,
                    null
            );

            Bitmap unchecked = Bitmap.createBitmap(aRB.getWidth(), aRB.getHeight(), Bitmap.Config.ARGB_8888);
            new Canvas(unchecked).drawBitmap(
                    Bitmap.createScaledBitmap(
                            Utils.drawableToBitmap(uncheckedDrawable),
                            Math.min(aRB.getWidth(), aRB.getHeight()),
                            Math.min(aRB.getWidth(), aRB.getHeight()),
                            true
                    ),
                    (aRB.getWidth() - Math.min(aRB.getWidth(), aRB.getHeight())) * 0.5f,
                    (aRB.getHeight() - Math.min(aRB.getWidth(), aRB.getHeight())) * 0.5f,
                    null
            );

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[] { android.R.attr.state_checked }, new BitmapDrawable(activity.getResources(), checked));
            stateListDrawable.addState(new int[] { }, new BitmapDrawable(activity.getResources(), unchecked));
            aRB.setButtonDrawable(stateListDrawable);

        }

    }

    public static void randomToMain(Activity activity) {

        ViewFlipper flipper = activity.findViewById(id.random_layouts_flipper);
        flipper.setDisplayedChild(0);

    }

    /**
     * Sets green background and green text color for {@link TextView textView}; called when difference is greater then 0
     * @param activity required to get resources
     * @param textView session info TextView
     * @param value difference that should be set
     */
    public static void setSessionTrueValue(Activity activity, @NonNull TextView textView, String value) {
        int color = ContextCompat.getColor(activity, R.color.session_green);
        Drawable background = AppCompatResources.getDrawable(activity, R.drawable.background_sessions_true);

        textView.setText(value);
        textView.setTextColor(color);
        textView.setBackground(background);
    }

    /**
     * Sets red background and red text color for {@link TextView textView}; called when difference is less then 0
     * @param activity required to get resources
     * @param textView session info TextView
     * @param value difference that should be set
     */
    public static void setSessionFalseValue(Activity activity, @NonNull TextView textView, String value) {
        int color = ContextCompat.getColor(activity, R.color.session_red);
        Drawable background = AppCompatResources.getDrawable(activity, R.drawable.background_sessions_false);

        textView.setText(value);
        textView.setTextColor(color);
        textView.setBackground(background);
    }

    public static void playCycledAnimation(@NonNull View view, Boolean needToSetClickable) {
        if (needToSetClickable) { view.setClickable(false); }
        ScaleAnimation animTo = new ScaleAnimation(
                1f, 0.75f, 1f, 0.75f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animTo.setDuration(125);
        animTo.setFillAfter(true);

        ScaleAnimation animFrom = new ScaleAnimation(
                0.75f, 1f, 0.75f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animFrom.setDuration(125);
        animFrom.setFillAfter(true);

        view.startAnimation(animTo);
        new Handler().postDelayed(() -> view.startAnimation(animFrom), 125);
        if (needToSetClickable) { new Handler().postDelayed(() -> view.setClickable(true), 250); }
    }

    public static void setSelectedRegion(Activity activity, int number) {
        View ru = activity.findViewById(id.select_region_ru);
        View eu = activity.findViewById(id.select_region_eu);
        View na = activity.findViewById(id.select_region_na);
        View asia = activity.findViewById(id.select_region_asia);

        if (number == 0) {
            ru.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested_selected));
            eu.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
        } else if (number == 1) {
            ru.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested_selected));
            na.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
        } else if (number == 2) {
            ru.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested_selected));
            asia.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
        } else if (number == 3) {
            ru.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, drawable.background_layout_nested_selected));
        }

        int padding = activity.getResources().getDimensionPixelSize(dimen.padding_very_big);
        ru.setPadding(padding, padding, padding, padding);
        eu.setPadding(padding, padding, padding, padding);
        na.setPadding(padding, padding, padding, padding);
        asia.setPadding(padding, padding, padding, padding);

    }

    public static float pxToDp(Activity activity, int px) {
        return px / ( (float) activity.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
