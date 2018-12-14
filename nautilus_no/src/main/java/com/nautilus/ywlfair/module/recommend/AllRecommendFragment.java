package com.nautilus.ywlfair.module.recommend;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.entity.bean.BannerArticleInfo;
import com.nautilus.ywlfair.entity.bean.event.EventArticleType;
import com.nautilus.ywlfair.entity.request.GetArticleAndRecommendRequest;
import com.nautilus.ywlfair.entity.response.GetArticleAndRecommendResponse;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.module.recommend.Adapter.RecommendListAdapter;
import com.nautilus.ywlfair.widget.CirclePageIndicator;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/11/14.
 */
public class AllRecommendFragment extends Fragment {

    private static AllRecommendFragment mInstance;

    private ViewPager mViewPager;

    private TimeCount time;

    private static final int PAGE_START_NUMBER = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PER_PAGE_NUMBER = 10;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private RecommendListAdapter mAdapter;

    private List<ArticleInfo> commentInfoList;

    private List<View> mViewList = null;

    private Context mContext;

    private int index = 0;

    private int bannerCount;

    private View headerView;

    private int checkType = 0;//0 header 1 list item

    private int checkPosition;

    private List<BannerArticleInfo> bannerInfoList;

    public static AllRecommendFragment getInstance(Bundle bundle) {

        if (mInstance == null) {
            mInstance = new AllRecommendFragment();

            mInstance.setArguments(bundle);
        }

        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mContext = getActivity();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_activity, null);

        LoadMoreListView mListView = (LoadMoreListView) view.findViewById(R.id.list_recommend);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        commentInfoList = new ArrayList<>();

        mAdapter = new RecommendListAdapter(getActivity(), commentInfoList);

        headerView = inflater.inflate(R.layout.recommend_channel_header, null);

        mListView.addHeaderView(headerView);

        mListView.setAdapter(mAdapter);

        mRequestingNumber = PAGE_START_NUMBER;

        getData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checkType = 1;

                checkPosition = position - 1;

                ArticleInfo articleInfo = commentInfoList.get(checkPosition);

                int commentType = articleInfo.getArticleType() == 1 ? 3 : 2;

                WebViewActivity.startWebViewActivity(mContext,commentType+"", articleInfo.getArticleUrl(),
                        articleInfo.getArticleId()+"", articleInfo.getHasLike(), 0);

            }
        });
        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });

        mListView
                .setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mIsNoMoreResult) {

                        } else if (commentInfoList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = commentInfoList.size();
                                getData();
                            }
                        }
                    }
                });

        return view;
    }

    private void showHeaderView() {

        bannerCount = bannerInfoList.size();

        mViewPager = (ViewPager) headerView.findViewById(R.id.main_view_pager);

        mViewList = new ArrayList<>();

        for (int i = 0; i < bannerInfoList.size(); i++) {

            final BannerArticleInfo info = bannerInfoList.get(i);

            View viewPagerItem = View.inflate(mContext, R.layout.recommend_header_item, null);

            ImageView imageView = (ImageView) viewPagerItem
                    .findViewById(R.id.recommend_header_image);

            DisplayImageOptions options = ImageLoadUtils
                    .createNoRoundedOptions();
            ImageLoader.getInstance().displayImage(info.getRecommendImageUrl(),
                    imageView, options);

            TextView title = (TextView) viewPagerItem.findViewById(R.id.recommend_header_title);
            title.setText(info.getRecommendInfo().getTitle());

            TextView author = (TextView) viewPagerItem.findViewById(R.id.recommend_header_author);
            author.setText(info.getRecommendInfo().getEditorName());

            TextView content = (TextView) viewPagerItem.findViewById(R.id.recommend_header_content);
            content.setText(info.getRecommendInfo().getDesc());

            TextView tagNameView = (TextView) viewPagerItem.findViewById(R.id.recommend_header_tag);
            tagNameView.setText(info.getRecommendInfo().getTagName());

            View cover = viewPagerItem.findViewById(R.id.recommend_header_cover);

            cover.setTag(i);

            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkType = 0;

                    checkPosition = (int) v.getTag();

                    int commentType = info.getRecommendInfo().getArticleType() == 1 ? 3 : 2;

                    WebViewActivity.startWebViewActivity(mContext, commentType+"", info.getRecommendInfo().getArticleUrl(),
                            info.getRecommendInfo().getArticleId()+"",
                            bannerInfoList.get(checkPosition).getRecommendInfo().getHasLike(), 0);
                }
            });

            mViewList.add(viewPagerItem);
        }

        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(mViewList.get(position));

                return mViewList.get(position);
            }
        });

        mViewPager.addOnPageChangeListener(new MyPagerChangeListener());

        CirclePageIndicator indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);

        indicator.setViewPager(mViewPager);

        if (time == null) {
            time = new TimeCount(Integer.MAX_VALUE, 3500);

            time.start();
        }
    }

    private boolean isPause = false;

    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1://手势滑动中
                    isPause = true;
                    mSwipeRefreshLayout.setEnabled(false);
                    break;
                case 0://up
                    mSwipeRefreshLayout.setEnabled(true);
                    break;
            }
        }
    }

    class TimeCount extends CountDownTimer {// 计时器

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

            if (!isPause) {
                index = mViewPager.getCurrentItem();

                index++;

                if (index == bannerCount) {
                    index = 0;
                }
                mViewPager.setCurrentItem(index);
            } else {
                isPause = !isPause;
            }

        }

    }

    private void getData() {
        GetArticleAndRecommendRequest request = new GetArticleAndRecommendRequest(mRequestingNumber, PER_PAGE_NUMBER, 0,
                new ResponseListener<GetArticleAndRecommendResponse>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);

                        mIsRequesting = true;
                    }

                    @Override
                    public void onCacheResponse(GetArticleAndRecommendResponse response) {
                        if (response == null || response.getResult().getBannerInfoList() == null ||
                                response.getResult().getArticleInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            bannerInfoList = response.getResult().getBannerInfoList();

                            showHeaderView();

                            commentInfoList.clear();

                            commentInfoList.addAll(response.getResult().getArticleInfoList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetArticleAndRecommendResponse response) {
                        if (response != null && response.getResult().getBannerInfoList() != null) {

                            if (mRequestingNumber == PAGE_START_NUMBER) {

                                bannerInfoList = response.getResult().getBannerInfoList();

                                showHeaderView();

                                commentInfoList.clear();
                            }

                            if (response.getResult().getArticleInfoList().size() < PER_PAGE_NUMBER) {
                                mIsNoMoreResult = true;
                            }

                            commentInfoList.addAll(response.getResult().getArticleInfoList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        mSwipeRefreshLayout.setRefreshing(false);

                        mIsRequesting = false;
                    }
                });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Subscribe
    public void onEventMainThread(EventArticleType eventArticleType){

        if(eventArticleType.getType() != 2){
            return;
        }

        if(checkType == 0){

            bannerInfoList.get(checkPosition).getRecommendInfo().setHasLike(eventArticleType.getHasLike());

        }else if(checkType == 1){

            commentInfoList.get(checkPosition).setHasLike(eventArticleType.getHasLike());
        }
    }
}
