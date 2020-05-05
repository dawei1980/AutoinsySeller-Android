package com.autionsy.seller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.OrderListAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Order;
import com.autionsy.seller.utils.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.trade_flow_lv)
    ListView trade_flow_lv;

    private OrderListAdapter mAdapter;
    private List<Order> mList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_status_list);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);

        // 首先获取到意图对象
        Intent intent = getIntent();
        String orderState = intent.getStringExtra("order_state");

        switch (orderState){
            case "1": /**订单状态为1,待发货*/
                title_tv.setText(R.string.send_goods_title);
                break;
            case "2": /**订单状态为2，待签收*/
                title_tv.setText(R.string.receive_goods_title);
                break;
            case "3":  /**订单状态为3，待评价*/
                title_tv.setText(R.string.appraise_title);
                break;
            case "4": /**订单状态为4，退货/退款*/
                title_tv.setText(R.string.refund_title);
                break;
        }
        postAsynHttpOrderStatus(orderState);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void postAsynHttpOrderStatus(final String status){
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        order = new Order();

        String url = Constants.HTTP_URL + "getOrderListByState";

        Map<String,String> map = new HashMap<>();
        map.put("orderState",status);
        map.put("username",username);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                    order.setGoodsTitle(jsonObjectData.getString("goodsTitle"));
                                    order.setAddress(jsonObjectData.getString("address"));
                                    order.setMobilePhoneNum(jsonObjectData.getString("mobilePhoneNum"));
                                    order.setOrderNum(jsonObjectData.getString("orderNum"));
                                    order.setOrderState(jsonObjectData.getString("orderState"));
                                    order.setQuantity(jsonObjectData.getString("orderQuantity"));
                                    order.setPrice(jsonObjectData.getString("goodsPrice"));
                                    order.setRealName(jsonObjectData.getString("realName"));
                                    order.setUsername(jsonObjectData.getString("username"));
                                    order.setTotal(jsonObjectData.getString("total"));
                                    order.setPictureUrl(jsonObjectData.getString("pictureUrl"));
                                    mList.add(order);
                                }

                                /**需要根据状态来发送请求*/
                                mAdapter = new OrderListAdapter(OrderListActivity.this,mList,status);
                                trade_flow_lv.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                            }else if("403".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(trade_flow_lv != null){
            trade_flow_lv = null;
        }
        if(title_tv != null){
            title_tv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(order != null){
            order = null;
        }
    }
}
