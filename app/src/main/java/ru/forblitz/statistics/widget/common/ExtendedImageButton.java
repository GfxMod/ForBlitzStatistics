package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.HapticUtils;
import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * {@link ExtendedImageButton} is a custom widget that extends
 * {@link AppCompatImageButton} to create an image button with extended
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
public class ExtendedImageButton extends AppCompatImageButton {

    private boolean switchable;

    private Drawable drawableOn;
    private Drawable drawableOff;

    /**
     * Equals -1 if not set; 0 if the main dimension is horizontal; 1 if the
     * main dimension is vertical.
     */
    private int mainDimension;

    public ExtendedImageButton(@NonNull Context context) {
        super(context);

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }
    }

    public ExtendedImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedImageButton);

        switchable = a.getBoolean(R.styleable.ExtendedImageButton_switchable, false);
        drawableOn = a.getDrawable(R.styleable.ExtendedImageButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.ExtendedImageButton_button_unchecked);
        mainDimension = a.getInt(R.styleable.ExtendedImageButton_main_dimension, -1);

        a.recycle();

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }

     }

    public ExtendedImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedImageButton, defStyleAttr, 0);

        switchable = a.getBoolean(R.styleable.ExtendedImageButton_switchable, false);
        drawableOn = a.getDrawable(R.styleable.ExtendedImageButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.ExtendedImageButton_button_unchecked);
        mainDimension = a.getInt(R.styleable.ExtendedImageButton_main_dimension, -1);

        a.recycle();

        if (!hasOnClickListeners()) {
            setOnClickListener(v -> { });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasOnLongClickListeners()) {
            setOnLongClickListener(v -> false);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(v -> {
            HapticUtils.performHapticFeedback(this);
            if (switchable) {
                setActivated(!isActivated());
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

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        flipDrawable(activated);
    }

    private void flipDrawable(boolean activated) {
        if (switchable && drawableOn != null && drawableOff != null) {
            if (!activated) {
                setImageDrawable(drawableOff);
                startAnimation(InterfaceUtils.getAnimFrom());
            } else {
                setImageDrawable(drawableOn);
                startAnimation(InterfaceUtils.getAnimTo());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mainDimension == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (mainDimension == 0) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else if (mainDimension == 1) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

}
