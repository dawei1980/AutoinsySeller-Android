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

public class PublishRescueActivity extends BaseActivity {
    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.rescue_rescue_title_et)
    EditText rescue_rescue_title_et;
    @BindView(R.id.rescue_company_name_et)
    EditText rescue_company_name_et;
    @BindView(R.id.rescue_phone_number_et)
    EditText rescue_phone_number_et;
    @BindView(R.id.rescue_address_detail_et)
    EditText rescue_address_detail_et;
    @BindView(R.id.rescue_service_scope_et)
    EditText rescue_service_scope_et;
    @BindView(R.id.rescue_company_introduce_et)
    EditText rescue_company_introduce_et;

    private String title;
    private String companyName;
    private String phoneNum;
    private String addressDetail;
    private String serviceScope;
    private String companyIntroduce;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private List<String> path;//路径集合

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_rescue);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_rescue);
        submit_tv.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv})
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
        title = rescue_rescue_title_et.getText().toString().trim();
        companyName = rescue_company_name_et.getText().toString().trim();
        phoneNum = rescue_phone_number_et.getText().toString().trim();
        addressDetail = rescue_address_detail_et.getText().toString().trim();
        serviceScope = rescue_service_scope_et.getText().toString().trim();
        companyIntroduce = rescue_company_introduce_et.getText().toString().trim();

        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        sharedPreferences = getSharedPreferences("seller_login_data", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String username = sharedPreferences.getString("USERNAME", "");

        String url = Constants.HTTP_URL + "addRescue";

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
                        requestBody.addFormDataPart("upload_rescue_image", file.getName(), body);
                    }
                }
            }
            //要上传的文字参数
            Map<String,String> map = new HashMap<>();
            map.put("rescueTittle", title);
            map.put("rescueCompanyName", companyName);
            map.put("rescuePhoneNumber", phoneNum);
            map.put("rescueAddressDetail", addressDetail);
            map.put("rescueServiceScope", serviceScope);
            map.put("rescueCompanyIntroduce", companyIntroduce);
            map.put("username", username);
            map.put("upload_type", "7");

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
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishRescueActivity.this);
        upload_image_gv.setAdapter(adapter);
    }
    private void showImage(Intent data) {
        path = ImageSelector.getImagePaths(data); //集合获取path(这里的path是集合)
    }
}
