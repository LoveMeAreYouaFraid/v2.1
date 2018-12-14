package com.nautilus.ywlfair.module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.CommentDetailListAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.event.EventCommentType;
import com.nautilus.ywlfair.entity.request.DeleteCommentRequest;
import com.nautilus.ywlfair.entity.request.GetCommentDetailRequest;
import com.nautilus.ywlfair.entity.request.PostCommentByCommentIdRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetCommentDetailResponse;
import com.nautilus.ywlfair.entity.response.GetCommentDetailResponse.CommentDetailInfo;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.launch.LoginActivity;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailActivity extends BaseActivity implements
        OnClickListener {

    private Context mContext;

    private LoadMoreListView mListView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 10;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private View headerView;

    private ImageView img_back;

    private CommentDetailListAdapter mAdapter;

    private CommentDetailInfo commentInfo;

    private String commentId;

    private LinearLayout replyView;

    private List<CommentInfo> commentInfoList;

    private DisplayImageOptions options = ImageLoadUtils
            .createDisplayOptions(0);

    private EditText replyInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_detail);

        mContext = this;

        commentId = getIntent().getStringExtra(Constant.KEY.COMMENT_ID);

        initView();

        getData();
    }

    private void initView() {

        View confirmReply = findViewById(R.id.tv_confirm_reply);
        confirmReply.setOnClickListener(this);

        replyView = (LinearLayout) findViewById(R.id.ll_reply);

        replyInputView = (EditText) findViewById(R.id.et_reply_input);

        img_back = (ImageView) findViewById(R.id.back);

        img_back.setOnClickListener(this);

        mListView = (LoadMoreListView) findViewById(R.id.lv_comment_detail);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        headerView = View.inflate(this, R.layout.comment_detail_header, null);

        mListView.addHeaderView(headerView);

        commentInfoList = new ArrayList<>();

        mAdapter = new CommentDetailListAdapter(this, commentInfoList);

        mListView.setAdapter(mAdapter);

        mListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (replyView.getVisibility() == View.VISIBLE) {

                    hideCommentLayout();

                } else {
                }
                return false;
            }
        });
        replyInputView.addTextChangedListener(
                new TextWatcher() {


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
                if (position != 0) {
                    showCommentLayout(commentInfoList.get(position - 1));
                }
            }
        });

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

                        } else if (commentInfoList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = commentInfoList.size();
                                getData();
                            }
                        }
                    }
                });
    }

    private void getData() {

        GetCommentDetailRequest request = new GetCommentDetailRequest(commentId,
                mRequestingNumber, PER_PAGE_NUMBER, new ResponseListener<GetCommentDetailResponse>() {
            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);

                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(GetCommentDetailResponse response) {
                if (response == null || response.getResult().getCommentInfo() == null) {
                    return;
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    commentInfo = response.getResult().getCommentInfo();

                    showHeaderView(headerView);

                    commentInfoList.clear();

                    commentInfoList.addAll(response.getResult().getCommentInfo().getReplyCommentList());

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponse(GetCommentDetailResponse response) {
                if (response == null || response.getResult().getCommentInfo() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    commentInfoList.clear();

                    commentInfo = response.getResult().getCommentInfo();

                    showHeaderView(headerView);
                }

                if (response.getResult().getCommentInfo().getReplyCommentList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;
                }

                commentInfoList.addAll(response.getResult().getCommentInfo().getReplyCommentList());

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

    private void showHeaderView(View headerView) {

        ImageView header = (ImageView) headerView
                .findViewById(R.id.iv_header);

        View replyBtn = headerView.findViewById(R.id.tv_comment_num);
        replyBtn.setOnClickListener(this);

        ImageView officialTag = (ImageView) headerView.findViewById(R.id.iv_official_tag);

        if (commentInfo.getAuthor().getUserType() == 2) {
            officialTag.setVisibility(View.VISIBLE);
        } else {
            officialTag.setVisibility(View.GONE);
        }

        ImageLoadUtils.setRoundHeadView(header, commentInfo.getAuthor().getAvatar(), R.drawable.default_avatar, 80);

        TextView nameView = (TextView) headerView.findViewById(R.id.tv_name);
        nameView.setText(commentInfo.getAuthor().getNickName());

        TextView dateView = (TextView) headerView.findViewById(R.id.tv_time);
        dateView.setText(TimeUtil.castLastDate(Long.valueOf(commentInfo
                .getAddTime())));

        TextView deleteView = (TextView) headerView
                .findViewById(R.id.tv_address);


        if (MyApplication.getInstance().isLogin() && commentInfo.getAuthor().getUserId() == GetUserInfoUtil.getUserInfo().getUserId()) {

            deleteView.setVisibility(View.VISIBLE);

            deleteView.setOnClickListener(this);

        } else {

            deleteView.setVisibility(View.GONE);

        }

        praiseView = (TextView) headerView.findViewById(R.id.tv_praise);
        praiseView.setText(commentInfo.getLikeNum() + "");

        praiseView.setOnClickListener(this);

        if (commentInfo.getHasLike() == 1) {
            praiseView.setTextColor(getResources().getColor(R.color.ju_hong));
        } else {
            praiseView.setTextColor(getResources()
                    .getColor(R.color.normal_gray));
        }

        TextView replyView = (TextView) headerView
                .findViewById(R.id.tv_comment_num);
        replyView.setText(commentInfo.getReplyNum() + "");

        TextView contentView = (TextView) headerView
                .findViewById(R.id.tv_content);
        contentView.setText(commentInfo.getContent());

        showPictures(headerView);
    }

    private void showPictures(View headerView) {

        TableRow row1 = (TableRow) headerView.findViewById(R.id.tr_row1);

        TableRow row2 = (TableRow) headerView.findViewById(R.id.tr_row2);

        AutoAdjustHeightImageView[] imageViews = new AutoAdjustHeightImageView[6];

        imageViews[0] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_0);

        imageViews[1] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_1);

        imageViews[2] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_2);

        imageViews[3] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_3);

        imageViews[4] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_4);

        imageViews[5] = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_multi_pics_5);

        final List<PicInfo> picInfoList = commentInfo.getPhotos();

        if (picInfoList != null) {
            if (picInfoList.size() > 3) {
                row1.setVisibility(View.VISIBLE);

                row2.setVisibility(View.VISIBLE);
            } else if (picInfoList.size() > 0 && picInfoList.size() <= 3) {
                row1.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < picInfoList.size(); i++) {

                if (i >= imageViews.length) {
                    continue;
                }
                ImageLoader.getInstance().displayImage(
                        picInfoList.get(i).getThumbnailUrl(), imageViews[i], options);
                imageViews[i].setTag(i);

                imageViews[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext,
                                ShowImagesPagerActivity.class);

                        intent.putExtra(Constant.KEY.PICINFO_LIST,
                                (ArrayList<PicInfo>) picInfoList);
                        intent.putExtra(Constant.KEY.POSITION,
                                (Integer) v.getTag());

                        mContext.startActivity(intent);
                    }
                });

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.tv_confirm_reply:


                String replyContent = replyInputView.getText().toString();

                if (TextUtils.isEmpty(replyContent)) {
                    Toast.makeText(MyApplication.getInstance(), "请输入回复内容",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmReply(replyContent);
                break;

            case R.id.tv_comment_num:

                if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    showCommentLayout(null);
                }

                break;

            case R.id.tv_praise:

                if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    praise();
                }

                break;

            case R.id.tv_address:
                deleteComment();
                break;

        }

    }

    private void deleteComment() {

        DeleteCommentRequest request = new DeleteCommentRequest(commentId,
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

                            commentInfo.setContent("此条评论已被删除！");

                            commentInfo.getPhotos().clear();

                            showHeaderView(headerView);
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

    /**
     * 点赞
     */
    private void praise() {

        if(commentInfo.getHasLike() == 1){
            return;
        }

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
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
                            praiseView.setText("" + response.getResult().getLikeNum());

                            int hasLike = commentInfo.getHasLike() == 0 ? 1 : 0;

                            commentInfo.setHasLike(hasLike);

                            commentInfo.setLikeNum(response.getResult().getLikeNum());

                            if (commentInfo.getHasLike() == 1) {
                                praiseView.setTextColor(getResources().getColor(
                                        R.color.ju_hong));
                            } else {
                                praiseView.setTextColor(getResources().getColor(
                                        R.color.normal_gray));
                            }

                            EventCommentType eventCommentType = new EventCommentType(null, 1, commentInfo.getLikeNum());

                            EventBus.getDefault().post(eventCommentType);

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

    private int replyId;

    private void confirmReply(String content) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        PostCommentByCommentIdRequest request = new PostCommentByCommentIdRequest(String.valueOf(replyId), userId,
                content, new ResponseListener<PostCommentResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "留言中...");
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

                mRequestingNumber = PAGE_START_NUMBER;

                mIsNoMoreResult = false;

                hideCommentLayout();

                getData();

                EventCommentType eventCommentType = new EventCommentType(null, 2, commentInfo.getReplyNum());

                EventBus.getDefault().post(eventCommentType);
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

    private AlertDialog loginDialog;

    private TextView praiseView;


    private void hideCommentLayout() {

        replyInputView.setText("");
        replyInputView.setHint("");

        KeyBoardUtil.hideSoftKeyboard(replyView);

        replyView.setVisibility(View.GONE);
    }

    private void showCommentLayout(CommentInfo replyComment) {
        replyView.setVisibility(View.VISIBLE);

        replyInputView.requestFocus();

        KeyBoardUtil.showSoftKeyboard(replyInputView);

        if (replyComment != null) {
            replyInputView.setHint("回复" + replyComment.getAuthor().getNickName() + ": ");
            replyId = replyComment.getCommentId();
        } else {
            replyInputView.setHint("");
            replyId = commentInfo.getCommentId();
        }

    }

    @Override
    public void onBackPressed() {

        if (replyView.getVisibility() == View.VISIBLE) {
            hideCommentLayout();
        } else {
            finish();
            super.onBackPressed();
        }

    }
}
