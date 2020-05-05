package com.autionsy.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.autionsy.seller.entity.Discover;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Discover> mList = new ArrayList<>();
    private DiscoverGridAdapter discoverImageAdapter;

    public DiscoverListAdapter(Context context, ArrayList<Discover> mList){
        this.context = context;
        this.mList = mList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discover, null);

            mHolder = new ViewHolder(convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_header)
                .error(R.mipmap.default_header);
        Glide.with(context)
                .load(mList.get(position).getHeaderUrl())
                .apply(RequestOptions.circleCropTransform())
                .apply(options)
                .into(mHolder.discover_header);

        mHolder.discover_content.setText(mList.get(position).getSellerName());
        mHolder.discover_publish_time.setText(mList.get(position).getPublishTime());

        if(!mList.get(position).getImageList().isEmpty()){
            discoverImageAdapter = new DiscoverGridAdapter(context, mList.get(position).getImageList());
            mHolder.item_discover_gv.setAdapter(discoverImageAdapter);
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.discover_header)
        ImageView discover_header;
        @BindView(R.id.discover_content)
        TextView discover_content;
        @BindView(R.id.discover_publish_time)
        TextView discover_publish_time;
        @BindView(R.id.item_discover_gv)
        GridView item_discover_gv;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
