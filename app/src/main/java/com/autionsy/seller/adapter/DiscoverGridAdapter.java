package com.autionsy.seller.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.autionsy.seller.R;
import com.autionsy.seller.entity.DiscoverImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DiscoverImage> discoverImagesList = new ArrayList<>();

    public DiscoverGridAdapter(Context context, ArrayList<DiscoverImage> list){
        this.context = context;
        this.discoverImagesList = list;
    }

    @Override
    public int getCount() {
        return discoverImagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return discoverImagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discover_image, null);

            mHolder = new ViewHolder(convertView);

            ViewGroup.LayoutParams p = mHolder.discover_gridview_iv.getLayoutParams();
            p.width = p.height = mGetScreenWidth() / 3 - 20;

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_product)
                .error(R.mipmap.default_product);
        Glide.with(context)
                .load(discoverImagesList.get(position).getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                .apply(options)
                .into(mHolder.discover_gridview_iv);

        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.discover_gridview_iv)
        ImageView discover_gridview_iv;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    private int mGetScreenWidth() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
