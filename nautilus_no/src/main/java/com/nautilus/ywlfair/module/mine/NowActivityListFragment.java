package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
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
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.request.GetUserActivityListRequest;
import com.nautilus.ywlfair.entity.request.GetVendorActivityListRequest;
import com.nautilus.ywlfair.entity.response.GetUserActivityResponse;
import com.nautilus.ywlfair.entity.response.GetVendorActivityResponse;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.module.mine.adapter.VendorActivityListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class NowActivityListFragment extends Fragment {
    private static NowActivityListFragment mInstance;

    private LoadMoreListView listExperience;

    private Context mContext;

    private VendorActivityListAdapter activityListAdapter;

    private int PAGE_START_NUMBER = 0;

    private int PER_PAGE_NUMBER = 8;

    private boolean isNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private SwipeRefreshLayout swipeContainer;

    private int ordAndNow;

    private List<HomePagerActivityInfo> activityInfo;

    public static NowActivityListFragment getInstance(Bundle bundle) {

        mInstance = new NowActivityListFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        activityInfo = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            ordAndNow = arguments.getInt(Constant.KEY.TYPE);
        }
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.experience_liset, null);
        listExperience = (LoadMoreListView) view.findViewById(R.id.list_experience);

        activityListAdapter = new VendorActivityListAdapter(mContext, ordAndNow, activityInfo);//3 old //now 4

        listExperience.setAdapter(activityListAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeContainer.setColorSchemeResources(R.color.lv);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!mIsRequesting) {

                    isNoMoreResult = false;

                    mRequestingNumber = 0;

                    getData();
                }
            }
        });

        listExperience.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (mIsRequesting || isNoMoreResult) {
                    return;
                }
                mRequestingNumber = activityInfo.size();

                getData();
            }
        });

        listExperience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActiveWebViewActivity.class);

                intent.putExtra(Constant.KEY.ITEM_ID, activityInfo.get(position).getActId());

                mContext.startActivity(intent);
            }
        });
        return view;
    }

    private void getData() {
        if (MyApplication.getInstance().getUserType() == 0) {
            getUserData();
        } else {
            getVendorData();
        }
    }

    private void getUserData() {

        GetUserActivityListRequest request = new GetUserActivityListRequest(ordAndNow + "", mRequestingNumber + "", PER_PAGE_NUMBER + ""
                , new ResponseListener<GetUserActivityResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                swipeContainer.setRefreshing(true);

            }

            @Override
            public void onCacheResponse(GetUserActivityResponse response) {
                if(response != null && response.getResult().getActivityInfoList() != null){
                    if(mRequestingNumber == 0){
                        activityInfo.clear();

                        activityInfo.addAll(response.getResult().getActivityInfoList());

                        activityListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onResponse(GetUserActivityResponse response) {
                if (response == null || response.getResult().getActivityInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                    return;
                }
                if (response.getResult().getActivityInfoList().size() < PER_PAGE_NUMBER) {

                    if (mRequestingNumber > 0)
                        listExperience.setFooter(true);

                    isNoMoreResult = true;

                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    listExperience.setFooter(false);

                    activityInfo.clear();

                }

                activityInfo.addAll(response.getResult().getActivityInfoList());

                activityListAdapter.notifyDataSetChanged();

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

                swipeContainer.setRefreshing(false);
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }


    private void getVendorData() {
        GetVendorActivityListRequest request = new GetVendorActivityListRequest(ordAndNow, mRequestingNumber, PER_PAGE_NUMBER, new ResponseListener<GetVendorActivityResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(GetVendorActivityResponse response) {

            }

            @Override
            public void onResponse(GetVendorActivityResponse response) {
                if (response == null || response.getResult().getActivityInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                    return;
                }

                if (response.getResult().getActivityInfoList().size() < PER_PAGE_NUMBER) {

                    if (mRequestingNumber > 0)
                        listExperience.setFooter(true);

                    isNoMoreResult = true;

                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    activityInfo.clear();

                    listExperience.setFooter(false);
                }

                activityInfo.addAll(response.getResult().getActivityInfoList());

                activityListAdapter.notifyDataSetChanged();
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
                swipeContainer.setRefreshing(false);
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    public void scrollTop(){
        listExperience.setSelection(0);
    }

}
