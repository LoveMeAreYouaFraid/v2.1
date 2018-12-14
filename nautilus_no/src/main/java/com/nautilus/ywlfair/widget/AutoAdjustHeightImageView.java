package com.nautilus.ywlfair.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;

/**
 * 可根据设定的宽高比自动设置高度的ImageView
 */
@SuppressLint("DrawAllocation")
public class AutoAdjustHeightImageView extends ImageView {
	private int imageWidth;
	private int imageHeight;
	private double aspectRatio;

	public AutoAdjustHeightImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray appearance;
		appearance = context.obtainStyledAttributes(attrs,
				R.styleable.AutoAdjustHeightImageView);
		aspectRatio = appearance.getFloat(
				R.styleable.AutoAdjustHeightImageView_aspectRatio, 0);
		if (aspectRatio == 0) {
			getImageSize();
		}
		appearance.recycle();
	}

	private void getImageSize() {
		Drawable drawable = this.getDrawable();
		if (drawable == null)
			return;
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		imageWidth = bitmap.getWidth();
		imageHeight = bitmap.getHeight();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height;
		if (aspectRatio == 0) {
			height = width * imageHeight / imageWidth;
		} else {
			height = (int) (width / aspectRatio);
		}
		this.setMeasuredDimension(width, height);
	}

    /**
     * 获取宽高比
     *
     * @return
     */
	public double getAspectRatio() {
		return aspectRatio;
	}

    /**
     * 设置宽高比
     *
     * @param aspectRatio
     */
	public void setAspectRatio(double aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
	
}
