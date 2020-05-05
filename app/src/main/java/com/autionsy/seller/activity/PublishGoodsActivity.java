package com.autionsy.seller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.UploadImageAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.views.GridViewInScrollView;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishGoodsActivity extends BaseActivity{
    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.goods_name_et)
    EditText goods_name_et;
    @BindView(R.id.goods_quantity_et)
    EditText goods_quantity_et;
    @BindView(R.id.goods_product_place_et)
    EditText goods_product_place_et;
    @BindView(R.id.goods_price_et)
    EditText goods_price_et;
    @BindView(R.id.motocycle_frame_code_et)
    EditText motocycle_frame_code_et;

    @BindView(R.id.goods_type_tv)
    TextView goods_type_tv;
    @BindView(R.id.goods_brand_tv)
    TextView goods_brand_tv;

    private String goodsName;
    private String goodsQuantity;
    private String goodsProductPlace;
    private String goodsPrice;
    private String goodsFrameCode;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private List<String> path;//路径集合

    private Intent intent;

    private SharedPreferences sharedPreferences;
    private String brandMessage;
    private String categoryMessage;

    private static final int BRAND = 0;
    private static final int CATEGORY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_goods);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_goods);
        submit_tv.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv,
            R.id.type_selector_layout,
            R.id.brand_selector_layout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.image_selector_layout:
                ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
                break;
            case R.id.submit_tv:
                uploadImage();
                break;
            case R.id.type_selector_layout:
                intent = new Intent(PublishGoodsActivity.this, CategoryActivity.class);
                startActivityForResult(intent, CATEGORY);
                break;
            case R.id.brand_selector_layout:
                intent = new Intent(PublishGoodsActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND);
                break;
        }
    }

    /**回调并上传多张*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showImage(data); //设置图片 跟图片目录
            path = ImageSelector.getImagePaths(data);
//            uploadImage(data);
            return;
        }else if(requestCode == BRAND){
            Bundle brandBundle = data.getExtras();
            brandMessage = brandBundle.getString("BrandName");
            goods_brand_tv.setText(brandMessage);
        }else if(requestCode == CATEGORY){
            Bundle brandBundle = data.getExtras();
            categoryMessage = brandBundle.getString("CategoryName");
            goods_type_tv.setText(categoryMessage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**okhttp3上传多张图片
     * Intent data
     * if(data != null)
     * */
    private void uploadImage() {
        goodsName = goods_name_et.getText().toString().trim();
        goodsQuantity = goods_quantity_et.getText().toString().trim();
        goodsProductPlace = goods_product_place_et.getText().toString().trim();
        goodsPrice = goods_price_et.getText().toString().trim();
        goodsFrameCode = motocycle_frame_code_et.getText().toString().trim();

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        String url = Constants.HTTP_URL + "addGoods";

        if (path.size() != 0) {
//            path = ImageSelector.getImagePaths(data);

            //初始化OkHttpClient
            OkHttpClient client = new OkHttpClient();
            // form 表单形式上传
            MultipartBody.Builder requestBody = new MultipartBody.Builder();
            requestBody.setType(MultipartBody.FORM);
            //pathList是文件路径对应的列表
            if (null != path && path.size() > 0) {
                for (String path : path) {
                    File file = new File(path);
                    if (file != null) {
                        // MediaType.parse() 里面是上传的文件类型。
                        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                        // 参数分别为， 请求key ，文件名称 ， RequestBody
                        requestBody.addFormDataPart("upload_goods_image", file.getName(), body);
                    }
                }
            }
            //要上传的文字参数
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("goodName",goodsName);
            map.put("goodPrice",goodsPrice);
            map.put("quantity",goodsQuantity);
            map.put("productPlace",goodsProductPlace);
            map.put("motorcycleFrameNumber",goodsFrameCode);
            map.put("subType","轮胎");
            map.put("brand",brandMessage);
            map.put("upload_type","3");
            if (map != null) {
                for (String key : map.keySet()) {
                    requestBody.addFormDataPart(key, map.get(key));
                }
            }
            //创建Request对象
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody.build())
                    .build();
            // readTimeout("请求超时时间" , 时间单位);
            client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //请求失败处理
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String str = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if("200".equals(str)){
                                    Toast.makeText(getApplicationContext(),"上传图片成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if("403".equals(str)){
                                    Toast.makeText(getApplicationContext(),"参数错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

        }
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishGoodsActivity.this);
        upload_image_gv.setAdapter(adapter);
    }

    private void showImage(Intent data) {
        path = ImageSelector.getImagePaths(data); //集合获取path(这里的path是集合)
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(title_tv != null){
            title_tv = null;
        }
        if(upload_image_gv != null){
            upload_image_gv = null;
        }
        if(submit_tv != null){
            submit_tv = null;
        }
        if(goods_name_et != null){
            goods_name_et = null;
        }
        if(goods_quantity_et != null){
            goods_quantity_et = null;
        }
        if(goods_product_place_et != null){
            goods_product_place_et = null;
        }
        if(goods_price_et != null){
            goods_price_et = null;
        }
        if(motocycle_frame_code_et != null){
            motocycle_frame_code_et = null;
        }
        if(goods_type_tv != null){
            goods_type_tv = null;
        }
        if(goods_brand_tv != null){
            goods_brand_tv = null;
        }
        if(path.size() != 0){
            path.clear();
        }
    }
}
