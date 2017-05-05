package com.thatnight.sectiondemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ThatNight on 2017.5.5.
 */

public class GroupListViewAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortEntity> mEntities;
    private Context mContext;

    public GroupListViewAdapter(List<SortEntity> entities, Context context) {
        mEntities = entities;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return mEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        SortEntity sortEntity = mEntities.get(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_lv, null);
            viewHolder.mTvLetter = (TextView) view.findViewById(R.id.tv_item_letter);
            viewHolder.mTvTitle = (TextView) view.findViewById(R.id.tv_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        int section = getSectionForPosition(i);
        if (i == getPositionForSection(section)) {
            viewHolder.mTvLetter.setVisibility(View.VISIBLE);
            viewHolder.mTvLetter.setText(sortEntity.getSortLetters());
        } else {
            viewHolder.mTvLetter.setVisibility(View.GONE);
        }
        viewHolder.mTvTitle.setText(sortEntity.getName());
        return view;
    }

    class ViewHolder {
        TextView mTvTitle;
        TextView mTvLetter;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int position) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mEntities.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == position) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        return mEntities.get(i).getSortLetters().charAt(0);
    }
}
