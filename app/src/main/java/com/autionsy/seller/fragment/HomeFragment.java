package com.autionsy.seller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.activity.ExpressDeliveryCatchOrderActivity;
import com.autionsy.seller.activity.NewsActivity;
import com.autionsy.seller.activity.NewsDetailActivity;
import com.autionsy.seller.activity.NoticeActivity;
import com.autionsy.seller.activity.OrderExpressDeliveryActivity;
import com.autionsy.seller.activity.PublishGoodsActivity;
import com.autionsy.seller.activity.PublishLeaseActivity;
import com.autionsy.seller.activity.PublishOrnamentActivity;
import com.autionsy.seller.activity.PublishRecruitActivity;
import com.autionsy.seller.activity.PublishRescueActivity;
import com.autionsy.seller.activity.PublishServiceActivity;
import com.autionsy.seller.adapter.HomeAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Advertisement;
import com.autionsy.seller.entity.News;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.autionsy.seller.views.ListViewInScrollView;
import com.autionsy.seller.zxing.activity.CaptureActivity;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends BaseFragment implements OnBannerListener{

    private View view;
    private Intent intent;
    private Banner banner;
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private ArrayList<String> list_title = new ArrayList<>();

    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.scan_btn)
    ImageView scan_btn;
    @BindView(R.id.search_layout)
    LinearLayout search_layout;
    @BindView(R.id.notice_btn)
    ImageView notice_btn;
    @BindView(R.id.home_news_listview)
    ListViewInScrollView home_news_listview;

    private ArrayList<News> newsList = new ArrayList<>();
    private News news;
    private HomeAdapter homeAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {

        banner = view.findViewById(R.id.banner);

        postAsynHttpAdvertisement();

        //简单使用
        banner.setImages(imageUrlList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default)
                .setBannerTitles(list_title)
                .setOnBannerListener(this)
                .start();

        back_btn.setVisibility(View.GONE);
        scan_btn.setVisibility(View.VISIBLE);
        search_layout.setVisibility(View.VISIBLE);
        notice_btn.setVisibility(View.VISIBLE);

        postAsynHttpMainPageNews();
    }

    @OnClick({R.id.scan_btn,
            R.id.search_layout,
            R.id.notice_btn,
            R.id.publish_goods_layout,
            R.id.publish_service_layout,
            R.id.publish_rescue_layout,
            R.id.publish_lease_layout,
            R.id.publish_recruit_layout,
            R.id.publish_ornament_layout,
            R.id.order_delivery_layout,
            R.id.publish_catch_order_layout,
            R.id.more_news})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.scan_btn:
                intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.search_layout:

                break;
            case R.id.notice_btn:
                intent = new Intent(getActivity(),NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_goods_layout:
                intent = new Intent(getActivity(),PublishGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_service_layout:
                intent = new Intent(getActivity(),PublishServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_rescue_layout:
                intent = new Intent(getActivity(),PublishRescueActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_lease_layout:
                intent = new Intent(getActivity(),PublishLeaseActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_recruit_layout:
                intent = new Intent(getActivity(),PublishRecruitActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_ornament_layout:
                intent = new Intent(getActivity(),PublishOrnamentActivity.class);
                startActivity(intent);
                break;
            case R.id.order_delivery_layout:
                intent = new Intent(getActivity(),OrderExpressDeliveryActivity.class);
                startActivity(intent);
                break;
            case R.id.publish_catch_order_layout:
                intent = new Intent(getActivity(),ExpressDeliveryCatchOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.more_news:
                intent = new Intent(getActivity(),NewsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .into(imageView);
        }
    }

    private void postAsynHttpMainPageNews(){
        news = new News();

        String url = Constants.HTTP_URL + "getNewsByTime";
        Map<String,String> map = new HashMap<>();

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectNews = jsonArray.getJSONObject(i);

                                    news.setNewsId(jsonObjectNews.getLong("newsID"));
                                    news.setContent(jsonObjectNews.getString("content"));
                                    news.setDate(jsonObjectNews.getString("publishTime"));
                                    news.setTitle(jsonObjectNews.getString("newsTitle"));
                                    news.setImageUrl(jsonObjectNews.getString("newsImageUrl1"));
                                    newsList.add(news);
                                }
                                homeAdapter = new HomeAdapter(getActivity(),newsList);
                                home_news_listview.setAdapter(homeAdapter);
                                home_news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                                        intent.putExtra("newsId",news.getNewsId());
                                        startActivity(intent);
                                    }
                                });
                                homeAdapter.notifyDataSetChanged();
                            }else if("403".equals(resultCode)){
                                Toast.makeText(getActivity(),R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void postAsynHttpAdvertisement(){
        String url = Constants.HTTP_URL + "appRotateAdvertisement";

        Map<String,String> map = new HashMap<>();

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                JSONArray jsonArray = jsonObject.getJSONArray(data);

                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectAdertisement = jsonArray.getJSONObject(i);
                                    String advertisementTitle = jsonObjectAdertisement.getString("appRotateAdvertisementName");
                                    String advertisementUrl = jsonObjectAdertisement.getString("appRotateAdvertisementUrl");
                                    imageUrlList.add(advertisementUrl);
                                    list_title.add(advertisementTitle);
                                }

                            }else if("403".equals(resultCode)){
                                Toast.makeText(getActivity(),R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),R.string.login_fail,Toast.LENGTH_SHORT).show();
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
        if(back_btn != null){
            back_btn = null;
        }
        if(scan_btn != null){
            scan_btn = null;
        }
        if(search_layout != null){
            search_layout = null;
        }
        if(notice_btn != null){
            notice_btn = null;
        }
        if(home_news_listview != null){
            home_news_listview = null;
        }
        if(newsList.size() != 0){
            newsList.clear();
        }
        if(banner != null){
            banner = null;
        }
        if(imageUrlList.size() != 0){
            imageUrlList.clear();
        }
        if(list_title.size() != 0){
            list_title.clear();
        }
        if(view != null){
            view = null;
        }
    }
}
