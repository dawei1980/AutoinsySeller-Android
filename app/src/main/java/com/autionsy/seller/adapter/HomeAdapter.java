package com.autionsy.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.autionsy.seller.entity.News;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<News> mList = new ArrayList<>();

    public HomeAdapter(Context context,List<News> list){
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home, null);

            mHolder = new ViewHolder(convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.empty_image)
                .error(R.mipmap.empty_image);
        Glide.with(context)
                .load(mList.get(position).getImageUrl())
                .apply(options)
                .into(mHolder.home_news_iv);

        mHolder.home_news_title_tv.setText(mList.get(position).getTitle());
        mHolder.home_news_content_tv.setText(mList.get(position).getContent());
        mHolder.home_news_date.setText(mList.get(position).getDate());

        return convertView;
    }

    public class ViewHolder{

        @BindView(R.id.home_news_iv)
        ImageView home_news_iv;
        @BindView(R.id.home_news_title_tv)
        TextView home_news_title_tv;
        @BindView(R.id.home_news_content_tv)
        TextView home_news_content_tv;
        @BindView(R.id.home_news_date)
        TextView home_news_date;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
