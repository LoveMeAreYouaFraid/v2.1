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
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.MyDiyListAdapter;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.entity.request.GetUserArticlesRequest;
import com.nautilus.ywlfair.entity.response.GetUserArticlesResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

public class MyDiyActivity extends BaseActivity implements OnClickListener {

	private Context mContext;

	private LoadMoreListView mListView;

	private List<ArticleInfo> articleList;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private MyDiyListAdapter mAdapter;

	private View mEmptyView;

	private static final int PAGE_START_NUMBER = 0;

	private static final int PER_PAGE_NUMBER = 20;

	private boolean mIsNoMoreResult = false;

	private boolean mIsRequesting = false;

	private int mRequestingNumber = PAGE_START_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_diy_liset);

		mContext = this;

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);

		articleList = new ArrayList<>();

		mListView = (LoadMoreListView) findViewById(R.id.lv_diy_list);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

		mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

		mEmptyView = findViewById(R.id.empty);

		mAdapter = new MyDiyListAdapter(mContext, articleList);

		mListView.setAdapter(mAdapter);

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

						} else if (articleList.size() > 0) {

							if (!mIsRequesting) {
								mRequestingNumber = articleList.size();
								getData();
							}
						}
					}
				});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArticleInfo articleInfo = articleList.get(position);

                WebViewActivity.startWebViewActivity(mContext,"3", articleInfo.getArticleUrl(),
                        articleInfo.getArticleId()+"", articleInfo.getHasLike(), 0);

			}
		});
		
		getData();
	}

	private void getData() {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo()
                        .getUserId());

        GetUserArticlesRequest request = new GetUserArticlesRequest(userId, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetUserArticlesResponse>() {
                    @Override
                    public void onStart() {
                        mIsRequesting = true;

                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetUserArticlesResponse response) {
                        if (response == null || response.getResult().getArticleInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            articleList.clear();

                            articleList.addAll(response.getResult().getArticleInfoList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetUserArticlesResponse response) {
                        if(response == null || response.getResult().getArticleInfoList() == null){
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }
                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            articleList.clear();
                        }

                        if (response.getResult().getArticleInfoList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;
                        }

                        articleList.addAll(response.getResult().getArticleInfoList());

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
                        mIsRequesting = false;

                        mSwipeRefreshLayout.setRefreshing(false);

                        mListView.setEmptyView(mEmptyView);
                    }
                });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_bar_back:
			finish();
			break;

		default:
			break;
		}

	}
}
