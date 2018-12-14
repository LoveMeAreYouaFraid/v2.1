package com.nautilus.ywlfair.module.active;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.CommentListAdapter;
import com.nautilus.ywlfair.adapter.CommentListAdapter.OnClickPraiseListener;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.event.EventCommentType;
import com.nautilus.ywlfair.entity.request.DeleteCommentRequest;
import com.nautilus.ywlfair.entity.request.GetActivityCommentsRequest;
import com.nautilus.ywlfair.entity.request.PostCommentByCommentIdRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetActivityCommentsResponse;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.launch.LoginActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends BaseActivity implements OnClickPraiseListener, View.OnClickListener {

    private Context mContext;

    private CommentListAdapter mAdapter;

    private List<CommentInfo> commentInfoList;

    private String itemId;

    private String itemType;

    private LoadMoreListView mListView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private int deletePosition = -1;

    private LinearLayout replyView;

    private EditText replyInputView;

    private int checkPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_fragment);

        mContext = this;

        itemId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);
        Log.e("123", itemId);

        itemType = getIntent().getStringExtra(Constant.KEY.ITEM_TYPE);

        EventBus.getDefault().register(this);

        initViews();

        getData();
    }

    private void initViews() {

        View topBarBack = findViewById(R.id.image_map_back);
        topBarBack.setOnClickListener(this);

        mListView = (LoadMoreListView) findViewById(R.id.lv_comment);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = findViewById(R.id.empty);

        mListView.setEmptyView(mEmptyView);

        commentInfoList = new ArrayList<>();

        mAdapter = new CommentListAdapter(mContext, commentInfoList);

        mListView.setAdapter(mAdapter);

        mAdapter.setOnClickPraiseListener(this);

        replyView = (LinearLayout) findViewById(R.id.ll_reply);

        replyInputView = (EditText) findViewById(R.id.et_reply_input);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });
        replyInputView.addTextChangedListener(new TextWatcher() {


                                                  @Override
                                                  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                  }

                                                  @Override
                                                  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                  }

                                                  @Override
                                                  public void afterTextChanged(Editable editable) {
                                                      String inputText = replyInputView.getText().toString();

                                                      if (inputText.length() > 141) {
                                                          replyInputView.setText(inputText.substring(0, 141));
                                                      }

                                                  }
                                              }

        );


        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                CommentInfo commentInfo = commentInfoList.get(position);

                if (commentInfo.getDelFlag() != 1) {

                    checkPosition = position;

                    Intent intent = new Intent(mContext,
                            CommentDetailActivity.class);

                    intent.putExtra(Constant.KEY.COMMENT_ID, commentInfo.getCommentId() + "");

                    startActivity(intent);
                } else {
                    ToastUtil.showShortToast("该评论已被删除");
                }

            }
        });

        mListView.setOnTouchListener(onTouchListener);

        mListView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && !mIsRequesting) {
                    mRequestingNumber = commentInfoList.size();

                    getData();
                }
            }
        });

        View createCommendView = findViewById(R.id.iv_create_commend);
        createCommendView.setOnClickListener(this);

        if (itemType.equals("4")) {
            createCommendView.setVisibility(View.GONE);
        }

        View confirmReply = findViewById(R.id.tv_confirm_reply);
        confirmReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = replyInputView.getText().toString();

                if (TextUtils.isEmpty(replyContent)) {
                    Toast.makeText(MyApplication.getInstance(), "请输入回复内容",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmReply(replyContent);
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(EventCommentType eventCommentType) {

        if (eventCommentType.getType() == 0) {

            commentInfoList.add(0, eventCommentType.getCommentInfo());

        } else if (eventCommentType.getType() == 1) {
            commentInfoList.get(checkPosition).setLikeNum(eventCommentType.getChangeNum());
        } else if (eventCommentType.getType() == 2) {
            commentInfoList.get(checkPosition).setReplyNum(eventCommentType.getChangeNum());
        }

        mAdapter.notifyDataSetChanged();

        mListView.setSelection(0);
    }

    private void confirmReply(String content) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        String replyId = String.valueOf(commentInfoList.get(deletePosition).getCommentId());

        PostCommentByCommentIdRequest request = new PostCommentByCommentIdRequest(String.valueOf(replyId), userId,
                content, new ResponseListener<PostCommentResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "提交中...");
            }

            @Override
            public void onCacheResponse(PostCommentResponse response) {
            }

            @Override
            public void onResponse(PostCommentResponse response) {
                if (response == null || response.getResult().getCommentInfo() == null) {
                    ToastUtil.showShortToast("操作失败");
                    return;
                }
                Toast.makeText(MyApplication.getInstance(), "回复成功",
                        Toast.LENGTH_SHORT).show();

                commentInfoList.get(deletePosition).setReplyNum(commentInfoList.get(deletePosition).getReplyNum() + 1);

                mAdapter.notifyDataSetChanged();

                hideCommentLayout();
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

    private void getData() {

        GetActivityCommentsRequest request = new GetActivityCommentsRequest(itemId, itemType, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetActivityCommentsResponse>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mIsRequesting = true;
                    }

                    @Override
                    public void onCacheResponse(GetActivityCommentsResponse response) {
                        if (response == null || response.getResult().getCommentInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            commentInfoList.clear();

                            commentInfoList.addAll(response.getResult().getCommentInfoList());

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

                            mListView.setFooter(false);

                            commentInfoList.clear();
                        }

                        if (response.getResult().getCommentInfoList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;

                            if (mRequestingNumber > 0)
                                mListView.setFooter(true);
                        }

                        commentInfoList.addAll(response.getResult().getCommentInfoList());

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
                    }
                });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (replyView.getVisibility() == View.VISIBLE) {

                        hideCommentLayout();

                    }
                    break;
            }
            return false;
        }
    };


    private AlertDialog loginDialog;

    /**
     * 用户若未登录，显示登录对话框
     */
    private void showLoginAlert() {
        if (loginDialog != null) {
            loginDialog.show();
        } else {
            loginDialog = new AlertDialog.Builder(mContext).create();
            loginDialog.show();
            Window window = loginDialog.getWindow();
            window.setContentView(R.layout.dlg_unlogin);
            TextView loginTextView = (TextView) window
                    .findViewById(R.id.tv_login);
            loginTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,
                            LoginActivity.class);

                    intent.putExtra(
                            Constant.KEY.MODE,
                            LoginActivity.Mode.PASSIVE);

                    mContext.startActivity(intent);

                    loginDialog.cancel();
                }
            });

            TextView cancelSchoolTextView = (TextView) window
                    .findViewById(R.id.tv_cancel);
            cancelSchoolTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loginDialog.cancel();
                }
            });
        }
    }

    /**
     * 点赞
     */
    private void praise(int position) {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        CommentInfo commentInfo = commentInfoList.get(position);

        if(commentInfo.getHasLike() == 1){
            return;
        }

        int isLike = commentInfo.getHasLike() == 0 ? 1 : 2;

        PostLikeRequest request = new PostLikeRequest(commentInfo.getCommentId() + "", 5, isLike, userId,
                new ResponseListener<PostLikeAndJoinResponse>() {
                    @Override
                    public void onStart() {
                        praiseView.setEnabled(false);
                    }

                    @Override
                    public void onCacheResponse(PostLikeAndJoinResponse response) {

                    }

                    @Override
                    public void onResponse(PostLikeAndJoinResponse response) {
                        if (response != null) {
                            praiseView.setText(response.getResult().getLikeNum());

                            int hasLike = commentInfoList.get(changePosition).getHasLike() == 0 ? 1 : 0;

                            commentInfoList.get(changePosition).setHasLike(hasLike);

                            commentInfoList.get(changePosition).setLikeNum(response.getResult().getLikeNum());
                        } else {
                            ToastUtil.showShortToast("操作失败");
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
                        praiseView.setEnabled(true);
                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void deleteComment() {

        DeleteCommentRequest request = new DeleteCommentRequest(commentInfoList.get(deletePosition).getCommentId() + "",
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在删除评论...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            ToastUtil.showShortToast("删除成功");

                            commentInfoList.get(deletePosition).setDelFlag(1);

                            commentInfoList.get(deletePosition).setContent("此条评论已被删除!");

                            mAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showShortToast("操作失败");
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

    private TextView praiseView;
    private int changePosition;

    @Override
    public void onClickPraise(int position, View view) {

        praise(position);

        praiseView = (TextView) view;

        changePosition = position;

    }

    @Override
    public void onDeleteComment(int position) {
        if (MyApplication.getInstance().isLogin()) {

            deletePosition = position;

            showDeleteConfirm();

        } else {
            showLoginAlert();
        }
    }

    @Override
    public void onComment(int position) {
        if (MyApplication.getInstance().isLogin()) {

            deletePosition = position;

            showCommentController();

        } else {
            showLoginAlert();
        }
    }

    private void showCommentController() {
        showCommentLayout();
    }

    private void hideCommentLayout() {

        replyInputView.setText("");
        replyInputView.setHint("");

        KeyBoardUtil.hideSoftKeyboard(replyView);

        replyView.setVisibility(View.GONE);

    }

    private void showCommentLayout() {
        replyView.setVisibility(View.VISIBLE);

        replyInputView.requestFocus();

        KeyBoardUtil.showSoftKeyboard(replyInputView);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_create_commend:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                Intent intent = new Intent();

                intent.putExtra(Constant.KEY.ITEM_ID, itemId);

                intent.setClass(mContext, EditPagerActivity.class);

                intent.putExtra(Constant.KEY.TYPE, itemType);

                startActivity(intent);
                break;

            case R.id.image_map_back:
                finish();
                break;
        }
    }
}
