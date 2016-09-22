package com.lchli.pinedrecyclerlistview.library;

import android.util.SparseArray;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * this is a helper to find list section.
 * Created by lchli on 2016/9/22.
 */

public class SectionFinder {

    private List<ListSectionData> sectionDataList = new ArrayList<>();
    private SparseArray<SoftReference<Object>> cacheMap = new SparseArray<>();


    /**
     * find previous section data before current firstVisiablePosition.
     *
     * @param firstVisiablePosition
     * @return
     */
    public ListSectionData findSectionData(int firstVisiablePosition) {

        SoftReference<Object> cacheRef = cacheMap.get(firstVisiablePosition);
        if (cacheRef != null) {
            Object cache = cacheRef.get();
            if (cache != null) {
                if (cache instanceof ListSectionData) {
                    return (ListSectionData) cache;
                } else {
                    return null;
                }
            }
        }
        ListSectionData sectionData = findRecusive(0, sectionDataList.size() - 1, firstVisiablePosition);

        if (sectionData != null) {
            cacheMap.put(firstVisiablePosition, new SoftReference<Object>(sectionData));
        } else {
            cacheMap.put(firstVisiablePosition, new SoftReference<Object>(new ListSectionData.Null()));
        }
        return sectionData;
    }

    /**
     * binary search .
     *
     * @param low
     * @param high
     * @param firstVisiablePosition
     * @return
     */
    private ListSectionData findRecusive(int low, int high, int firstVisiablePosition) {
        if (low > high) {
            return null;
        }
        int middle = (low + high) / 2;
        ListSectionData mid = sectionDataList.get(middle);
        if (firstVisiablePosition >= mid.inAdapterPosition) {
            if (middle + 1 > high || firstVisiablePosition < sectionDataList.get(middle + 1).inAdapterPosition) {
                return mid;
            } else {
                return findRecusive(middle + 1, high, firstVisiablePosition);
            }

        } else {
            return findRecusive(low, middle - 1, firstVisiablePosition);
        }


    }

    /**
     * add section data to sectionDataList.
     *
     * @param datas
     */
    private void saveSectionDatas(List datas) {
        if (datas != null && !datas.isEmpty()) {
            for (int i = 0; i < datas.size(); i++) {
                Object o = datas.get(i);
                if (o instanceof ListSectionData) {
                    ListSectionData sectionData = (ListSectionData) o;
                    sectionData.inAdapterPosition = i;
                    sectionDataList.add(sectionData);
                }

            }

        }
        Collections.sort(sectionDataList, new Comparator<ListSectionData>() {
            @Override
            public int compare(ListSectionData lhs, ListSectionData rhs) {
                return lhs.inAdapterPosition - rhs.inAdapterPosition;
            }
        });
    }

    /**
     * when you changed adapter data,you must call this to reset.
     *
     * @param datas
     */
    public void refresh(List datas) {
        cacheMap.clear();
        sectionDataList.clear();
        saveSectionDatas(datas);

    }
}
