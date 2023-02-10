package android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.forblitzstatistics.R;
import com.example.forblitzstatistics.data.Utils;

public class AnimatedImageButton extends AppCompatImageButton {

    private Drawable drawableOn;
    private Drawable drawableOff;

    public AnimatedImageButton(@NonNull Context context) {
        super(context);
    }

    public AnimatedImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedImageButton);

        drawableOn = a.getDrawable(R.styleable.AnimatedImageButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.AnimatedImageButton_button_unchecked);

        a.recycle();
    }

    public AnimatedImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedImageButton, defStyleAttr, 0);

        drawableOn = a.getDrawable(R.styleable.AnimatedImageButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.AnimatedImageButton_button_unchecked);

        a.recycle();
    }

    public void setOnClickListener(Activity activity) {
        super.setOnClickListener(l -> {

            ScaleAnimation anim;
            if (!isActivated()) {
                anim = Utils.getAnimTo();
                setImageDrawable(drawableOn);
            } else {
                anim = Utils.getAnimFrom();
                setImageDrawable(drawableOff);
            }
            startAnimation(anim);
            setActivated(!this.isActivated());

            Utils.allActivatedCheck(activity);

        });
    }

}
