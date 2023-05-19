package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import ru.forblitz.statistics.R;

public class ExtendedRadioGroup extends LinearLayout {

    private int indexOfCheckedItem = -1;

    private int animDuration;

    private Drawable checkedBackground;

    private Drawable uncheckedBackground;

    public ExtendedRadioGroup(Context context) {
        super(context);
    }

    public ExtendedRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioGroup);

        checkedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_checked);
        uncheckedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_unchecked);
        animDuration = a.getInt(R.styleable.ExtendedRadioGroup_anim_duration, 0);

        a.recycle();
    }

    public ExtendedRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioGroup);

        checkedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_checked);
        uncheckedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_unchecked);
        animDuration = a.getInt(R.styleable.ExtendedRadioGroup_anim_duration, 0);

        a.recycle();
    }

    public Drawable getCheckedBackground() {
        return checkedBackground;
    }

    public void setCheckedBackground(Drawable checkedBackground) {
        this.checkedBackground = checkedBackground;
    }

    public Drawable getUncheckedBackground() {
        return uncheckedBackground;
    }

    public void setUncheckedBackground(Drawable uncheckedBackground) {
        this.uncheckedBackground = uncheckedBackground;
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public View getCheckedItem() {
        return this.getChildAt(indexOfCheckedItem);
    }

    public void setCheckedItem(int index) {
        this.setCheckedItem(this.getChildAt(index));
    }
    public void setCheckedItem(String tag) {
        this.setCheckedItem(this.findViewWithTag(tag));
    }

    private void setCheckedItem(View view) {
        if (checkedBackground != null && uncheckedBackground != null) {

            if (indexOfCheckedItem != this.indexOfChild(view)) {

                TransitionDrawable transitionDrawableCheck = new TransitionDrawable(new Drawable[] { uncheckedBackground, checkedBackground });
                transitionDrawableCheck.setCrossFadeEnabled(true);
                transitionDrawableCheck.startTransition(animDuration);
                view.setBackground(transitionDrawableCheck);

                if (indexOfCheckedItem != -1) {
                    TransitionDrawable transitionDrawableUncheck = new TransitionDrawable(new Drawable[] { checkedBackground, uncheckedBackground });
                    transitionDrawableUncheck.setCrossFadeEnabled(true);
                    transitionDrawableUncheck.startTransition(animDuration);
                    this.getChildAt(indexOfCheckedItem).setBackground(transitionDrawableUncheck);
                }

            }

        }

        indexOfCheckedItem = indexOfChild(view);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (uncheckedBackground != null) {
            child.setBackground(uncheckedBackground);
        }
    }
}
