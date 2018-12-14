package com.nautilus.ywlfair.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.CarouselInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerItem;
import com.nautilus.ywlfair.entity.bean.event.EventHomePager;
import com.nautilus.ywlfair.entity.request.GetArticleFunListRequest;
import com.nautilus.ywlfair.entity.request.GetHomePagerRequest;
import com.nautilus.ywlfair.entity.response.GetArticleFunListResponse;
import com.nautilus.ywlfair.entity.response.GetHomePagerResponse;
import com.nautilus.ywlfair.module.main.adapter.HomePagerListAdapter;
import com.nautilus.ywlfair.module.main.adapter.RecommendAdapter;
import com.nautilus.ywlfair.module.mine.AllActivityListActivity;
import com.nautilus.ywlfair.module.mine.level.GetMoneyCodeActivity;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.module.webview.ArticleWebViewActivity;
import com.nautilus.ywlfair.module.webview.CalendarWebViewActivity;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.MyBannerView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import zxing.activity.CaptureActivity;


public class HomePagerFragment extends Fragment implements View.OnClickListener {

    private final int ACTIVE = 1;

    private final int RECOMMEND = 2;

    private final int ORIGINAL = 3;

    private final int GOODS = 4;

    private final int AD = 5;

    private static final int PER_PAGER_NUM = 10;

    private boolean isNoMoreResult = false;

    private Context mContext;

    private List<View> mViewList = null;

    private MyBannerView myBannerView;

    private ImageView loginButton;

    private int bannerCount;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int CHECK_CAROUSEL = 0;

    private static final int CHECK_RECOMMEND = 1;

    private static final int CHECK_ORIGINAL = 2;

    private int checkType = CHECK_CAROUSEL;

    private int checkPosition;

    private View headerView;

    private LoadMoreListView listView;

    private HomePagerListAdapter adapter;

    private List<HomePagerArticleInfo> mList;

    private List<CarouselInfo> carouselInfoList;

    private ImageView scanCode;

    /**
     * The cache of fragment view.
     */
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = getActivity();

        EventBus.getDefault().register(this);

        initRootView();

        ViewGroup parent = (ViewGroup) rootView.getParent();

        if (parent != null) {
            parent.removeView(rootView);
        }

//        getData();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getUserType() == 1) {
            scanCode.setImageResource(R.drawable.ic_homepager_qr);
        } else {
            scanCode.setImageResource(R.drawable.saoyisao);
        }
    }

    /**
     * 初始化界面控件
     */
    public void initRootView() {

        if (rootView == null) {

            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.main_view_layout, null);

            listView = (LoadMoreListView) rootView.findViewById(R.id.home_pager_list);

            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.homepager_header, null);

            listView.addHeaderView(headerView);

            mList = new ArrayList<>();

            adapter = new HomePagerListAdapter(mContext, mList);

            listView.setAdapter(adapter);

            myBannerView = (MyBannerView) headerView.findViewById(R.id.my_banner);

            loginButton = (ImageView)
                    rootView.findViewById(R.id.imagebutton_login);

            scanCode = (ImageView) rootView.findViewById(R.id.scanning_code);

            scanCode.setOnClickListener(this);


            if (TextUtils.isEmpty(MyApplication.calendarUrl)) {
                loginButton.setVisibility(View.INVISIBLE);
            }

            loginButton.setOnClickListener(this);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

            mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

            mSwipeRefreshLayout
                    .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            isNoMoreResult = false;

                            getData();
                        }
                    });

            View allActive = rootView.findViewById(R.id.tv_all_activity);
            allActive.setOnClickListener(this);

            listView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
                @Override
                public void onLastItemVisible() {
                    if (!isNoMoreResult && mList.size() >= PER_PAGER_NUM) {
                        getMoreArtile();
                    }
                }
            });
        }

    }

    private void setValue(final HomePagerItem mHomePagerItem) {

        bannerCount = mHomePagerItem.getCarouselInfoList().size();

        mList.clear();

        mList.addAll(mHomePagerItem.getArticleInfoList());

        adapter.notifyDataSetChanged();

        mViewList = new ArrayList<>();

        setActivities(mHomePagerItem);

        carouselInfoList = mHomePagerItem.getCarouselInfoList();

        if (carouselInfoList == null || carouselInfoList.isEmpty()) {
            myBannerView.setVisibility(View.GONE);
        } else {
            myBannerView.setVisibility(View.VISIBLE);

            for (int i = 0; i < carouselInfoList.size(); i++) {

                final CarouselInfo info = carouselInfoList.get(i);

                View viewPagerItem = View.inflate(mContext, R.layout.viewpaper_item, null);

                ImageView imageView = (ImageView) viewPagerItem
                        .findViewById(R.id.title_image);

                DisplayImageOptions options = ImageLoadUtils
                        .createNoRoundedOptions();

                ImageLoader.getInstance().displayImage(info.getImgUrl(),
                        imageView, options);

                imageView.setTag(i);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        checkType = CHECK_CAROUSEL;

                        checkPosition = (int) v.getTag();

                        checkCarousel(carouselInfoList.get(checkPosition));

                    }
                });

                mViewList.add(viewPagerItem);

            }

            myBannerView.setValue(mViewList, mSwipeRefreshLayout);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(mContext, ArticleWebViewActivity.class);

                    intent.putExtra(Constant.KEY.ARTICLE, mList.get(position - 1));

                    startActivity(intent);
                }
            }
        });

    }

    private void checkCarousel(CarouselInfo info) {

        switch (info.getType()) {
            case ACTIVE:
                Intent intent = new Intent(mContext,
                        ActiveWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY.ITEM_ID,
                        info.getItemId() + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;

            case RECOMMEND:
            case ORIGINAL:

                WebViewActivity.startWebViewActivity(mContext, info.getType() + "", info.getUrl(),
                        info.getItemId() + "", info.getHasLike(), WebViewActivity.HOMEPAGER_ARTICLE);
                break;

            case AD:
                WebViewActivity.startWebViewActivity(mContext, "-1", info.getUrl(), info.getItemId() + "", 0, 0);
                break;

            case GOODS:
                break;

        }

    }


    private void setActivities(final HomePagerItem homePagerItem) {

        GridView gridView = (GridView) headerView.findViewById(R.id.gd_recommend);

        gridView.setAdapter(new RecommendAdapter(mContext, homePagerItem.getActivityList()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HomePagerActivityInfo homePagerActivityInfo = homePagerItem.getActivityList().get(position);

                showActiveDetail(homePagerActivityInfo);
            }
        });

    }

    private void showActiveDetail(HomePagerActivityInfo homePagerActivityInfo) {
        Intent intent = new Intent(mContext, ActiveWebViewActivity.class);

        intent.putExtra(Constant.KEY.ITEM_ID, homePagerActivityInfo.getActId());

        startActivity(intent);
    }

    private void getData() {
        GetHomePagerRequest request = new GetHomePagerRequest(new ResponseListener<GetHomePagerResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetHomePagerResponse response) {
                if (response != null) {
                    setValue(response.getHomePagerItem());
                }
            }

            @Override
            public void onResponse(GetHomePagerResponse response) {
                if (response == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                setValue(response.getHomePagerItem());
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
                ProgressDialog.getInstance().cancel();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        request.setShouldCache(true);

//        VolleyUtil.addToRequestQueue(request);
    }

    private void getMoreArtile() {
        GetArticleFunListRequest request = new GetArticleFunListRequest(mList.size(), PER_PAGER_NUM,
                new ResponseListener<GetArticleFunListResponse>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetArticleFunListResponse response) {

                    }

                    @Override
                    public void onResponse(GetArticleFunListResponse response) {
                        if (response != null) {
                            if (response.getResult().getHomePagerArticleInfoList().size() < PER_PAGER_NUM) {
                                isNoMoreResult = true;
                            }

                            mList.addAll(response.getResult().getHomePagerArticleInfoList());

                            adapter.notifyDataSetChanged();
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
                    }
                });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.scanning_code:

                if(!LoginWarmUtil.getInstance().checkLoginStatus(mContext)){
                    return;
                }
                if (MyApplication.getInstance().getUserType() == 1) {
                    startActivity(new Intent(mContext, GetMoneyCodeActivity.class));
                } else {
                    startActivity(new Intent(mContext, CaptureActivity.class));
                }

                break;
            case R.id.imagebutton_login:
                Intent messageIntent = new Intent(mContext, CalendarWebViewActivity.class);

                startActivity(messageIntent);

                break;

            case R.id.tv_all_activity:

                Intent allActiveIntent = new Intent(getActivity(), AllActivityListActivity.class);
                startActivity(allActiveIntent);

                break;

        }
    }


    @Subscribe
    public void onEventMainThread(EventHomePager eventHomePager) {

        switch (checkType) {
            case CHECK_CAROUSEL:
                carouselInfoList.get(checkPosition).setHasLike(eventHomePager.getHasLike());
                break;

            case CHECK_RECOMMEND:
//                recommendList.get(checkPosition).getRecommendInfo().setHasLike(eventHomePager.getHasLike());
                break;

            case CHECK_ORIGINAL:
//                articleInfoList.get(checkPosition).getRecommendInfo().setHasLike(eventHomePager.getHasLike());
                break;
        }
    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
}
