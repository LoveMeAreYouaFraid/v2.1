package com.nautilus.ywlfair.module.goods;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ShowGoodsTypeMenu;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.event.EventGoodsLike;
import com.nautilus.ywlfair.entity.request.GetGoodsListRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetGoodsListResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.goods.adapter.RecyclerViewAdapter;
import com.nautilus.ywlfair.module.webview.GoodsWebViewActivity;
import com.nautilus.ywlfair.widget.HeaderGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/12/28.
 */
public class GoodsListActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAdapter.MOnClickListener {

    private Context mContext;

    private GoodsClassInfo goodsClassInfo;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HeaderGridView mHeaderGridView;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private List<GoodsInfo> goodsInfoList;

    private RecyclerViewAdapter adapter;

    private ArrayList<GoodsClassInfo> childClass;

    private int checkType;

    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.goods_fragment);

        mContext = this;

        EventBus.getDefault().register(this);

        goodsClassInfo = (GoodsClassInfo) getIntent().getSerializableExtra(Constant.KEY.GOODS_CATEGORY);

        childClass = (ArrayList<GoodsClassInfo>) getIntent().getSerializableExtra(Constant.KEY.SECOND_CLASS);

        checkType = getIntent().getIntExtra(Constant.KEY.CHECK_TYPE, 0);

        initViews();

        getData();

        mHeaderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checkPosition = position;

                GoodsWebViewActivity.startWebViewActivity(mContext, goodsInfoList.get(position), 0, checkType);
            }
        });
    }

    private void initViews() {
        View back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        emptyView = findViewById(R.id.empty);

        TextView goodsClass = (TextView) findViewById(R.id.channel_title);
        goodsClass.setVisibility(View.VISIBLE);
        goodsClass.setText(goodsClassInfo.getClassName());

        View menuView = findViewById(R.id.iv_menu);
        if (childClass != null && childClass.size() > 0) {
            menuView.setVisibility(View.VISIBLE);
            menuView.setOnClickListener(this);
        } else {
            menuView.setVisibility(View.INVISIBLE);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mHeaderGridView = (HeaderGridView) findViewById(R.id.goods_header_grid);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });

        mHeaderGridView.setOnLastItemVisibleListener(new HeaderGridView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && !mIsRequesting) {
                    mRequestingNumber = goodsInfoList.size();

                    getData();
                }
            }
        });

        goodsInfoList = new ArrayList<>();

        adapter = new RecyclerViewAdapter(mContext, goodsInfoList);

        adapter.setMOnClickListener(this);

        mHeaderGridView.setAdapter(adapter);

    }

    private void getData() {
        GetGoodsListRequest request = new GetGoodsListRequest(goodsClassInfo.getId() + "", goodsClassInfo.getLevel(), mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetGoodsListResponse>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);

                        mIsRequesting = true;
                    }

                    @Override
                    public void onCacheResponse(GetGoodsListResponse response) {
                        if (response == null || response.getResult() == null || response.getResult().getGoodsInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            goodsInfoList.clear();

                            goodsInfoList.addAll(response.getResult().getGoodsInfoList());

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetGoodsListResponse response) {
                        if (response == null || response.getResult() == null || response.getResult().getGoodsInfoList() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");

                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            goodsInfoList.clear();
                        }

                        if (response.getResult().getGoodsInfoList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;
                        }

                        goodsInfoList.addAll(response.getResult().getGoodsInfoList());

                        adapter.notifyDataSetChanged();

                        mHeaderGridView.setEmptyView(emptyView);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.iv_menu:
                ShowGoodsTypeMenu.getInstance().initMenuDialog(mContext, childClass, getHeight(), 0);
                break;
        }
    }

    private int getHeight() {
        Activity activity = (Activity) mContext;

        Rect outRect = new Rect();

        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        return Common.getInstance().getScreenHeight() - outRect.top;
    }

    boolean isSuccess;


    private void praise(final GoodsInfo nautilusItem, final ImageView likeImage) {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        final int isLike = nautilusItem.getHasLike();

        PostLikeRequest request = new PostLikeRequest(nautilusItem.getGoodsId() + "", 4, isLike + 1, userId,
                new ResponseListener<PostLikeAndJoinResponse>() {
                    @Override
                    public void onStart() {

                        likeImage.setEnabled(false);
                    }

                    @Override
                    public void onCacheResponse(PostLikeAndJoinResponse response) {

                    }

                    @Override
                    public void onResponse(PostLikeAndJoinResponse response) {
                        if (response != null) {

                            isSuccess = true;

                            if (isLike == 1) {
                                likeImage.setImageResource(R.drawable.commodity_islike);

                                nautilusItem.setHasLike(0);

                            } else if (isLike == 0) {
                                likeImage.setImageResource(R.drawable.commodity_like);

                                nautilusItem.setHasLike(1);
                            }

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
                        likeImage.setEnabled(true);

                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public boolean onClick(ImageView v, int position) {
        isSuccess = false;

        praise(goodsInfoList.get(position), v);

        return isSuccess;
    }

    private int checkPosition;

    @Subscribe
    public void onEventMainThread(EventGoodsLike eventGoodsLike) {

        if (eventGoodsLike.getType() != checkType) {
            return;
        }
        goodsInfoList.get(checkPosition).setHasLike(eventGoodsLike.getHasLike());

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
