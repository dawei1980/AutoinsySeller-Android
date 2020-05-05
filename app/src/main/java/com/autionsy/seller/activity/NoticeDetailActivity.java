package com.autionsy.seller.activity;

import android.os.Bundle;
import android.view.View;
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

public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.notice_detail_title_tv)
    TextView notice_detail_title_tv;
    @BindView(R.id.notice_detail_time_tv)
    TextView notice_detail_time_tv;
    @BindView(R.id.notice_detail_content_tv)
    TextView notice_detail_content_tv;

    private String noticeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notice_detail);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.notice_center);

        Bundle bundle = this.getIntent().getExtras();
        noticeId = bundle.getString("notice_id");

        postAsynHttpNoticeDetail();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void postAsynHttpNoticeDetail(){
        String url = Constants.HTTP_URL + "getNotice";

        Map<String,String> map = new HashMap<>();
        map.put("notice_id", noticeId);

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
                                JSONObject jsonObjectNotice = jsonObject.getJSONObject(data);
                                String content = jsonObjectNotice.getString("noticeContent");
                                String title = jsonObjectNotice.getString("noticeTitle");
                                String publishTime = jsonObjectNotice.getString("noticeTime");

                                notice_detail_title_tv.setText(title);
                                notice_detail_time_tv.setText(publishTime);
                                notice_detail_content_tv.setText(content);
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
}
