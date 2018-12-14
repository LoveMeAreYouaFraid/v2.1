package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.adapter.RecommendListAdapter;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.entity.bean.RecommendInfo;
import com.nautilus.ywlfair.entity.request.GetRecommendRequest;
import com.nautilus.ywlfair.entity.response.GetRecommendResponse;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;

import java.util.ArrayList;
import java.util.List;

public class RecommendListActivity extends BaseActivity {

	private LoadMoreListView mListView;

	private Context mContext;

	private List<RecommendInfo> recommendList;

	private RecommendListAdapter mAdapter;

    private View mEmptyView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 10;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recommend_liset);

		mContext = this;

		mListView = (LoadMoreListView) findViewById(R.id.lv_recommend_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = findViewById(R.id.empty);

        recommendList = new ArrayList<>();

        mAdapter = new RecommendListAdapter(mContext, recommendList);

        mListView.setAdapter(mAdapter);

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                RecommendInfo recommendInfo = recommendList.get(position);

                WebViewActivity.startWebViewActivity(mContext,"2", recommendInfo.getArticleUrl(),
                        recommendInfo.getArticleId()+"", 0, 0);

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

                        } else if (recommendList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = recommendList.size();
                                getData();
                            }
                        }
                    }
                });

        getData();

	}

    private void getData(){
        GetRecommendRequest request = new GetRecommendRequest(mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetRecommendResponse>() {
            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);
                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(GetRecommendResponse response) {
                if (response == null || response.getResult().getRecommendInfoList() == null) {
                    return;
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    recommendList.clear();

                    recommendList.addAll(response.getResult().getRecommendInfoList());

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponse(GetRecommendResponse response) {
                if(response == null || response.getResult().getRecommendInfoList() == null){
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    recommendList.clear();
                }

                if (response.getResult().getRecommendInfoList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;
                }

                recommendList.addAll(response.getResult().getRecommendInfoList());

                mAdapter.notifyDataSetChanged();
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
                mListView.setEmptyView(mEmptyView);

                mSwipeRefreshLayout.setRefreshing(false);

                mIsRequesting = false;
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

}
