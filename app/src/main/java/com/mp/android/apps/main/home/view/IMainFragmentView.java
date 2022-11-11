package com.mp.android.apps.main.home.view;


import com.mp.android.apps.main.home.bean.HomeDesignBean;
import com.mp.android.apps.main.home.bean.SourceListContent;
import com.mp.android.apps.basemvplib.IView;


import java.util.List;

public interface IMainFragmentView extends IView {
    void notifyRecyclerView(List<HomeDesignBean> list, List<String> carouselImages, List<SourceListContent> listContents, boolean useCache);

    void notifyContentItemUpdate(int position, List<SourceListContent> sourceListContents);
}
