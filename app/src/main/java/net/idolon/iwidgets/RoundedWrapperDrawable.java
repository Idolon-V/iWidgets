package net.idolon.iwidgets;

import java.io.IOException;
import java.lang.reflect.Field;

import net.idolon.iwidgets.util.DrawableUtil;
import net.idolon.iwidgets.util.ReflectionUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

class RoundedWrapperDrawable extends Drawable implements Drawable.Callback {

    enum Orientation {
        VERTICAL, HORIZONTAL;
    }

    private static final String LOG_TAG = "RoundedDrawable";
    private final Drawable mDrawable;
    private final int mRadius;
    private final boolean mFirst;
    private final boolean mLast;
    private final boolean mVerticalOrientation;

    RoundedWrapperDrawable(Drawable d, int radius, boolean first, boolean last, Orientation orientation) {
        mDrawable = d;
        mRadius = radius;
        mFirst = first;
        mLast = last;
        mVerticalOrientation = (orientation == Orientation.VERTICAL);
        mDrawable.setCallback(this);
    }

    protected boolean onStateChange(int[] state) {
        boolean changed = mDrawable.setState(state);
        return changed;
    }

    protected boolean onLevelChange(int level) {
        boolean changed = mDrawable.setLevel(level);
        return changed;
    }

    protected void onBoundsChange(Rect bounds) {
        mDrawable.setBounds(bounds);
    }

    public void draw(Canvas canvas) {
        Rect rect = canvas.getClipBounds();
        Bitmap bm = Bitmap.createBitmap(rect.width(), rect.height(), Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bm);
        mDrawable.draw(bitmapCanvas);
        Bitmap roundedBitmap = DrawableUtil.getRoundedCornerBitmap(bm, mRadius, mFirst, mLast, mVerticalOrientation);
        canvas.drawBitmap(roundedBitmap, 0, 0, null);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mDrawable.setBounds(left, top, right, bottom);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == RoundedWrapperDrawable.class) {
            RoundedWrapperDrawable other = (RoundedWrapperDrawable) o;
            return mDrawable.equals(other.mDrawable);
        }
        return false;
    }

    public void setChangingConfigurations(int configs) {
        mDrawable.setChangingConfigurations(configs);
    }

    public int getChangingConfigurations() {
        return mDrawable.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        mDrawable.setDither(dither);
    }

    public void setFilterBitmap(boolean filter) {
        mDrawable.setFilterBitmap(filter);
    }

    public int hashCode() {
        return mDrawable.hashCode();
    }

//    public final void setCallback(Callback cb) {
//      mDrawable.setCallback(cb);
//    }
//
//    public Callback getCallback() {
//      return mDrawable.getCallback();
//    }

    public void invalidateSelf() {
        mDrawable.invalidateSelf();
    }

    public String toString() {
        return mDrawable.toString();
    }

    public void scheduleSelf(Runnable what, long when) {
        mDrawable.scheduleSelf(what, when);
    }

    public void unscheduleSelf(Runnable what) {
        mDrawable.unscheduleSelf(what);
    }

    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        mDrawable.setColorFilter(cf);
    }

    public void setColorFilter(int color, Mode mode) {
        mDrawable.setColorFilter(color, mode);
    }

    public void clearColorFilter() {
        mDrawable.clearColorFilter();
    }

    public boolean isStateful() {
        return mDrawable.isStateful();
    }

    public int[] getState() {
        return mDrawable.getState();
    }

//    public void jumpToCurrentState() {
//      mDrawable.jumpToCurrentState();
//    }

    public Drawable getCurrent() {
        return mDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return mDrawable.setVisible(visible, restart);
    }

    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    public Region getTransparentRegion() {
        return mDrawable.getTransparentRegion();
    }

    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    public int getMinimumWidth() {
        return mDrawable.getMinimumWidth();
    }

    public int getMinimumHeight() {
        return mDrawable.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return mDrawable.getPadding(padding);
    }

    public Drawable mutate() {
        return mDrawable.mutate();
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        mDrawable.inflate(r, parser, attrs);
    }

    public ConstantState getConstantState() {
        return mDrawable.getConstantState();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallbackImpl();
        if (callback != null) {
            callback.scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallbackImpl();
        if (callback != null) {
            callback.unscheduleDrawable(who, what);
        }
    }

    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallbackImpl();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    private Callback getCallbackImpl() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return PreHoneycombApi.getCallback(this);
        } else {
            return HoneycombApi.getCallback(this);
        }
    }

    static class PreHoneycombApi {

        static final Field fCallback;

        static {
            fCallback = ReflectionUtil.tryMakeFieldAccessible(Drawable.class, "mCallback");
        }

        static Callback getCallback(Drawable drawable) {
            return ReflectionUtil.tryGet(fCallback, drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static class HoneycombApi {
        static Callback getCallback(Drawable drawable) {
            return drawable.getCallback();
        }
    }

}