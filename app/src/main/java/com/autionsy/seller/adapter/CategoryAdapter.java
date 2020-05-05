package com.autionsy.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.autionsy.seller.entity.Category;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class CategoryAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context context;
    private List<Category> mList;
    private int selectedPosition = 0;

    public CategoryAdapter(Context mContext, List<Category> list) {
        this.context = mContext;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.empty_image)
                .error(R.mipmap.empty_image);
        Glide.with(context)
                .load(mList.get(position).getSubClassifyImage())
                .apply(options)
                .into(holder.category_iv);

        holder.category_name.setText(mList.get(position).getSubClassify());
        holder.category_info.setText(mList.get(position).getClassifyRemark());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_header, null);
            holder.category_header = convertView.findViewById(R.id.category_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set proj_plans_header text as first char in name
        String headerText = this.mList.get(position).getMainClassifyName();
        holder.category_header.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.parseLong(this.mList.get(position).getMainClassifyCode());
    }

    public class HeaderViewHolder {
        TextView category_header;
    }

    public class ViewHolder{
        @BindView(R.id.category_iv)
        ImageView category_iv;
        @BindView(R.id.category_name)
        TextView category_name;
        @BindView(R.id.category_info)
        TextView category_info;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
