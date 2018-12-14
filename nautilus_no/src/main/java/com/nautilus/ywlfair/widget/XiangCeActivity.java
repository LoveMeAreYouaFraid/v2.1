package com.nautilus.ywlfair.widget;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.AlbumGridAdapter;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;

/**
 * 显示手机上图片供用户选择
 */
public class XiangCeActivity extends Activity implements OnClickListener{

	private AlbumGridAdapter albumGridAdapter;

	private float scale;

	private ArrayList<Uri> uris;

	private ArrayList<Uri> checkedUris = new ArrayList<Uri>();

	private int itemheight;

	public SparseBooleanArray booleanArray = new SparseBooleanArray();

	private Button wancheng;

	private Button yulan;

	private GridView mGridView;

	private int canSelectCount;

	protected Context mContext;

	private String[] projection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_xiangce_gridview);

		scale = Common.getInstance().getScaledDensity();

		mContext = this;

//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

		projection = new String[]{Media._ID,/*Thumbnails.IMAGE_ID,*/Media.DATA};

		//可以选择的图片的数量
		canSelectCount = getIntent().getIntExtra(Constant.KEY.PIC_NUM, 9);

		initTopView();

		initUI();

		wancheng.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData();
			}
		}, 100);
		
	}

	/**
	 * 
	 */
	public void initUI() {
		wancheng = (Button) findViewById(R.id.wancheng);
		wancheng.setOnClickListener(this);
		yulan = (Button) findViewById(R.id.yulan);
		yulan.setOnClickListener(this);
		
		
		mGridView = (GridView) findViewById(R.id.pull_refresh_grid);
//		mGridView.setOnScrollListener(new PauseOnScrollListener(MyApplication.getInstance().getImageLoaderInstance(), true, false));

		int columnnum = 3;
		mGridView.setBackgroundColor(Color.parseColor("#ffffff"));
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setNumColumns(columnnum);
		mGridView.setVerticalSpacing((int) (2*scale));
		mGridView.setHorizontalSpacing((int) (2*scale));
		mGridView.setPadding((int)(2*scale), (int)(2*scale), (int)(2*scale), (int)(2*scale));
    	itemheight = (int) (Common.getInstance().getScreenWidth()-(columnnum+1)*5*scale)/columnnum;
    	
    	uris = new ArrayList<Uri>();
//    	imageIds = new ArrayList<Long>();
		albumGridAdapter = new AlbumGridAdapter(this, uris, itemheight);
		mGridView.setAdapter(albumGridAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> gridview, View view, int position, long id) {
				
				Uri masterMapUri = uris.get(position);
				
				if (checkedUris.size() == canSelectCount) {
					if (checkedUris.indexOf(masterMapUri) == -1) {
						Toast.makeText(mContext, "本次只能选择" + canSelectCount + "张图片", Toast.LENGTH_SHORT).show();
						return;	
					}
				}
				
				int visiblePos = mGridView.getFirstVisiblePosition();
				View  convertView = mGridView.getChildAt(position - visiblePos);
				ImageView check = (ImageView) convertView.findViewById(R.id.check);
				if (booleanArray.get(position)) {
					check.setImageResource(R.drawable.zhaopian_weixuanze);
					booleanArray.put(position, false);
					checkedUris.remove(masterMapUri);
				} else {
					check.setImageResource(R.drawable.zhaopian_xuanze);
					booleanArray.put(position, true);
					if (!checkedUris.contains(masterMapUri)) {
						checkedUris.add(masterMapUri);
					}	
				}
				convertView.invalidate();
				if (checkedUris.size() > 0) {
					 
					TextView count = (TextView) findViewById(R.id.count);
					count.setText(checkedUris.size()+"");
					count.setVisibility(View.VISIBLE);
					
					wancheng.setBackgroundResource(R.drawable.wancheng_enable);
					wancheng.setTextColor(Color.parseColor("#ffffff"));
					wancheng.setEnabled(true);
					
					yulan.setBackgroundResource(R.drawable.yulan_enable);
					yulan.setTextColor(Color.parseColor("#ffffff"));
					yulan.setEnabled(true);
				} else { 
					TextView count = (TextView) findViewById(R.id.count);
					count.setVisibility(View.GONE);
					
					wancheng.setBackgroundResource(R.drawable.wancheng_disenable);
					wancheng.setTextColor(getResources().getColor(R.color.gray));
					wancheng.setEnabled(false);
					
					yulan.setBackgroundResource(R.drawable.yulan_disenable);
					yulan.setTextColor(getResources().getColor(R.color.gray));
					yulan.setEnabled(false);
				}
			}
		});
	}

	/**
	 * 获取相册数据
	 */
	public void getData() {
        ProgressDialog.getInstance().show(XiangCeActivity.this, "正在加载图片");

		new AsyncQueryHandler(getContentResolver()) {

			@Override
			protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

				Uri mUri = Media.getContentUri("external");

				int i = 0;

				if (cursor != null) {

					int _idColumn = cursor.getColumnIndex(Thumbnails._ID);

//					int imageIdColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);

					int pathColumn = cursor.getColumnIndex(Thumbnails.DATA);

//					final Options options = new Options();
//					options.inSampleSize = 10;
					File file = null;


					while(cursor.moveToNext()) {

						int _id =  cursor.getInt(_idColumn);

//						final long image_id =  cursor.getLong(imageIdColumn);

						String path =  cursor.getString(pathColumn);

						file = new File(path);

						if (file.exists()) {

							Uri uri = Uri.withAppendedPath(mUri, "" + _id);

							uris.add(uri);

							booleanArray.put(i, false);

							i++;
						}
						file = null;	
					}

					cursor.close();

			        Message message = handler.obtainMessage();

			        message.what = 1;

			        message.sendToTarget();

				}else {
                    ProgressDialog.getInstance().cancel();
				}
			}
		}.startQuery(0, null, Media.EXTERNAL_CONTENT_URI, projection, null, null, Media.DATE_MODIFIED + " DESC");
	}

	public void initTopView() {

        View topBarBack= findViewById(R.id.tv_top_bar_back);

        topBarBack.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_bar_back:
			finish();
			break;
		case R.id.wancheng:	
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra(Constant.KEY.URIS, checkedUris);
			    setResult(Activity.RESULT_OK, intent);	
			    finish();
			break;
		case R.id.yulan:
			if (checkedUris.size() > canSelectCount) {
				Toast.makeText(XiangCeActivity.this, "本次只能选择"+canSelectCount+"张图片", Toast.LENGTH_LONG).show();
				return;
			}
			Intent intent2 = new Intent(XiangCeActivity.this,YuLanActivity.class);
			
			intent2.putParcelableArrayListExtra(Constant.KEY.URIS, checkedUris);
			
			startActivityForResult(intent2, Constant.REQUEST_CODE.RC_SELECT_AVATAR_FROM_ALBUM);
			
			break;
	      }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constant.REQUEST_CODE.RC_SELECT_AVATAR_FROM_ALBUM) { 
				if (data != null) {
					ArrayList<Uri> uris = data.getParcelableArrayListExtra(Constant.KEY.URIS);	
					if (uris != null) {
						for (int i = 0; i < checkedUris.size(); i++) {
							if (uris.size() > 0) {
								 for (int j = 0; j < uris.size(); j++) {
									   Uri tempUri = checkedUris.get(i);
									   if (uris.indexOf(tempUri) == -1) {
										   booleanArray.put(this.uris.indexOf(tempUri), false);
									   }
								   }
							}else {
								booleanArray.put(this.uris.indexOf(checkedUris.get(i)), false);
							}
						  }
						boolean flag = true;
						if (uris.size() != checkedUris.size()) {
							   flag  = true;
						   }else {
							   flag = false;
						   }
							checkedUris.clear();
							checkedUris.addAll(uris);
							if (checkedUris.size() > 0) {
								TextView count = (TextView) findViewById(R.id.count);
								count.setText(checkedUris.size()+"");
								count.setVisibility(View.VISIBLE);
							}else {
								TextView count = (TextView) findViewById(R.id.count);
								count.setText("");
								count.setVisibility(View.GONE);
							}
							if (flag) {
								albumGridAdapter = new AlbumGridAdapter(this, this.uris,itemheight);
								mGridView.setAdapter(albumGridAdapter);
							}
					}
				}
			}
		}
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				albumGridAdapter.notifyDataSetChanged();
                ProgressDialog.getInstance().cancel();
				break;
			default:
				break;
			}
		}		
	};
}
