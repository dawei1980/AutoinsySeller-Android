package com.autionsy.seller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.utils.OkHttp3Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgetPasswordActivity extends BaseActivity{
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.forget_input_mobile_phone_num_et)
    EditText forget_input_mobile_phone_num_et;
    @BindView(R.id.forget_input_password_et)
    EditText forget_input_password_et;

    private String mobilePhoneNum;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_forget_password);
        ButterKnife.bind(this);
        // 初始化页面
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.forget_password);
    }

    @OnClick({R.id.back_btn,R.id.forget_ok_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.forget_ok_btn:
                postAsynHttpForgetPassword();
                break;
        }
    }

    private void postAsynHttpForgetPassword(){
        mobilePhoneNum = forget_input_mobile_phone_num_et.getText().toString().trim();
        password = forget_input_password_et.getText().toString().trim();

        String url = Constants.HTTP_URL + "sellerForgetPassword";
        Map<String,String> map = new HashMap<>();
        map.put("username", mobilePhoneNum);
        map.put("password", password);

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
                            JSONObject jsonObject  = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.send_sms_code_success,Toast.LENGTH_SHORT).show();
                            }else if("403".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.send_sms_code_fail,Toast.LENGTH_SHORT).show();
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
        if(forget_input_mobile_phone_num_et != null){
            forget_input_mobile_phone_num_et = null;
        }
        if(forget_input_password_et != null){
            forget_input_password_et = null;
        }
    }
}
