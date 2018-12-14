package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.LeaveTypeAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.entity.bean.event.EventProductType;
import com.nautilus.ywlfair.module.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * 分类第一层 Created by DY on 2015/7/13.
 */
public class SelectSymptomTypeActivity extends BaseActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {

	public static SelectSymptomTypeActivity instance;

	private ListView loadMoreListView;

	private ArrayList<GoodsClassInfo> typeList;

	private String goodsCategory;

	private String typeId;

	private BaseAdapter adapter;

	private Context mContext;

	public enum Mode {
		FIRST, SECOND
	}

	private Mode mode;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_leave_type);

		instance = this;

		typeList = (ArrayList<GoodsClassInfo>) getIntent()
				.getSerializableExtra(Constant.KEY.CLASS_TYPE);

		mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

		if (mode == Mode.SECOND) {
			goodsCategory = getIntent().getStringExtra(
					Constant.KEY.GOODS_CATEGORY);

			typeId = getIntent().getStringExtra(Constant.KEY.TYPE);
		}

		mContext = this;

		init();

	}

	private void init() {

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);

		loadMoreListView = (ListView) findViewById(R.id.lv_class_list);

		adapter = new LeaveTypeAdapter(mContext, typeList);

		loadMoreListView.setAdapter(adapter);

		loadMoreListView.setOnItemClickListener(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long l) {

		if (mode == Mode.FIRST) {

			ArrayList<GoodsClassInfo> childClass = (ArrayList<GoodsClassInfo>) typeList
					.get(position).getChildClassList();

			if (childClass.size() > 0) {

				Intent intent = new Intent(mContext,
						SelectSymptomTypeActivity.class);

				intent.putExtra(Constant.KEY.CLASS_TYPE, childClass);

				intent.putExtra(Constant.KEY.MODE, Mode.SECOND);

				intent.putExtra(Constant.KEY.GOODS_CATEGORY,
						typeList.get(position).getClassName());

				intent.putExtra(Constant.KEY.TYPE, typeList.get(position)
						.getId() + ",");

				startActivity(intent);

			} else {

				EventProductType eventProductType = new EventProductType(
						typeList.get(position).getClassName(), typeList.get(
								position).getId()
								+ "");

				EventBus.getDefault().post(eventProductType);

				finish();
			}
		} else {

            ArrayList<GoodsClassInfo> childClass = (ArrayList<GoodsClassInfo>) typeList
                    .get(position).getChildClassList();

            if (childClass != null && childClass.size() > 0) {

                Intent intent = new Intent(mContext,
						ChooseSymptomActivity.class);

				intent.putExtra(Constant.KEY.CLASS_TYPE, childClass);

				intent.putExtra(Constant.KEY.GOODS_CATEGORY, goodsCategory
                        + "," + typeList.get(position).getClassName());

				intent.putExtra(Constant.KEY.TYPE,
                        typeId + typeList.get(position).getId());

				startActivity(intent);
            }else{
                String category = goodsCategory
                        + "," + typeList.get(position).getClassName();

                String ids = typeId + typeList.get(position).getId();

                EventProductType eventProductType = new EventProductType(
                        category, ids);

                EventBus.getDefault().post(eventProductType);

                Intent intent = new Intent(mContext, GoodsInfoActivity.class);

                startActivity(intent);
            }

		}

	}

	@Override
	public void onClick(View view) {

		finish();
	}
}
