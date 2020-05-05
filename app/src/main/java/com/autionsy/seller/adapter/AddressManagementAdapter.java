package com.autionsy.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.autionsy.seller.entity.Address;
import com.autionsy.seller.entity.Notice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressManagementAdapter extends BaseAdapter {

    private Context context;
    List<Address> addressList = new ArrayList<>();
    private int selectItem = -1;

    public AddressManagementAdapter(Context context,List<Address> list){
        this.context = context;
        this.addressList = list;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if(view == null && addressList.size() != 0){
            view = LayoutInflater.from(context).inflate(R.layout.item_address_management, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.address_receiver_name.setText(addressList.get(position).getAddress());
        viewHolder.address_mobile_num.setText(addressList.get(position).getMobilePhoneNum());
        viewHolder.address_detail.setText(addressList.get(position).getAddress());

        viewHolder.address_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.address_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (selectItem == position){
            viewHolder.address_select_iv.setSelected(true);
            viewHolder.address_select_iv.setPressed(true);
        }else {
            viewHolder.address_select_iv.setSelected(false);
            viewHolder.address_select_iv.setPressed(false);
        }

        return view;
    }

    public class ViewHolder{

        @BindView(R.id.address_receiver_name)
        TextView address_receiver_name;
        @BindView(R.id.address_mobile_num)
        TextView address_mobile_num;
        @BindView(R.id.address_detail)
        TextView address_detail;
        @BindView(R.id.address_select_iv)
        ImageView address_select_iv;
        @BindView(R.id.address_edit_btn)
        Button address_edit_btn;
        @BindView(R.id.address_delete_btn)
        Button address_delete_btn;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
