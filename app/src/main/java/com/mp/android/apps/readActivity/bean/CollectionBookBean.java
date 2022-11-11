package com.mp.android.apps.readActivity.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.mp.android.apps.MyApplication;
import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.book.dao.BookChapterBeanDao;
import com.mp.android.apps.book.dao.CollectionBookBeanDao;
import com.mp.android.apps.book.dao.DaoSession;
import com.mp.android.apps.readActivity.utils.Constant;
import com.mp.android.apps.readActivity.utils.StringUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


/**
 * 收藏的书籍
 */
@Entity
public class CollectionBookBean implements Parcelable {

    public static final int STATUS_UNCACHE = 0; //未缓存
    public static final int STATUS_CACHING = 1; //正在缓存
    public static final int STATUS_CACHED = 2;  //已经缓存
    /**
     * _id : 53663ae356bdc93e49004474
     * title : 逍遥派
     * author : 白马出淤泥
     * shortIntro : 金庸武侠中有不少的神秘高手，书中或提起名字，或不曾提起，总之他们要么留下了绝世秘笈，要么就名震武林。 独孤九剑的创始者，独孤求败，他真的只创出九剑吗？ 残本葵花...
     * cover : /cover/149273897447137
     * hasCp : true
     * latelyFollower : 60213
     * retentionRatio : 22.87
     * updated : 2017-05-07T18:24:34.720Z
     * <p>
     * chaptersCount : 1660
     * lastChapter : 第1659章 朱长老
     */
    @Id
    private String _id; // 本地书籍中，path 的 md5 值作为本地书籍的 id
    private String title;//书名
    private String author;//作者
    private String shortIntro;//图书简介
    private String cover; // 在本地书籍中，该字段作为本地文件的路径//网络图书为图片
    private boolean hasCp;
    private int latelyFollower;
    private double retentionRatio;
    //最新更新日期 //必填项
    private String updated;
    //最新阅读日期//必填项
    private String lastRead;
    private int chaptersCount;
    private String lastChapter;
    //是否更新或未阅读
    private boolean isUpdate = true;
    //是否是本地文件
    private boolean isLocal = false;
    //数据源地址标识
    private String bookTag;
    //章节集合地址
    private String bookChapterUrl;

    public String getBookChapterUrl() {
        return bookChapterUrl;
    }

    public void setBookChapterUrl(String bookChapterUrl) {
        this.bookChapterUrl = bookChapterUrl;
    }

    @ToMany(referencedJoinProperty = "bookId")
    private List<BookChapterBean> bookChapterList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 729793913)
    private transient CollectionBookBeanDao myDao;

    @Generated(hash = 1052023064)
    public CollectionBookBean(String _id, String title, String author, String shortIntro, String cover, boolean hasCp,
            int latelyFollower, double retentionRatio, String updated, String lastRead, int chaptersCount, String lastChapter,
            boolean isUpdate, boolean isLocal, String bookTag, String bookChapterUrl) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.shortIntro = shortIntro;
        this.cover = cover;
        this.hasCp = hasCp;
        this.latelyFollower = latelyFollower;
        this.retentionRatio = retentionRatio;
        this.updated = updated;
        this.lastRead = lastRead;
        this.chaptersCount = chaptersCount;
        this.lastChapter = lastChapter;
        this.isUpdate = isUpdate;
        this.isLocal = isLocal;
        this.bookTag = bookTag;
        this.bookChapterUrl = bookChapterUrl;
    }

    public CollectionBookBean() {
    }

    public String get_id() {
        return _id;
    }

    /**
     * searchBookBean.getNoteUrl()
     * 默认设置为
     *
     * @param _id
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return StringUtils.convertCC(title, MyApplication.getInstance());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return StringUtils.convertCC(author, MyApplication.getInstance());
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortIntro() {
        return StringUtils.convertCC(shortIntro, MyApplication.getInstance());
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getCover() {
        return StringUtils.convertCC(cover, MyApplication.getInstance());
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isHasCp() {
        return hasCp;
    }

    public void setHasCp(boolean hasCp) {
        this.hasCp = hasCp;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public String getUpdated() {
        return StringUtils.convertCC(updated, MyApplication.getInstance());
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public String getLastChapter() {
        return StringUtils.convertCC(lastChapter, MyApplication.getInstance());
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean getHasCp() {
        return this.hasCp;
    }

    public boolean getIsUpdate() {
        return this.isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getLastRead() {
        return StringUtils.convertCC(lastRead, MyApplication.getInstance());
    }

    public String getBookTag() {
        return bookTag;
    }

    public void setBookTag(String bookTag) {
        this.bookTag = bookTag;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public void setBookChapters(List<BookChapterBean> beans) {
        bookChapterList = beans;
        for (BookChapterBean bean : bookChapterList) {
            bean.setBookId(get_id());
        }
    }

    public List<BookChapterBean> getBookChapters() {
        if (daoSession == null) {
            return bookChapterList;
        } else {
            return getBookChapterList();
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1624660715)
    public List<BookChapterBean> getBookChapterList() {
        if (bookChapterList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookChapterBeanDao targetDao = daoSession.getBookChapterBeanDao();
            List<BookChapterBean> bookChapterListNew = targetDao._queryCollectionBookBean_BookChapterList(_id);
            synchronized (this) {
                if (bookChapterList == null) {
                    bookChapterList = bookChapterListNew;
                }
            }
        }
        return bookChapterList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1077762221)
    public synchronized void resetBookChapterList() {
        bookChapterList = null;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    public boolean getIsLocal() {
        return this.isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.shortIntro);
        dest.writeString(this.cover);
        dest.writeByte(this.hasCp ? (byte) 1 : (byte) 0);
        dest.writeInt(this.latelyFollower);
        dest.writeDouble(this.retentionRatio);
        dest.writeString(this.updated);
        dest.writeString(this.lastRead);
        dest.writeInt(this.chaptersCount);
        dest.writeString(this.lastChapter);
        dest.writeByte(this.isUpdate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
        dest.writeString(this.bookChapterUrl);
    }

    protected CollectionBookBean(Parcel in) {
        this._id = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        this.shortIntro = in.readString();
        this.cover = in.readString();
        this.hasCp = in.readByte() != 0;
        this.latelyFollower = in.readInt();
        this.retentionRatio = in.readDouble();
        this.updated = in.readString();
        this.lastRead = in.readString();
        this.chaptersCount = in.readInt();
        this.lastChapter = in.readString();
        this.isUpdate = in.readByte() != 0;
        this.isLocal = in.readByte() != 0;
        this.bookChapterUrl = in.readString();
    }

    public static final Creator<CollectionBookBean> CREATOR = new Creator<CollectionBookBean>() {
        @Override
        public CollectionBookBean createFromParcel(Parcel source) {
            return new CollectionBookBean(source);
        }

        @Override
        public CollectionBookBean[] newArray(int size) {
            return new CollectionBookBean[size];
        }
    };

    /**
     * searchBook转换为CollBook
     *
     * @param searchBookBean
     * @return
     */
    public CollectionBookBean getCollBookBeanFromSearch(SearchBookBean searchBookBean) {
        CollectionBookBean collBookBean = new CollectionBookBean();
        collBookBean.set_id(searchBookBean.getNoteUrl());
        collBookBean.setAuthor(searchBookBean.getAuthor());
        collBookBean.setCover(searchBookBean.getCoverUrl());
        collBookBean.setIsLocal(false);
        collBookBean.setIsUpdate(false);
        collBookBean.setTitle(searchBookBean.getName());
        collBookBean.setLastRead(StringUtils.
                dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
        if (searchBookBean.getLastChapter() != null) {
            collBookBean.setLastChapter(searchBookBean.getLastChapter());
        } else {
            collBookBean.setLastChapter("无章节");
        }

        collBookBean.setHasCp(false);
        if (searchBookBean.getUpdated() != null && searchBookBean.getUpdated().length() > 0) {
            collBookBean.setUpdated(searchBookBean.getUpdated());
        } else {
            collBookBean.setUpdated(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_FILE_DATE));
        }
        if (TextUtils.isEmpty(searchBookBean.getDesc())) {
            collBookBean.setShortIntro("暂无介绍");
        } else {
            collBookBean.setShortIntro(searchBookBean.getDesc());
        }
        collBookBean.setBookTag(searchBookBean.getTag());
        return collBookBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1699652971)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCollectionBookBeanDao() : null;
    }
}