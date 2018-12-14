package com.nautilus.ywlfair.component;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.ReceiveMessage;
import com.nautilus.ywlfair.module.mine.MsgDetailActivity;
import com.nautilus.ywlfair.module.mine.level.VendorScoreDetailActivity;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.module.active.LotteryWebView;
import com.nautilus.ywlfair.module.booth.MyBoothDetailActivity;
import com.nautilus.ywlfair.module.mine.AllMsgActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MyMessageReceiver extends MessageReceiver {

    private static final String ACTIVE = "act";

    private static final String MESSAGE = "message";

    private static final String BOOTH = "booth";

    private static final String DRAW_LOG = "drawLog";

    private static final String VENDER_MESSAGE = "vendorMessage";

    private static final String SCORE = "score";

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    private JsonUtil<ReceiveMessage> jsonUtil = new JsonUtil<>();

    @Override
    protected void onNotification(Context context, String s, String s1, Map<String, String> map) {
        LogUtil.e(REC_TAG, "onNotification");

    }

    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        LogUtil.e(REC_TAG, "onMessage");
    }

    @Override
    protected void onNotificationRemoved(Context context, String s) {
        LogUtil.e(REC_TAG, "onNotificationRemoved");
    }

    @Override
    protected void onNotificationOpened(Context context, String s, String s1, String value) {

        LogUtil.e(REC_TAG, "onNotificationOpened" + s + "  " + s1 + "  " + value);

        try {

            if (TextUtils.isEmpty(value)) {
                return;
            }
            JSONObject jsonObject = new JSONObject(value);

            String type = "";

            if (jsonObject.has("type"))
                type = jsonObject.getString("type");

            if (TextUtils.isEmpty(type)) {
                return;
            }
            switch (type) {
                case SCORE:
//                    context.startActivity(new Intent(context, VendorScoreDetailActivity.class));
                    Intent VendorScoreDetailActivityintent = new Intent(context, VendorScoreDetailActivity.class);
                    VendorScoreDetailActivityintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    VendorScoreDetailActivityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(VendorScoreDetailActivityintent);
                    break;
                case VENDER_MESSAGE:
//                    context.startActivity(new Intent(context, MsgDetailActivity.class).putExtra(Constant.KEY.TYPE, -1));
                    Intent MsgDetailActivityintent = new Intent(context, MsgDetailActivity.class);
                    MsgDetailActivityintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MsgDetailActivityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MsgDetailActivityintent.putExtra(Constant.KEY.TYPE, "1");
                    context.startActivity(MsgDetailActivityintent);
                    break;
                case MESSAGE:
//                    Intent AllMsgActivityIntent = new Intent(context, AllMsgActivity.class);
//                    context.startActivity(AllMsgActivityIntent);
                    Intent intent1 = new Intent(context, AllMsgActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    break;
                case DRAW_LOG:
                    if (jsonObject.has("value")) {
                        Intent intent = new Intent(context, LotteryWebView.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constant.KEY.COMMENT_TYPE, "2");
                        String valueString = jsonObject.getString("value");
                        intent.putExtra(Constant.KEY.URL, valueString);
                        intent.putExtra(Constant.REQUEST.KEY.ACT_ID, "0");
                        context.startActivity(intent);
                    } else {
                        ToastUtil.showLongToast("请稍后再试");
                    }

                    break;
                case ACTIVE:
                    if (jsonObject.has("value")) {
                        Intent ActiveWebViewIntent = new Intent(context, ActiveWebViewActivity.class);
                        ActiveWebViewIntent.putExtra(Constant.KEY.ITEM_ID, jsonObject.getString("value"));
                        ActiveWebViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActiveWebViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(ActiveWebViewIntent);
                    } else {
                        ToastUtil.showLongToast("请稍后再试");
                    }

                    break;
                case BOOTH:
                    if (jsonObject.has("value")) {
                        Intent MyBoothDetailActivityIntent = new Intent(context, MyBoothDetailActivity.class);
                        MyBoothDetailActivityIntent.putExtra(Constant.KEY.ORDER_ID, jsonObject.getString("value"));
                        MyBoothDetailActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MyBoothDetailActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(MyBoothDetailActivityIntent);
                    } else {
                        ToastUtil.showLongToast("请稍后再试");
                    }

                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showLongToast("请稍后再试");
        }

    }

}
