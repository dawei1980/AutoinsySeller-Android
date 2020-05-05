package com.autionsy.seller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autionsy.seller.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {
    //image_1、声明控件
    private int[] imgeIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> list;//存放图片的ImageView对象
    private ViewPager viewPager;
    private GuideViewPagerAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guide);

        // 初始化页面
        initViews();
    }

    private void initViews() {

        //image_2、初始化控件
        viewPager = findViewById(R.id.guide_viewpager);
        //获取数据源
        initData();
        //创建适配器
        vpAdapter = new GuideViewPagerAdapter();
        //给ViewPager设置适配器
        viewPager.setAdapter(vpAdapter);
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < imgeIds.length; i++) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setImageResource(imgeIds[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            list.add(imageView);
            //点击最后一张图片跳转到MainActivity
            if (i == imgeIds.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        }
    }

    //自定义适配器
    public class GuideViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(list.size() != 0){
            list.clear();
        }
    }
}
