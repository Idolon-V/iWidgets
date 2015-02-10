package net.idolon.iwidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioGroup;

import static net.idolon.iwidgets.RoundedWrapperDrawable.Orientation;

public class RoundedRadioGroup extends RadioGroup {

    private static final int DEFAULT_RADIUS = 16;

    private final int mRadius;

    public RoundedRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedListView, 0, 0);
        int defaultRadius = getDefaultRadius(context);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundedListView_radius, defaultRadius);
        a.recycle();
    }

    public RoundedRadioGroup(Context context) {
        super(context);
        mRadius = getDefaultRadius(context);
    }

    private int getDefaultRadius(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int defaultRadius = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, metrics) + 0.5f);
        return defaultRadius;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        View decorated = decorateChild(child);
        return super.drawChild(canvas, decorated, drawingTime);
    }

    private View decorateChild(View child) {
        int count = getChildCount();
        if (count == 0) {
            return child;
        }

        boolean first = (child == getChildAt(0));
        boolean last = (child == getChildAt(count - 1));
        if (first || last) {
            Drawable bgDrawable = child.getBackground();
            if (bgDrawable != null && !(bgDrawable instanceof RoundedWrapperDrawable)) {
                Orientation orientation = getOrientation() == VERTICAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;
                RoundedWrapperDrawable roundedBgDrawable = new RoundedWrapperDrawable(bgDrawable, mRadius, first, last, orientation);
                child.setBackgroundDrawable(roundedBgDrawable);
                bgDrawable.setCallback(roundedBgDrawable);
            }
        }
        return child;
    }

}
