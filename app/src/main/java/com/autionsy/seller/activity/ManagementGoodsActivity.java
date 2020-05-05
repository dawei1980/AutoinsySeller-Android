package com.autionsy.seller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.ManagementGoodsAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Goods;
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

public class ManagementGoodsActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.goods_management_lv)
    ListView goods_management_lv;

    private ManagementGoodsAdapter mAdapter;
    private List<Goods> mList = new ArrayList<>();

    private Goods goods;

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(title_tv != null){
            title_tv = null;
        }
        if(goods_management_lv != null){
            goods_management_lv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(goods != null){
            goods = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_management_goods);

        ButterKnife.bind(this);
        initView();
        postAsynHttpGoods();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.goods_management);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    /**商品*/
    private void postAsynHttpGoods(){
        SharedPreferences prefs = getSharedPreferences("seller_login_data", MODE_PRIVATE); //获取对象，读取data文件
        String username = prefs.getString("USERNAME", ""); //获取文件中的数据

        String url = Constants.HTTP_URL + "getAllGoods";

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
                                goods = new Goods();
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectGoods = jsonArray.getJSONObject(i);
                                    goods.setGoodsId(jsonObjectGoods.getString("goodsId"));
                                    goods.setBrand(jsonObjectGoods.getString("brand"));
                                    goods.setGoodsName(jsonObjectGoods.getString("goodsName"));
                                    goods.setGoodsPic(jsonObjectGoods.getString("goodsPic"));
                                    goods.setPrice(jsonObjectGoods.getString("price"));
                                    goods.setQuantity(jsonObjectGoods.getString("quantity"));
                                    goods.setDescribe(jsonObjectGoods.getString("describe"));
                                    goods.setMotorcycleFrameNumber(jsonObjectGoods.getString("motorcycleFrameNumber"));
                                    goods.setProductPlace(jsonObjectGoods.getString("productPlace"));
                                    goods.setPublishTime(jsonObjectGoods.getString("publishTime"));
                                    mList.add(goods);
                                }

                                /**需要根据状态来发送请求*/
                                mAdapter = new ManagementGoodsAdapter(ManagementGoodsActivity.this,mList);
                                goods_management_lv.setAdapter(mAdapter);
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
}
