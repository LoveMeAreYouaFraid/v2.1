package com.nautilus.ywlfair.adapter;

import java.io.File;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.widget.XiangCeActivity;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

/**
 * 选择照片相册界面适配器
 */
public class AlbumGridAdapter extends BaseAdapter  {

	LayoutInflater inflater;
    Context context;
    List<Uri> main_datas;
    int itemheight;
	private int color;
	private String[] projection;

	private Uri mUri;

	private ContentResolver contentResolver;
	private DisplayImageOptions mOptions;

	public AlbumGridAdapter(Context context, List<Uri> main_datas, int itemheight) {

		this.itemheight = itemheight;
		this.context = context;
		this.main_datas = main_datas;
		projection = new String[]{Thumbnails._ID,Thumbnails.IMAGE_ID,Thumbnails.DATA};
		mUri = Thumbnails.getContentUri("external");
		contentResolver = MyApplication.getInstance().getContentResolver();
		inflater = LayoutInflater.from(context);
		color = Color.parseColor("#F0F1F3");

		mOptions = new DisplayImageOptions.Builder()
//				.showImageOnLoading(0)	// image在加载过程中，显示的图片
//				.showImageForEmptyUri(0) // empty URI时显示的图片
//				.showImageOnFail(R.drawable.df_avatar) // 不是图片文件 显示图片
				.considerExifParams(true)
				.resetViewBeforeLoading(true) // default
				.cacheInMemory(true) // default 不缓存至内存
				.imageScaleType(ImageScaleType.EXACTLY)	// default
				.cacheOnDisk(true) // default 不缓存至手机SDCard
				.build();

	}

	class ViewHolder {
		ImageView image;
		ImageView check;
	}

	@Override
	public int getCount() {
		return main_datas.size();
	}

	@Override
	public Object getItem(int position) {			
		return main_datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {			
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.album_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(viewHolder);			
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
//			viewHolder.image.setImageBitmap(null);
			viewHolder.image.setBackgroundColor(color);
		}
		
		if (((XiangCeActivity)context).booleanArray.get(position)) {
			viewHolder.check.setImageResource(R.drawable.zhaopian_xuanze);
		}else {
			viewHolder.check.setImageResource(R.drawable.zhaopian_weixuanze);
		}
		
		Uri uri = main_datas.get(position);

		String idStr = uri.toString().substring(uri.toString().lastIndexOf("/") + 1, uri.toString().length()) + "";

		int id = Integer.parseInt(idStr);

		if (uri != null) {

			if (viewHolder.image.getTag(R.id.uri) == null || (viewHolder.image.getTag(R.id.uri) != null && !uri.toString().equals(viewHolder.image.getTag(R.id.uri).toString()))) {
				viewHolder.image.setImageBitmap(null);
				viewHolder.image.setBackgroundColor(color);

				Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, Thumbnails.IMAGE_ID+" = "+id, null, null);
				long thumbnails_id = 0;
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					thumbnails_id  =  cursor.getLong(cursor.getColumnIndex(Thumbnails._ID));
					long _id =  cursor.getLong(cursor.getColumnIndex(Thumbnails.IMAGE_ID));
					String mPath =  cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
					if (!cursor.isClosed()) {
						cursor.close();
					}
					File file = new File(mPath);
					if (file.exists()) {
						Uri mThumbnailsUri = Uri.withAppendedPath(mUri, "" + thumbnails_id);
						ImageLoader.getInstance().displayImage(mThumbnailsUri.toString(), viewHolder.image, mOptions);
//						ImageUtil.setItemImageView(viewHolder.image, mThumbnailsURI.toString(), 0, ImageScaleType.EXACTLY, false);
						viewHolder.image.setTag(R.id.uri, uri);

					}else {
						getNewThumbnails(viewHolder.image, id, thumbnails_id);
					}
				}
				else {
					getNewThumbnails(viewHolder.image, id, thumbnails_id);
				}
			}
		}

		GridView.LayoutParams params  = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.height = itemheight;

		convertView.setLayoutParams(params);

		return convertView;
	}


    /**
	 *
	 * @param image
	 * @param id
	 * @param thumbnails_id
	 */
	public void getNewThumbnails(ImageView image, final int id, long thumbnails_id) {

		String uri = "drawable://" + R.drawable.df_avatar;
		if (uri != null) {
			File file = DiskCacheUtils.findInCache(uri, ImageLoader.getInstance().getDiskCache());
			if (file != null && file.exists()) {
                LogUtil.i("删除缓存文件", "删除一个缓存文件");
				file.delete();
			}
			BitmapProcessor bitmapProcessor = new BitmapProcessor() {
				@Override
				public Bitmap process(Bitmap inBitmap) {
					if (inBitmap != null && !inBitmap.isRecycled()) {
						inBitmap.recycle();
					}
					Options options = new Options();
					return Thumbnails.getThumbnail(contentResolver, id, Thumbnails.MINI_KIND, options);
				}
			};

			ImageLoadUtils.setItemImageView(image, uri, 0, ImageScaleType.EXACTLY, bitmapProcessor, false);
		}
	}

}