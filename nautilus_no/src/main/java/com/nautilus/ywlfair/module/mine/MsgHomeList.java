package com.nautilus.ywlfair.module.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.MessageListAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.MessageInfo;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.GetUserMessagesRequest;
import com.nautilus.ywlfair.entity.request.GetUserTicketRequest;
import com.nautilus.ywlfair.entity.request.PutMessagesRequest;
import com.nautilus.ywlfair.entity.response.GetUserMessagesResponse;
import com.nautilus.ywlfair.entity.response.GetUserTicketResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.active.LotteryWebView;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class MsgHomeList extends BaseActivity implements OnClickListener {

    private Context mContext;

    private LoadMoreListView mListView;

    private MessageListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private List<MessageInfo> messageInfoList;

    private String userId;

    private int type = 0;// 消息类型。0:全部;1:系统消息;2:私信。不传则默认为全部

    private int readStatus = -1;// 只读状态。0:未读;1:已读;-1:全部。不传则默认未读

    private LinearLayout editController;

    private boolean isEdit = false;

    private List<MessageInfo> checkedList;

    private TextView editView;

    private boolean isShowDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.msg_home_list);

        mContext = this;

        if (!MyApplication.getInstance().isLogin()) {
            return;
        }

        userId = String.valueOf(GetUserInfoUtil.getUserInfo()
                .getUserId());

        checkedList = new ArrayList<>();

        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(this);

        editView = (TextView) findViewById(R.id.tv_edit);
        editView.setOnClickListener(this);

        View deleteAll = findViewById(R.id.tv_all_delete);
        deleteAll.setOnClickListener(this);

        View readAll = findViewById(R.id.tv_all_read);
        readAll.setOnClickListener(this);

        editController = (LinearLayout) findViewById(R.id.ll_edit_control);

        mListView = (LoadMoreListView) findViewById(R.id.lv_message_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = findViewById(R.id.empty);

        messageInfoList = new ArrayList<>();

        mAdapter = new MessageListAdapter(mContext, messageInfoList);

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

                        } else if (messageInfoList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = messageInfoList.size();
                                getData();
                            }
                        }
                    }
                });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                showManageDialog(messageInfoList.get(position));
                return true;
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MessageInfo messageInfo = messageInfoList.get(position);

                if (isEdit) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

                    checkBox.toggle();

                    if (checkBox.isChecked()) {
                        checkedList.add(messageInfo);
                    } else {
                        checkedList.remove(messageInfo);
                    }
                } else {
                    int subType = messageInfo.getSubType();

                    if (subType == 4) {
                        Intent intent = new Intent(mContext,
                                CommentDetailActivity.class);

                        String[] extendFields = messageInfo.getExtendField().split(",");

                        if (extendFields.length > 0) {
                            intent.putExtra(Constant.KEY.COMMENT_ID, extendFields[0]);

                            startActivity(intent);
                        }

                    } else if (subType == 1) {

                        String[] extendFields = messageInfo.getExtendField().split(",");

                        if (extendFields.length > 0) {
                            onCheckTicket(extendFields[0]);

                        }

                    } else if (subType == 18) {
                        String actid = messageInfo.getExtendField();
                        String uri = MyApplication.getInstance().getActDrawLogUrl() + "actId=" + actid + "&userId=" +
                                GetUserInfoUtil.getUserInfo().getUserId() + "";
                        LotteryWebView.startLotteryWebView(mContext, "2", uri, actid);

                    }

                    isShowDialog = false;

                    readMessage(messageInfo);
                }

            }
        });

        getData();
    }

    private void getData() {

        GetUserMessagesRequest request = new GetUserMessagesRequest(userId, type, readStatus,
                mRequestingNumber, PER_PAGE_NUMBER, new ResponseListener<GetUserMessagesResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onCacheResponse(GetUserMessagesResponse response) {
                if (response == null || response.getResult().getMessageList() == null) {
                    return;
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    messageInfoList.clear();

                    messageInfoList.addAll(response.getResult().getMessageList());

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponse(GetUserMessagesResponse response) {
                if (response == null || response.getResult().getMessageList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    messageInfoList.clear();
                }

                if (response.getResult().getMessageList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;
                }

                messageInfoList.addAll(response.getResult().getMessageList());

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
                mIsRequesting = false;

                mListView.setEmptyView(mEmptyView);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        request.setShouldCache(true);
        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_edit:
                isEdit = !isEdit;
                checkedList.clear();
                mAdapter.update(isEdit, checkedList);
                if (isEdit) {
                    editView.setText("完成");
                    editController.setVisibility(View.VISIBLE);
                } else {
                    editView.setText("编辑");
                    editController.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_all_delete:

                if (checkedList.size() == 0) {
                    Toast.makeText(MyApplication.getInstance(), "请选择删除的条目",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                handleType = 2;

                setMessageStatus(getUnreadMessageId(checkedList));
                break;

            case R.id.tv_all_read:
                if (checkedList.size() == 0) {
                    Toast.makeText(MyApplication.getInstance(), "请选择设置的条目",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                handleType = 1;

                setMessageStatus(getUnreadMessageId(checkedList));

                break;

        }

    }

    private int handleType = 1;

    private void setMessageStatus(String unReadMessageIds) {//1设为已读 2 删除

        PutMessagesRequest request = new PutMessagesRequest(unReadMessageIds, handleType,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {

                        if (handleType == 1) {
                            MyApplication.getInstance().setMessageCount(MyApplication.getInstance().getMessageCount() - changeCount);

                            UserMainEventItem item = new UserMainEventItem();

                            item.setType(2);

                            EventBus.getDefault().post(item);
                        } else if (handleType == 2) {
                            ToastUtil.showShortToast("删除成功");
                        }

                        setReadUi();

                        if (isEdit)
                            editView.performClick();
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

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private int changeCount = 0;

    private String getUnreadMessageId(List<MessageInfo> list) {
        changeCount = list.size();

        StringBuffer sb = new StringBuffer();

        for (MessageInfo messageInfo : list) {
            sb.append(messageInfo.getMessageId());

            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private void setReadUi() {

        if (handleType == 1) {
            for (MessageInfo messageInfo : messageInfoList) {

                if (checkedList.contains(messageInfo)) {
                    messageInfo.setReadStatus(1);
                }
            }
        } else if (handleType == 2) {

            for (MessageInfo messageInfo : checkedList) {
                messageInfoList.remove(messageInfo);
            }

        }

        mAdapter.notifyDataSetChanged();

        checkedList.clear();
    }

    /**
     * Show Dialog
     */
    public void showManageDialog(final MessageInfo messageInfo) {

        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dlg_menue);
        //View view = LayoutInflater.from(this).inflate(R.layout.dlg_menue, null);

        TextView deleteTextView = (TextView) window.findViewById(R.id.tv_delete);

        deleteTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                checkedList.clear();

                checkedList.add(messageInfo);

                handleType = 2;

                setMessageStatus(getUnreadMessageId(checkedList));

                dialog.dismiss();
            }
        });

        TextView setReadTextView = (TextView)
                window.findViewById(R.id.tv_set_read);

        if (messageInfo.getReadStatus() != 0) {
            setReadTextView.setVisibility(View.GONE);
        }

        setReadTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkedList.clear();

                checkedList.add(messageInfo);

                handleType = 1;

                setMessageStatus(getUnreadMessageId(checkedList));

                dialog.dismiss();
            }
        });


    }

    private void readMessage(MessageInfo messageInfo) {
        checkedList.clear();

        checkedList.add(messageInfo);

        handleType = 1;

        setMessageStatus(getUnreadMessageId(checkedList));
    }

    public void onCheckTicket(String activeId) {

        GetUserTicketRequest request = new GetUserTicketRequest(userId + "", activeId,
                new ResponseListener<GetUserTicketResponse>() {
                    @Override
                    public void onStart() {
                        if (isShowDialog)
                            ProgressDialog.getInstance().show(mContext, "获取门票信息...");
                    }

                    @Override
                    public void onCacheResponse(GetUserTicketResponse response) {
                    }

                    @Override
                    public void onResponse(GetUserTicketResponse response) {
                        if (response == null || response.getResult().getTicketInfoList() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }
                        toShowTicket(response.getResult().getTicketInfoList());
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
                        isShowDialog = true;
                    }
                });
        request.setShouldCache(false);
        VolleyUtil.addToRequestQueue(request);

    }

    private void toShowTicket(List<TicketInfoList> ticketList) {
        if (ticketList.size() > 0) {

            List<PicInfo> picInfoList = new ArrayList<>();

            for (TicketInfoList ticket : ticketList) {
                PicInfo pickInfo = new PicInfo();

                pickInfo.setImgUrl(ticket.getTicketImgUrl());

                pickInfo.setThumbnailUrl(ticket.getTicketImgUrl());

                pickInfo.setDesc(ticket.getTicketCode());

                picInfoList.add(pickInfo);
            }
            Intent intent = new Intent(mContext,
                    ShowImagesPagerActivity.class);

            intent.putExtra(Constant.KEY.MODE, ShowImagesPagerActivity.Mode.TICKET);

            intent.putExtra(Constant.KEY.PICINFO_LIST,
                    (ArrayList<PicInfo>) picInfoList);

            intent.putExtra(Constant.KEY.POSITION, 0);

            mContext.startActivity(intent);
        } else {
            Toast.makeText(MyApplication.getInstance(), "暂时没有当前活动门票", Toast.LENGTH_LONG).show();
        }
    }

}
