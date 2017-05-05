package com.thatnight.sectiondemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mLvFriends;
    private Toolbar mTb;
    private List<SortEntity> mFriends;
    private Handler mHandler;
    private String mUserId;
    private GroupListViewAdapter mAdapter;
    private SortEntity mSortEntity;
    private SideBar mSideBar;
    private TextView mTvDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        mLvFriends = (ListView) findViewById(R.id.lv_main);
        mSideBar = (SideBar) findViewById(R.id.sidebar);
        mTvDialog = (TextView) findViewById(R.id.tv_dialog);
        mSideBar.setTvDialog(mTvDialog);
    }

    private void initEvent() {
        mSortEntity = new SortEntity();
        mFriends = fillData(getResources().getStringArray(R.array.title));
        Collections.sort(mFriends, mSortEntity);
        mAdapter = new GroupListViewAdapter(mFriends, this);
        mSideBar.setOnTouchLetterChangeListener(new SideBar.OnTouchLetterChangeListener() {
            @Override
            public void letterChange(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLvFriends.setSelection(position);
                }
            }
        });
        mLvFriends.setAdapter(mAdapter);
    }

    private List<SortEntity> fillData(String[] data) {
        List<SortEntity> entities = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            SortEntity sortEntity = new SortEntity();
            sortEntity.setName(data[i]);
            sortEntity.setSortLetters(data[i].substring(0, 1).toUpperCase());
            entities.add(sortEntity);
        }
        return entities;
    }
}