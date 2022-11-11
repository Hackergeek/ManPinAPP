package com.mp.android.apps.book.model;

import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.readActivity.bean.BookChapterBean;
import com.mp.android.apps.readActivity.bean.ChapterInfoBean;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IReaderBookModel {

    /**
     * 搜索图书
     *
     * @param content 搜索图书内容
     * @param page
     * @return
     */
    Observable<List<SearchBookBean>> searchBook(String content, int page);

    /**
     * 获取图书详情
     *
     * @param collBookBean
     * @return
     */
    Observable<CollectionBookBean> getBookInfo(CollectionBookBean collBookBean);

    /**
     * 获取图书章节
     *
     * @param collBookBean
     * @return
     */
    Single<List<BookChapterBean>> getBookChapters(CollectionBookBean collBookBean);

    /**
     * 获取当前章节详细内容
     *
     * @param url
     * @return
     */
    Single<ChapterInfoBean> getChapterInfo(String url);

    /**
     * 返回当前model TAG
     * example: http://www.baidu.com
     * 注意： 地址后缀不能加/
     * @return
     */
    String getTAG();


}
