package com.sevele.ds.activity;

import android.view.View;

import com.baidu.android.activity.BaseActivity;
import com.example.decryptstranger.R;
import com.sevele.ds.view.TitleBar;

/**
 * @author:liu ge
 * @createTime:2015年5月18日
 * @descrption:公用的title
 */
public abstract class TitleBarActivity extends BaseActivity implements
        TitleBar.OnClickListener {

    private TitleBar mTitleBar;

    private void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.titleBar);
        mTitleBar.setOnClickListener(this);
    };

    public void setTitleLeft(View view) {
        checkTitleBar();
        mTitleBar.setLeft(view);
    }

    public void setTitleLeft(View view, int gravity) {
        checkTitleBar();
        mTitleBar.setLeft(view, gravity);
    }

    public void setTitleMiddle(View view) {
        checkTitleBar();
        mTitleBar.setMiddle(view);
    }

    public void setTitleRight(View view) {
        checkTitleBar();
        mTitleBar.setRight(view);
    }

    public void setTitleRight(View view, int gravity) {
        checkTitleBar();
        mTitleBar.setRight(view, gravity);
    }

    private void checkTitleBar() {
        if (null == mTitleBar) {
            initTitleBar();
        }
    }

    @Override
    public void onClickLeft() {
        finish();
    }

    @Override
    public void onClickMiddle() {
        // do nothing
    }

    @Override
    public void onClickRight() {
        // do nothing
    }
}
