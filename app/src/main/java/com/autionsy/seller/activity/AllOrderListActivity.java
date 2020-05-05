package com.autionsy.seller.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.AllOrderListAdapter;
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

public class AllOrderListActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.all_order_lv)
    ListView all_order_lv;

    private AllOrderListAdapter mAdapter;
    private List<Order> mList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_all_order_list);

        ButterKnife.bind(this);
        initView();
        postAsynHttpAllOrder();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.all_order);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void postAsynHttpAllOrder(){
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        order = new Order();

        String url = Constants.HTTP_URL + "getAllOrderList";

        Map<String,String> map = new HashMap<>();
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
                                mAdapter = new AllOrderListAdapter(AllOrderListActivity.this,mList);
                                all_order_lv.setAdapter(mAdapter);
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
        if(title_tv != null){
            title_tv = null;
        }
        if(all_order_lv != null){
            all_order_lv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(order != null){
            order = null;
        }
    }
}
