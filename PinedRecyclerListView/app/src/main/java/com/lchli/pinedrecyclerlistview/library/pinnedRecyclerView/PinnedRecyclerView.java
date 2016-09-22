package com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lchli.pinedrecyclerlistview.library.ListSectionData;

/**
 * Created by lchli on 2016/9/19.
 * this view can surpport different pinned view at same time.
 */

public class PinnedRecyclerView extends RecyclerView {

    private View pinnedView;
    private int mWidthMode;
    private int mHeightMode;
    private ViewHolder vh;

    public PinnedRecyclerView(Context context) {
        super(context);
        init();
    }

    public PinnedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinnedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new MyOnScrollListener());
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            PinnedRecyclerAdapter adapter = (PinnedRecyclerAdapter) getAdapter();
            if (adapter == null) {
                return;
            }
            int first;
            LayoutManager manager = getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                first = layoutManager.findFirstVisibleItemPosition();
            } else {
                throw new IllegalArgumentException("PinnedRecyclerView only surpport LinearLayoutManager/GridLayoutManager so far.");
            }

            ListSectionData previousSection = adapter.getSectionFinder().findSectionData(first);
            if (previousSection == null) {
                setPinnedView(null);
                return;
            }

            if (vh == null || vh.getItemViewType() != previousSection.sectionViewType) {
                vh = adapter.onCreateViewHolder(PinnedRecyclerView.this, previousSection.sectionViewType);
            }
            adapter.onBindViewHolder(vh, previousSection.inAdapterPosition);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);
            int heightSpec;
            ViewGroup.LayoutParams layoutParams = vh.itemView.getLayoutParams();//note:if itemview use fixedsize will be invalid.
            if (layoutParams != null && layoutParams.height > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
            } else {
                heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            vh.itemView.measure(widthSpec, heightSpec);
            vh.itemView.layout(0, 0, vh.itemView.getMeasuredWidth(), vh.itemView.getMeasuredHeight());

            setPinnedView(vh.itemView);
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

    public void setPinnedAdapter(PinnedRecyclerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        throw new UnsupportedOperationException("you must use PinnedRecyclerView#setPinnedAdapter instead of setAdapter.");
    }
}
