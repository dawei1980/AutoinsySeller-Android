package com.autionsy.seller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.ManagementOrnamentAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Ornament;
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

public class ManagementOrnamenActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.ornament_management_lv)
    ListView ornament_management_lv;

    private ManagementOrnamentAdapter mAdapter;
    private List<Ornament> mList = new ArrayList<>();

    private Ornament ornament;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_management_ornament);

        ButterKnife.bind(this);
        initView();
        postAsynHttpGoods();
    }
    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.ornament_management);
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

        String url = Constants.HTTP_URL + "getAllOrnament";

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
                                ornament = new Ornament();
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectGoods = jsonArray.getJSONObject(i);
                                    ornament.setOrnamentId(jsonObjectGoods.getString("ornamentId"));
                                    ornament.setOrnamentName(jsonObjectGoods.getString("ornamentName"));
                                    ornament.setImageUrl(jsonObjectGoods.getString("ornamentImageUrl"));
                                    ornament.setPrice(jsonObjectGoods.getString("price"));
                                    ornament.setQuantity(jsonObjectGoods.getString("quantity"));
                                    ornament.setMotorcycleFrameNumber(jsonObjectGoods.getString("motorcycleFrameNumber"));
                                    ornament.setBrand(jsonObjectGoods.getString("brand"));
                                    ornament.setWeight(jsonObjectGoods.getString("weight"));
                                    mList.add(ornament);
                                }

                                /**需要根据状态来发送请求*/
                                mAdapter = new ManagementOrnamentAdapter(ManagementOrnamenActivity.this,mList);
                                ornament_management_lv.setAdapter(mAdapter);
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
        if(ornament_management_lv != null){
            ornament_management_lv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(ornament != null){
            ornament = null;
        }
    }
}
