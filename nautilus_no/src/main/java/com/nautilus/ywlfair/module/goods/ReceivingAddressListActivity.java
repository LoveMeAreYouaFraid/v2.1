package com.nautilus.ywlfair.module.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ShippingAddressInfo;
import com.nautilus.ywlfair.entity.request.DeleteEditAddressRequest;
import com.nautilus.ywlfair.entity.request.GetShippingAddressListRequest;
import com.nautilus.ywlfair.entity.request.PutDefaultAddressRequest;
import com.nautilus.ywlfair.entity.response.GetShippingAddressList;
import com.nautilus.ywlfair.entity.response.PutDefaultAddressResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.goods.adapter.ReceivingAddressListAdapter;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址列表
 * Created by Administrator on 2015/12/28.
 */
public class ReceivingAddressListActivity extends BaseActivity implements View.OnClickListener, ReceivingAddressListAdapter.OnViewsClickListener {
    private ListView mlist;
    private ReceivingAddressListAdapter adapter;
    private Context mContext;
    private GetShippingAddressList mresponse;
    private List<ShippingAddressInfo> list;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        list = new ArrayList<>();
        setContentView(R.layout.receiving_address_liset);
        View back = findViewById(R.id.img_back);
        View add = findViewById(R.id.tv_add);
        add.setOnClickListener(this);
        back.setOnClickListener(this);
        mlist = (ListView) findViewById(R.id.address_list);
        adapter = new ReceivingAddressListAdapter(mContext, list);
        adapter.setOnViewsClickListener(this);
        mlist.setAdapter(adapter);
        loadlist();
        intent = new Intent();
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShippingAddressInfo shippingAddressInfo = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shipping", shippingAddressInfo);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                backButton();
                break;
            case R.id.tv_add:
                Bundle bundle = new Bundle();
                bundle.putSerializable("GetShippingAddressList", mresponse);
                startActivity(new Intent(mContext, ModifyShippingAddressActivity.class).putExtra("key", "0").putExtras(bundle));
                break;
        }

    }

    /**
     * 获取收货地址列表 并刷新界面
     */
    public void loadlist() {
        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        GetShippingAddressListRequest getShippingAddressListRequest = new GetShippingAddressListRequest(userId,
                new ResponseListener<GetShippingAddressList>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "数据加载中....");
                    }

                    @Override
                    public void onCacheResponse(GetShippingAddressList response) {

                    }

                    @Override
                    public void onResponse(GetShippingAddressList response) {

                        mresponse = response;

                        list.clear();

                        list.addAll(response.getResult().getShippingAddressInfoList());

                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }

                    @Override
                    public void onFinish() {
                        ProgressDialog.getInstance().cancel();

                    }
                });
        getShippingAddressListRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(getShippingAddressListRequest);

    }

    /**
     * 设置默认地址
     *
     * @param id
     */
    void DefaultAddress(String id) {
        PutDefaultAddressRequest putDefaultAddressRequest = new PutDefaultAddressRequest(GetUserInfoUtil.getUserInfo().getUserId() + ""
                , id,
                new ResponseListener<PutDefaultAddressResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(PutDefaultAddressResponse response) {

                    }

                    @Override
                    public void onResponse(PutDefaultAddressResponse response) {
                        loadlist();
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
        putDefaultAddressRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(putDefaultAddressRequest);

    }

    /**
     * 删除 收货地址
     *
     * @param id
     */
    private void Delete(String id) {

        DeleteEditAddressRequest deleteEditAddressRequest = new DeleteEditAddressRequest(GetUserInfoUtil.getUserInfo().getUserId() + "", id,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                            loadlist();
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
        deleteEditAddressRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(deleteEditAddressRequest);
    }

    /**
     * adpater 监听
     *
     * @param position
     * @param v
     */
    @Override
    public void mViewClick(int position, View v) {
        Log.e("123", position + "");
        switch (v.getId()) {

            case R.id.tv_delete:
                Delete(list.get(position).getId());
                loadlist();
                break;
            case R.id.tv_default:
            case R.id.cb_default:
                DefaultAddress(list.get(position).getId());

                break;
            case R.id.tv_modify:
                Bundle bundle = new Bundle();
                bundle.putSerializable("shippingAddressInfo", mresponse);
                startActivity(new Intent(mContext, ModifyShippingAddressActivity.class).putExtra("key", "1").putExtras(bundle)
                        .putExtra("position", position + ""));
                break;
        }

    }

    @Override
    protected void onRestart() {
        loadlist();
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        backButton();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 如果有默认地址，显示默认，没有则显示第一条
     *
     * @return
     */
    private void backButton() {
        if (mresponse == null) {//断网时
            finish();
            return;
        }
        if (mresponse.getResult().getShippingAddressInfoList().isEmpty()) {//没有收货地址时
            setResult(0, null);
            finish();
            return;
        }
        ShippingAddressInfo shippingAddressInfo = list.get(0);//没有默认且没有选择item时
        Bundle bundle = new Bundle();
        bundle.putSerializable("shipping", shippingAddressInfo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        return;

    }
}
