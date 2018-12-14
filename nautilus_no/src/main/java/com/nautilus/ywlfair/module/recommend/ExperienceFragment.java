package com.nautilus.ywlfair.module.recommend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.entity.bean.event.EventExperience;
import com.nautilus.ywlfair.entity.request.GetArticleAndRecommendRequest;
import com.nautilus.ywlfair.entity.response.GetArticleAndRecommendResponse;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.module.recommend.Adapter.ExperienceListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/11/14.
 */
public class ExperienceFragment extends Fragment {

    private static ExperienceFragment mInstance;

    private Context mContext;

    private List<ArticleInfo> commentInfoList;

    private ExperienceListAdapter mAdapter;

    private static final int PAGE_START_NUMBER = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PER_PAGE_NUMBER = 10;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private int type;

    private ArticleInfo articleInfo;

    public static ExperienceFragment getInstance(Bundle bundle) {

        mInstance = new ExperienceFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        EventBus.getDefault().register(this);

        type = getArguments().getInt(Constant.KEY.TYPE);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.experience_liset, null);

        LoadMoreListView mListView = (LoadMoreListView) view.findViewById(R.id.list_experience);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        commentInfoList = new ArrayList<>();

        mAdapter = new ExperienceListAdapter(mContext, commentInfoList);

        mListView.setAdapter(mAdapter);

        getData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                articleInfo = commentInfoList.get(position);

                int commentType = articleInfo.getArticleType() == 1 ? 3 : 2;

                WebViewActivity.startWebViewActivity(mContext, commentType+"", articleInfo.getArticleUrl(),
                        articleInfo.getArticleId()+"", articleInfo.getHasLike(), 1);
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

    private void getData() {
        GetArticleAndRecommendRequest request = new GetArticleAndRecommendRequest(mRequestingNumber, PER_PAGE_NUMBER, type,
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

                            commentInfoList.clear();

                            commentInfoList.addAll(response.getResult().getArticleInfoList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetArticleAndRecommendResponse response) {
                        if (response != null && response.getResult().getBannerInfoList() != null) {

                            if (mRequestingNumber == PAGE_START_NUMBER) {
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
    public void onEventMainThread(EventExperience eventExperience){

        if(eventExperience.getType() != 2){
            return;
        }

        articleInfo.setHasLike(eventExperience.getHasLike());
    }

}
