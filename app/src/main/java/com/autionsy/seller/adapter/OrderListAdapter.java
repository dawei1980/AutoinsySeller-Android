package com.autionsy.seller.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class OrderListAdapter extends BaseAdapter {

    private Context context;
    List<Order> tradeFlowList = new ArrayList<>();
    private String orderNum;
    private String orderStatus;
    private Intent intent;

    public OrderListAdapter(Context context, List<Order> list,String orderStatus){
        this.context = context;
        this.tradeFlowList = list;
        this.orderStatus = orderStatus;
    }

    @Override
    public int getCount() {
        return tradeFlowList.size();
    }

    @Override
    public Object getItem(int position) {
        return tradeFlowList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_status, null);

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
                .load(tradeFlowList.get(position).getPictureUrl())
                .apply(options)
                .into(holder.trade_flow_goods_iv);

        holder.trade_flow_goods_title_tv.setText(tradeFlowList.get(position).getGoodsTitle());
        holder.trade_flow_goods_price.setText("¥"+tradeFlowList.get(position).getPrice());
        holder.trade_flow_goods_quantity.setText("x"+tradeFlowList.get(position).getQuantity());
        holder.trade_flow_actual_quantity.setText("共"+tradeFlowList.get(position).getQuantity());
        holder.trade_flow_total.setText("¥"+tradeFlowList.get(position).getTotal());

        switch (orderStatus){
            case "1": /**订单状态为1,待发货*/
                holder.trade_flow_send_goods_layout.setVisibility(View.VISIBLE);
                holder.trade_flow_cancel_order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postAsynHttpDeleteOrder(orderNum);
                    }
                });
                holder.trade_flow_send_goods_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postAsynHttpUpdateOrderStatus(orderNum,"2");
                    }
                });
                break;
            case "2":  /**订单状态为2，待收货*/
                holder.trade_flow_receive_goods_layout.setVisibility(View.VISIBLE);
                holder.trade_flow_check_delivery_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case "3": /**订单状态为3，待评价*/
                holder.trade_flow_appraise_layout.setVisibility(View.VISIBLE);
                holder.trade_flow_appraise_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case "4": /**订单状态为4，退货/退款*/
                holder.trade_flow_refund_layout.setVisibility(View.VISIBLE);
                holder.trade_flow_refund_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.trade_flow_goods_iv)
        ImageView trade_flow_goods_iv;
        @BindView(R.id.trade_flow_goods_title_tv)
        TextView trade_flow_goods_title_tv;
        @BindView(R.id.trade_flow_goods_price)
        TextView trade_flow_goods_price;
        @BindView(R.id.trade_flow_goods_quantity)
        TextView trade_flow_goods_quantity;
        @BindView(R.id.trade_flow_actual_quantity)
        TextView trade_flow_actual_quantity;
        @BindView(R.id.trade_flow_total)
        TextView trade_flow_total;

        /**订单状态为1,待发货*/
        @BindView(R.id.trade_flow_send_goods_layout)
        LinearLayout trade_flow_send_goods_layout;
        @BindView(R.id.trade_flow_cancel_order_btn)
        Button trade_flow_cancel_order_btn;
        @BindView(R.id.trade_flow_send_goods_btn)
        Button trade_flow_send_goods_btn;

        /**订单状态为2，待收货*/
        @BindView(R.id.trade_flow_receive_goods_layout)
        LinearLayout trade_flow_receive_goods_layout;
        @BindView(R.id.trade_flow_check_delivery_btn)
        Button trade_flow_check_delivery_btn;

        /**订单状态为3，待评价*/
        @BindView(R.id.trade_flow_appraise_layout)
        LinearLayout trade_flow_appraise_layout;
        @BindView(R.id.trade_flow_appraise_btn)
        Button trade_flow_appraise_btn;

        /**订单状态为4，退货/退款*/
        @BindView(R.id.trade_flow_refund_layout)
        LinearLayout trade_flow_refund_layout;
        @BindView(R.id.trade_flow_refund_btn)
        Button trade_flow_refund_btn;

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

    private void postAsynHttpUpdateOrderStatus(String orderNum,String status){
        String url = Constants.HTTP_URL + "updateOrderStatus";

        Map<String,String> map = new HashMap<>();
        map.put("order_number",orderNum);
        map.put("order_status",status);

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
