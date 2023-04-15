package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.utils.InterfaceUtils;

public class AnimatedRadioButton extends AppCompatRadioButton {

    private Drawable drawableOn;
    private Drawable drawableOff;

    public AnimatedRadioButton(Context context) {
        super(context);
    }

    public AnimatedRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRadioButton);

        drawableOn = a.getDrawable(R.styleable.AnimatedRadioButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.AnimatedRadioButton_button_unchecked);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_checked }, drawableOn);
        stateListDrawable.addState(StateSet.WILD_CARD, drawableOff);
        this.setButtonDrawable(stateListDrawable);

        a.recycle();

        setOnLongClickListener();
    }

    public AnimatedRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRadioButton, defStyleAttr, 0);

        drawableOn = a.getDrawable(R.styleable.AnimatedRadioButton_button_checked);
        drawableOff = a.getDrawable(R.styleable.AnimatedRadioButton_button_unchecked);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_checked }, drawableOn);
        stateListDrawable.addState(StateSet.WILD_CARD, drawableOff);
        this.setButtonDrawable(stateListDrawable);

        a.recycle();

        setOnLongClickListener();
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
            anim = InterfaceUtils.getAnimTo();
        } else {
            anim = InterfaceUtils.getAnimFrom();
        }
        startAnimation(anim);

        super.setChecked(checked);

    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
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

    private void setOnLongClickListener() {
        if (getContentDescription() != null) {
            setOnLongClickListener(v -> {
                Toast.makeText(getContext(), getContentDescription(), Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

}
