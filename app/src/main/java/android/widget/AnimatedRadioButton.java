package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.forblitzstatistics.data.Utils;

public class AnimatedRadioButton extends AppCompatRadioButton {

    public AnimatedRadioButton(Context context) {
        super(context);
    }

    public AnimatedRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    @Override
    public void setChecked(boolean checked) {

        ScaleAnimation anim;
        if (checked) {
            anim = Utils.getAnimTo();
        } else {
            anim = Utils.getAnimFrom();
        }
        startAnimation(anim);

        super.setChecked(checked);
    }

}
