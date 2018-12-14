package com.nautilus.ywlfair.module.vendor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.event.EventProductType;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.module.BaseActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by dingying on 2015/7/13.
 */
public class ChooseSymptomActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;

    private ArrayList<GoodsClassInfo> typeList;

    private Context mContext;

    private SymptomListAdapter mAdapter;
    
    private String goodsCategory;
    
    private String typeId;

    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_symptom);

        typeList = (ArrayList<GoodsClassInfo>) getIntent().getSerializableExtra(Constant.KEY.CLASS_TYPE);
        
        goodsCategory = getIntent().getStringExtra(Constant.KEY.GOODS_CATEGORY);
        
        typeId = getIntent().getStringExtra(Constant.KEY.TYPE);

        mContext = this;

        init();

    }

    private void init() {

        View backImageView = findViewById(R.id.tv_top_bar_back);
        backImageView.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.lv_choose_symptom);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        mAdapter=new SymptomListAdapter(typeList);

        mListView.setAdapter(mAdapter);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String category = goodsCategory + "," + typeList.get(position).getClassName();
				
				String ids = typeId + "," + typeList.get(position).getId();
				
				EventProductType eventProductType = new EventProductType(category, ids);
				
				EventBus.getDefault().post(eventProductType);
				
				Intent intent = new Intent(mContext, GoodsInfoActivity.class);

				startActivity(intent);

                finish();
			}
		});
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_top_bar_back:
                finish();
                break;

        }
    }


    public class SymptomListAdapter extends BaseAdapter {

        private List<GoodsClassInfo> list;

        public SymptomListAdapter(List<GoodsClassInfo> typeList){
            this.list = typeList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            CheckedTextView checkedTextView =
            		(CheckedTextView) LayoutInflater.from(mContext).inflate(R.layout.item_lv_choose_leave_symptom,null);

            checkedTextView.setText(list.get(i).getClassName());

            return checkedTextView;
        }
    }
}
