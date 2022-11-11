package com.mp.android.apps.main.bookR.view.impl;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.mp.android.apps.R;
import com.mp.android.apps.main.bookR.adapter.MybookViewPagerAdapter;
import com.mp.android.apps.main.bookR.presenter.IBookRFragmentPresenter;
import com.mp.android.apps.main.bookR.presenter.impl.BookRFragmentPresenterImpl;
import com.mp.android.apps.main.bookR.view.IBookRFragmentView;
import com.mp.android.apps.basemvplib.impl.BaseFragment;
import com.mp.android.apps.book.view.impl.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class BookStoreFragment extends BaseFragment<IBookRFragmentPresenter> implements IBookRFragmentView, View.OnClickListener {
    public TextView layoutRecommend;
    public TextView layoutMan;
    public TextView layoutWomen;
    private ViewPager viewPager;
    private List<BaseFragment> sourceList;
    private ImageView searchImage;

    /**
     * 推荐fragment
     */
    private BookRecommendFragment recommendFragment;

    /**
     * manFragment 男士专区
     */
    private BookManFragment manFragment;

    /**
     * womanFragment 女士专区
     */
    private BookWomanFragmentImpl womanFragment;

    private static final int RECOMMEND_FRAGMENT = 0;
    private static final int MAN_FRAGMENT = 1;
    private static final int WOMAN_FRAGMENT = 2;

    @Override
    protected void initData() {
        sourceList = new ArrayList<>();
        recommendFragment = new BookRecommendFragment();
        manFragment = new BookManFragment();
        womanFragment = new BookWomanFragmentImpl();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.mp_book_r_layout,container,false);
    }

    @Override
    protected void bindView() {
        super.bindView();
        layoutRecommend = view.findViewById(R.id.mp_bookr_layout_recommend);
        layoutRecommend.setOnClickListener(this);
        layoutMan = view.findViewById(R.id.mp_bookr_layout_men);
        layoutMan.setOnClickListener(this);
        layoutWomen = view.findViewById(R.id.mp_bookr_layout_women);
        layoutWomen.setOnClickListener(this);
        viewPager = view.findViewById(R.id.mp_bookr_viewpager);

        searchImage = view.findViewById(R.id.bookr_fragment_search);
        searchImage.setOnClickListener(this);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        sourceList.add(recommendFragment);
        sourceList.add(manFragment);
        sourceList.add(womanFragment);
        viewPager.setAdapter(new MybookViewPagerAdapter(getActivity().getSupportFragmentManager(), sourceList));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new MybookViewPageChangeListener());
    }

    @Override
    protected IBookRFragmentPresenter initInjector() {
        return new BookRFragmentPresenterImpl();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mp_bookr_layout_recommend:
                viewPager.setCurrentItem(RECOMMEND_FRAGMENT);
                break;
            case R.id.mp_bookr_layout_men:
                viewPager.setCurrentItem(MAN_FRAGMENT);
                break;
            case R.id.mp_bookr_layout_women:
                viewPager.setCurrentItem(WOMAN_FRAGMENT);
                break;
            case R.id.bookr_fragment_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
            default:
                break;
        }
    }

    private class MybookViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case RECOMMEND_FRAGMENT:
                    setSelectPageTextSize(layoutRecommend);
                    break;
                case MAN_FRAGMENT:
                    setSelectPageTextSize(layoutMan);
                    break;
                case WOMAN_FRAGMENT:
                    setSelectPageTextSize(layoutWomen);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        private void setSelectPageTextSize(TextView text) {
            layoutRecommend.setTextSize(20);
            layoutMan.setTextSize(20);
            layoutWomen.setTextSize(20);
            text.setTextSize(26);
        }
    }
}
