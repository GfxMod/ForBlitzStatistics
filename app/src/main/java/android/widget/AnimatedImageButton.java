package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import ru.forblitz.statistics.R.styleable;
import ru.forblitz.statistics.utils.Utils;

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
    }

    public AnimatedImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, styleable.AnimatedImageButton, defStyleAttr, 0);

        drawableOn = a.getDrawable(styleable.AnimatedImageButton_button_checked);
        drawableOff = a.getDrawable(styleable.AnimatedImageButton_button_unchecked);

        a.recycle();

        setOnClickListener();
    }

    public void setOnClickListener() {
        super.setOnClickListener(l -> {
            setActivated(!isActivated());
            Utils.allActivatedCheck((ViewGroup) this.getParent());
        });
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        if (!activated) {
            setImageDrawable(drawableOff);
            startAnimation(Utils.getAnimFrom());
        } else {
            setImageDrawable(drawableOn);
            startAnimation(Utils.getAnimTo());
        }
    }

}
