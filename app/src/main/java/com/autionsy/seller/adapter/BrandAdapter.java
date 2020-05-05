package com.autionsy.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autionsy.seller.R;
import com.autionsy.seller.entity.Brand;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandAdapter extends BaseAdapter {

    private Context context;
    private List<Brand> mList = new ArrayList<>();

    public BrandAdapter(Context context, List<Brand> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_brand, null);

            mHolder = new ViewHolder(convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_header)
                .error(R.mipmap.default_header);
        Glide.with(context)
                .load(mList.get(position).getImage())
                .apply(RequestOptions.circleCropTransform())
                .apply(options)
                .into(mHolder.brand_iv);

        mHolder.brand_name.setText(mList.get(position).getName());

        return convertView;
    }

    public class ViewHolder{

        @BindView(R.id.brand_iv)
        ImageView brand_iv;
        @BindView(R.id.brand_name)
        TextView brand_name;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
