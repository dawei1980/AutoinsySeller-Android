package com.autionsy.seller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.activity.NoticeActivity;
import com.autionsy.seller.activity.NoticeDetailActivity;
import com.autionsy.seller.adapter.NoticeAdapter;
import com.autionsy.seller.constant.Constants;
import com.autionsy.seller.entity.Notice;
import com.autionsy.seller.utils.OkHttp3Utils;

import org.json.JSONArray;
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

public class NoticeFragment extends BaseFragment {
    private View view;
    private Intent intent;

    @BindView(R.id.notice_lv)
    ListView notice_lv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;

    private NoticeAdapter noticeAdapter;
    private List<Notice> mList = new ArrayList<>();
    private String noticeId;
    private Notice notice;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.frag_notice, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.notice_center);

        back_btn.setVisibility(View.GONE);

        postAsynHttpNotice();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                getActivity().finish();
                break;
        }
    }

    private void postAsynHttpNotice(){
        String url = Constants.HTTP_URL + "noticeAll";
        Map<String,String> map = new HashMap<>();

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
                                notice = new Notice();
                                JSONArray jsonArray = jsonObject.getJSONArray(data);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObjectNotice = jsonArray.getJSONObject(i);
                                    notice.setContent(jsonObjectNotice.getString("noticeContent"));
                                    notice.setHeader(jsonObjectNotice.getString("noticePhoto"));
                                    notice.setTime(jsonObjectNotice.getString("noticeTime"));
                                    notice.setTitle(jsonObjectNotice.getString("noticeTitle"));
                                    notice.setId(jsonObjectNotice.getLong("noticeId"));
                                    mList.add(notice);
                                }

                                noticeAdapter = new NoticeAdapter(getActivity(),mList);
                                notice_lv.setAdapter(noticeAdapter);
                                notice_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        noticeId = String.valueOf(notice.getId());

                                        // 实例化一个Bundle
                                        Bundle bundle = new Bundle();
                                        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                                        bundle.putString("notice_id",noticeId);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                noticeAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(title_tv != null){
            title_tv = null;
        }
        if(notice_lv != null){
            notice_lv = null;
        }
        if(notice != null){
            notice = null;
        }
        if(mList.size() != 0){
            mList.clear();
        }
    }
}
