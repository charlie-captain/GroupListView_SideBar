package com.thatnight.sectiondemo;

import java.util.Comparator;

/**
 * Created by ThatNight on 2017.5.5.
 */

public class SortEntity implements Comparator<SortEntity> {
    private String mName;
    private String mSortLetters;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSortLetters() {
        return mSortLetters;
    }

    public void setSortLetters(String sortLetters) {
        mSortLetters = sortLetters;
    }

    @Override
    public int compare(SortEntity t1, SortEntity t2) {
        return t1.getSortLetters().compareTo(t2.getSortLetters());
    }
}
