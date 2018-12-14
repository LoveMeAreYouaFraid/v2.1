package com.nautilus.ywlfair.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.entity.bean.VendorLevel;

import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public class CustomLineView extends View {

    private int firstColor;

    private int secondColor;

    private int circleColor;

    private int levelNameLightColor;

    private int levelNameColor;

    private int mCircleWidth;

    private Paint mPaint;

    private float progress = 0;

    private float maxProgress;

    private int changeSpeedPosition;

    private List<VendorLevel> list;

    private OnLineScrollListener lineScrollListener;

    private int screenWidth;

    private float radius;

    private float y = 115;

    private Paint textPaint;

    private Paint whitePaint;

    private Paint circlePaint;

    private float perAngle;

    private double perNumber;

    private Bitmap bitmap;

    public CustomLineView(Context context) {
        this(context, null);
    }

    public CustomLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomLineView, defStyleAttr, 0);

        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.CustomLineView_firstColor:
                    firstColor = typedArray.getColor(attr, Color.GREEN);
                    break;

                case R.styleable.CustomLineView_secondColor:
                    secondColor = typedArray.getColor(attr, Color.RED);
                    break;

                case R.styleable.CustomLineView_circleColor:
                    circleColor = typedArray.getColor(attr, Color.WHITE);
                    break;

                case R.styleable.CustomLineView_levelNameLightColor:
                    levelNameLightColor = typedArray.getColor(attr, Color.RED);
                    break;

                case R.styleable.CustomLineView_levelNameColor:
                    levelNameColor = typedArray.getColor(attr, Color.GRAY);
                    break;

                case R.styleable.CustomLineView_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }

        typedArray.recycle();

        initPaints();

    }

    public void refreshProgress(double standard){

        initValues(standard);

        this.progress = maxProgress;

        invalidate();

    }

    public void startDraw(List<VendorLevel> list, double standard) {

        this.list = list;

        initValues(standard);

        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", 0, (int) maxProgress);

        int duration = 1500 * changeSpeedPosition == 0 ? 1500 : 1500 * changeSpeedPosition;

        animator.setDuration(duration);

        animator.start();

    }

    private void initValues(double standard){
        for (int i = 0; i < list.size(); i++) {

            VendorLevel vendorLevel = list.get(i);

            if (vendorLevel.getUpperLimit() > standard) {

                changeSpeedPosition = i;

                float center = radius * (5 + 4 * i);

                double num = standard - vendorLevel.getLowerLimit();

                float per = (2 * radius) / (float) (vendorLevel.getUpperLimit() - vendorLevel.getLowerLimit());

                maxProgress = (int) (center - radius + num * per);

                perNumber = standard / (double) maxProgress;

                break;
            }
        }
    }

    private void initPaints() {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;

        radius = screenWidth / 10f;

        perAngle = 90 / radius;

        originalX = 5 * radius;

        mPaint = new Paint();

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);

        circlePaint.setStrokeWidth(mCircleWidth);

        mPaint.setAntiAlias(true);

        textPaint = new Paint();

        textPaint.setAntiAlias(true);

        textPaint.setTextSize(radius * 4 / 10);

        textPaint.setFakeBoldText(true);

        textPaint.setTextAlign(Paint.Align.CENTER);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_current_level);

        y = radius + mCircleWidth;

        whitePaint = new Paint();

        whitePaint.setAntiAlias(true);

        mPaint.setStrokeWidth(radius / 10);

        mPaint.setStyle(Paint.Style.STROKE);

        circlePaint.setStyle(Paint.Style.STROKE);

        whitePaint.setStrokeWidth(radius - mCircleWidth);

        whitePaint.setColor(circleColor);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(screenWidth * 3 + screenWidth * 2 / 5, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (list != null) {
            paintFirst(canvas);

            paintSecond(canvas);
        }

    }

    private void paintFirst(Canvas canvas) {

        mPaint.setColor(firstColor);

        circlePaint.setColor(firstColor);

        textPaint.setColor(levelNameColor);

        for (int i = 0; i < list.size(); i++) {

            float center = radius * (5 + 4 * i);

            if (i == 0) {

                int firstLineStart = 0;

                float firstLineEnd = center - radius;

                canvas.drawLine(firstLineStart, y, firstLineEnd, y, mPaint);


            } else {

                float firstLineStart = center - 3 * radius;

                float firstLineEnd = center - radius;

                canvas.drawLine(firstLineStart, y, firstLineEnd, y, mPaint);

            }

            if (i == list.size() - 1) {//最后一个圈  还要收尾
                float lineStart = center + radius;

                float lineEnd = center + 5 * radius;

                canvas.drawLine(lineStart, y, lineEnd, y, mPaint);
            }

            canvas.drawCircle(center, y, radius + mCircleWidth / 2, circlePaint);

            textPaint.setFakeBoldText(true);

            drawTextInfo(canvas, list.get(i).getLevelName(), center, y, levelNameColor);

            textPaint.setFakeBoldText(false);

//            if (i > currentPosition) {
//
//                textPaint.setTextSize(radius * 7 / 20);
//
//                drawTextInfo(canvas, "￥" + StringUtils.getScoreFormat(list.get(i).getLowerLimit()), center, y + radius + radius / 2, Color.WHITE);
//
//                drawTextInfo(canvas, "目标销售额", center, y + radius + radius, Color.WHITE);
//
//                textPaint.setTextSize(radius * 4 / 10);
//            }

        }
    }

    public interface OnLineScrollListener {
        void onLineScroll(float originalX, float scrollX);
    }

    float originalX;

    int currentPosition = 0;

    private void paintSecond(Canvas canvas) {

        mPaint.setColor(secondColor);

        circlePaint.setColor(secondColor);

        canvas.drawLine(0, y, progress, y, mPaint);

        for (int i = 0; i < list.size(); i++) {

            float center = radius * (5 + 4 * i);

            if (i == currentPosition) {

                textPaint.setColor(levelNameLightColor);

                int currentNumber = (int) (progress * perNumber);

                textPaint.setTextSize(radius * 7 / 20);

                drawTextInfo(canvas, "￥" + StringUtils.getScoreFormat(currentNumber), center, y + radius + radius / 2, Color.YELLOW);

                drawTextInfo(canvas, "当前销售额", center, y + radius + radius, Color.WHITE);

                textPaint.setTextSize(radius * 4 / 10);
            } else {

                textPaint.setColor(levelNameColor);

//                if (progress > center - radius) {
//
//                } else {
                    textPaint.setTextSize(radius * 7 / 20);

                    drawTextInfo(canvas, "￥" + StringUtils.getScoreFormat(list.get(i).getLowerLimit()), center, y + radius + radius / 2, Color.WHITE);

                    drawTextInfo(canvas, "目标销售额", center, y + radius + radius, Color.WHITE);

                    textPaint.setTextSize(radius * 4 / 10);
//                }
            }

            if (i == changeSpeedPosition) {

                Rect rect = new Rect((int) (center - radius), (int) (y - radius), (int) (center + radius), (int) (y + radius));

                mPaint.setFilterBitmap(true);

                canvas.drawBitmap(bitmap, null, rect, mPaint);

                drawTextInfo(canvas, list.get(i).getLevelName(), center, y, levelNameLightColor);

            } else {
                canvas.drawCircle(center, y, radius, whitePaint);

                drawTextInfo(canvas, list.get(i).getLevelName(), center, y, levelNameColor);
            }

            if (progress > center - radius) {

                drawRectF(canvas, center, radius + mCircleWidth / 2);

                if (progress > originalX + radius) {

                    if (lineScrollListener != null) {

                        float nextScrollX = 4 * radius;

                        float scrollOriginal = originalX - 5 * radius;

                        lineScrollListener.onLineScroll(scrollOriginal, nextScrollX + scrollOriginal);

                        originalX = nextScrollX + originalX;

                        currentPosition = currentPosition + 1;
                    }
                }
            }
        }
    }

    private void drawRectF(Canvas canvas, float center, float radius) {

        RectF oval = new RectF(center - radius, y - radius, center + radius, y + radius);

        float angle = perAngle * (progress - center + radius);

        float startAngle = 180 - angle;

        canvas.drawArc(oval, startAngle, 2 * angle, false, circlePaint);
    }

    private void drawTextInfo(Canvas canvas, String text, float baseX, float baseY, int color) {
        textPaint.setColor(color);
        // FontMetrics对象
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;

        float offY = fontTotalHeight / 2 - fontMetrics.bottom;

        float newY = baseY + offY;

        canvas.drawText(text, baseX, newY, textPaint);

    }

    public OnLineScrollListener getLineScrollListener() {
        return lineScrollListener;
    }

    public void setLineScrollListener(OnLineScrollListener lineScrollListener) {
        this.lineScrollListener = lineScrollListener;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(int progress) {

        this.progress = progress;

        postInvalidate();
    }

}
