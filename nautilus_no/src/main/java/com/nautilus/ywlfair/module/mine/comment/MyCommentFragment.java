package com.nautilus.ywlfair.module.mine.comment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.MyCommentListAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.request.DeleteCommentRequest;
import com.nautilus.ywlfair.entity.request.GetCommentsByUserIdRequest;
import com.nautilus.ywlfair.entity.response.GetActivityCommentsResponse;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyCommentFragment extends Fragment {

    private Context mContext;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private int deletePosition = -1;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LoadMoreListView mListView;

    private int type;

    private MyCommentListAdapter mAdapter;

    private View mEmptyView;

    private List<CommentInfo> commentList;

    public static MyCommentFragment getInstance(Bundle bundle) {

        MyCommentFragment mInstance = new MyCommentFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_comment_fragment, null);

        mContext = getActivity();

        type = getArguments().getInt(Constant.KEY.TYPE);

        mListView = (LoadMoreListView) rootView.findViewById(R.id.lv_my_comment);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = rootView.findViewById(R.id.empty);

        commentList = new ArrayList<>();

        mAdapter = new MyCommentListAdapter(mContext, commentList);

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

                        } else if (commentList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = commentList.size();
                                getData();
                            }
                        }
                    }
                });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(mContext, CommentDetailActivity.class);

                intent.putExtra(Constant.KEY.COMMENT_ID, commentList.get(position).getCommentId() + "");

                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                if (type == 1) {
                    deletePosition = position;

                    showManageDialog();
                }
                return true;
            }
        });

        getData();

        return rootView;
    }

    private void getData() {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        GetCommentsByUserIdRequest request = new GetCommentsByUserIdRequest(userId, type, mRequestingNumber
                , PER_PAGE_NUMBER, new ResponseListener<GetActivityCommentsResponse>() {
            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);

                mIsRequesting = true;

                mListView.setEmptyView(null);
            }

            @Override
            public void onCacheResponse(GetActivityCommentsResponse response) {
                if (response == null || response.getResult().getCommentInfoList() == null) {
                    return;
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    commentList.clear();

                    commentList.addAll(response.getResult().getCommentInfoList());

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponse(GetActivityCommentsResponse response) {
                if (response == null || response.getResult().getCommentInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    commentList.clear();

                    mIsNoMoreResult = false;

                    mListView.setFooter(false);
                }

                if (response.getResult().getCommentInfoList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;

                    if (mRequestingNumber > 0)
                        mListView.setFooter(true);
                }

                commentList.addAll(response.getResult().getCommentInfoList());

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
                mSwipeRefreshLayout.setRefreshing(false);

                mIsRequesting = false;

                mListView.setEmptyView(mEmptyView);

            }
        });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private void deleteComment() {

        DeleteCommentRequest request = new DeleteCommentRequest(String.valueOf(commentList.get(deletePosition).getCommentId()),
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在删除...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            Toast.makeText(MyApplication.getInstance(), "删除成功", Toast.LENGTH_SHORT).show();

                            commentList.remove(deletePosition);

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
                        ProgressDialog.getInstance().cancel();
                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    /**
     * Show Dialog
     */
    public void showManageDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.dialog);

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_menue, null);

        TextView deleteTextView = (TextView) view.findViewById(R.id.tv_delete);

        deleteTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteConfirm();

                dialog.dismiss();
            }
        });

        TextView setReadTextView = (TextView) view.findViewById(R.id.tv_set_read);
        setReadTextView.setVisibility(View.GONE);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    private void showDeleteConfirm() {
        final Dialog dialog = new Dialog(mContext, R.style.dialog);

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_confirm, null);

        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        int paddingPx = BaseInfoUtil.dip2px(MyApplication.getInstance(), 20);
        window.getDecorView().setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setText("提示");

        View dividerView = view.findViewById(R.id.view_divider);
        dividerView.setVisibility(View.VISIBLE);

        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        contentTextView.setVisibility(View.VISIBLE);
        contentTextView.setText("确定删除吗？");

        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_left);
        cancelTextView.setVisibility(View.VISIBLE);
        cancelTextView.setText("取消");

        TextView okTextView = (TextView) view.findViewById(R.id.tv_right);
        okTextView.setText("确定");

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                deleteComment();
            }
        });
    }

}
