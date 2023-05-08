package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import ru.forblitz.statistics.R.styleable;
import ru.forblitz.statistics.utils.InterfaceUtils;

public class AnimatedImageButton extends AppCompatImageButton {

    private Drawable drawableOn;
    private Drawable drawableOff;

    public AnimatedImageButton(@NonNull Context context) {
        super(context);
    }

    public AnimatedImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, styleable.AnimatedImageButton);

        drawableOn = a.getDrawable(styleable.AnimatedImageButton_button_checked);
        drawableOff = a.getDrawable(styleable.AnimatedImageButton_button_unchecked);

        a.recycle();

        setOnClickListener();
        setOnLongClickListener();
    }

    public AnimatedImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, styleable.AnimatedImageButton, defStyleAttr, 0);

        drawableOn = a.getDrawable(styleable.AnimatedImageButton_button_checked);
        drawableOff = a.getDrawable(styleable.AnimatedImageButton_button_unchecked);

        a.recycle();

        setOnClickListener();
        setOnLongClickListener();
    }

    private void setOnClickListener() {
        super.setOnClickListener(l -> setActivated(!isActivated()));
    }

    private void setOnLongClickListener() {
        if (getContentDescription() != null) {
            setOnLongClickListener(v -> {
                Toast.makeText(getContext(), getContentDescription(), Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        if (!activated) {
            setImageDrawable(drawableOff);
            startAnimation(InterfaceUtils.getAnimFrom());
        } else {
            setImageDrawable(drawableOn);
            startAnimation(InterfaceUtils.getAnimTo());
        }
    }

}