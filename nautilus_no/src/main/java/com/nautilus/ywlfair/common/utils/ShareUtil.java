package com.nautilus.ywlfair.common.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.DoubanShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONObject;

import java.io.File;

/**
 * 分享工具类 Created by dingying on 2015/7/1.
 */
public class ShareUtil {

    public static UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    /**
     * 分享到QQ
     *
     * @param context
     * @param title
     * @param content
     * @param imageUrl
     * @param targetUrl
     */
    public static void shareToQQ(Activity context, String title,
                                 String content, String imageUrl, String targetUrl) {

        setQQssoHandler(context);

        QQShareContent qqShareContent = new QQShareContent();

        if (imageUrl.startsWith("http")) {

            qqShareContent.setTitle(title);

            qqShareContent.setShareContent(content);

            qqShareContent.setShareMedia(new UMImage(context, imageUrl));

            if (!TextUtils.isEmpty(targetUrl)) {

                qqShareContent.setTargetUrl(targetUrl);

            } else {
                qqShareContent.setTargetUrl("http://www.ywl.me");
            }

        } else {

            qqShareContent.setShareMedia(new UMImage(context, new File(imageUrl)));
        }

        mController.setShareMedia(qqShareContent);

        toShare(SHARE_MEDIA.QQ, context);
    }

    /**
     * 分享到QQ空间
     */
    public static void shareToQZone(Activity context, String title,
                                    String content, String imageUrl, String targetUrl, String videoUrl) {

        QZoneShareContent qZoneShareContent = new QZoneShareContent();

        qZoneShareContent.setTitle(title);

        qZoneShareContent.setShareContent(content);

        if (!TextUtils.isEmpty(targetUrl)) {

            qZoneShareContent.setTargetUrl(targetUrl);

        }

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl.startsWith("http")) {

                qZoneShareContent.setShareImage(new UMImage(context, imageUrl));

            } else {
                qZoneShareContent.setShareImage(new UMImage(context, new File(imageUrl)));
            }
        }

        mController.setShareMedia(qZoneShareContent);
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
                "1104740881", "G7FaHeR4jJ7NKu6i");
        qZoneSsoHandler.addToSocialSDK();

        toShare(SHARE_MEDIA.QZONE, context);
    }

    /**
     * 分享到微信
     */
    public static void shareToWechat(Activity context, String title,
                                     String content, String imageUrl, String targetUrl) {

        setWeixinSsoHandler(context);

        WeiXinShareContent weixinContent = new WeiXinShareContent();

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl.startsWith("http")) {

                weixinContent.setShareContent(content);

                weixinContent.setTitle(title);

                if (!TextUtils.isEmpty(targetUrl)) {

                    weixinContent.setTargetUrl(targetUrl);

                } else {

                    weixinContent.setTargetUrl("http://www.ywl.me");
                }

                weixinContent.setShareMedia(new UMImage(context, imageUrl));
            } else {
                weixinContent.setShareMedia(new UMImage(context, new File(imageUrl)));
            }
        }
        mController.setShareMedia(weixinContent);

        toShare(SHARE_MEDIA.WEIXIN, context);
    }

    /**
     * 分享到微信朋友圈
     */
    public static void shareToWxCircle(Activity context, String title,
                                       String content, String imageUrl, String targetUrl) {

        UMWXHandler wxCircleHandler = new UMWXHandler(context,
                "wxf61bb23ed791fe9e", "6a54eb67e31276142fd680857ca13acb");

        wxCircleHandler.showCompressToast(false);

        wxCircleHandler.setToCircle(true);

        wxCircleHandler.addToSocialSDK();

        CircleShareContent circleMedia = new CircleShareContent();

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.startsWith("http")) {

                circleMedia.setShareContent(content);

                circleMedia.setTitle(title);

                circleMedia.setShareImage(new UMImage(context, imageUrl));
            } else {
                circleMedia.setShareImage(new UMImage(context, new File(imageUrl)));
            }
        }

        if (!TextUtils.isEmpty(targetUrl)) {

            circleMedia.setTargetUrl(targetUrl);
        }

        mController.setShareMedia(circleMedia);

        toShare(SHARE_MEDIA.WEIXIN_CIRCLE, context);

    }

    /**
     * 分享到新浪微博
     */
    public static void shareToSina(Activity context, String title,
                                   String content, String imageUrl, String targetUrl, String videoUrl) {

        SinaShareContent sinaShareContent = new SinaShareContent();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());// SSO 授权
        // 免登录

        sinaShareContent.setTitle(title);

        // 设置分享图片, 参数2为图片的url地址

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl.startsWith("http")) {

                imageUrl = imageUrl.replace("userfiles//", "");

                sinaShareContent.setShareMedia(new UMImage(context, imageUrl));
            } else {
                sinaShareContent.setShareMedia(new UMImage(context, new File(imageUrl)));
            }
        }

        String webPath = "";

        if (!TextUtils.isEmpty(targetUrl)) {

            sinaShareContent.setTargetUrl(targetUrl);

            webPath = targetUrl;

        }

        // 设置分享内容
        String appName = context.getResources().getString(R.string.app_name);

        String header = "#" + appName + "#";

        sinaShareContent.setShareContent(header + content + " " + webPath);

        mController.setShareMedia(sinaShareContent);

        toShare(SHARE_MEDIA.SINA, context);
    }

    public static void shareToDouban(Activity context, String title, String content, String imageUrl, String targetUrl) {
        DoubanShareContent doubanShareContent = new DoubanShareContent();

        doubanShareContent.setTitle(title);

        // 设置分享图片, 参数2为图片的url地址
        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl.startsWith("http")) {

                doubanShareContent.setShareImage(new UMImage(context, imageUrl));
            } else {
                doubanShareContent.setShareImage(new UMImage(context, new File(imageUrl)));
            }
        }

        if (!TextUtils.isEmpty(targetUrl)) {

            doubanShareContent.setShareContent(content + "#请戳" + targetUrl);

        } else {
            doubanShareContent.setShareContent(content);
        }

        mController.setShareMedia(doubanShareContent);

        toShare(SHARE_MEDIA.DOUBAN, context);
    }

    /**
     * 分享接口回调
     *
     * @param type
     * @param context
     */
    private static void toShare(final SHARE_MEDIA type, Activity context) {
        // 参数1为Context类型对象， 参数2为要分享到的目标平台， 参数3为分享操作的回调接口
        mController.getConfig().closeToast();
        mController.postShare(context, type,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        // 开始分享
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,
                                           SocializeEntity entity) {

                        if (platform.equals(type)) {

                            if (eCode == 200) {
                                // if(type.equals(SHARE_MEDIA.SINA))
                                Toast.makeText(MyApplication.getInstance(),
                                        "分享成功.", Toast.LENGTH_SHORT).show();
                            } else {
                                String eMsg = "";
                                if (eCode == -101) {
                                    eMsg = "没有授权";
                                } else if (eCode == 40000) {
                                    eMsg = "取消分享";
                                } else {
                                    eMsg = "分享失败";
                                }

                                ToastUtil.showShortToast(eMsg);
                            }
                        }
                    }
                });
    }

    public static void setQQssoHandler(Activity context) {
        UMQQSsoHandler qqSsoHandler;

        qqSsoHandler = new UMQQSsoHandler(context, "1104740881",
                "G7FaHeR4jJ7NKu6i");

        qqSsoHandler.addToSocialSDK();
    }

    public static void setWeixinSsoHandler(Activity context) {

        UMWXHandler wxHandler = new UMWXHandler(context, "wxf61bb23ed791fe9e",
                "6a54eb67e31276142fd680857ca13acb");

        wxHandler.showCompressToast(false);

        wxHandler.addToSocialSDK();
    }
}
