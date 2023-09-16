package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.HapticFeedbackConstants;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import ru.forblitz.statistics.ForBlitzStatisticsApplication;
import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * {@link ExtendedRadioButton} is a custom widget that extends
 * {@link AppCompatRadioButton} to create an image button with extended
 * functionality.
 * <ul>
 *     <li>{@link ru.forblitz.statistics.R.attr#switchable} A boolean resource
 *     indicating the need to switch images and activate.</li>
 *     <li>{@link ru.forblitz.statistics.R.attr#button_checked} The drawable
 *     resource for the button when it is activated.</li>
 *     <li>{@link ru.forblitz.statistics.R.attr#button_unchecked} The drawable
 *     resource for the button when it is not activated.</li>
 * </ul>
 */
public class ExtendedRadioButton extends AppCompatRadioButton {

    private boolean switchable;
    private Drawable drawableOn;
    private Drawable drawableOff;

    public ExtendedRadioButton(Context context) {
        super(context);

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }
    }

    public ExtendedRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioButton);

        switchable = a.getBoolean(R.styleable.ExtendedImageButton_switchable, false);
        drawableOn = a.getDrawable(R.styleable.ExtendedRadioButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.ExtendedRadioButton_button_unchecked);

        if (switchable) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[] { android.R.attr.state_checked }, drawableOn);
            stateListDrawable.addState(StateSet.WILD_CARD, drawableOff);
            this.setButtonDrawable(stateListDrawable);
        }

        a.recycle();

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }
    }

    public ExtendedRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioButton, defStyleAttr, 0);

        switchable = a.getBoolean(R.styleable.ExtendedImageButton_switchable, false);
        drawableOn = a.getDrawable(R.styleable.ExtendedRadioButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.ExtendedRadioButton_button_unchecked);

        if (switchable) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[] { android.R.attr.state_checked }, drawableOn);
            stateListDrawable.addState(StateSet.WILD_CARD, drawableOff);
            this.setButtonDrawable(stateListDrawable);
        }

        a.recycle();

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (switchable) {
            ScaleAnimation anim;
            if (checked) {
                anim = InterfaceUtils.getAnimTo();
            } else {
                anim = InterfaceUtils.getAnimFrom();
            }
            startAnimation(anim);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (switchable) {
            int minDimen = Math.min(getMeasuredWidth(), getMeasuredHeight());

            if (minDimen > 0 && drawableOn != null && drawableOff != null) {
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(
                        new int[] { android.R.attr.state_checked },
                        InterfaceUtils.createScaledSquareDrawable(this.getContext(), drawableOn, minDimen, minDimen)
                );
                stateListDrawable.addState(
                        StateSet.WILD_CARD,
                        InterfaceUtils.createScaledSquareDrawable(this.getContext(), drawableOff, minDimen, minDimen)
                );
                this.setButtonDrawable(stateListDrawable);
            }
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(v -> {
            if (((ForBlitzStatisticsApplication) getContext().getApplicationContext()).isHapticsEnabled()) {
                performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
            }
            if (l != null) {
                l.onClick(v);
            }
        });
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        if (getContentDescription() != null && !getContentDescription().equals("")) {
            super.setOnLongClickListener(v -> {
                Toast.makeText(getContext(), getContentDescription(), Toast.LENGTH_SHORT).show();
                if (l != null) {
                    l.onLongClick(v);
                }
                return true;
            });
        } else {
            super.setOnLongClickListener(l);
        }
    }

}
