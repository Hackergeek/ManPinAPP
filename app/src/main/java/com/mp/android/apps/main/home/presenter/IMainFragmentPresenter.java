package com.mp.android.apps.main.home.presenter;

import com.mp.android.apps.basemvplib.IPresenter;


public interface IMainFragmentPresenter extends IPresenter {


    void initHomeData();

    void getContentPosition(int mContentPosition, String kinds);
}
