package com.nautilus.ywlfair.module.active;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.CommentListAdapter;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.entity.bean.ScanCodeInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveLike;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.bean.event.EventCommentType;
import com.nautilus.ywlfair.entity.request.DeleteCommentRequest;
import com.nautilus.ywlfair.entity.request.GetActivityInfoRequest;
import com.nautilus.ywlfair.entity.request.PostCommentByCommentIdRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetActivityInfoResponse;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaiduMapActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.mine.MySignActivity;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.RippleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class ActiveFragment extends Fragment implements OnClickListener,
        RippleView.OnEndClickListener, CommentListAdapter.OnClickPraiseListener {
    private static ActiveFragment mInstance = null;

    private Context mContext;

    private RippleView takeTicket;

    private TextView joinBtn, interestBtn;

    private AutoAdjustHeightImageView activePoster;

    private String itemId;

    private String endTime, startTime;

    private NautilusItem mNautilusItem;

    private TextView activeName;

    private TextView ticketPrice;

    private TextView timeView;

    private TextView dateView;

    private TextView addressView;

    private View lottery;

    private TextView activeIntro;

    private LinearLayout contentContainer;

    private ImageView imgLike, imgConfirm;

    private LinearLayout avatarContainer;

    private TextView wantNum, tv_TicketsNum;

    private View recruitView;

    private ImageView interestedView, joinView, activeTag;

    private View interestLayout, LayoutJoin;

    private LoadMoreListView mListView;

    private CommentListAdapter mAdapter;

    private DisplayImageOptions options = ImageLoadUtils
            .createNoRoundedOptions();

    private GetShareInfoListener getShareInfoListener;

    private LinearLayout replyView, controlBar;

    private EditText replyInputView;

    private JsonUtil<ScanCodeInfo> jsonUtil = new JsonUtil<>();

    private int[] activeTags = new int[]{R.drawable.active_status_ing, R.drawable.active_status_past, R.drawable.active_status_be};

    public static ActiveFragment getInstance(Bundle bundle) {

        mInstance = new ActiveFragment();

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

        EventBus.getDefault().register(this);

        super.onCreate(icicle);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_acticity, null);


        initViews(view);

        getData();

        return view;

    }

    private void initViews(View view) {
        imgLike = (ImageView) view.findViewById(R.id.bt_like);
        imgLike.setOnClickListener(this);

        lottery = view.findViewById(R.id.iv_lotto);
        lottery.setOnClickListener(this);


        imgConfirm = (ImageView) view.findViewById(R.id.bt_confirm);
        imgConfirm.setOnClickListener(this);

        controlBar = (LinearLayout) view.findViewById(R.id.ll_control_bar);

        replyInputView = (EditText) view.findViewById(R.id.et_reply_input);

        takeTicket = (RippleView) view.findViewById(R.id.bt_buy_ticket);
        takeTicket.setOnEndClickListener(takeTicket.getId(), this);

//        ticketPrice = (TextView) view.findViewById(R.id.tv_ticket_price);

        mListView = (LoadMoreListView) view.findViewById(R.id.lv_comment_detail);

        replyView = (LinearLayout) view.findViewById(R.id.ll_reply);

        View headerView = View.inflate(mContext, R.layout.detail_fragment_header, null);

        activeTag = (ImageView) headerView.findViewById(R.id.iv_active_tag);
        tv_TicketsNum = (TextView) headerView.findViewById(R.id.tv_TicketsNum);

        View signView = headerView.findViewById(R.id.image_sign);
        signView.setOnClickListener(this);

        View commentView = headerView.findViewById(R.id.rl_comment_bar);
        commentView.setOnClickListener(this);

        avatarContainer = (LinearLayout) headerView.findViewById(R.id.avatar_container);

        wantNum = (TextView) headerView.findViewById(R.id.tv_want_num);

        activePoster = (AutoAdjustHeightImageView) headerView
                .findViewById(R.id.iv_active_poster);

        activeName = (TextView) headerView.findViewById(R.id.tv_active_name);

        timeView = (TextView) headerView.findViewById(R.id.tv_time);

        dateView = (TextView) headerView.findViewById(R.id.tv_date);

        addressView = (TextView) headerView.findViewById(R.id.tv_address);
        addressView.setOnClickListener(this);

        activeIntro = (TextView) headerView.findViewById(R.id.tv_active_introduce);

        mListView.addHeaderView(headerView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }

                Intent intent = new Intent(mContext,
                        CommentDetailActivity.class);

                intent.putExtra(Constant.KEY.COMMENT_ID, mNautilusItem.getCommentInfoList().get(position - 1).getCommentId() + "");

                startActivity(intent);

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

        View confirmReply = view.findViewById(R.id.tv_confirm_reply);
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

        mListView.setOnTouchListener(onTouchListener);
    }

    private void setValue(NautilusItem mNautilusItem) {


        if (mNautilusItem.getType() == 1) {
            tv_TicketsNum.setVisibility(View.GONE);

            addressView.setVisibility(View.GONE);

            takeTicket.setVisibility(View.GONE);
        } else {
            if (mNautilusItem.getActivityStatus() == 1) {
                if (this.isAdded())
                    takeTicket.setBackgroundColor(getResources().getColor(R.color.content_color));

                takeTicket.setEnabled(false);
            }

            if (mNautilusItem.getTicketInfoList().size() > 0) {
                double temp = mNautilusItem.getTicketInfoList().get(0).getPrice();

                tv_TicketsNum.setText("门票：￥" + StringUtils.getMoneyFormat(mNautilusItem.getTicketInfoList().get(0).getPrice()));

                if (temp == 0) {
                    takeTicket.setText("免门票");
                    if (this.isAdded())
                        takeTicket.setBackgroundColor(getResources().getColor(R.color.content_color));

                    takeTicket.setEnabled(false);
                }
            }

            addressView.setText("地址：" + mNautilusItem.getAddress());

        }

        if (mNautilusItem.getHasLike() == 1) {
            imgLike.setImageResource(R.drawable.bt_islike);
        } else if (mNautilusItem.getHasLike() == 0) {
            imgLike.setImageResource(R.drawable.bt_like);
        }

        activeTag.setImageResource(activeTags[mNautilusItem.getActivityStatus()]);

        ImageLoader.getInstance().displayImage(
                mNautilusItem.getPosterInfo().getImgUrl(), activePoster,
                options);

        activeName.setText(mNautilusItem.getName());

        startTime = mNautilusItem.getStartTime();

        endTime = mNautilusItem.getEndTime();

        String timeText = "时间：" + TimeUtil.getHourAndMin(Long.valueOf(startTime)) + " ~ "
                + TimeUtil.getHourAndMin(Long.valueOf(endTime));
        timeView.setText(timeText);

        String dateText = "日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(startTime)) + " ~ "
                + TimeUtil.getYearMonthAndDay(Long.valueOf(endTime));
        dateView.setText(dateText);

        activeIntro.setText(mNautilusItem.getIntroduction());

//        joinBtn.setText(mNautilusItem.getWantJoinNum() + "人想参加");
//
//        interestBtn.setText(mNautilusItem.getLikeNum() + "人感兴趣");

        wantNum.setText(mNautilusItem.getWantJoinNum() + "人想参加");

        List<UserInfo> wantList = mNautilusItem.getWantJoinUserInfoList();

        if (wantList != null) {

            avatarContainer.removeAllViews();

            for (int i = 0; i < wantList.size(); i++) {
                UserInfo userInfo = wantList.get(i);

                ImageView imageView = new ImageView(mContext);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        BaseInfoUtil.dip2px(30), BaseInfoUtil.dip2px(30));

                params.setMargins(0, 0, BaseInfoUtil.dip2px(5), 0);

                imageView.setLayoutParams(params);

                ImageLoadUtils.setRoundHeadView(imageView,
                        userInfo.getAvatar(), R.drawable.default_avatar, 50);

                avatarContainer.addView(imageView);
            }
        }

        mAdapter = new CommentListAdapter(mContext, mNautilusItem.getCommentInfoList());

        mAdapter.setOnClickPraiseListener(this);

        setListViewFooter();

        mListView.setAdapter(mAdapter);

    }

    private void setListViewFooter() {
        if (mNautilusItem.getCommentInfoList().size() >= 20) {

            TextView textView = new TextView(mContext);

            textView.setText("查看更多评论");

            textView.setGravity(Gravity.CENTER);

            textView.setBackgroundColor(Color.WHITE);

            textView.setTextColor(getResources().getColor(R.color.blue));

            textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, BaseInfoUtil.dip2px(50)));

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCommentListActivity();
                }
            });

            if (mListView.getFooterViewsCount() == 0)
                mListView.addFooterView(textView);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address:

                if (TextUtils.isEmpty(mNautilusItem.getAddrMap())) {
                    ToastUtil.showShortToast("没有活动位置信息");
                    return;
                }
                Intent mapIntent = new Intent(getActivity().getApplicationContext(),
                        BaiduMapActivity.class);

                mapIntent.putExtra(Constant.KEY.NAUTILUSITEM, mNautilusItem);

                startActivity(mapIntent);
                break;

            case R.id.iv_lotto:
                isScanCode();
                break;

            case R.id.bt_confirm:
                if (mNautilusItem == null)
                    return;
                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                Intent commentIntent = new Intent(getActivity(), EditPagerActivity.class);

                commentIntent.putExtra(Constant.KEY.ITEM_ID, itemId);

                commentIntent.putExtra(Constant.KEY.TYPE, "1");

                startActivity(commentIntent);

                break;
            case R.id.bt_like:
                if (mNautilusItem == null)
                    return;

                praise();
                break;
            case R.id.image_sign:

                if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {
                    Intent intent = new Intent(mContext, MySignActivity.class);

                    intent.putExtra(Constant.KEY.ITEM_ID, itemId);

                    intent.putExtra(Constant.KEY.TYPE, mNautilusItem.getType());

                    startActivity(intent);
                }

                break;

            case R.id.rl_comment_bar:

                startCommentListActivity();

                break;

        }

    }

    private void isScanCode() {
        if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {
            String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";
            String data = TimeUtil.getYearMonthAndDay(System.currentTimeMillis());

            String key = Constant.KEY.TWO_CODE_INFO + userId + mNautilusItem.getActId();
            if (mNautilusItem != null) {
                if (!TextUtils.isEmpty(PreferencesUtil.getString(key))) {
                    ScanCodeInfo info = jsonUtil.json2Bean(PreferencesUtil.getString(key), ScanCodeInfo.class.getName());
                    if (TimeUtil.getYearMonthAndDay(info.getData()).equals(data)) {
                        LotteryWebView.startLotteryWebView(mContext, "1", info.getUrl() +"&userId=" + userId + "",mNautilusItem.getActId() +
                                        "&deviceId=" + Common.getInstance().getIMSI());

                    } else {
                        LotteryWebView.startLotteryWebView(mContext, "1", MyApplication.getInstance().getActDrawUrl() + "actId=" + mNautilusItem.getActId() +
                                "&userId=" + userId + "", mNautilusItem.getActId());
                    }
                } else {
                    LotteryWebView.startLotteryWebView(mContext, "1", MyApplication.getInstance().getActDrawUrl() + "actId=" + mNautilusItem.getActId() +
                            "&userId=" + userId + "", mNautilusItem.getActId());
                }

            } else {
                ToastUtil.showLongToast("活动信息获取失败，请稍后再试。");
                return;
            }

        }
    }

    private void startCommentListActivity() {
        Intent commendListIntent = new Intent(mContext, CommentListActivity.class);

        commendListIntent.putExtra(Constant.KEY.ITEM_ID, itemId);

        commendListIntent.putExtra(Constant.KEY.ITEM_TYPE, "1");

        startActivity(commendListIntent);
    }

    private void confirmReply(String content) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        String replyId = String.valueOf(mNautilusItem.getCommentInfoList().get(changePosition).getCommentId());

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

                mNautilusItem.getCommentInfoList().get(changePosition)
                        .setReplyNum(mNautilusItem.getCommentInfoList().get(changePosition).getReplyNum() + 1);

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

    /**
     * 感兴趣
     */
    private void praise() {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        int isLike = mNautilusItem.getHasLike() == 0 ? 1 : 2;

        PostLikeRequest request = new PostLikeRequest(mNautilusItem.getActId(), 1, isLike, userId,
                new ResponseListener<PostLikeAndJoinResponse>() {
                    @Override
                    public void onStart() {
                        imgLike
                                .setBackgroundResource(R.drawable.bg_green_rounded_gray_solid_rectangle);

                        imgLike.setEnabled(false);
                    }

                    @Override
                    public void onCacheResponse(PostLikeAndJoinResponse response) {

                    }

                    @Override
                    public void onResponse(PostLikeAndJoinResponse response) {
                        if (response != null) {

                            int hasLike = mNautilusItem.getHasLike() == 0 ? 1 : 0;

                            if (hasLike == 1) {
                                imgLike.setImageResource(R.drawable.bt_islike);
                            }

                            if (hasLike == 0) {
                                imgLike.setImageResource(R.drawable.bt_like);
                            }

                            mNautilusItem.setHasLike(hasLike);

                            EventBus.getDefault().post(new EventActiveLike(hasLike));
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
                        imgLike.setEnabled(true);

                        imgLike.setBackgroundResource(R.drawable.bg_green_rounded_rectangle);
                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }


    private void getData() {
        GetActivityInfoRequest request = new GetActivityInfoRequest(itemId, new ResponseListener<GetActivityInfoResponse>() {
            @Override
            public void onStart() {
                if (isVisible)
                    ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetActivityInfoResponse response) {
                if (response != null & response.getResult().getNautilusItem() != null) {

                    mNautilusItem = response.getResult().getNautilusItem();

                    setValue(mNautilusItem);

                }
            }

            @Override
            public void onResponse(GetActivityInfoResponse response) {

                if (response == null || response.getResult().getNautilusItem() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                mNautilusItem = response.getResult().getNautilusItem();

                if (mNautilusItem.getIsDrawOpen() == 1) {
                    lottery.setVisibility(View.VISIBLE);
                }
                // lottery.setVisibility(View.VISIBLE);
                setValue(mNautilusItem);

                EventBus.getDefault().post(new EventActiveStatus(-1, 0, mNautilusItem));

                if (getShareInfoListener != null) {
                    getShareInfoListener.getShareInfoStart(mNautilusItem.getActId());
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
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);

    }


    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.bt_buy_ticket:
                if (mNautilusItem == null)
                    return;

                Intent ticketIntent = new Intent(getActivity(),
                        TicketOrderActivity.class);

                ticketIntent.putExtra(
                        Constant.KEY.MODE,
                        TicketOrderActivity.Mode.TICKET);

                ticketIntent.putExtra(Constant.KEY.NAUTILUSITEM, mNautilusItem);

                startActivity(ticketIntent);
                break;
        }

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
        if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

            changePosition = position;

            showDeleteConfirm();

        }
    }

    @Override
    public void onComment(int position) {
        if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

            changePosition = position;

            showCommentController();

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

        controlBar.setVisibility(View.VISIBLE);

    }

    private void showCommentLayout() {
        replyView.setVisibility(View.VISIBLE);

        controlBar.setVisibility(View.GONE);

        replyInputView.requestFocus();

        KeyBoardUtil.showSoftKeyboard(replyInputView);

    }

    private void deleteComment() {

        String commentId = mNautilusItem.getCommentInfoList().get(changePosition).getCommentId() + "";

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

                            mNautilusItem.getCommentInfoList().get(changePosition).setDelFlag(1);

                            mNautilusItem.getCommentInfoList().get(changePosition).setContent("此条评论已被删除!");

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

    /**
     * 点赞
     */
    private void praise(int position) {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        final CommentInfo commentInfo = mNautilusItem.getCommentInfoList().get(position);
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
                            praiseView.setText("赞 " + response.getResult().getLikeNum());

                            int hasLike = commentInfo.getHasLike() == 0 ? 1 : 0;

                            mNautilusItem.getCommentInfoList().get(changePosition).setHasLike(hasLike);

                            mNautilusItem.getCommentInfoList().get(changePosition).setLikeNum(response.getResult().getLikeNum());
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

    public interface GetShareInfoListener {
        void getShareInfoStart(String itemId);
    }

    public void setGetShareInfoListener(GetShareInfoListener getShareInfoListener) {
        this.getShareInfoListener = getShareInfoListener;
    }

    private boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCommentType eventCommentType) {

        if (eventCommentType.getType() == 0) {

            mNautilusItem.getCommentInfoList().add(0, eventCommentType.getCommentInfo());

        }

        mAdapter.notifyDataSetChanged();

        mListView.setSelection(0);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
