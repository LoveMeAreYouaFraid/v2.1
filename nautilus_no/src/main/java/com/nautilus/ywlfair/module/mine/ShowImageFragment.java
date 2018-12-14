package com.nautilus.ywlfair.module.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ShowImageFragment extends Fragment {

    private ImageView img;
    private String uris, code, type;
    private ImageView imgType;

    public static ShowImageFragment getInstance(Bundle bundle) {

        ShowImageFragment mInstance = new ShowImageFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            uris = arguments.getString(Constant.KEY.URIS);
            code = arguments.getString(Constant.KEY.CODE);
            type = arguments.getString(Constant.KEY.TYPE);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tickets_page, null);

        img = (ImageView) view.findViewById(R.id.img_tickets_image);
        imgType = (ImageView) view.findViewById(R.id.img_tickets_type);
        TextView tvCode = (TextView) view.findViewById(R.id.tv_tickets_code);

        tvCode.setText("验证码：" + code);
        setType(type);
        ImageLoader.getInstance().displayImage(uris, img, ImageLoadUtils.createDisplayOptions(0));

        return view;
    }


    public void setType(String imgTypes) {
        if (imgTypes.equals("1")) {
            imgType.setImageDrawable(getResources().getDrawable(R.drawable.y_use_img));
        } else if (imgTypes.equals("0")) {
            imgType.setImageDrawable(getResources().getDrawable(R.drawable.n_use_img));
        } else {
            imgType.setVisibility(View.INVISIBLE);
        }
    }

}
