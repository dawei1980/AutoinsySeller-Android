package com.autionsy.seller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.autionsy.seller.R;
import com.scrat.app.selectorlibrary.ImageSelector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderExpressDeliveryActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_express_delivery);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.order_express_delivery);
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(title_tv != null){
            title_tv = null;
        }
    }
}
