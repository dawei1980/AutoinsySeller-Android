package com.autionsy.seller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.AddressManagementAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Address;
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

public class AddressManagementActivity extends BaseActivity {

    @BindView(R.id.address_management_lv)
    ListView address_management_lv;

    @BindView(R.id.title_tv)
    TextView title_tv;

    private AddressManagementAdapter mAdapter;
    private List<Address> mList = new ArrayList<>();

    private Address address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address_management);

        ButterKnife.bind(this);
        initView();
        postAsynHttpAddress();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.address_management);
    }

    @OnClick({R.id.back_btn,R.id.add_address_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_address_btn:
                Intent intent = new Intent(AddressManagementActivity.this, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void postAsynHttpAddress(){

        SharedPreferences prefs = getSharedPreferences("seller_login_data", MODE_PRIVATE); //获取对象，读取data文件
        String username = prefs.getString("USERNAME", ""); //获取文件中的数据

        String url = Constants.HTTP_URL + "getAddressList";
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
                                address = new Address();
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectAddress = jsonArray.getJSONObject(i);
                                    address.setAddress(jsonObjectAddress.getString("address"));
                                    address.setIsDefault(jsonObjectAddress.getString("isDefault"));
                                    address.setMobilePhoneNum(jsonObjectAddress.getString("mobilePhoneNum"));
                                    address.setName(jsonObjectAddress.getString("name"));
                                    mList.add(address);
                                }

                                mAdapter = new AddressManagementAdapter(AddressManagementActivity.this,mList);
                                address_management_lv.setAdapter(mAdapter);
                                mAdapter.notifyDataSetInvalidated();
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
        if(address_management_lv != null){
            address_management_lv =  null;
        }
        if(title_tv != null){
            title_tv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(address != null){
            address = null;
        }
    }
}
