package ru.forblitz.statistics.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * It's ViewFlipper, but setDisplayedChild() works only if whichChild
 * difference by already displayed child
 */
public class DifferenceViewFlipper extends ViewFlipper {

    public DifferenceViewFlipper(Context context) {
        super(context);
    }

    public DifferenceViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param whichChild the index of the child view to display
     */
    @Override
    public void setDisplayedChild(int whichChild) {
        if (getDisplayedChild() != whichChild) {
            super.setDisplayedChild(whichChild);
        }
    }

}
