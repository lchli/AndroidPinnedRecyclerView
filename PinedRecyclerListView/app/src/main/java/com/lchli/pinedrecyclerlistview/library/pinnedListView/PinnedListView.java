package com.lchli.pinedrecyclerlistview.library.pinnedListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lchli.pinedrecyclerlistview.library.ListSectionData;

/**
 * Created by lchli on 2016/9/22.
 */

public class PinnedListView extends ListView {

    private View pinnedView;
    private int mWidthMode;
    private int mHeightMode;
    private AbsAdapter.AbsViewHolder vh;
    private OnScrollListener mOnScrollListener;

    public PinnedListView(Context context) {
        super(context);
        init();
    }

    public PinnedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinnedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setOnScrollListener(OnScrollListener lsn) {
        mOnScrollListener = lsn;
    }

    private void init() {
        super.setOnScrollListener(new MyOnScrollListener());
    }

    private class MyOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            PinnedListAdapter adapter = (PinnedListAdapter) getAdapter();
            if (adapter == null) {
                return;
            }

            ListSectionData previousSection = adapter.getSectionFinder().findSectionData(firstVisibleItem);
            if (previousSection == null) {
                setPinnedView(null);
                return;
            }

            if (vh == null || vh.viewType != previousSection.sectionViewType) {
                vh = adapter.onCreateViewHolder(PinnedListView.this, previousSection.sectionViewType);
            }
            adapter.onBindViewHolder(vh, previousSection.inAdapterPosition);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);
            int heightSpec;
            ViewGroup.LayoutParams layoutParams = vh.getItemView().getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
            } else {
                heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            vh.getItemView().measure(widthSpec, heightSpec);
            vh.getItemView().layout(0, 0, vh.getItemView().getMeasuredWidth(), vh.getItemView().getMeasuredHeight());

            setPinnedView(vh.getItemView());
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (pinnedView != null) {
            int offset = 0;
            int saveCount = canvas.save();
            canvas.translate(0, offset);
            canvas.clipRect(0, 0, getWidth(), pinnedView.getMeasuredHeight()); // needed

            pinnedView.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    private void setPinnedView(View pinedView) {
        this.pinnedView = pinedView;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
    }

    public void setPinnedAdapter(PinnedListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        throw new UnsupportedOperationException("you must use setPinnedAdapter instead of setAdapter.");
    }
}
