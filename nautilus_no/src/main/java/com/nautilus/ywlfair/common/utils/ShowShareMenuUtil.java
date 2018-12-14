package com.nautilus.ywlfair.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ShowShareMenuUtil implements View.OnClickListener {
    private Dialog shareMenuDialog;

    private static ShowShareMenuUtil instance;

    private Activity mContext;

    private static RelativeLayout toWeixin,
            toQqZone, toWeixinCircle, toQQ, toDouban, toSina;


    private ActiveShareInfo activeShareInfo;

    public static ShowShareMenuUtil getInstance() {
        if (instance == null) {

            instance = new ShowShareMenuUtil();
        }

        return instance;
    }

    /**
     * @return void 返回类型
     * @Title: initDialog
     */
    public void initShareMenuDialog(Activity mContext, ActiveShareInfo activeShareInfo) {

        if (mContext == this.mContext && shareMenuDialog != null) {

            if (!shareMenuDialog.isShowing()) {

                shareMenuDialog.show();

            }

            return;
        }

        this.mContext = mContext;

        this.activeShareInfo = activeShareInfo;

        View view = View.inflate(mContext, R.layout.share, null);
        /* 设置透明度 */
        // view.getBackground().setAlpha(100);
        shareMenuDialog = new Dialog(mContext, R.style.share_dialog);
        /* 设置视图 */
        shareMenuDialog.setContentView(view);
        /* 设置宽 高 */
        shareMenuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = shareMenuDialog.getWindow();

        shareMenuDialog.setCanceledOnTouchOutside(true);
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);
        /* 拿到Dialog里面的图标 并添加监听 */
        toWeixin = (RelativeLayout) view
                .findViewById(R.id.share_to_weixin);
        toQqZone = (RelativeLayout) view
                .findViewById(R.id.share_to_qq_zone);
        toSina = (RelativeLayout) view
                .findViewById(R.id.share_to_sina);
        toWeixinCircle = (RelativeLayout) view
                .findViewById(R.id.share_to_weixin_circle);
        toQQ = (RelativeLayout) view
                .findViewById(R.id.share_to_qq);

        toDouban = (RelativeLayout) view
                .findViewById(R.id.share_to_douban);

        TextView shareCancel = (TextView) view.findViewById(R.id.share_cancel);
        toWeixin.setOnClickListener(this);
        toSina.setOnClickListener(this);
        toWeixinCircle.setOnClickListener(this);
        toQQ.setOnClickListener(this);
        shareCancel.setOnClickListener(this);
        toQqZone.setOnClickListener(this);
        toDouban.setOnClickListener(this);

        shareMenuDialog.show();
    }

    public static void setWeixinCircleGon() {
        toWeixinCircle.setVisibility(View.GONE);
    }

    public static void setQQZone() {
        toQqZone.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.share_to_weixin:
                // 分享到微信好友
                ShareUtil
                        .shareToWechat(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_weixin_circle:
                // 分享到微信朋友圈
                ShareUtil
                        .shareToWxCircle(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_qq:
                // 分享到QQ
                ShareUtil
                        .shareToQQ(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_sina:
                // 分享到新浪微博
                ShareUtil
                        .shareToSina(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl(), "");
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_qq_zone:
                // 分享到QQ空间
                ShareUtil
                        .shareToQZone(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl(), "");
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;

            case R.id.share_to_douban:
                ShareUtil
                        .shareToDouban(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_cancel:
                // 取消分享
                shareMenuDialog.cancel();
                break;
        }

    }
}
