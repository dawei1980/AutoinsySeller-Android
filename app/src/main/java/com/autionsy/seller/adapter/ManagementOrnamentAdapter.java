package com.autionsy.seller.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.autionsy.seller.entity.Ornament;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.autionsy.seller.utils.ScreenUtils;
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

public class ManagementOrnamentAdapter extends BaseAdapter {

    private Context context;
    List<Ornament> commodityList = new ArrayList<>();
    private String ornamentId;

    public ManagementOrnamentAdapter(Context context, List<Ornament> list){
        this.context = context;
        this.commodityList = list;
    }

    @Override
    public int getCount() {
        return commodityList.size();
    }

    @Override
    public Object getItem(int position) {
        return commodityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_management_ornament, null);

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
                .load(commodityList.get(position).getImageUrl())
                .apply(options)
                .into(holder.ornament_management_iv);

        holder.ornament_management_title_tv.setText(commodityList.get(position).getOrnamentName());
        holder.ornament_unit_price.setText(commodityList.get(position).getPrice());
        holder.ornament_unit_quantity.setText(commodityList.get(position).getQuantity());

        ornamentId = commodityList.get(position).getOrnamentId();

        holder.ornament_management_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.ornament_management_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return convertView;
    }

    //初始化并弹出对话框方法
    private void showDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.dailog_delete_commodity,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();

        Button btn_cancel_high_opion = view.findViewById(R.id.btn_cancel_high_opion);
        Button btn_agree_high_opion = view.findViewById(R.id.btn_agree_high_opion);

        btn_cancel_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_agree_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAsynHttpDeleteOrnament();
                dialog.dismiss();
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(context)/4*3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public class ViewHolder{
        @BindView(R.id.ornament_management_iv)
        ImageView ornament_management_iv;
        @BindView(R.id.ornament_management_title_tv)
        TextView ornament_management_title_tv;
        @BindView(R.id.ornament_unit_price)
        TextView ornament_unit_price;
        @BindView(R.id.ornament_unit_quantity)
        TextView ornament_unit_quantity;
        @BindView(R.id.ornament_management_edit_btn)
        Button ornament_management_edit_btn;
        @BindView(R.id.ornament_management_delete_btn)
        Button ornament_management_delete_btn;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    private void postAsynHttpDeleteOrnament(){
        String url = Constants.HTTP_URL + "deleteOrnamentInfo";

        Map<String,String> map = new HashMap<>();
        map.put("ornament_id",ornamentId);

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
