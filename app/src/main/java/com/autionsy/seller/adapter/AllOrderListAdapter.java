package com.autionsy.seller.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Order;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AllOrderListAdapter extends BaseAdapter {

    private Context context;
    List<Order> ordersList = new ArrayList<>();

    public AllOrderListAdapter(Context context, List<Order> list){
        this.context = context;
        this.ordersList = list;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_order, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_header)
                .override(300, 300)
                .error(R.mipmap.default_header);
        Glide.with(context)
                .load(ordersList.get(position).getPictureUrl())
                .apply(options)
                .into(holder.all_order_iv);

        holder.all_order_title_tv.setText(ordersList.get(position).getGoodsTitle());
        holder.all_order_price.setText("¥"+ordersList.get(position).getPrice());
        holder.all_order_quantity.setText("x"+ordersList.get(position).getQuantity());
        holder.all_order_actual_quantity.setText("共"+ordersList.get(position).getQuantity());
        holder.all_order_total.setText("¥"+ordersList.get(position).getTotal());

        final String orderNum = ordersList.get(position).getOrderNum();

        holder.trade_flow_delete_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAsynHttpDeleteOrder(orderNum);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.all_order_iv)
        ImageView all_order_iv;
        @BindView(R.id.all_order_title_tv)
        TextView all_order_title_tv;
        @BindView(R.id.all_order_price)
        TextView all_order_price;
        @BindView(R.id.all_order_quantity)
        TextView all_order_quantity;
        @BindView(R.id.all_order_actual_quantity)
        TextView all_order_actual_quantity;
        @BindView(R.id.all_order_total)
        TextView all_order_total;
        @BindView(R.id.trade_flow_delete_order_btn)
        Button trade_flow_delete_order_btn;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    private void postAsynHttpDeleteOrder(String orderNum){
        String url = Constants.HTTP_URL + "deleteOrder";

        Map<String,String> map = new HashMap<>();
        map.put("order_number",orderNum);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                notifyDataSetChanged();
                            }else if("403".equals(resultCode)){
                                Toast.makeText(context,R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context,R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
