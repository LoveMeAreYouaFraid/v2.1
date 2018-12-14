package com.nautilus.ywlfair.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.booth.DepositRecordDetailsActivity;
import com.nautilus.ywlfair.module.booth.MyBoothDetailActivity;
import com.nautilus.ywlfair.module.goods.AgainOrderSelectionActivity;
import com.nautilus.ywlfair.module.main.MainActivity;
import com.nautilus.ywlfair.module.mine.MyTicketDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.appcpa.Order;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/12/12.
 */
public class CompleteOrderActivity extends BaseActivity {

    private OrderInfo orderInfo;

    private int type;

    public static void startCompleteActivity(Context context,OrderInfo orderInfo){

        Intent intent = new Intent(context, CompleteOrderActivity.class);

        intent.putExtra(Constant.KEY.ORDER, orderInfo);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.complete_order);

        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constant.KEY.ORDER);

        orderInfo.setOrderStatus(1);

        type = Integer.valueOf(orderInfo.getOrderType()) - 1;


        if (type == 1) {

            EventBus.getDefault().post(new EventActiveStatus(3, 1, null));
        } else if (type == 3) {

            EventBus.getDefault().post(new EventActiveStatus(1, 1, null));

        }

        View backView = findViewById(R.id.back_button);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTo();
            }
        });

        View checkDetail = findViewById(R.id.tv_check_detail);
        checkDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        checkTicketDetail();
                        break;

                    case 1:
                        Intent intent = new Intent(CompleteOrderActivity.this, MyBoothDetailActivity.class);

                        intent.putExtra(Constant.KEY.ORDER_ID, orderInfo.getOrderId());

                        startActivity(intent);

                        finish();

                        break;

                    case 2:
                        Intent detailIntent = new Intent(CompleteOrderActivity.this, AgainOrderSelectionActivity.class);

                        detailIntent.putExtra(Constant.KEY.ORDER_ID, orderInfo.getOrderId());

                        startActivity(detailIntent);

                        finish();

                        break;

                    case 3:

                        Intent depositIntent = new Intent(CompleteOrderActivity.this, DepositRecordDetailsActivity.class);

                        depositIntent.putExtra(Constant.REQUEST.KEY.ITEM_ID, orderInfo.getDepositId());

                        startActivity(depositIntent);

                        finish();

                        break;
                }
            }
        });

    }

    private void checkTicketDetail(){
        Intent intent = new Intent(this, MyTicketDetailActivity.class);

        intent.putExtra(Constant.KEY.ORDER_ID, orderInfo.getOrderId());

        startActivity(intent);

        finish();
    }

    private void backTo() {

//        if (type == 1) {
//            Intent intent = new Intent(this, MyBoothDetailActivity.class);
//
//            intent.putExtra(Constant.KEY.ORDER_ID, orderInfo.getOrderId());
//
//            startActivity(intent);
//        }

        finish();
    }

    @Override
    public void onBackPressed() {

        backTo();

        super.onBackPressed();
    }
}
