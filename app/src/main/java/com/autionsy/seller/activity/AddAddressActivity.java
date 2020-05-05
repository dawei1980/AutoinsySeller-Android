package com.autionsy.seller.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.pickview.GetJsonDataUtil;
import com.autionsy.seller.pickview.ShengBean;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.address_receiver_et)
    EditText address_receiver_et;
    @BindView(R.id.address_receiver_mobile_num_et)
    EditText address_receiver_mobile_num_et;
    @BindView(R.id.address_area_layout)
    RelativeLayout address_area_layout;
    @BindView(R.id.address_detail_et)
    EditText address_detail_et;
    @BindView(R.id.address_area_tv)
    TextView address_area_tv;

    @BindView(R.id.title_tv)
    TextView title_tv;

    private String receiver;
    private String mobileNum;
    private String addressDetail;

    private String addressStr;

    //  省
    private List<ShengBean> options1Items = new ArrayList<ShengBean>();
    //  市
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    //  区
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_address);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.new_address);
    }

    @OnClick({R.id.address_area_layout,R.id.back_btn,R.id.add_address_btn})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.address_area_layout:
//              解析数据
                parseData();
//              展示省市区选择器
                showPickerView();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_address_btn:
                postHttpAddress();
                break;
        }
    }

    /**
     * 解析数据并组装成自己想要的list
     */
    private void parseData(){
        String jsonStr = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
//     数据解析
        Gson gson =new Gson();
        java.lang.reflect.Type type =new TypeToken<List<ShengBean>>(){}.getType();
        List<ShengBean>shengList=gson.fromJson(jsonStr, type);
//     把解析后的数据组装成想要的list
        options1Items = shengList;
//     遍历省
        for(int i = 0; i <shengList.size() ; i++) {
//         存放城市
            ArrayList<String> cityList = new ArrayList<>();
//         存放区
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
//         遍历市
            for(int c = 0; c <shengList.get(i).city.size() ; c++) {
//        拿到城市名称
                String cityName = shengList.get(i).city.get(c).name;
                cityList.add(cityName);

                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                if (shengList.get(i).city.get(c).area == null || shengList.get(i).city.get(c).area.size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(shengList.get(i).city.get(c).area);
                }
                province_AreaList.add(city_AreaList);
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    /**
     * 展示选择器
     */
    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                addressStr = options1Items.get(options1).name +
                            options2Items.get(options1).get(options2) +
                            options3Items.get(options1).get(options2).get(options3);

                Toast.makeText(AddAddressActivity.this, addressStr, Toast.LENGTH_SHORT).show();
                address_area_tv.setText(addressStr);
            }
        }).setTitleText("城市选择")
          .setDividerColor(Color.BLACK)
          .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
          .setContentTextSize(20)
          .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void postHttpAddress(){
        receiver = address_receiver_et.getText().toString().trim();
        mobileNum = address_receiver_mobile_num_et.getText().toString().trim();
        addressDetail = address_detail_et.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("seller_login_data", MODE_PRIVATE); //获取对象，读取data文件
        String username = prefs.getString("USERNAME", ""); //获取文件中的数据

        String url = Constants.HTTP_URL + "addAddress";
        Map<String,String> map = new HashMap<>();
        map.put("username", username);
        map.put("mobile_phone_num", mobileNum);
        map.put("address", addressStr);
        map.put("address_detail", addressDetail);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject  = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.save_address,Toast.LENGTH_SHORT).show();
                            }else if("411".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.user_already_register,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(address_receiver_et != null){
            address_receiver_et = null;
        }
        if(address_receiver_mobile_num_et != null){
            address_receiver_mobile_num_et = null;
        }
        if(address_area_layout != null){
            address_area_layout = null;
        }
        if(address_detail_et != null){
            address_detail_et = null;
        }
        if(address_area_tv != null){
            address_area_tv = null;
        }
        if(title_tv != null){
            title_tv = null;
        }
    }

}

