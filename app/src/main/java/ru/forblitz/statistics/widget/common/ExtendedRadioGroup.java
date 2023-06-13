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
import ru.forblitz.statistics.utils.InterfaceUtils;

/**
 * {@link ExtendedRadioGroup} is a custom widget that extends
 * {@link LinearLayout}. Allows you to use any widget that has a background as
 * a radio button and use custom animations to switch.
 * <ul>
 *     <li>{@link ru.forblitz.statistics.R.attr#background_checked} The
 *     drawable resource for the button when it is activated.</li>
 *     <li>{@link ru.forblitz.statistics.R.attr#background_unchecked} The
 *     drawable resource for the button when it is not activated.</li>
 *     <li>{@link ru.forblitz.statistics.R.attr#anim_duration} The duration of
 *     the animation for changing the selected element.</li>
 *     <li>{@link ru.forblitz.statistics.R.attr#child_height} The child height
 *     (by default -1 (set any other value for health).</li>
 * </ul>
 */
public class ExtendedRadioGroup extends LinearLayout {

    private int indexOfCheckedItem = -1;

    private int animDuration;

    private Drawable checkedBackground;

    private Drawable uncheckedBackground;

    private int childHeight;

    public ExtendedRadioGroup(Context context) {
        super(context);
    }

    public ExtendedRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioGroup);

        checkedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_checked);
        uncheckedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_unchecked);
        animDuration = a.getInt(R.styleable.ExtendedRadioGroup_anim_duration, 0);
        childHeight = a.getInt(R.styleable.ExtendedRadioGroup_child_height, -1);

        a.recycle();
    }

    public ExtendedRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedRadioGroup);

        checkedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_checked);
        uncheckedBackground = a.getDrawable(R.styleable.ExtendedRadioGroup_background_unchecked);
        animDuration = a.getInt(R.styleable.ExtendedRadioGroup_anim_duration, 0);
        childHeight = a.getInt(R.styleable.ExtendedRadioGroup_child_height, -1);

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

    public int getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(int childHeight) {
        this.childHeight = childHeight;

        if (childHeight != -1) {
            for (int i = 0; i < this.getChildCount(); i++) {
                View child = this.getChildAt(i);

                InterfaceUtils.doBySavingThePadding(
                        child,
                        () -> {
                            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                            layoutParams.height = childHeight;
                            layoutParams.weight = 0;
                            child.setLayoutParams(layoutParams);
                        }
                );
            }
        }
    }

    private void setChildHeight() {
        setChildHeight(childHeight);
    }

    private void setCheckedItem(View view) {
        if (checkedBackground != null && uncheckedBackground != null) {

            if (indexOfCheckedItem != this.indexOfChild(view)) {

                InterfaceUtils.doBySavingThePadding(
                        view,
                        () -> {
                            TransitionDrawable transitionDrawableCheck = new TransitionDrawable(new Drawable[] { uncheckedBackground, checkedBackground });
                            transitionDrawableCheck.setCrossFadeEnabled(true);
                            transitionDrawableCheck.startTransition(animDuration);
                            view.setBackground(transitionDrawableCheck);
                        }
                );

                if (indexOfCheckedItem != -1) {
                    InterfaceUtils.doBySavingThePadding(
                            this.getChildAt(indexOfCheckedItem),
                            () -> {
                                TransitionDrawable transitionDrawableUncheck = new TransitionDrawable(new Drawable[] { checkedBackground, uncheckedBackground });
                                transitionDrawableUncheck.setCrossFadeEnabled(true);
                                transitionDrawableUncheck.startTransition(animDuration);
                                this.getChildAt(indexOfCheckedItem).setBackground(transitionDrawableUncheck);
                            }
                    );
                }

            }

        }

        indexOfCheckedItem = indexOfChild(view);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (uncheckedBackground != null) {
            InterfaceUtils.doBySavingThePadding(
                    child, () -> child.setBackground(uncheckedBackground)
            );
        }
    }
}
