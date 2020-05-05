package com.autionsy.seller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.CategoryAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Category;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.facebook.drawee.backends.pipeline.Fresco;

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
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.category_lv)
    StickyListHeadersListView category_lv;

    private List<Category> categoryList = new ArrayList<>();
    private Category category;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_category);
        ButterKnife.bind(this);
        Fresco.initialize(this);
        initView();
        loadData();
    }

    private void initView() {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.category_title_text);

    }

    private void loadData() {
        String url = Constants.CATEGORY;

        Map<String, String> map = new HashMap<>();
        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");
                            if("200".equals(resultCode)){
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                category = new Category();
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectMainData = jsonArray.getJSONObject(i);
                                    String mainClassifyName = jsonObjectMainData.getString("mainClassify");
                                    String mainClassifyCode = jsonObjectMainData.getString("mainClassifyCode");
                                    JSONArray jsonArraySub = jsonObjectMainData.getJSONArray("subClassifySet");
                                    for (int j=0; j<jsonArraySub.length();j++){
                                        JSONObject jsonObjectSubData = jsonArraySub.getJSONObject(j);
                                        category.setSubClassify(jsonObjectSubData.getString("subClassify"));
                                        category.setSubClassifyId(jsonObjectSubData.getString("subClassifyId"));
                                        category.setClassifyRemark(jsonObjectSubData.getString("subClassifyRemark"));
                                        category.setMainClassifyCode(jsonObjectSubData.getString("mainClassifyCode"));
                                        category.setSubClassifyImage(jsonObjectSubData.getString("subClassifyImage"));
                                        categoryList.add(category);
                                    }
                                }
                                categoryAdapter = new CategoryAdapter(CategoryActivity.this,categoryList);
                                category_lv.setAdapter(categoryAdapter);
                                category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(CategoryActivity.this, PublishGoodsActivity.class);
                                        String categoryNameStr = category.getSubClassify();
                                        intent.putExtra("CategoryName", categoryNameStr);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });

                            }else if("410".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),"没有数据",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (title_tv != null) {
            title_tv = null;
        }

    }
}
