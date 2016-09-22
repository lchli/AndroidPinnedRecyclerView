package com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by lchli on 2016/4/2.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> mDatas;
    protected boolean isScrolling = false;


    public void refresh(List<T> datas) {
        mDatas = datas;
        this.notifyDataSetChanged();
    }

    public T getItem(int pos) {
        if (mDatas == null || mDatas.isEmpty()) {
            return null;
        }
        return mDatas.get(pos);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public RecyclerView.OnScrollListener getListOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrolling = false;
                    notifyDataSetChanged();
                } else {
                    isScrolling = true;
                }
            }

        };
    }
}
