package com.lchli.pinedrecyclerlistview.library.pinnedListView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class AbsAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected boolean isScrolling = false;


    @Override
    public int getCount() {
        return isEmptyList(mDatas) ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {

        return isEmptyList(mDatas) ? null : mDatas.get(position);
    }

    private static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public  void refresh(List<T> datas) {
        mDatas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || ((AbsViewHolder) convertView.getTag()).viewType != getItemViewType(position)) {
            AbsViewHolder vh = onCreateViewHolder(parent, getItemViewType(position));
            onBindViewHolder(vh, position);
            View item = vh.getItemView();
            item.setTag(vh);
            return item;
        } else {
            AbsViewHolder vh = (AbsViewHolder) convertView.getTag();
            onBindViewHolder(vh, position);
            return convertView;
        }
    }

    public abstract AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(final AbsViewHolder holder, int position);

    public static abstract class AbsViewHolder {

        public int viewType;

        public AbsViewHolder(int viewType) {
            this.viewType = viewType;
        }

        protected abstract View getItemView();

    }

    public AbsListView.OnScrollListener getListOnScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    isScrolling = false;
                    notifyDataSetChanged();
                } else {
                    isScrolling = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };
    }

}
