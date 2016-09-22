package com.lchli.pinedrecyclerlistview.library;

import java.io.Serializable;

/**
 * Created by lchli on 2016/9/20.
 */

/**
 * data will be pinned at listview top.
 */
public abstract class ListSectionData implements Serializable {

    public int sectionViewType;
    public int inAdapterPosition;

    public ListSectionData(int sectionViewType) {
        this.sectionViewType = sectionViewType;
    }

    static class Null {

    }
}
