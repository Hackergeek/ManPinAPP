
package com.mp.android.apps.book.presenter.impl;

import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.mp.android.apps.basemvplib.IView;
import com.mp.android.apps.basemvplib.impl.BasePresenterImpl;
import com.mp.android.apps.book.base.observer.SimpleObserver;
import com.mp.android.apps.book.common.RxBusTag;
import com.mp.android.apps.book.presenter.IMainPresenter;
import com.mp.android.apps.book.utils.BookSourceCheckUtils;
import com.mp.android.apps.book.utils.NetworkUtil;
import com.mp.android.apps.book.view.IMainView;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;
import com.mp.android.apps.readActivity.local.BookRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenterImpl extends BasePresenterImpl<IMainView> implements IMainPresenter {

    public void queryBookShelf(final Boolean needRefresh) {
        if (needRefresh) {
            mView.activityRefreshView();
        }
        Observable.create(new ObservableOnSubscribe<List<CollectionBookBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CollectionBookBean>> e) throws Exception {
                List<CollectionBookBean> bookShelfes;
                try {
                    bookShelfes = BookRepository.getInstance().getCollBooks();
                } catch (Exception e1) {
                    bookShelfes = null;
                }

                e.onNext(bookShelfes == null ? new ArrayList<CollectionBookBean>() : bookShelfes);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<List<CollectionBookBean>>() {
                    @Override
                    public void onNext(List<CollectionBookBean> value) {
                        if (null != value) {
                            mView.refreshBookShelf(value);
                            if (needRefresh) {
                                startRefreshBook(value);
                            } else {
                                mView.refreshFinish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.refreshError(NetworkUtil.getErrorTip(NetworkUtil.ERROR_CODE_ANALY));
                    }
                });
    }

    @Override
    public boolean bookSourceSwitch() {
        return BookSourceCheckUtils.bookSourceSwitch(mView.getContext());
    }

    public void startRefreshBook(List<CollectionBookBean> value) {
        if (value != null && value.size() > 0) {
            mView.setRecyclerMaxProgress(value.size());
            refreshBookShelf(value, 0);
        } else {
            mView.refreshFinish();
        }
    }

    private void refreshBookShelf(final List<CollectionBookBean> value, final int index) {
        if (index <= value.size() - 1) {
            saveBookToShelf(value, index);
        } else {
            queryBookShelf(false);
        }
    }

    private void saveBookToShelf(final List<CollectionBookBean> datas, final int index) {
        Observable.create(new ObservableOnSubscribe<CollectionBookBean>() {
            @Override
            public void subscribe(ObservableEmitter<CollectionBookBean> e) throws Exception {
                BookRepository.getInstance().saveCollBook(datas.get(index));
                e.onNext(datas.get(index));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<CollectionBookBean>() {
                    @Override
                    public void onNext(CollectionBookBean value) {
                        mView.refreshRecyclerViewItemAdd();
                        refreshBookShelf(datas, index + 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.refreshError(NetworkUtil.getErrorTip(NetworkUtil.ERROR_CODE_NONET));
                    }
                });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                    @Tag(RxBusTag.HAD_ADD_BOOK),
                    @Tag(RxBusTag.HAD_REMOVE_BOOK),
                    @Tag(RxBusTag.UPDATE_BOOK_PROGRESS)
            }
    )
    public void hadddOrRemoveBook(CollectionBookBean bookShelfBean) {
        queryBookShelf(false);
    }
}
