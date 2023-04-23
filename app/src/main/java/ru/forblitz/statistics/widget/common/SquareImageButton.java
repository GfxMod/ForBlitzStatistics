package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import ru.forblitz.statistics.R;

public class SquareImageButton extends AppCompatImageButton {

    int orientation;

    public SquareImageButton(@NonNull Context context) {
        super(context);
    }

    public SquareImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageButton);

        orientation = a.getInt(R.styleable.SquareImageButton_main_dimension, -1);

        a.recycle();

    }

    public SquareImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageButton, defStyleAttr, 0);

        orientation = a.getInt(R.styleable.SquareImageButton_main_dimension, -1);

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        if (orientation == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (orientation == 0) {
            size = widthMeasureSpec;
            super.onMeasure(size, size);
        } else {
            size = heightMeasureSpec;
            super.onMeasure(size, size);
        }
    }
}
