
package com.mp.android.apps.book.presenter.impl;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.mp.android.apps.basemvplib.IView;
import com.mp.android.apps.basemvplib.impl.BaseActivity;
import com.mp.android.apps.basemvplib.impl.BasePresenterImpl;
import com.mp.android.apps.book.base.observer.SimpleObserver;
import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.book.bean.SearchHistoryBean;
import com.mp.android.apps.book.common.RxBusTag;
import com.mp.android.apps.book.dao.SearchHistoryBeanDao;
import com.mp.android.apps.book.model.WebBookModelControl;
import com.mp.android.apps.book.presenter.ISearchPresenter;
import com.mp.android.apps.book.view.ISearchView;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;
import com.mp.android.apps.readActivity.local.DaoDbHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenterImpl extends BasePresenterImpl<ISearchView> implements ISearchPresenter {
    public static final String TAG_KEY = "tag";

    public static final int BOOK = 2;

    private Boolean hasSearch = false;   //判断是否搜索过

    private int page = 1;
    private List<Map<String, String>> searchEngine;


    private long startThisSearchTime;

    private List<CollectionBookBean> collectionBookBeans = new ArrayList<>();   //用来比对搜索的书籍是否已经添加进书架

    private Boolean isInput = false;

    public SearchPresenterImpl() {
        List<CollectionBookBean> temp = DaoDbHelper.getInstance().getSession().getCollectionBookBeanDao().queryBuilder().list();
        if (temp != null && temp.size() > 0) {
            collectionBookBeans.addAll(temp);
        }
    }


    @Override
    public Boolean getHasSearch() {
        return hasSearch;
    }

    @Override
    public void setHasSearch(Boolean hasSearch) {
        this.hasSearch = hasSearch;
    }

    @Override
    public void insertSearchHistory() {
        final int type = SearchPresenterImpl.BOOK;
        final String content = mView.getEditContent().getText().toString().trim();
        Observable.create((ObservableOnSubscribe<SearchHistoryBean>) e -> {
            List<SearchHistoryBean> datas = DaoDbHelper.getInstance().getSession().getSearchHistoryBeanDao()
                    .queryBuilder()
                    .where(SearchHistoryBeanDao.Properties.Type.eq(type), SearchHistoryBeanDao.Properties.Content.eq(content))
                    .limit(1)
                    .build().list();
            SearchHistoryBean searchHistoryBean = null;
            if (null != datas && datas.size() > 0) {
                searchHistoryBean = datas.get(0);
                searchHistoryBean.setDate(System.currentTimeMillis());
                DaoDbHelper.getInstance().getSession().getSearchHistoryBeanDao().update(searchHistoryBean);
            } else {
                searchHistoryBean = new SearchHistoryBean(type, content, System.currentTimeMillis());
                DaoDbHelper.getInstance().getSession().getSearchHistoryBeanDao().insert(searchHistoryBean);
            }
            e.onNext(searchHistoryBean);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<SearchHistoryBean>() {
                    @Override
                    public void onNext(SearchHistoryBean value) {
                        mView.insertSearchHistorySuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void cleanSearchHistory() {
        final int type = SearchPresenterImpl.BOOK;
        final String content = mView.getEditContent().getText().toString().trim();
        Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        int a = DaoDbHelper.getInstance().getDatabase().delete(SearchHistoryBeanDao.TABLENAME, SearchHistoryBeanDao.Properties.Type.columnName + "=? and " + SearchHistoryBeanDao.Properties.Content.columnName + " like ?", new String[]{String.valueOf(type), "%" + content + "%"});
                        e.onNext(a);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Integer>() {
                    @Override
                    public void onNext(Integer value) {
                        if (value > 0) {
                            mView.querySearchHistorySuccess(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void querySearchHistory() {
        final int type = SearchPresenterImpl.BOOK;
        final String content = mView.getEditContent().getText().toString().trim();
        Observable.create(new ObservableOnSubscribe<List<SearchHistoryBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<SearchHistoryBean>> e) throws Exception {
                        List<SearchHistoryBean> datas = DaoDbHelper.getInstance().getSession().getSearchHistoryBeanDao()
                                .queryBuilder()
                                .where(SearchHistoryBeanDao.Properties.Type.eq(type), SearchHistoryBeanDao.Properties.Content.like("%" + content + "%"))
                                .orderDesc(SearchHistoryBeanDao.Properties.Date)
                                .limit(20)
                                .build().list();
                        e.onNext(datas);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<SearchHistoryBean>>() {
                    @Override
                    public void onNext(List<SearchHistoryBean> value) {
                        if (null != value)
                            mView.querySearchHistorySuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void initPage() {
        this.page = 1;
    }

    @Override
    public void toSearchBooks(String key, Boolean fromError) {
        if (key != null) {
            this.startThisSearchTime = System.currentTimeMillis();
        }
        searchBook(key, startThisSearchTime, fromError);
    }


    private AtomicInteger callBackNumber;

    private void searchBook(final String content, final long searchTime, Boolean fromError) {
        callBackNumber = new AtomicInteger();

        for (int i = 0; i < searchEngine.size(); i++) {
            WebBookModelControl.getInstance()
                    .searchOtherBook(content, page, (String) searchEngine.get(i).get(TAG_KEY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<List<SearchBookBean>>() {
                        @Override
                        public void onNext(List<SearchBookBean> value) {

                            // 判断当前搜索图书是否已经加入本地
                            for (SearchBookBean temp : value) {
                                for (CollectionBookBean collectionBookBean : collectionBookBeans) {
                                    if (temp.getNoteUrl().equals(collectionBookBean.get_id())) {
                                        temp.setAdd(true);
                                        break;
                                    }
                                }
                            }
                            mView.loadMoreSearchBook(content, value);
                            int number = callBackNumber.incrementAndGet();
                            if (number == searchEngine.size()) {
                                mView.refreshFinish(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            int number = callBackNumber.incrementAndGet();
                            if (number == searchEngine.size()) {
                                mView.refreshFinish(false);
                            }
                        }
                    });
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void addBookToShelf(final SearchBookBean searchBookBean) {
        CollectionBookBean collBookBean = new CollectionBookBean().getCollBookBeanFromSearch(searchBookBean);
        if (collBookBean != null) {
            if (collBookBean.getBookChapterUrl() != null) {
                BookShelUtils.getInstance().addToBookShelfUtils(collBookBean);
            } else {
                WebBookModelControl.getInstance().getBookInfo(collBookBean).subscribeOn(Schedulers.io())
                        .compose(((BaseActivity) mView.getContext()).<CollectionBookBean>bindUntilEvent(ActivityEvent.DESTROY))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SimpleObserver<CollectionBookBean>() {
                            @Override
                            public void onNext(CollectionBookBean collBookBean) {
                                BookShelUtils.getInstance().addToBookShelfUtils(collBookBean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mView.getContext(), "加入书架失败", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void attachView(@NonNull IView iView) {
        super.attachView(iView);
        //搜索引擎初始化
        searchEngine = new ArrayList<>();
        WebBookModelControl.getInstance().registerSearchEngine(searchEngine, mView.getContext());
        //注册 eventBus
        RxBus.get().register(this);
    }

    @Override
    public void detachView() {
        RxBus.get().unregister(this);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(RxBusTag.HAD_ADD_BOOK)
            }
    )
    /**
     * rxbus发送消息进行接受
     */
    public void hadAddBook(CollectionBookBean collBookBean) {
        collectionBookBeans.add(collBookBean);
        List<SearchBookBean> datas = mView.getSearchBookAdapter().getSearchBooks();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getNoteUrl().equals(collBookBean.get_id())) {
                datas.get(i).setAdd(true);
                mView.updateSearchItem(i);
                break;
            }
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(RxBusTag.HAD_REMOVE_BOOK)
            }
    )
    public void hadRemoveBook(CollectionBookBean collBookBean) {
        if (collectionBookBeans != null) {
            for (int i = 0; i < collectionBookBeans.size(); i++) {
                if (collectionBookBeans.get(i).get_id().equals(collBookBean.get_id())) {
                    collectionBookBeans.remove(i);
                    break;
                }
            }
        }
        List<SearchBookBean> datas = mView.getSearchBookAdapter().getSearchBooks();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getNoteUrl().equals(collBookBean.get_id())) {
                datas.get(i).setAdd(false);
                mView.updateSearchItem(i);
                break;
            }
        }
    }

    @Override
    public Boolean getInput() {
        return isInput;
    }

    @Override
    public void setInput(Boolean input) {
        isInput = input;
    }
}
