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

public class PublishServiceActivity extends BaseActivity {

    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.service_store_et)
    EditText service_store_et;
    @BindView(R.id.service_contact_et)
    EditText service_contact_et;
    @BindView(R.id.contact_phone_num_et)
    EditText contact_phone_num_et;
    @BindView(R.id.service_address_et)
    EditText service_address_et;
    @BindView(R.id.service_describe_et)
    EditText service_describe_et;
    @BindView(R.id.service_area_et)
    EditText service_area_et;
    @BindView(R.id.service_type_et)
    EditText service_type_et;
    @BindView(R.id.service_title_et)
    EditText service_title_et;
    @BindView(R.id.upload_image_textview)
    TextView upload_image_textview;

    private String serviceStoreName;
    private String contact;
    private String phoneNum;
    private String address;
    private String describe;
    private String serviceArea;
    private String serviceType;
    private String title;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private List<String> path;//路径集合

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_service);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_service);
        submit_tv.setVisibility(View.VISIBLE);
        upload_image_textview.setText(R.string.upload_seller_shop_photo);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv,})
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
        title = service_title_et.getText().toString().trim();
        serviceStoreName = service_store_et.getText().toString().trim();
        contact = service_contact_et.getText().toString().trim();
        phoneNum = contact_phone_num_et.getText().toString().trim();
        address = service_address_et.getText().toString().trim();
        describe = service_describe_et.getText().toString().trim();
        serviceArea = service_area_et.getText().toString().trim();
        serviceType = service_type_et.getText().toString().trim();

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        String url = Constants.HTTP_URL + "addService";

        if (path.size() != 0) {

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
                        requestBody.addFormDataPart("upload_service_image", file.getName(), body);
                    }
                }
            }
            //要上传的文字参数
            Map<String,String> map = new HashMap<>();
            map.put("title",title);
            map.put("storeName", serviceStoreName);
            map.put("contacts", contact);
            map.put("mobilePhoneNum", phoneNum);
            map.put("address", address);
            map.put("descript", describe);
            map.put("serviceArea", serviceArea);
            map.put("serviceType", serviceType);
            map.put("username", username);
            map.put("upload_type", "4");

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
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishServiceActivity.this);
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
        if(service_store_et != null){
            service_store_et = null;
        }
        if(service_contact_et != null){
            service_contact_et = null;
        }
        if(contact_phone_num_et != null){
            contact_phone_num_et = null;
        }
        if(service_address_et != null){
            service_address_et = null;
        }
        if(service_describe_et != null){
            service_describe_et = null;
        }
        if(service_area_et != null){
            service_area_et = null;
        }
        if(service_type_et != null){
            service_type_et = null;
        }
        if(service_title_et != null){
            service_title_et = null;
        }
        if(path.size() != 0){
            path.clear();
        }
    }
}
