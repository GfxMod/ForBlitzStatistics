package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;

import ru.forblitz.statistics.R;

public class DifferenceIndicatorView extends AppCompatTextView {

    Drawable plusBackground;
    Drawable minusBackground;
    int plusTextColor;
    int minusTextColor;

    public DifferenceIndicatorView(@NonNull Context context) {
        super(context);
    }

    public DifferenceIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DifferenceIndicatorView);

        plusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_plusBackground);
        minusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_minusBackground);
        plusTextColor = a.getColor(R.styleable.DifferenceIndicatorView_plusTextColor, context.getColor(R.color.session_green));
        minusTextColor = a.getColor(R.styleable.DifferenceIndicatorView_minusTextColor, context.getColor(R.color.session_red));

        if (plusBackground == null) {
            plusBackground = AppCompatResources.getDrawable(context, R.drawable.background_sessions_true);
        }
        if (minusBackground == null) {
            minusBackground = AppCompatResources.getDrawable(context, R.drawable.background_sessions_false);
        }

        a.recycle();
    }

    public DifferenceIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DifferenceIndicatorView, defStyleAttr, 0);

        plusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_plusBackground);
        minusBackground = a.getDrawable(R.styleable.DifferenceIndicatorView_minusBackground);
        plusTextColor = a.getColor(R.styleable.DifferenceIndicatorView_plusTextColor, context.getColor(R.color.session_green));
        minusTextColor = a.getColor(R.styleable.DifferenceIndicatorView_minusTextColor, context.getColor(R.color.session_red));

        if (plusBackground == null) {
            plusBackground = AppCompatResources.getDrawable(context, R.drawable.background_sessions_true);
        }
        if (minusBackground == null) {
            minusBackground = AppCompatResources.getDrawable(context, R.drawable.background_sessions_false);
        }

        a.recycle();
    }

    public void setValue(String text, boolean lastSymbolNotANumber) {

        setText(text);

        double value;
        if (!lastSymbolNotANumber) {
            value = Double.parseDouble(text);
        } else {
            value = Double.parseDouble(text.substring(0, text.length() - 1));
        }

        if (value > 0) {
            setVisibility(VISIBLE);
            setTextColor(plusTextColor);
            setBackgroundDrawable(plusBackground);
        } else if (value < 0) {
            setVisibility(VISIBLE);
            setTextColor(minusTextColor);
            setBackgroundDrawable(minusBackground);
        } else {
            setVisibility(INVISIBLE);
        }

    }

}
