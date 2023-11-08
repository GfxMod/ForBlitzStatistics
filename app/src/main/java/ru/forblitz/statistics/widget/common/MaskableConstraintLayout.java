package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import ru.forblitz.statistics.R;

/**
 * {@link MaskableConstraintLayout} is a custom widget that extends
 * {@link ConstraintLayout}. Allows you to apply a mask in two modes:
 * <ul>
 *     <li>Paddings and corner mode (rounded rectangle)</li>
 *     <li>Custom path mode</li>
 * </ul>
 */
public class MaskableConstraintLayout extends ConstraintLayout {

    private int corner;

    private int maskPadding;
    private int maskPaddingStart;
    private int maskPaddingEnd;
    private int maskPaddingTop;
    private int maskPaddingBottom;

    private Path customPath;

    public MaskableConstraintLayout(@NonNull Context context) {
        super(context);
    }

    public MaskableConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskableConstraintLayout);

        corner = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_corner, 0);
        maskPadding = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPadding, 0);
        maskPaddingStart = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingStart, 0);
        maskPaddingEnd = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingEnd, 0);
        maskPaddingTop = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingTop, 0);
        maskPaddingBottom = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingBottom, 0);

        a.recycle();
    }

    public MaskableConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskableConstraintLayout, defStyleAttr, 0);

        corner = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_corner, 0);
        maskPadding = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPadding, 0);
        maskPaddingStart = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingStart, 0);
        maskPaddingEnd = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingEnd, 0);
        maskPaddingTop = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingTop, 0);
        maskPaddingBottom = a.getDimensionPixelSize(R.styleable.MaskableConstraintLayout_maskPaddingBottom, 0);

        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        Path currentPath;
        if (customPath != null) {
            currentPath = customPath;
        } else {
            int start = 0;
            int end = getWidth();
            int top = 0;
            int bottom = getHeight();

            if (maskPaddingStart != 0) { start += maskPaddingStart; } else { start += maskPadding; }
            if (maskPaddingEnd != 0) { end -= maskPaddingEnd; } else { end -= maskPadding; }
            if (maskPaddingTop != 0) { top += maskPaddingTop; } else { top += maskPadding; }
            if (maskPaddingBottom != 0) { bottom -= maskPaddingBottom; } else { bottom -= maskPadding; }

            currentPath = new Path();
            currentPath.addRoundRect(new RectF(start, top, end, bottom), corner, corner, Path.Direction.CW);
        }
        canvas.clipPath(currentPath);
        try {
            super.draw(canvas);
        } catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public void setCustomPath(Path customPath) {
        this.customPath = customPath;
    }

}
