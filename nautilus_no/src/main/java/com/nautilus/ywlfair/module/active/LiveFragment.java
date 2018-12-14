package com.nautilus.ywlfair.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.VideoInfo;
import com.nautilus.ywlfair.entity.request.GetActivityVideosRequest;
import com.nautilus.ywlfair.entity.response.GetActivityVideosResponse;
import com.nautilus.ywlfair.module.active.adapter.ActiveVideoListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LiveFragment extends Fragment {
    private static LiveFragment mInstance = null;

    private LoadMoreListView mListView = null;

    private Context mContext;

    private DisplayImageOptions options = ImageLoadUtils
            .createNoRoundedOptions();

    private String itemId;

    private LinearLayout contentContainer;

    private ActiveVideoListAdapter mAdapter;

    private List<VideoInfo> videoList;

    private TextView picItem, videoItem;

    public static LiveFragment getInstance(Bundle bundle) {

        mInstance = new LiveFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle icicle) {
        Bundle arguments = getArguments();
        if (arguments != null) {

            itemId = arguments.getString(Constant.KEY.ITEM_ID);
        }

        mContext = getActivity();

        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_fragment, null);

        initViews(view);

        getData();

        return view;
    }

    private void initViews(View view) {

        mListView = (LoadMoreListView) view.findViewById(R.id.lv_live);

        View headerView = View.inflate(mContext, R.layout.active_live_header, null);

        contentContainer = (LinearLayout) headerView.findViewById(R.id.ll_gallery);

        picItem = (TextView) headerView.findViewById(R.id.tv_pic_item);

        videoItem = (TextView) headerView.findViewById(R.id.tv_video_item);

        videoList = new ArrayList<>();

        mAdapter = new ActiveVideoListAdapter(mContext, videoList);

        mListView.addHeaderView(headerView);

        mListView.setAdapter(mAdapter);

    }

    private void showHeaderView(final List<PicInfo> photos) {

        contentContainer.removeAllViews();

        if (photos != null && photos.size() > 0) {
            for (int i = 0; i < photos.size(); i++) {

                ImageView imageView = new ImageView(mContext);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        BaseInfoUtil.dip2px(100), BaseInfoUtil.dip2px(100));

                params.setMargins(0, 0, BaseInfoUtil.dip2px(10), 0);

                imageView.setLayoutParams(params);

                ImageLoader.getInstance().displayImage(
                        photos.get(i).getThumbnailUrl(), imageView, options);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setTag(i);

                imageView.setBackgroundColor(mContext.getResources().getColor(R.color.content_color));

                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,
                                ShowImagesPagerActivity.class);

                        intent.putExtra(Constant.KEY.PICINFO_LIST,
                                (ArrayList<PicInfo>) photos);
                        intent.putExtra(Constant.KEY.POSITION,
                                (Integer) v.getTag());

                        mContext.startActivity(intent);
                    }
                });

                contentContainer.addView(imageView);

            }
        } else {
            picItem.setVisibility(View.GONE);
        }
    }

    private void getData() {
        GetActivityVideosRequest request = new GetActivityVideosRequest(itemId,
                new ResponseListener<GetActivityVideosResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(GetActivityVideosResponse response) {
                        if (response == null || response.getResult().getVideoInfoList() == null) {
                            return;
                        }

                        showHeaderView(response.getResult().getPicInfoList());

                        videoList.clear();

                        videoList.addAll(response.getResult().getVideoInfoList());

                        refreshListView();
                    }

                    @Override
                    public void onResponse(GetActivityVideosResponse response) {
                        if (response == null || response.getResult().getVideoInfoList() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }
                        showHeaderView(response.getResult().getPicInfoList());

                        videoList.clear();

                        videoList.addAll(response.getResult().getVideoInfoList());

                        refreshListView();
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

    private void refreshListView() {

        if (videoList == null || videoList.size() == 0) {
            videoItem.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

}
