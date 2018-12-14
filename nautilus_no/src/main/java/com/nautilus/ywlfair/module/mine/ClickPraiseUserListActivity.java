package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.GetPraiseUserListRequest;
import com.nautilus.ywlfair.entity.response.GetPraiseUserListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.adapter.ClickPraiseUserListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

public class ClickPraiseUserListActivity extends BaseActivity implements OnClickListener {

	private Context mContext;

	private LoadMoreListView mListView;

	private List<GetPraiseUserListResponse.PraiseUser> articleList;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ClickPraiseUserListAdapter mAdapter;

	private View mEmptyView;

	private static final int PAGE_START_NUMBER = 0;

	private static final int PER_PAGE_NUMBER = 20;

	private boolean mIsNoMoreResult = false;

	private boolean mIsRequesting = false;

	private int mRequestingNumber = PAGE_START_NUMBER;

    private String itemId;

    private String itemType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.click_praise_user_list_activity);

		mContext = this;

        itemId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        itemType = getIntent().getStringExtra(Constant.KEY.ITEM_TYPE);

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);

		articleList = new ArrayList<>();

		mListView = (LoadMoreListView) findViewById(R.id.lv_diy_list);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

		mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

		mEmptyView = findViewById(R.id.empty);

		mAdapter = new ClickPraiseUserListAdapter(mContext, articleList);

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

		getData();
	}

	private void getData() {

        if(mIsRequesting){
            return;
        }

        GetPraiseUserListRequest request = new GetPraiseUserListRequest(itemId, itemType, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetPraiseUserListResponse>() {
                    @Override
                    public void onStart() {
                        mIsRequesting = true;

                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetPraiseUserListResponse response) {
                        if (response == null || response.getResult().getUserList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            articleList.clear();

                            articleList.addAll(response.getResult().getUserList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetPraiseUserListResponse response) {
                        if(response == null || response.getResult().getUserList() == null){
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }
                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            articleList.clear();

                            mListView.setFooter(false);
                        }

                        if (response.getResult().getUserList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;

                            if(mRequestingNumber > 0)
                                mListView.setFooter(true);
                        }

                        articleList.addAll(response.getResult().getUserList());

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
