
package com.mp.android.apps.book.model.impl;

import com.google.android.apps.photolab.storyboard.download.MD5Utils;
import com.mp.android.apps.book.base.MBaseModelImpl;
import com.mp.android.apps.book.bean.SearchBookBean;
import com.mp.android.apps.book.common.api.IYb3API;
import com.mp.android.apps.book.model.IReaderBookModel;
import com.mp.android.apps.book.model.ObtainBookInfoUtils;
import com.mp.android.apps.readActivity.bean.BookChapterBean;
import com.mp.android.apps.readActivity.bean.ChapterInfoBean;
import com.mp.android.apps.readActivity.bean.CollectionBookBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

/**
 *网站不可访问
 */
public class ContentYb3ModelImpl extends MBaseModelImpl implements IReaderBookModel {
    public static final String TAG = "https://www.yb3.cc";

    public static ContentYb3ModelImpl getInstance() {
        return new ContentYb3ModelImpl();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Observable<List<SearchBookBean>> searchBook(String content, int page) {
        return getRetrofitObject(TAG).create(IYb3API.class).searchBook(content).flatMap((Function<String, ObservableSource<List<SearchBookBean>>>) s -> analySearchBook(s));
    }

    public Observable<List<SearchBookBean>> analySearchBook(final String s) {
        return Observable.create(e -> {
            try {
                Document doc = Jsoup.parse(s);
                Elements booksE = doc.getElementsByClass("novelslist2").get(0).getElementsByTag("li");
                if (null != booksE && booksE.size() > 1) {
                    List<SearchBookBean> books = new ArrayList<SearchBookBean>();
                    for (int i = 1; i < booksE.size(); i++) {
                        SearchBookBean item = new SearchBookBean();
                        item.setTag(TAG);
                        item.setAuthor(booksE.get(i).getElementsByClass("s4").get(0).text());
                        item.setKind(booksE.get(i).getElementsByClass("s2").get(0).text());
                        item.setLastChapter(booksE.get(i).getElementsByClass("s3").get(0).getElementsByTag("a").get(0).text());
                        item.setOrigin("yb3.cc");
                        item.setName(booksE.get(i).getElementsByClass("s2").get(1).getElementsByTag("a").get(0).text());
                        item.setNoteUrl(TAG + booksE.get(i).getElementsByClass("s2").get(1).getElementsByTag("a").get(0).attr("href"));
                        item.setCoverUrl("noimage");
                        item.setUpdated(booksE.get(i).getElementsByClass("s5").get(0).text());
                        books.add(item);
                    }
                    e.onNext(books);
                } else {
                    e.onNext(new ArrayList<SearchBookBean>());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onNext(new ArrayList<SearchBookBean>());
            }
            e.onComplete();
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public Observable<CollectionBookBean> getBookInfo(CollectionBookBean collBookBean) {
        return getRetrofitObject(TAG).create(IYb3API.class).getBookInfo(collBookBean.get_id().replace(TAG, "")).flatMap((Function<String, ObservableSource<CollectionBookBean>>) s -> analyBookInfo(s, collBookBean));
    }

    private Observable<CollectionBookBean> analyBookInfo(final String s, final CollectionBookBean collBookBean) {
        return Observable.create(e -> {
            collBookBean.setBookTag(TAG);
            Document doc = Jsoup.parse(s);
            Element resultE = doc.getElementsByClass("box_con").get(0);
            collBookBean.set_id(collBookBean.get_id());
            collBookBean.setCover(resultE.getElementById("fmimg").getElementsByTag("img").get(0).attr("src"));

            collBookBean.setTitle(resultE.getElementById("info").getElementsByTag("h1").get(0).text());
            String author = resultE.getElementById("info").getElementsByTag("p").get(0).text().toString().trim();
            author = author.replace(" ", "").replace("  ", "").replace("作者：", "");
            collBookBean.setAuthor(author);

            List<TextNode> contentEs = resultE.getElementById("intro").textNodes();
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < contentEs.size(); i++) {
                String temp = contentEs.get(i).text().trim();
                temp = temp.replaceAll(" ", "").replaceAll(" ", "");
                if (temp.length() > 0) {
                    content.append("\u3000\u3000" + temp);
                    if (i < contentEs.size() - 1) {
                        content.append("\r\n");
                    }
                }
            }
            collBookBean.setUpdated(resultE.getElementById("info").getElementsByTag("p").get(2).text().toString().trim());
            collBookBean.setShortIntro(content.toString());
            collBookBean.setBookChapterUrl(collBookBean.get_id());
            String lastChapter = resultE.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").get(0).text();
            collBookBean.setLastChapter(lastChapter);
            try {
                ObtainBookInfoUtils.getInstance().senMessageManpin(collBookBean, "", lastChapter);
            } catch (Exception e1) {

            }
            e.onNext(collBookBean);
            e.onComplete();
        });
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////


    public Single<List<BookChapterBean>> getBookChapters(CollectionBookBean collBookBean) {
        return getRetrofitObject(TAG).create(IYb3API.class).getChapterLists(collBookBean.getBookChapterUrl())
                .flatMap((Function<String, Single<List<BookChapterBean>>>) s -> Single.create((SingleOnSubscribe<List<BookChapterBean>>) emitter -> {
                    emitter.onSuccess(analyChapterlist(s, collBookBean));
                }));
    }

    private List<BookChapterBean> analyChapterlist(String s, CollectionBookBean collBookBean) {
        List<BookChapterBean> chapterBeans = new ArrayList<BookChapterBean>();
        Document doc = Jsoup.parse(s);
        Elements chapterlist = doc.getElementById("list").getElementsByTag("dd");

        for (int i = 0; i < chapterlist.size(); i++) {
            BookChapterBean temp = new BookChapterBean();
            String linkUrl = TAG + chapterlist.get(i).getElementsByTag("a").get(0).attr("href");
            temp.setId(MD5Utils.strToMd5By16(linkUrl));
            temp.setTitle(chapterlist.get(i).getElementsByTag("a").get(0).text());
            temp.setPosition(i);
            temp.setLink(linkUrl);
            temp.setBookId(collBookBean.get_id());
            temp.setUnreadble(false);
            chapterBeans.add(temp);
        }

        return chapterBeans;


    }

    /////////////////////////////////////////////////////////////////////////////////


    @Override
    public Single<ChapterInfoBean> getChapterInfo(String url) {
        return getRetrofitObject(TAG).create(IYb3API.class).getChapterInfo(url).flatMap(s -> Single.create(emitter -> emitter.onSuccess(analysisChapterInfo(s, url))));
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    private ChapterInfoBean analysisChapterInfo(String s, String url) {
        ChapterInfoBean chapterInfoBean = new ChapterInfoBean();

        try {
            Document doc = Jsoup.parse(s);
            List<TextNode> contentEs = doc.getElementById("content").textNodes();
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < contentEs.size(); i++) {
                String temp = contentEs.get(i).text().trim();
                temp = temp.replaceAll(" ", "").replaceAll(" ", "");
                if (temp.length() > 0) {
                    content.append("\u3000\u3000" + temp);
                    if (i < contentEs.size() - 1) {
                        content.append("\r\n");
                    }
                }
            }
            chapterInfoBean.setBody(content.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            chapterInfoBean.setBody("章节解析失败，请翻页尝试，或到我的界面。联系管理员");
        }
        return chapterInfoBean;
    }

}
