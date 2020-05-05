package com.autionsy.seller.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.activity.AllOrderListActivity;
import com.autionsy.seller.activity.ManagementGoodsActivity;
import com.autionsy.seller.activity.ManagementLeaseActivity;
import com.autionsy.seller.activity.ManagementRecruitActivity;
import com.autionsy.seller.activity.ManagementRescueActivity;
import com.autionsy.seller.activity.ManagementServiceActivity;
import com.autionsy.seller.activity.OrderListActivity;
import com.autionsy.seller.activity.ManagementOrnamenActivity;
import com.autionsy.seller.activity.SettingActivity;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Seller;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends BaseFragment {
    private View view;

    @BindView(R.id.mine_header_iv)
    ImageView mine_header_iv;
    @BindView(R.id.mine_username_tv)
    TextView mine_username_tv;

    private Intent intent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.frag_mine, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this, view);

        postAsynHttpMine();
        return view;
    }

    private void postAsynHttpMine(){
        final Seller seller = new Seller();
        SharedPreferences prefs = getActivity().getSharedPreferences("seller_login_data", MODE_PRIVATE); //获取对象，读取data文件
        String username = prefs.getString("USERNAME", ""); //获取文件中的数据

        String url = Constants.HTTP_URL + "getSellerInfoByUsername";

        Map<String,String> map = new HashMap<>();
        map.put("username",username);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){

                                JSONObject jsonObjectSeller = jsonObject.getJSONObject(data);

                                seller.setNickName(jsonObjectSeller.getString("nickName"));
                                seller.setHeadUrl(jsonObjectSeller.getString("headUrl"));

                                RequestOptions options = new RequestOptions()
                                        .placeholder(R.mipmap.default_header)
                                        .error(R.mipmap.default_header);
                                Glide.with(getActivity())
                                        .load(seller.getHeadUrl())
                                        .apply(RequestOptions.circleCropTransform())
                                        .apply(options)
                                        .into(mine_header_iv);

                            }else if("403".equals(resultCode)){
                                Toast.makeText(getActivity(),R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.mine_setting_iv,
            R.id.wait_send_stock,
            R.id.wait_receive_stock,
            R.id.wait_comment_stock,
            R.id.exchange_stock,
            R.id.check_all_order,
            R.id.mine_product_management_layout,
            R.id.mine_ornament_management_text_layout,
            R.id.mine_service_management_text_layout,
            R.id.mine_lease_management_text_layout,
            R.id.mine_recuit_management_text_layout,
            R.id.mine_rescue_management_text_layout,})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.check_all_order:
                intent = new Intent(getActivity(), AllOrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_setting_iv:
                intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.wait_send_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","1");
                startActivity(intent);
                break;
            case R.id.wait_receive_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","2");
                startActivity(intent);
                break;
            case R.id.wait_comment_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","3");
                startActivity(intent);
                break;
            case R.id.exchange_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","4");
                startActivity(intent);
                break;
            case R.id.mine_product_management_layout: //1代表汽配商品管理
                intent = new Intent(getActivity(), ManagementGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_ornament_management_text_layout: //2代表内饰商品管理
                intent = new Intent(getActivity(), ManagementOrnamenActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_service_management_text_layout: //3代表服务管理
                intent = new Intent(getActivity(), ManagementServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_lease_management_text_layout: //4代表租赁管理
                intent = new Intent(getActivity(), ManagementLeaseActivity.class);
                startActivity(intent);
                break;
            case  R.id.mine_recuit_management_text_layout: //5代表招聘管理
                intent = new Intent(getActivity(), ManagementRecruitActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_rescue_management_text_layout: //6代表道路救援管理
                intent = new Intent(getActivity(), ManagementRescueActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mine_header_iv != null){
            mine_header_iv = null;
        }
        if(mine_username_tv != null){
            mine_username_tv = null;
        }
        if(view != null){
            view = null;
        }
    }
}
