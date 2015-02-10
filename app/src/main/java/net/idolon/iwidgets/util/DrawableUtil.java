package net.idolon.iwidgets.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class DrawableUtil {

    public static int dipsToPixels(int dips, Context context) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, context.getResources().getDisplayMetrics()));
    }

    public static int dipsToPixels(int dips, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, displayMetrics));
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius, boolean startRounded, boolean endRounded, boolean verticalOrientation) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = radius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        if (verticalOrientation) {
            if (!endRounded) {
                canvas.drawRect(0, radius, width, height, paint);
            }
            if (!startRounded) {
                canvas.drawRect(0, 0, width, height - radius, paint);
            }
        } else {
            if (!endRounded) {
                canvas.drawRect(radius, 0, width, height, paint);
            }
            if (!startRounded) {
                canvas.drawRect(0, 0, width - radius, height, paint);
            }
        }

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        return getRoundedCornerBitmap(bitmap, pixels, true, true, true);
    }

    public static Paint getTiledPaint(Context ctx, int tileDrawableId, TileMode tileModeX, TileMode tileModeY) {
        Resources res = ctx.getResources();
        Paint paint = new Paint();
        Bitmap tileBitmap = BitmapFactory.decodeResource(res, tileDrawableId);
        paint.setShader(new BitmapShader(tileBitmap, tileModeX, tileModeY));
        return paint;
    }

    public static Bitmap getRoundedCornerTiledBitmap(Context context, int width, int height, int radius, boolean topRounded, boolean bottomRounded,
                                                     int tileDrawableResId, TileMode tileModeX, TileMode tileModeY) {
        Bitmap rectBm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(rectBm);
        Paint tiledPaint = getTiledPaint(context, tileDrawableResId, tileModeX, tileModeY);
        canvas.drawPaint(tiledPaint);
        Bitmap roundedBm = getRoundedCornerBitmap(rectBm, radius, topRounded, bottomRounded, true);
        return roundedBm;
    }

    public static Bitmap convertToGrayscale(Bitmap bitmap) {
        ColorMatrix bwMatrix = new ColorMatrix();
        bwMatrix.setSaturation(0);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(bwMatrix);
        Bitmap bmGray = bitmap.copy(Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        Canvas myCanvas = new Canvas(bmGray);
        myCanvas.drawBitmap(bmGray, 0, 0, paint);
        return bmGray;
    }

    public static Bitmap getBitmapFromView(View v, int widthMeasureSpec, int heightMeasureSpec) {
        v.measure(widthMeasureSpec, heightMeasureSpec);
        return getBitmapFromView(v);
    }

    public static Bitmap getBitmapFromView(View v) {
        int width = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //   v.layout(0, 0, width, height);
        v.draw(c);
        return b;
    }

}
