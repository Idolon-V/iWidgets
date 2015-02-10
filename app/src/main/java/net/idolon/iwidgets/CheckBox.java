package net.idolon.iwidgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

/**
 * Custom CheckBox used to eleminate pre-JB bug with drawable padding
 *
 * @author idolon
 */
public class CheckBox extends android.widget.CheckBox {

    private static final boolean sFixPadding = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1;

    private Drawable mButtonDrawable;

    public CheckBox(Context context) {
        super(context);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getCompoundPaddingLeft() {
        if (sFixPadding) {
            int padding = super.getCompoundPaddingLeft();
            final Drawable buttonDrawable = mButtonDrawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth();
            }
            return padding;
        } else {
            return super.getCompoundPaddingLeft();
        }
    }

    @Override
    public void setButtonDrawable(Drawable d) {
        super.setButtonDrawable(d);
        mButtonDrawable = d;
    }

}