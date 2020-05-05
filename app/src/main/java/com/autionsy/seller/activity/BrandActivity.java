package com.autionsy.seller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.BrandAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Brand;
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

public class BrandActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.brand_gv)
    GridView brand_gv;

    private BrandAdapter brandAdapter;
    private List<Brand> mList = new ArrayList<>();

    private Brand brand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_brand);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_goods);

        postAsynHttpBrand();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void postAsynHttpBrand(){
        brand = new Brand();

        String url = Constants.HTTP_URL + "login";

        Map<String,String> map = new HashMap<>();
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
                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObjectBrand = jsonArray.getJSONObject(i);
                                    brand.setName(jsonObjectBrand.getString("brandName"));
                                    brand.setImage(jsonObjectBrand.getString("brandLogo"));
                                    mList.add(brand);
                                }

                                brandAdapter = new BrandAdapter(BrandActivity.this,mList);
                                brand_gv.setAdapter(brandAdapter);
                                brand_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent = new Intent(BrandActivity.this, PublishGoodsActivity.class);
                                        String brandNameStr = brand.getName();
                                        intent.putExtra("BrandName", brandNameStr);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });

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
        if(brand_gv != null){
            brand_gv = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
        if(brand != null){
            brand = null;
        }
    }
}
