package com.autionsy.seller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.ManagementRecuseAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Rescue;
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

public class ManagementRescueActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.rescue_management_lv)
    ListView rescue_management_lv;

    private ManagementRecuseAdapter mAdapter;
    private List<Rescue> mList = new ArrayList<>();

    private Rescue rescue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_management_rescue);

        ButterKnife.bind(this);
        initView();
        postAsynHttpRecuse();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.rescue_management);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void postAsynHttpRecuse(){
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
                                rescue = new Rescue();
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectRescue = jsonArray.getJSONObject(i);
                                    rescue.setRescueAddressDetail(jsonObjectRescue.getString("rescueAddressDetail"));
                                    rescue.setRescueCompanyIntroduce(jsonObjectRescue.getString("rescueCompanyIntroduce"));
                                    rescue.setRescueCompanyName(jsonObjectRescue.getString("rescueCompanyName"));
                                    rescue.setRescueId(jsonObjectRescue.getString("rescueId"));
                                    rescue.setRescueImageUrl(jsonObjectRescue.getString("rescueImageUrl"));
                                    rescue.setRescuePhoneNumber(jsonObjectRescue.getString("rescuePhoneNumber"));
                                    rescue.setRescueServiceScope(jsonObjectRescue.getString("rescueServiceScope"));
                                    rescue.setRescueTitle(jsonObjectRescue.getString("rescueTitle"));
                                    mList.add(rescue);
                                }

                                /**需要根据状态来发送请求*/
                                mAdapter = new ManagementRecuseAdapter(ManagementRescueActivity.this,mList);
                                rescue_management_lv.setAdapter(mAdapter);
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
        if(rescue_management_lv != null){
            rescue_management_lv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(rescue != null){
            rescue = null;
        }
    }
}
