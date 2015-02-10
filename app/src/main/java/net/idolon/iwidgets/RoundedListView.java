package net.idolon.iwidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import static net.idolon.iwidgets.RoundedWrapperDrawable.Orientation;

public class RoundedListView extends ListView {

    //TODO move this to dimension resource
    private static final int DEFAULT_RADIUS = 16;

    private final int mRadius;

    public RoundedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedListView, 0, 0);
        int defaultRadius = getDefaultRadius(context);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundedListView_radius, defaultRadius);
        a.recycle();
    }

    public RoundedListView(Context context) {
        super(context);
        mRadius = getDefaultRadius(context);
    }

    private int getDefaultRadius(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int defaultRadius = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, metrics) + 0.5f);
        return defaultRadius;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        ListAdapter wrapperAdapter = (adapter == null) ? null : new RoundedWrapperAdapter(adapter, mRadius);
        super.setAdapter(wrapperAdapter);
    }

    private static class RoundedWrapperAdapter implements WrapperListAdapter, Filterable {

        private final ListAdapter mAdapter;
        private final boolean mIsFilterable;
        private final int mRadius;

        public RoundedWrapperAdapter(ListAdapter adapter, int radius) {
            mAdapter = adapter;
            mIsFilterable = adapter instanceof Filterable;
            mRadius = radius;
        }

        public boolean isEmpty() {
            return mAdapter.isEmpty();
        }

        public int getCount() {
            return mAdapter.getCount();
        }

        public boolean areAllItemsEnabled() {
            return mAdapter.areAllItemsEnabled();
        }

        public boolean isEnabled(int position) {
            return mAdapter.isEnabled(position);
        }

        public Object getItem(int position) {
            return mAdapter.getItem(position);
        }

        public long getItemId(int position) {
            return mAdapter.getItemId(position);
        }

        public boolean hasStableIds() {
            return mAdapter.hasStableIds();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mAdapter.getView(position, convertView, parent);
            return decorateView(position, view, parent);
        }

        private View decorateView(int position, View view, ViewGroup parent) {
            boolean first = isFirst(position);
            boolean last = isLast(position);
            if (first || last) {
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable != null && !(bgDrawable instanceof RoundedWrapperDrawable)) {
                    RoundedWrapperDrawable roundedBgDrawable = new RoundedWrapperDrawable(bgDrawable, mRadius, first, last, Orientation.VERTICAL);
                    int left = view.getPaddingLeft();
                    int top = view.getPaddingTop();
                    int right = view.getPaddingRight();
                    int bottom = view.getPaddingBottom();
                    view.setBackgroundDrawable(roundedBgDrawable);
                    view.setPadding(left, top, right, bottom);
                    bgDrawable.setCallback(roundedBgDrawable);
                }
            }
            return view;
        }

        private boolean isFirstOrLast(int position) {
            return position == 0 || position == (mAdapter.getCount() - 1);
        }

        private boolean isFirst(int position) {
            return position == 0;
        }

        private boolean isLast(int position) {
            return position == (mAdapter.getCount() - 1);
        }

        public int getItemViewType(int position) {
            if (isFirstOrLast(position)) {
                return AdapterView.ITEM_VIEW_TYPE_IGNORE;
            } else {
                return mAdapter.getItemViewType(position);
            }
        }

        public int getViewTypeCount() {
            return mAdapter.getViewTypeCount();
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            mAdapter.registerDataSetObserver(observer);
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            mAdapter.unregisterDataSetObserver(observer);
        }

        public Filter getFilter() {
            if (mIsFilterable) {
                return ((Filterable) mAdapter).getFilter();
            }
            return null;
        }

        public ListAdapter getWrappedAdapter() {
            return mAdapter;
        }
    }

}
