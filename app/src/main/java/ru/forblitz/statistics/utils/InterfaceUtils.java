package ru.forblitz.statistics.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.forblitz.statistics.R;

public class InterfaceUtils {

    /**
     * @return width of app window
     */
    public static int getX() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * @param context required to get resources
     * @return height of app window
     */
    @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
    public static int getY(Context context) {
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int statusBarId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(statusBarId);
        height -= statusBarHeight;
        return height;
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

    public static void allActivatedCheck(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (!viewGroup.getChildAt(i).isActivated()) { return; }
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setActivated(false);
        }

    }

    /**
     * Sets displayed child for {@link ru.forblitz.statistics.R.layout#fragment_random base statistics}
     * @param activity required to get resources
     * @param v visibility value to be set
     */
    public static void setBaseStatisticsVisibility(Activity activity, boolean v) {

        ViewFlipper randomFlipper = activity.findViewById(R.id.fragment_random);
        ViewFlipper tanksFlipper = activity.findViewById(R.id.fragment_tanks);

        if (v) {
            randomFlipper.setDisplayedChild(0);
            tanksFlipper.setDisplayedChild(0);
        } else {
            randomFlipper.setDisplayedChild(1);
            tanksFlipper.setDisplayedChild(1);
        }

    }

    /**
     * Sets displayed child for {@link R.layout#fragment_rating rating statistics}
     * @param activity required to get resources
     * @param v visibility value to be set
     */
    public static void setRatingStatisticsVisibility(Activity activity, boolean v) {

        ViewFlipper ratingFlipper = activity.findViewById(R.id.fragment_rating);

        if (v) {
            ratingFlipper.setDisplayedChild(0);
        } else {
            ratingFlipper.setDisplayedChild(1);
        }

    }

    public static MaterialAlertDialogBuilder createNetworkAlertDialog(Activity activity, Runnable r) {

        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(activity);
        alertDialog.setTitle(activity.getString(R.string.network_error));
        alertDialog.setMessage(activity.getString(R.string.network_error_desc));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(activity.getString(R.string.network_error_try_again), (d, w) -> r.run());
        activity.runOnUiThread(alertDialog::show);
        return alertDialog;

    }

    public static MaterialAlertDialogBuilder createErrorAlertDialog(Activity activity, String title, String message) {

        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(activity.getString(android.R.string.ok), (d, w) -> {});
        activity.runOnUiThread(alertDialog::show);
        return alertDialog;

    }

    public static Bitmap drawableToBitmap (@NonNull Drawable drawable) {
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

    public static BitmapDrawable createScaledSquareDrawable(Context context, Drawable drawable, int canvasSize, int drawableSize) {
        Bitmap bitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888);
        new Canvas(bitmap).drawBitmap(
                Bitmap.createScaledBitmap(
                        drawableToBitmap(drawable),
                        drawableSize,
                        drawableSize,
                        true
                ),
                (canvasSize - drawableSize) * 0.5f,
                (canvasSize - drawableSize) * 0.5f,
                null
        );
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static void randomToMain(Activity activity) {

        ViewFlipper flipper = activity.findViewById(R.id.random_layouts_flipper);
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
        View ru = activity.findViewById(R.id.select_region_ru);
        View eu = activity.findViewById(R.id.select_region_eu);
        View na = activity.findViewById(R.id.select_region_na);
        View asia = activity.findViewById(R.id.select_region_asia);

        if (number == 0) {
            ru.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested_selected));
            eu.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
        } else if (number == 1) {
            ru.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested_selected));
            na.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
        } else if (number == 2) {
            ru.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested_selected));
            asia.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
        } else if (number == 3) {
            ru.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            eu.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            na.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested));
            asia.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout_nested_selected));
        }

        int padding = activity.getResources().getDimensionPixelSize(R.dimen.padding_very_big);
        ru.setPadding(padding, padding, padding, padding);
        eu.setPadding(padding, padding, padding, padding);
        na.setPadding(padding, padding, padding, padding);
        asia.setPadding(padding, padding, padding, padding);

    }

    public static int pxToDp(Context context, int px) {
        return (int) ((double) px / ( (double) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void search(Context context, String nickname) {
        ((EditText) ((Activity) context).findViewById(R.id.search_field)).setText(nickname, TextView.BufferType.EDITABLE);
        ((Activity) context).findViewById(R.id.search_button).performClick();
    }

}
