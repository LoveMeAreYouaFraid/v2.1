package com.nautilus.ywlfair.module.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ShowGoodsTypeMenu;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.GoodsRecommendInfo;
import com.nautilus.ywlfair.entity.bean.event.EventGoodsLike;
import com.nautilus.ywlfair.entity.request.GetGoodsClassRequest;
import com.nautilus.ywlfair.entity.request.GetGoodsHomePagerRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetGoodsClassResponse;
import com.nautilus.ywlfair.entity.response.GetGoodsHomePagerResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.goods.adapter.RecyclerViewAdapter;
import com.nautilus.ywlfair.module.webview.GoodsWebViewActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.HeaderGridView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * Created by Administrator on 2015/12/21.
 */
public class GoodsFragment extends Fragment implements RecyclerViewAdapter.MOnClickListener {

    private Context mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HeaderGridView mHeaderGridView;

    private View rootView;

    private RecyclerViewAdapter adapter;

    private View header;

    private List<GoodsClassInfo> goodsClassList;

    private List<GoodsInfo> goodsInfoList;

    private List<GoodsRecommendInfo> goodsRecommendInfoList;

    private int checkType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        EventBus.getDefault().register(this);

        initRootView();

        getData();

        getGoodsClass();

        ViewGroup parent = (ViewGroup) rootView.getParent();

        if (parent != null) {
            parent.removeView(rootView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return rootView;
    }

    /**
     * 初始化界面控件
     */
    public void initRootView() {

        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.goods_fragment, null);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);

            mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData();
                }
            });

            mHeaderGridView = (HeaderGridView) rootView.findViewById(R.id.goods_header_grid);

            header = View.inflate(mContext, R.layout.goods_list_header, null);

            View menu = rootView.findViewById(R.id.iv_menu);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (goodsClassList == null) {
                        ToastUtil.showShortToast("暂无分类信息!");
                        return;
                    }
                    ShowGoodsTypeMenu.getInstance().initMenuDialog(mContext, goodsClassList, rootView.getHeight(), checkType + 1);
                }
            });
        }

    }

    private void setValue(GetGoodsHomePagerResponse.Result result) {

        goodsRecommendInfoList = result.getGoodsRecommendInfoList();

        setBannerValue();

        loadGridData(result);
    }

    /**
     * grid显示
     */
    private void loadGridData(GetGoodsHomePagerResponse.Result result) {

        goodsInfoList = result.getGoodsInfoList();

        adapter = new RecyclerViewAdapter(mContext, goodsInfoList);

        if (mHeaderGridView.getHeaderViewCount() == 0) {
            mHeaderGridView.addHeaderView(header);
        }
        adapter.setMOnClickListener(this);
        mHeaderGridView.setAdapter(adapter);

        mHeaderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < 2) {
                    return;
                }

                checkType = 1;

                checkPosition = position - 2;

                GoodsWebViewActivity.startWebViewActivity(mContext, goodsInfoList.get(checkPosition), 0, checkType);
            }
        });

    }

    private void setBannerValue() {

        AutoAdjustHeightImageView bigImage = (AutoAdjustHeightImageView) header.findViewById(R.id.iv_goods_big);

        AutoAdjustHeightImageView middleImage = (AutoAdjustHeightImageView) header.findViewById(R.id.iv_goods_middle);

        AutoAdjustHeightImageView firstSmallImage = (AutoAdjustHeightImageView) header.findViewById(R.id.iv_goods_small_one);

        AutoAdjustHeightImageView secondSmallImage = (AutoAdjustHeightImageView) header.findViewById(R.id.iv_goods_small_two);

        int smallIndex = 0;

        for (int i = 0; i < goodsRecommendInfoList.size(); i++) {

            final GoodsRecommendInfo goodsRecommendInfo = goodsRecommendInfoList.get(i);

            switch (goodsRecommendInfo.getType()) {
                case 1:
                    if (!TextUtils.isEmpty(goodsRecommendInfo.getImgUrl())) {
                        bigImage.setVisibility(View.VISIBLE);
                    }

                    ImageLoader.getInstance().displayImage(goodsRecommendInfo.getImgUrl(),
                            bigImage, ImageLoadUtils.createNoRoundedOptions());

                    checkType = 0;

                    bigImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkPosition = (int) v.getTag();

                            GoodsWebViewActivity.startWebViewActivity(mContext, getGoodsInfo(goodsRecommendInfoList.get(checkPosition)), 0, 0);
                        }
                    });

                    bigImage.setTag(i);

                    break;
                case 2:
                    if (!TextUtils.isEmpty(goodsRecommendInfo.getImgUrl())) {
                        middleImage.setVisibility(View.VISIBLE);
                    }

                    ImageLoader.getInstance().displayImage(goodsRecommendInfo.getImgUrl(),
                            middleImage, ImageLoadUtils.createNoRoundedOptions());

                    checkType = 0;

                    middleImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkPosition = (int) v.getTag();

                            GoodsWebViewActivity.startWebViewActivity(mContext, getGoodsInfo(goodsRecommendInfoList.get(checkPosition)), 0, 0);
                        }
                    });

                    middleImage.setTag(i);

                    break;
                case 3:
                    if (smallIndex == 0) {

                        if (!TextUtils.isEmpty(goodsRecommendInfo.getImgUrl())) {
                            firstSmallImage.setVisibility(View.VISIBLE);
                        }

                        ImageLoader.getInstance().displayImage(goodsRecommendInfo.getImgUrl(),
                                firstSmallImage, ImageLoadUtils.createNoRoundedOptions());

                        checkType = 0;

                        firstSmallImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                checkPosition = (int) v.getTag();

                                GoodsWebViewActivity.startWebViewActivity(mContext, getGoodsInfo(goodsRecommendInfoList.get(checkPosition)), 0, 0);
                            }
                        });

                        smallIndex = smallIndex + 1;

                        firstSmallImage.setTag(i);

                    } else {

                        if (!TextUtils.isEmpty(goodsRecommendInfo.getImgUrl())) {
                            secondSmallImage.setVisibility(View.VISIBLE);
                        }

                        ImageLoader.getInstance().displayImage(goodsRecommendInfo.getImgUrl(),
                                secondSmallImage, ImageLoadUtils.createNoRoundedOptions());

                        checkType = 0;

                        secondSmallImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                checkPosition = (int) v.getTag();

                                GoodsWebViewActivity.startWebViewActivity(mContext, getGoodsInfo(goodsRecommendInfoList.get(checkPosition)), 0, 0);
                            }
                        });

                        secondSmallImage.setTag(i);
                    }
                    break;
            }
        }

    }

    /**
     * 获取商品首页信息
     */
    private void getData() {
        GetGoodsHomePagerRequest request = new GetGoodsHomePagerRequest(new ResponseListener<GetGoodsHomePagerResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在加载...");
            }

            @Override
            public void onCacheResponse(GetGoodsHomePagerResponse response) {

            }

            @Override
            public void onResponse(GetGoodsHomePagerResponse response) {
                if (response != null && response.getResult() != null) {
                    setValue(response.getResult());
                } else {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
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
                ProgressDialog.getInstance().cancel();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private void getGoodsClass() {
        GetGoodsClassRequest request = new GetGoodsClassRequest(new ResponseListener<GetGoodsClassResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetGoodsClassResponse response) {

            }

            @Override
            public void onResponse(GetGoodsClassResponse response) {
                if (response == null || response.getResult() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");

                    return;
                }

                goodsClassList = response.getResult().getGoodsClassInfoList();

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

            }
        });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private GoodsInfo getGoodsInfo(GoodsRecommendInfo goodsRecommendInfo) {
        GoodsInfo goodsInfo = new GoodsInfo();

        goodsInfo.setHasLike(goodsRecommendInfo.getHasLike());

        goodsInfo.setGoodsId(goodsRecommendInfo.getGoodsId());

        goodsInfo.setGoodsUrl(goodsRecommendInfo.getGoodsUrl());

        goodsInfo.setImageUrl(goodsRecommendInfo.getImgUrl());

        goodsInfo.setGoodsName(goodsRecommendInfo.getGoodsName());

        goodsInfo.setSkuInfoList(goodsRecommendInfo.getSkuInfoList());

        return goodsInfo;

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
        switch (checkType) {
            case 0:

                goodsRecommendInfoList.get(checkPosition).setHasLike(eventGoodsLike.getHasLike());

                break;

            case 1:

                goodsInfoList.get(checkPosition).setHasLike(eventGoodsLike.getHasLike());

                adapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
