package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;

import com.nautilus.ywlfair.R;

public class CustomGridView extends GridView {
	public CustomGridView(Context context) {
		super(context);
	}

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int gridWidth = getWidth();
        int spacing = getResources().getDimensionPixelSize(R.dimen.gridview_horizontal_spacing);
        int numColumns = getNumColumns();
        gridWidth = gridWidth - spacing * (numColumns * 2 + 2);
		setColumnWidth(gridWidth / numColumns);
		setHorizontalSpacing(spacing);
		setSelector(new ColorDrawable(Color.TRANSPARENT));
		setVerticalSpacing(spacing);
		setPadding(spacing, spacing, spacing, spacing);
		super.onDraw(canvas);
	}
}
