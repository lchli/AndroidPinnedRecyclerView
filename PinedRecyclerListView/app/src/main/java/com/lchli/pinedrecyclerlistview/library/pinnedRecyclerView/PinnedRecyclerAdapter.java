package com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView;

import com.lchli.pinedrecyclerlistview.library.SectionFinder;

import java.util.List;

/**
 * Created by lchli on 2016/9/20.
 */

public abstract class PinnedRecyclerAdapter extends BaseRecyclerAdapter<Object> {

    private SectionFinder mSectionFinder = new SectionFinder();

    /**
     * when you changed adapter data,you must call this to reset.
     *
     * @param datas
     */
    @Override
    public void refresh(List datas) {
        mSectionFinder.refresh(datas);

        super.refresh(datas);

    }

     SectionFinder getSectionFinder() {
        return mSectionFinder;
    }
}
