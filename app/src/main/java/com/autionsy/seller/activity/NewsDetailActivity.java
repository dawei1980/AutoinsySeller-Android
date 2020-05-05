package com.autionsy.seller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.News;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.autionsy.seller.views.MyScrollView;
import com.autionsy.seller.views.TextAndGraphicsView;

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

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.sv_main)
    MyScrollView sv_main;
    @BindView(R.id.news_publish_time)
    TextView news_publish_time;
    @BindView(R.id.news_title)
    TextView news_title;

    private News news;
    private ProgressDialog loadingDialog;
    private String newsId;

    public static final String TEXT_TAG =  "[#TEXT#]";//文字标识
    public static final String IMAGE_NET_TAG = "[#IMAGE_NET#]";//网络图片标识
    public static final String IMAGE_LOCAL_TAG = "[#IMAGE_LOCAL#]";//本地图片标识(assets目录下)
    public static final String SPLIT_TAG = "\\[\\#SPLIT\\#\\]";//分割处标识

    private String[] mData;//图文数据的列表
    private TextAndGraphicsView mTextAndGraphicsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_news_detail);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.news_detail_text);

        Bundle bundle = this.getIntent().getExtras();
        newsId = bundle.getString("newsId");

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("数据加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        postAsynHttpGoods();

        mTextAndGraphicsView = new TextAndGraphicsView(NewsDetailActivity.this,mData);
        sv_main.addView(mTextAndGraphicsView);
    }

    private void postAsynHttpGoods() {
        news = new News();

        String url = Constants.HTTP_URL + "getNews";

        Map<String, String> map = new HashMap<>();
        map.put("news_id",newsId);

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

                            if ("200".equals(resultCode)) {

                                JSONObject jsonObjectNews = jsonObject.getJSONObject(data);
                                String title = jsonObjectNews.getString("newsTitle");
                                String content1 = jsonObjectNews.getString("content1");
                                String content2 = jsonObjectNews.getString("content2");
                                String content3 = jsonObjectNews.getString("content3");
                                String content4 = jsonObjectNews.getString("content4");
                                String pulishTime = jsonObjectNews.getString("publishTime");
                                String imageUrl1 = jsonObjectNews.getString("newsImageUrl1");
                                String imageUrl2 = jsonObjectNews.getString("newsImageUrl2");
                                String imageUrl3 = jsonObjectNews.getString("newsImageUrl3");

                                news_title.setText(title);
                                news_publish_time.setText(pulishTime);

                                String content =  "[#TEXT#]" +content1
                                        +"[#SPLIT#]"
                                        +"[#IMAGE_NET#]" + imageUrl1
                                        +"[#SPLIT#]"
                                        +"[#TEXT#]" + content2
                                        +"[#SPLIT#]"
                                        +"[#IMAGE_NET#]" + imageUrl2
                                        +"[#SPLIT#]"
                                        +"[#TEXT#]" + content3
                                        +"[#SPLIT#]"
                                        +"[#IMAGE_NET#]" + imageUrl3
                                        +"[#SPLIT#]"
                                        +"[#TEXT#]" + content4;

                                mData = content.split(SPLIT_TAG);

                            } else if ("403".equals(resultCode)) {
                                Toast.makeText(getApplicationContext(), R.string.param_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(title_tv != null){
            title_tv = null;
        }
        if(loadingDialog != null){
            loadingDialog = null;
        }
        if(news != null){
            news = null;
        }
    }
}
