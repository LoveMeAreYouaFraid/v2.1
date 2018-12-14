package com.nautilus.ywlfair.module.mine.level;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.PrivilegeInfo;
import com.nautilus.ywlfair.entity.bean.VendorLevel;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class PrivilegeGridAdapter extends BaseAdapter {

    private Context mContext;

    private List<PrivilegeInfo> list;

    private String privilegeId;

    private int[] lightIcons = new int[]{R.drawable.level_tag_light,R.drawable.level_tag_light0, R.drawable.level_tag_light1, R.drawable.level_tag_light2,
            R.drawable.level_tag_light3, R.drawable.level_tag_light4, R.drawable.level_tag_light5};

    private int[] norIcons = new int[]{R.drawable.level_tag_nor,R.drawable.level_tag_nor0, R.drawable.level_tag_nor1, R.drawable.level_tag_nor2,
            R.drawable.level_tag_nor3, R.drawable.level_tag_nor4, R.drawable.level_tag_nor5,};

    public PrivilegeGridAdapter(Context context, List<PrivilegeInfo> list, String privilegeId) {

        this.mContext = context;

        this.list = list;

        this.privilegeId = privilegeId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.privilege_grid_item, null);

            viewHold = new ViewHold();

            viewHold.privilegeIcon = (AutoAdjustHeightImageView) convertView.findViewById(R.id.iv_privilege);

            viewHold.imageView = (ImageView) convertView.findViewById(R.id.iv_vendor_level);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        PrivilegeInfo privilegeInfo = list.get(position);

        if(position == list.size() - 1){
            viewHold.privilegeIcon.setBackgroundColor(mContext.getResources().getColor(R.color.gray_fyi));

            viewHold.privilegeIcon.setImageResource(R.drawable.ic_more_privilege);
        }else{
            ImageLoader.getInstance().displayImage(privilegeInfo.getPrivilegeImageUrl(),
                    viewHold.privilegeIcon, ImageLoadUtils.createNoRoundedOptions());

            int privilegeLevel = privilegeInfo.getPrivilegeLevel();

            if (privilegeLevel - 1 < lightIcons.length) {
                if (!TextUtils.isEmpty(privilegeId) &&
                        privilegeId.contains(privilegeInfo.getPrivilegeId() + "")) {

                    viewHold.imageView.setImageResource(lightIcons[privilegeLevel - 1]);

                    viewHold.privilegeIcon.setBackgroundColor(mContext.getResources().getColor(R.color.white));

                } else {
                    viewHold.imageView.setImageResource(norIcons[privilegeLevel - 1]);

                    viewHold.privilegeIcon.setBackgroundColor(mContext.getResources().getColor(R.color.gray_fyi));
                }
            }
        }

        return convertView;
    }

    class ViewHold {

        AutoAdjustHeightImageView privilegeIcon;

        ImageView imageView;
    }
}
