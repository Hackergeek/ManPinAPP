package com.mp.android.apps.monke.monkeybook.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mp.android.apps.monke.monkeybook.bean.BookInfoBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_INFO_BEAN".
*/
public class BookInfoBeanDao extends AbstractDao<BookInfoBean, String> {

    public static final String TABLENAME = "BOOK_INFO_BEAN";

    /**
     * Properties of entity BookInfoBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Tag = new Property(1, String.class, "tag", false, "TAG");
        public final static Property NoteUrl = new Property(2, String.class, "noteUrl", true, "NOTE_URL");
        public final static Property ChapterUrl = new Property(3, String.class, "chapterUrl", false, "CHAPTER_URL");
        public final static Property FinalRefreshData = new Property(4, long.class, "finalRefreshData", false, "FINAL_REFRESH_DATA");
        public final static Property CoverUrl = new Property(5, String.class, "coverUrl", false, "COVER_URL");
        public final static Property Author = new Property(6, String.class, "author", false, "AUTHOR");
        public final static Property Introduce = new Property(7, String.class, "introduce", false, "INTRODUCE");
        public final static Property Origin = new Property(8, String.class, "origin", false, "ORIGIN");
    }


    public BookInfoBeanDao(DaoConfig config) {
        super(config);
    }
    
    public BookInfoBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_INFO_BEAN\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"TAG\" TEXT," + // 1: tag
                "\"NOTE_URL\" TEXT PRIMARY KEY NOT NULL ," + // 2: noteUrl
                "\"CHAPTER_URL\" TEXT," + // 3: chapterUrl
                "\"FINAL_REFRESH_DATA\" INTEGER NOT NULL ," + // 4: finalRefreshData
                "\"COVER_URL\" TEXT," + // 5: coverUrl
                "\"AUTHOR\" TEXT," + // 6: author
                "\"INTRODUCE\" TEXT," + // 7: introduce
                "\"ORIGIN\" TEXT);"); // 8: origin
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_INFO_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookInfoBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(2, tag);
        }
 
        String noteUrl = entity.getNoteUrl();
        if (noteUrl != null) {
            stmt.bindString(3, noteUrl);
        }
 
        String chapterUrl = entity.getChapterUrl();
        if (chapterUrl != null) {
            stmt.bindString(4, chapterUrl);
        }
        stmt.bindLong(5, entity.getFinalRefreshData());
 
        String coverUrl = entity.getCoverUrl();
        if (coverUrl != null) {
            stmt.bindString(6, coverUrl);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(7, author);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(8, introduce);
        }
 
        String origin = entity.getOrigin();
        if (origin != null) {
            stmt.bindString(9, origin);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookInfoBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(2, tag);
        }
 
        String noteUrl = entity.getNoteUrl();
        if (noteUrl != null) {
            stmt.bindString(3, noteUrl);
        }
 
        String chapterUrl = entity.getChapterUrl();
        if (chapterUrl != null) {
            stmt.bindString(4, chapterUrl);
        }
        stmt.bindLong(5, entity.getFinalRefreshData());
 
        String coverUrl = entity.getCoverUrl();
        if (coverUrl != null) {
            stmt.bindString(6, coverUrl);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(7, author);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(8, introduce);
        }
 
        String origin = entity.getOrigin();
        if (origin != null) {
            stmt.bindString(9, origin);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2);
    }    

    @Override
    public BookInfoBean readEntity(Cursor cursor, int offset) {
        BookInfoBean entity = new BookInfoBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tag
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // noteUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // chapterUrl
            cursor.getLong(offset + 4), // finalRefreshData
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // coverUrl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // author
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // introduce
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // origin
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookInfoBean entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTag(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNoteUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setChapterUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFinalRefreshData(cursor.getLong(offset + 4));
        entity.setCoverUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAuthor(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIntroduce(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setOrigin(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BookInfoBean entity, long rowId) {
        return entity.getNoteUrl();
    }
    
    @Override
    public String getKey(BookInfoBean entity) {
        if(entity != null) {
            return entity.getNoteUrl();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookInfoBean entity) {
        return entity.getNoteUrl() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
