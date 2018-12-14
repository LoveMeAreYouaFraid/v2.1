/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.ScanCodeInfo;
import com.nautilus.ywlfair.module.active.LotteryWebView;
import com.nautilus.ywlfair.module.market.ScanCodePayActivity;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;

import java.io.IOException;
import java.lang.reflect.Field;

import zxing.camera.CameraManager;
import zxing.decode.DecodeThread;
import zxing.utils.BeepManager;
import zxing.utils.CaptureActivityHandler;
import zxing.utils.InactivityTimer;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.VENDOR_ID;

/**
 * 扫描二维码Activity
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Context mContext;
    private JsonUtil<ScanCodeInfo> jsonUtil;
    private ScanCodeInfo info;
    private String actid;
    private String Type = "0";


    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContext = this;
        info = new ScanCodeInfo();

        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.REQUEST.KEY.TYPE))) {
            Type = getIntent().getStringExtra(Constant.REQUEST.KEY.ACT_ID);
        }

        jsonUtil = new JsonUtil<>();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation ta1 = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT,
                0.9f);
        ta1.setDuration(1500);
        ta1.setStartTime(0);
        ta1.setRepeatCount(Integer.MAX_VALUE);
        ta1.setRepeatMode(Animation.REVERSE);
        scanLine.startAnimation(ta1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            initCamera(scanPreview.getHolder());
        } else {
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        finish();
        super.onRestart();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     * 扫面完成 返回数据
     */

    private String rawString;

    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        rawString = rawResult.getText();
        if (Type.equals("1")) {

            actWeb(rawString);

        } else {
            if (rawString.contains("offline/init")) {//线下支付
                Offline(rawString);

            } else if (rawString.contains("actmain/user")) {//购票

                if (MyApplication.getInstance().getUserType() == 0) {
                    Tickets(rawString);
                } else {
                    ToastUtil.showLongToast("请切换至普通用户身份后再购票");
                    finish();
                }

            } else if (rawString.contains("actmain/draw?actId")) {//抽奖
                Lottery(rawString);
            }else {
                ToastUtil.showShortToast("现只支持鹦鹉螺抽奖，购票，线下支付，sorry baby");
                finish();
            }

        }


        LogUtil.e("扫描", rawResult.getText());


    }

    private void actWeb(String rawResult) {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";
        String key = Constant.KEY.TWO_CODE_INFO + userId + actid;
        String url = rawResult + "&userId=" + userId + "&deviceId=" + Common.getInstance().getIMSI();
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY.URL, url);
        String result = rawResult;
        info.setUrl(result);
        info.setData(System.currentTimeMillis());

        LogUtil.e("result.getText()", result);
        LogUtil.e("key", key);
        if (result.contains(MyApplication.getInstance().getActDrawUrl()) &&
                StringUtils.getUrlParameter(rawResult, "actId").equals(actid)) {

            PreferencesUtil.putString(key, jsonUtil.bean2Json(info));

            setResult(RESULT_OK, intent);

            finish();
        } else {
            Toast.makeText(mContext, "此二维码非活动现场指定二维码", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开失败，您可以在设置里找到鹦鹉螺APP，然后打开访问相机的权限");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void Lottery(String rawString) {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";
        String key = Constant.KEY.TWO_CODE_INFO + userId + StringUtils.getUrlParameter(rawString, "actId");
        String url = rawString + "&userId=" + userId + "&deviceId=" + Common.getInstance().getIMSI();

        info.setUrl(rawString);
        info.setData(System.currentTimeMillis());

        PreferencesUtil.putString(key, jsonUtil.bean2Json(info));

        LotteryWebView.startLotteryWebView(mContext, "1", url, StringUtils.getUrlParameter(rawString, "actId"));

        finish();
    }

    private void Offline(String rawString) {
        startActivity(new Intent(mContext, ScanCodePayActivity.class).putExtra(VENDOR_ID, StringUtils.getUrlParameter(rawString, "vendorId")));

        finish();

    }

    private void Tickets(String rawString) {

        String s = "http://m.yingwuluo.cc/actmain/user/";

        s = rawString.substring(s.length());
        String activityId = "";
        for (int i = 0; i < s.length(); i++) {
            String b = s.substring(i, i + 1);
            if (b.equals("?")) {
                break;
            } else {
                activityId = activityId + b;
            }
        }
        LogUtil.e("123", "sb=============" + activityId);
        Intent intent = new Intent(mContext, ActiveWebViewActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString(Constant.KEY.ITEM_ID, activityId);

        intent.putExtras(bundle);

        startActivity(intent);

    }
}