package com.nautilus.ywlfair.widget;

import java.util.ArrayList;

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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.request.GetActivityPicturesRequest;
import com.nautilus.ywlfair.entity.response.GetActivityPicturesResponse;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGridFragment extends Fragment {

	private LayoutInflater mLayoutInflater = null;

	private LoadMoreGridView mGridView = null;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

	private ImageAdapter mAppsAdapter = null;

	private ArrayList<PicInfo> picInfoArrayList = null;

	private String itemId;

	private DisplayImageOptions options = ImageLoadUtils
			.createNoRoundedOptions();

	private static ImageGridFragment mInstance = null;

	public static ImageGridFragment getInstance(Bundle bundle) {
		mInstance = new ImageGridFragment();
		mInstance.setArguments(bundle);

		return mInstance;
	}

	@SuppressWarnings("unchecked")
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
		View view = inflater.inflate(R.layout.image_grid_fragment, null);

		mLayoutInflater = inflater;

		mGridView = (LoadMoreGridView) view.findViewById(R.id.grid);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = view.findViewById(R.id.empty);

        picInfoArrayList = new ArrayList<>();

        mAppsAdapter = new ImageAdapter();

        mGridView.setAdapter(mAppsAdapter);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });

        mGridView.setOnLastItemVisibleListener(new LoadMoreGridView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && picInfoArrayList.size() > 0 && !mIsRequesting) {
                    mRequestingNumber = picInfoArrayList.size();

                    getData();
                }
            }
        });

		mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(),
                        ShowImagesPagerActivity.class);

                intent.putExtra(Constant.KEY.PICINFO_LIST, picInfoArrayList);

                intent.putExtra(Constant.KEY.POSITION, position);

                getActivity().startActivity(intent);

            }
        });

        getData();

		return view;
	}

	public class ImageAdapter extends BaseAdapter {

		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.image_grid_item,
						null);
			}

			AutoAdjustHeightImageView imageView = (AutoAdjustHeightImageView) convertView
					.findViewById(R.id.image_view);

			String imagePath = picInfoArrayList.get(position).getImgUrl();

			ImageLoader.getInstance().displayImage(imagePath, imageView,
					options);

			return convertView;
		}

		public final int getCount() {
			return picInfoArrayList.size();
		}

		public final Object getItem(int position) {
			return picInfoArrayList.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

    private void getData(){
        GetActivityPicturesRequest request = new GetActivityPicturesRequest(itemId, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetActivityPicturesResponse>() {
                    @Override
                    public void onStart() {
                        mIsRequesting = true;
                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetActivityPicturesResponse response) {
                        if (response == null || response.getResult().getPictureInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            picInfoArrayList.clear();

                            picInfoArrayList.addAll(response.getResult().getPictureInfoList());

                            mAppsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetActivityPicturesResponse response) {
                        if(response == null || response.getResult().getPictureInfoList() == null){
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }
                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            picInfoArrayList.clear();
                        }

                        if (response.getResult().getPictureInfoList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;
                        }

                        picInfoArrayList.addAll(response.getResult().getPictureInfoList());

                        mAppsAdapter.notifyDataSetChanged();
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
                        mGridView.setEmptyView(mEmptyView);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mIsRequesting = false;
                    }
                });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);

    }
}
