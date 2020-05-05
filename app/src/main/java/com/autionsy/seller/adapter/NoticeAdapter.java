package com.autionsy.seller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.autionsy.seller.activity.NoticeDetailActivity;
import com.autionsy.seller.entity.Notice;
import com.autionsy.seller.utils.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeAdapter extends BaseAdapter {

    private Context context;
    List<Notice> noticesList = new ArrayList<>();

    public NoticeAdapter(Context context,List<Notice> list){
        this.context = context;
        this.noticesList = list;
    }

    @Override
    public int getCount() {
        return noticesList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notice, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_header)
                .transform(new GlideRoundTransform(5))
                .override(300, 300)
                .error(R.mipmap.default_header);
        Glide.with(context)
                .load(noticesList.get(position).getHeader())
                .apply(options)
                .into(holder.notice_header_iv);

        holder.notice_title_tv.setText(noticesList.get(position).getTitle());
        holder.notice_time.setText(noticesList.get(position).getTime());
        holder.notice_content_tv.setText(noticesList.get(position).getContent());

        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.notice_header_iv)
        ImageView notice_header_iv;
        @BindView(R.id.notice_title_tv)
        TextView notice_title_tv;
        @BindView(R.id.notice_time)
        TextView notice_time;
        @BindView(R.id.notice_content_tv)
        TextView notice_content_tv;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
