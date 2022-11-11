
package com.mp.android.apps.book.presenter.impl;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.mp.android.apps.MyApplication;
import com.mp.android.apps.basemvplib.IView;
import com.mp.android.apps.basemvplib.impl.BaseActivity;
import com.mp.android.apps.basemvplib.impl.BasePresenterImpl;
import com.mp.android.apps.book.BitIntentDataManager;
import com.mp.android.apps.book.base.observer.SimpleObserver;
import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.book.common.RxBusTag;
import com.mp.android.apps.book.dao.CollectionBookBeanDao;
import com.mp.android.apps.book.model.WebBookModelControl;
import com.mp.android.apps.book.presenter.IBookDetailPresenter;
import com.mp.android.apps.book.view.IBookDetailView;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;
import com.mp.android.apps.readActivity.local.BookRepository;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BookDetailPresenterImpl extends BasePresenterImpl<IBookDetailView> implements IBookDetailPresenter {
    public final static int FROM_BOOKSHELF = 1;
    public final static int FROM_SEARCH = 2;

    private int openfrom;
    private SearchBookBean searchBook;
    private CollectionBookBean collBookBean;
    private Boolean inBookShelf = false;
    private List<CollectionBookBean> localCollBooks = Collections.synchronizedList(new ArrayList<CollectionBookBean>());   //用来比对搜索的书籍是否已经添加进书架

    public BookDetailPresenterImpl(Intent intent) {
        openfrom = intent.getIntExtra("from", FROM_BOOKSHELF);


        if (openfrom == FROM_BOOKSHELF) {
            String key = intent.getStringExtra("data_key");
            collBookBean = (CollectionBookBean) BitIntentDataManager.getInstance().getData(key);
            BitIntentDataManager.getInstance().cleanData(key);
            inBookShelf = true;
        } else {
            searchBook = intent.getParcelableExtra("data");
            inBookShelf = searchBook.getAdd();
        }
    }

    public Boolean getInBookShelf() {
        return inBookShelf;
    }

    public void setInBookShelf(Boolean inBookShelf) {
        this.inBookShelf = inBookShelf;
    }

    public int getOpenFrom() {
        return openfrom;
    }

    public SearchBookBean getSearchBook() {
        return searchBook;
    }


    @Override
    public CollectionBookBean getCollBookBean() {
        return collBookBean;
    }

    @Override
    public void getBookShelfInfo() {
        CollectionBookBean collBookInfo = new CollectionBookBean().getCollBookBeanFromSearch(searchBook);
        //图书详情是否使用本地数据获取,本地数据会导致爬虫数据失效
        boolean UseLocalData = !"noimage".equals(collBookInfo.getCover()) && !TextUtils.isEmpty(collBookInfo.getBookChapterUrl());
        if (UseLocalData) {
            CollectionBookBean localCollBookBean = BookRepository.getInstance().getSession().getCollectionBookBeanDao().queryBuilder().where(CollectionBookBeanDao.Properties._id.eq(collBookInfo.get_id())).build().unique();
            if (localCollBookBean != null) {
                inBookShelf = true;
            }
            collBookBean = collBookInfo;
            mView.updateView();
        } else {
            Observable.create((ObservableOnSubscribe<CollectionBookBean>) emitter -> emitter.onNext(collBookInfo))
                    .flatMap((Function<CollectionBookBean, ObservableSource<CollectionBookBean>>)
                            collBookBean -> WebBookModelControl.getInstance().getBookInfo(collBookBean)).subscribeOn(Schedulers.io())
                    .compose(((BaseActivity) mView.getContext()).<CollectionBookBean>bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<CollectionBookBean>() {
                        @Override
                        public void onNext(CollectionBookBean value) {
                            CollectionBookBean localCollBookBean = BookRepository.getInstance().getSession().getCollectionBookBeanDao().queryBuilder().where(CollectionBookBeanDao.Properties._id.eq(value.get_id())).build().unique();
                            if (localCollBookBean != null) {
                                inBookShelf = true;
                            }
                            collBookBean = value;
                            mView.updateView();
                        }

                        @Override
                        public void onError(Throwable e) {
                            collBookBean = null;
                            mView.getBookShelfError();
                        }
                    });

        }


    }

    @Override
    public void addToBookShelf() {
        if (collBookBean != null) {
            BookShelUtils.getInstance().addToBookShelfUtils(collBookBean);
        }
    }

    @Override
    public void removeFromBookShelf() {
        if (collBookBean != null) {
            Observable.create((ObservableOnSubscribe<Boolean>) e -> {
                        BookRepository.getInstance().deleteCollBookSync(collBookBean);
                        e.onNext(true);
                        e.onComplete();
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((BaseActivity) mView.getContext()).<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new SimpleObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean value) {
                            if (value) {
                                RxBus.get().post(RxBusTag.HAD_REMOVE_BOOK, collBookBean);
                            } else {
                                Toast.makeText(MyApplication.getInstance(), "移出书架失败!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(MyApplication.getInstance(), "移出书架失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void attachView(@NonNull IView iView) {
        super.attachView(iView);
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
    public void hadAddBook(CollectionBookBean value) {
        if ((null != collBookBean && value.get_id().equals(collBookBean.get_id()))
                || (null != searchBook && value.get_id().equals(searchBook.getNoteUrl()))) {
            inBookShelf = true;
            if (null != searchBook) {
                searchBook.setAdd(inBookShelf);
            }
            mView.updateView();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(RxBusTag.HAD_REMOVE_BOOK)
            }
    )
    public void hadRemoveBook(CollectionBookBean value) {
        if (localCollBooks != null) {
            for (int i = 0; i < localCollBooks.size(); i++) {
                if (localCollBooks.get(i).get_id().equals(value.get_id())) {
                    localCollBooks.remove(i);
                    break;
                }
            }
        }
        if ((null != collBookBean && value.get_id().equals(collBookBean.get_id()))
                || (null != searchBook && value.get_id().equals(searchBook.getNoteUrl()))) {
            inBookShelf = false;
            if (null != searchBook) {
                searchBook.setAdd(false);
            }
            mView.updateView();
        }
    }


}
