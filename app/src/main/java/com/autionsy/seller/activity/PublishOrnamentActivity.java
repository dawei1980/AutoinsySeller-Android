package com.autionsy.seller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.UploadImageAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Ornament;
import com.autionsy.seller.utils.OkHttp3UploadFileUtil;
import com.autionsy.seller.views.GridViewInScrollView;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.ByteArrayOutputStream;
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

public class PublishOrnamentActivity extends BaseActivity{
    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.ornament_name_et)
    EditText ornament_name_et;
    @BindView(R.id.ornament_price_et)
    EditText ornament_price_et;
    @BindView(R.id.ornament_weight_et)
    EditText ornament_weight_et;
    @BindView(R.id.ornament_motorcycle_type_et)
    EditText ornament_motorcycle_type_et;
    @BindView(R.id.ornament_motorcycle_frame_code_et)
    EditText ornament_motorcycle_frame_code_et;

    private String ornamentName;
    private String ornamentPrice;
    private String ornamentWeight;
    private String motorcycleType;
    private String motorcycleFrameCode;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private List<String> path;//路径集合
    private Intent intent;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_ornament);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_ornament);
        submit_tv.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv,
            R.id.ornament_type_selector_layout,
            R.id.ornament_brand_selector_layout})
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
            case R.id.ornament_type_selector_layout:
                intent = new Intent(PublishOrnamentActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.ornament_brand_selector_layout:
                intent = new Intent(PublishOrnamentActivity.this, BrandActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showImage(data); //设置图片 跟图片目录
            path = ImageSelector.getImagePaths(data);
//            uploadImage(data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {

        ornamentName = ornament_name_et.getText().toString().trim();
        ornamentPrice = ornament_price_et.getText().toString().trim();
        ornamentWeight = ornament_weight_et.getText().toString().trim();
        motorcycleType = ornament_motorcycle_type_et.getText().toString().trim();
        motorcycleFrameCode = ornament_motorcycle_frame_code_et.getText().toString().trim();

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        String url = Constants.HTTP_URL + "addOrnament";

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
                        requestBody.addFormDataPart("upload_ornament_image", file.getName(), body);
                    }
                }
            }
            //要上传的文字参数
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("ornamentName",ornamentName);
            map.put("weight",ornamentWeight);
            map.put("price",ornamentPrice);
            map.put("motorcycleType",motorcycleType);
            map.put("motorcycleFrameNumber",motorcycleFrameCode);
            map.put("ornamentType","空调");
            map.put("brand","");
            map.put("upload_type","6");

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
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishOrnamentActivity.this);
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
        if(ornament_name_et != null){
            ornament_name_et = null;
        }
        if(ornament_price_et != null){
            ornament_price_et = null;
        }
        if(ornament_weight_et != null){
            ornament_weight_et = null;
        }
        if(ornament_motorcycle_type_et != null){
            ornament_motorcycle_type_et = null;
        }
        if(ornament_motorcycle_frame_code_et != null){
            ornament_motorcycle_frame_code_et = null;
        }
        if(path.size() != 0){
            path.clear();
        }
    }
}
