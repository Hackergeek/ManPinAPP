
package com.mp.android.apps.book.presenter;

import com.mp.android.apps.basemvplib.IPresenter;
import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;

public interface IBookDetailPresenter extends IPresenter {

    int getOpenFrom();

    SearchBookBean getSearchBook();


    CollectionBookBean getCollBookBean();

    /**
     * 是否是在本地书架中
     * @return
     */
    Boolean getInBookShelf();

    void getBookShelfInfo();

    void addToBookShelf();

    void removeFromBookShelf();
}
