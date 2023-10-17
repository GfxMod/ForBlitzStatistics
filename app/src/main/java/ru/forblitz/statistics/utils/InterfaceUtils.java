package ru.forblitz.statistics.utils;

import static ru.forblitz.statistics.data.Constants.StatisticsViewFlipperItems.FALSE;
import static ru.forblitz.statistics.data.Constants.StatisticsViewFlipperItems.STATISTICS;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.Constants;
import ru.forblitz.statistics.dto.StatisticsData;
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper;
import ru.forblitz.statistics.widget.data.DetailsLayout;
import ru.forblitz.statistics.widget.data.PlayerFastStat;

public class InterfaceUtils {

    /**
     * @return width of app window in pixels
     */
    public static int getX() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * @param context required to get resources
     * @return height of app window in pixels
     */
    @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
    public static int getY(Context context) {
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int statusBarId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(statusBarId);
        height -= statusBarHeight;
        return height;
    }

    /**
     * @return the base {@link ScaleAnimation} to zoom out
     */
    public static ScaleAnimation getAnimTo() {
        ScaleAnimation animTo = new ScaleAnimation(
                1f, 0.85f, 1f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animTo.setDuration(60);
        animTo.setFillAfter(true);
        return animTo;
    }

    /**
     * @return the base {@link ScaleAnimation} to zoom in
     */
    public static ScaleAnimation getAnimFrom() {
        ScaleAnimation animFrom = new ScaleAnimation(
                0.85f, 1f, 0.85f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animFrom.setDuration(60);
        animFrom.setFillAfter(true);
        return animFrom;
    }

    /**
     * @param drawable {@link Drawable} to be converted to {@link Bitmap}
     * @return Converted {@link Bitmap}
     */
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

    /**
     * @param context The {@link Context}
     * @param drawable {@link Drawable} for conversion
     * @param canvasSize The length in pixels of the side of the final canvas
     * @param drawableSize The length in pixels of the side of the drawing
     *                    object inscribed in a square on the final canvas
     * @return Drawable, inscribed in a square with the drawableSize side and
     * located in the center of the square with the canvasSize side
     */
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

    /**
     * @param view The view on which the animation will be played
     * @param needToSetClickable The need to make the View non-clickable for
     *                           the duration of the animation
     */
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

    /**
     * Converts pixels to density-independent pixels (dp).
     * @param context The Context
     * @param px Pixels for conversion
     * @return Density-independent pixels (dp)
     */
    public static int pxToDp(Context context, int px) {
        return (int) ((double) px / ( (double) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Substitutes a nickname in the search field and simulates a click on the
     * search button
     * @param context The Context
     * @param nickname Nickname for search
     */
    public static void search(Context context, String nickname) {
        ((EditText) ((Activity) context).findViewById(R.id.search_field)).setText(nickname, TextView.BufferType.EDITABLE);
        ((Activity) context).findViewById(R.id.search_button).performClick();
    }

    public static int getValueColor(Context context, double value, double[] steps) {
        if (value < steps[0]) {
            return context.getColor(R.color.white);
        }
        if (value < steps[1]) {
            return context.getColor(R.color.green);
        }
        if (value < steps[2]) {
            return context.getColor(R.color.blue);
        } else {
            return context.getColor(R.color.violet);
        }
    }

    public static int getValueColor(Context context, String stringValue, double[] steps) {
        return getValueColor(context, Double.parseDouble(stringValue), steps);
    }

    public static MaterialAlertDialogBuilder createAlertDialog(
            Context context,
            String title,
            CharSequence message
    ) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(context.getString(android.R.string.ok), (d, w) -> {});
        return alertDialog;
    }

    public static MaterialAlertDialogBuilder createAlertDialog(
            Context context, 
            String title,
            CharSequence message,
            String positiveButtonText, 
            Runnable positiveButtonAction
    ) {
        MaterialAlertDialogBuilder alertDialog = createAlertDialog(context, title, message);
        alertDialog.setPositiveButton(positiveButtonText, (d, w) -> positiveButtonAction.run());
        return alertDialog;
    }

    public static MaterialAlertDialogBuilder createAlertDialog(
            Context context,
            String title,
            CharSequence message,
            String positiveButtonText,
            Runnable positiveButtonAction,
            String negativeButtonText,
            Runnable negativeButtonAction
    ) {
        MaterialAlertDialogBuilder alertDialog = createAlertDialog(context, title, message, positiveButtonText, positiveButtonAction);
        alertDialog.setNegativeButton(negativeButtonText, (d, w) -> negativeButtonAction.run());
        return alertDialog;
    }

    /**
     * Sets values to {@link R.layout#fragment_statistics statistics layout}
     * @param activity required to get resources
     * @param statisticsData statisticsData to be set
     */
    public static void setStatistics(Activity activity, StatisticsData statisticsData) {

        if (!statisticsData.getBattles().equals("0")) {
            activity.runOnUiThread(() ->
                    ((DifferenceViewFlipper) activity.findViewById(R.id.fragment_statistics)).setDisplayedChild(STATISTICS));
        } else {
            activity.runOnUiThread(() ->
                    ((DifferenceViewFlipper) activity.findViewById(R.id.fragment_statistics)).setDisplayedChild(FALSE));
        }
        activity.runOnUiThread(() -> {
            ((PlayerFastStat) activity.findViewById(R.id.statistics_fast_stat)).setData(statisticsData);
            ((DetailsLayout) activity.findViewById(R.id.statistics_details_layout)).setData(statisticsData);
        });
    }

    public static void setRegionAlertVisibility(
            Context context,
            ConstraintLayout constraintLayout,
            View text,
            View buttons,
            boolean visible
    ) {
        int buttonsMargin = ((ConstraintLayout.LayoutParams) buttons.getLayoutParams()).topMargin;

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new Fade());
        transitionSet.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
        TransitionManager.beginDelayedTransition(constraintLayout, transitionSet);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.clear(text.getId(), ConstraintSet.BOTTOM);
        constraintSet.clear(text.getId(), ConstraintSet.TOP);

        if (visible) {
            constraintSet.clear(text.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(
                    text.getId(),
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    0
            );
            constraintSet.clear(buttons.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(
                    buttons.getId(),
                    ConstraintSet.TOP,
                    text.getId(),
                    ConstraintSet.BOTTOM,
                    0
            );
        } else {
            constraintSet.clear(text.getId(), ConstraintSet.TOP);
            constraintSet.connect(
                    text.getId(),
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    0
            );
            constraintSet.clear(buttons.getId(), ConstraintSet.TOP);
            constraintSet.connect(
                    buttons.getId(),
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    0
            );
        }

        constraintSet.applyTo(constraintLayout);
        if (visible) {
            buttons.setVisibility(View.VISIBLE);
        } else {
            buttons.setVisibility(View.INVISIBLE);
        }

        ConstraintLayout.LayoutParams buttonsLayoutParams =
                (ConstraintLayout.LayoutParams) buttons.getLayoutParams();
        buttonsLayoutParams.topMargin = buttonsMargin;
        buttons.setLayoutParams(buttonsLayoutParams);
    }

    public static void doBySavingThePadding(View view, Runnable runnable) {

        final int paddingLeft = view.getPaddingLeft();
        final int paddingTop = view.getPaddingTop();
        final int paddingRight = view.getPaddingRight();
        final int paddingBottom = view.getPaddingBottom();

        runnable.run();

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

    }

    /**
     * Creates an item for the selected locale, ready to be added to the list
     * of locales
     * @param context The Context
     * @param locale The locale for which this item is needed
     * @param action The action that will be performed when clicking on the item
     * @return Returns an item ready to be added to the list of locales
     */
    @SuppressLint("InflateParams")
    public static LinearLayout createLocaleItem(
            @NonNull Context context,
            @NonNull String locale,
            @NonNull Runnable action
    ) {

        LinearLayout localeLayout = (LinearLayout) LayoutInflater
                .from(context)
                .inflate(R.layout.item_locale, null);
        localeLayout.setTag(locale);

        TextView localeCodeView = localeLayout.findViewById(R.id.locale_code);
        TextView localeDescView = localeLayout.findViewById(R.id.locale_desc);

        Integer localeCodeRes = Constants.localeCodes.get(locale);
        if (localeCodeRes != null) {
            localeCodeView.setText(context.getString(localeCodeRes));
        }
        Integer localeDescRes = Constants.localeDescriptions.get(locale);
        if (localeDescRes != null) {
            localeDescView.setText(context.getString(localeDescRes));
        }

        localeLayout.setOnClickListener(l -> action.run());

        return localeLayout;
    }

}
