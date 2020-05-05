package com.autionsy.seller.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.fragment.HomeFragment;
import com.autionsy.seller.fragment.MessageFragment;
import com.autionsy.seller.fragment.MineFragment;
import com.autionsy.seller.fragment.NoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;

    @BindView(R.id.home_iv)
    ImageView home_iv;
    @BindView(R.id.message_iv)
    ImageView message_iv;
    @BindView(R.id.mine_iv)
    ImageView mine_iv;
    @BindView(R.id.notice_iv)
    ImageView notice_iv;

    @BindView(R.id.home_tv)
    TextView home_tv;
    @BindView(R.id.message_tv)
    TextView message_tv;
    @BindView(R.id.mine_tv)
    TextView mine_tv;
    @BindView(R.id.notice_tv)
    TextView notice_tv;

    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFrag;
    private NoticeFragment mNoticeFragment;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        setChioceItem(0);   // 初始化页面加载时显示第一个选项卡
    }

    @OnClick({R.id.home_layout,
            R.id.message_layout,
            R.id.mine_layout,
            R.id.notice_layout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_layout:
                setChioceItem(0);
                break;
            case R.id.message_layout:
                setChioceItem(1);
                break;
            case R.id.notice_layout:
                setChioceItem(2);
                break;
            case R.id.mine_layout:
                setChioceItem(3);
                break;
        }
    }

    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
                home_iv.setBackgroundResource(R.mipmap.home_press);
                home_tv.setTextColor(getResources().getColor(R.color.orange_text));

                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fragment_content, mHomeFragment);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }

                break;

            case 1:
                message_iv.setBackgroundResource(R.mipmap.notice_press);
                message_tv.setTextColor(getResources().getColor(R.color.orange_text));

                // 如果fg1为空，则创建一个并添加到界面上
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.fragment_content, mMessageFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mMessageFragment);
                }
                break;

            case 2:
                notice_iv.setBackgroundResource(R.mipmap.message_press);
                notice_tv.setTextColor(getResources().getColor(R.color.orange_text));

                // 如果fg1为空，则创建一个并添加到界面上
                if (mNoticeFragment == null) {
                    mNoticeFragment = new NoticeFragment();
                    fragmentTransaction.add(R.id.fragment_content, mNoticeFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mNoticeFragment);
                }
                break;

            case 3:
                mine_iv.setBackgroundResource(R.mipmap.mine_press);
                mine_tv.setTextColor(getResources().getColor(R.color.orange_text));

                if (mMineFrag == null) {
                    mMineFrag = new MineFragment();
                    fragmentTransaction.add(R.id.fragment_content, mMineFrag);
                } else {
                    fragmentTransaction.show(mMineFrag);
                }
                break;
        }
        fragmentTransaction.commit();   // 提交
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {

        home_iv.setBackgroundResource(R.mipmap.home);
        home_tv.setTextColor(getResources().getColor(R.color.black));

        message_iv.setBackgroundResource(R.mipmap.notice);
        message_tv.setTextColor(getResources().getColor(R.color.black));

        notice_iv.setBackgroundResource(R.mipmap.message);
        notice_tv.setTextColor(getResources().getColor(R.color.black));

        mine_iv.setBackgroundResource(R.mipmap.mine);
        mine_tv.setTextColor(getResources().getColor(R.color.black));
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mHomeFragment != null) {
            fragmentTransaction.hide(mHomeFragment);
        }

        if (mMessageFragment != null) {
            fragmentTransaction.hide(mMessageFragment);
        }

        if (mNoticeFragment != null) {
            fragmentTransaction.hide(mNoticeFragment);
        }

        if (mMineFrag != null) {
            fragmentTransaction.hide(mMineFrag);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(home_iv != null){
            home_iv = null;
        }
        if(message_iv != null){
            message_iv = null;
        }
        if(mine_iv != null){
            mine_iv = null;
        }
        if(message_tv != null){
            message_tv = null;
        }
        if(mHomeFragment != null){
            mHomeFragment = null;
        }
        if(mMessageFragment != null){
            mMessageFragment = null;
        }
        if(mMineFrag != null){
            mMineFrag = null;
        }
    }
}
