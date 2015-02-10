package net.idolon.iwidgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * {@link android.widget.HorizontalScrollView} which adds that nifty-looking bounce effect (like on iOS)
 * for edge scrolling.
 */
public class BounceHorizontalScrollView extends HorizontalScrollView {

    private static final int MAX_OVERSCROLL_DISTANCE_X = 160;

    private int mMaxOverscrollDistanceX;

    @SuppressLint("InlinedApi")
    private int mOverScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS;

    public BounceHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBouncing(context);
    }

    public BounceHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBouncing(context);
    }

    public BounceHorizontalScrollView(Context context) {
        super(context);
        initBouncing(context);
    }

    private void initBouncing(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        mMaxOverscrollDistanceX = (int) (density * MAX_OVERSCROLL_DISTANCE_X);
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {

        final int overScrollMode = mOverScrollMode;
        final boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        final boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
        final boolean overScrollHorizontal = overScrollMode == OVER_SCROLL_ALWAYS || (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollHorizontal);
        final boolean overScrollVertical = overScrollMode == OVER_SCROLL_ALWAYS || (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical);

        int newScrollX = scrollX + deltaX;
        if (overScrollHorizontal) {
            maxOverScrollX = mMaxOverscrollDistanceX;
        } else {
            maxOverScrollX = 0;
        }

        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }

        // Clamp values if at the limits and record
        final int left = -maxOverScrollX;
        final int right = maxOverScrollX + scrollRangeX;
        final int top = -maxOverScrollY;
        final int bottom = maxOverScrollY + scrollRangeY;

        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }

        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }

        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

        return clampedX || clampedY;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mOverScrollMode = mode;
    }
}
