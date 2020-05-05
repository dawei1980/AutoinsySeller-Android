package com.autionsy.seller.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Recruit;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.autionsy.seller.utils.ScreenUtils;
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

public class ManagementRecruitAdapter extends BaseAdapter {

    private Context context;
    List<Recruit> commodityList = new ArrayList<>();
    private String recruitId;

    public ManagementRecruitAdapter(Context context, List<Recruit> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_management_recruit, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.recruit_management_title_tv.setText(commodityList.get(position).getTitle());
        holder.recruit_management_start_salary_tv.setText(commodityList.get(position).getStartSalary());
        holder.recruit_management_end_salary_tv.setText(commodityList.get(position).getEndSalary());
        holder.recruit_management_company_name_tv.setText(commodityList.get(position).getCompanyName());

        recruitId = commodityList.get(position).getRecruitId();

        holder.recruit_management_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.recruit_management_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.recruit_management_title_tv)
        TextView recruit_management_title_tv;
        @BindView(R.id.recruit_management_start_salary_tv)
        TextView recruit_management_start_salary_tv;
        @BindView(R.id.recruit_management_end_salary_tv)
        TextView recruit_management_end_salary_tv;
        @BindView(R.id.recruit_management_company_name_tv)
        TextView recruit_management_company_name_tv;
        @BindView(R.id.recruit_management_edit_btn)
        Button recruit_management_edit_btn;
        @BindView(R.id.recruit_management_delete_btn)
        Button recruit_management_delete_btn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //初始化并弹出对话框方法
    private void showDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dailog_delete_commodity, null, false);
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
                postAsynHttpDeleteGoods();
                dialog.dismiss();
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(context) / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void postAsynHttpDeleteGoods() {
        String url = Constants.HTTP_URL + "deleteRecruitInfo";

        Map<String, String> map = new HashMap<>();
        map.put("recruit_id", recruitId);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if ("200".equals(resultCode)) {
                                notifyDataSetChanged();
                            } else if ("403".equals(resultCode)) {
                                Toast.makeText(context, R.string.param_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.login_fail, Toast.LENGTH_SHORT).show();
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
