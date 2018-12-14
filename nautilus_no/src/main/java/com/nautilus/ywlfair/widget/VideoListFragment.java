package com.nautilus.ywlfair.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.module.VideoPlayerActivity;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.VideoInfo;
import com.nautilus.ywlfair.entity.request.GetActivityVideosRequest;
import com.nautilus.ywlfair.entity.response.GetActivityVideosResponse;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class VideoListFragment extends Fragment {

	private LayoutInflater mLayoutInflater = null;

	private LoadMoreListView mListView = null;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

	private ImageAdapter mAppsAdapter = null;

	private static VideoListFragment mInstance = null;

	private List<VideoInfo> videoList = null;

	private DisplayImageOptions options = ImageLoadUtils
			.createDisplayOptions(0);

	private String itemId;

	public static VideoListFragment getInstance(Bundle bundle) {
		mInstance = new VideoListFragment();
		mInstance.setArguments(bundle);

		return mInstance;
	}

	@Override
	public void onCreate(Bundle icicle) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			itemId = arguments.getString(Constant.KEY.ITEM_ID);

		}
		super.onCreate(icicle);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_list_fragment, null);
		mLayoutInflater = inflater;

		mListView = (LoadMoreListView) view.findViewById(R.id.lv_video_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = view.findViewById(R.id.empty);

        videoList = new ArrayList<>();

        mAppsAdapter = new ImageAdapter();

        mListView.setAdapter(mAppsAdapter);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });

        mListView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && videoList.size() > 0 && !mIsRequesting) {
                    mRequestingNumber = videoList.size();

                    getData();
                }
            }
        });

		mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(),
                        VideoPlayerActivity.class);

                intent.putExtra(Constant.KEY.VIDEO_PATH, videoList
                        .get(position).getVideoUrl());

                startActivity(intent);
            }
        });

        getData();

		return view;
	}

    private void getData(){

    }
	public class ImageAdapter extends BaseAdapter {
		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.video_list_item,
						null);
			}

			VideoInfo videoInfo = videoList.get(position);

			final AutoAdjustHeightImageView imageView = (AutoAdjustHeightImageView) convertView
					.findViewById(R.id.iv_video_cover);

			ImageLoader.getInstance().displayImage(videoInfo.getImgUrl(),
					imageView, options);

			TextView dateView = (TextView) convertView
					.findViewById(R.id.tv_date);
			TextView description = (TextView) convertView
					.findViewById(R.id.tv_description);

			description.setText(videoInfo.getVideoDesc());

			dateView.setText(TimeUtil.getDateFormat(Long.valueOf(videoInfo.getUploadDate())));

			return convertView;
		}

		public final int getCount() {
			return videoList.size();
		}

		public final Object getItem(int position) {
			return videoList.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

}
