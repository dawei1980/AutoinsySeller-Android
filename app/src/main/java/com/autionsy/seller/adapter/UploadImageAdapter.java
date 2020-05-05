package com.autionsy.seller.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.autionsy.seller.R;
import com.autionsy.seller.entity.Image;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadImageAdapter extends BaseAdapter {

    private List<String> path;
    private Context context;

    public UploadImageAdapter(List<String> path, Context context) {
        this.path = path;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return path.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder = null;

        if(convertView==null){

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_upload_image, null);

            viewHolder.img = convertView.findViewById(R.id.upload_image_iv);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img.setImageBitmap(BitmapFactory.decodeFile(path.get(position))); //这里要用BitMap解析图片
        return convertView;
    }

    class ViewHolder{

        ImageView img;
    }
}
