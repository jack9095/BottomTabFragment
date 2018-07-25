package com.example.gaodun.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends AppCompatActivity {

    private PtrFrameLayout ptrFrameLayout;
    private RefreshHeadView headview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptrFrameLayout);
        headview = new RefreshHeadView(this);
        //设置headview
        ptrFrameLayout.setHeaderView(headview);
        //设置uihandler回调  因为headview实现了PtrUIHandler接口，所以这里还是headview
        ptrFrameLayout.addPtrUIHandler(headview);

        //阻尼系数 默认1.7f 越大下拉越吃力
        ptrFrameLayout.setResistance(1.7f);
        //触发刷新时移动的位置比例 默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        //回弹延时  默认 200ms，回弹到刷新高度所用时间
        ptrFrameLayout.setDurationToClose(500);
        //头部回弹时间 默认1000ms
        ptrFrameLayout.setDurationToCloseHeader(1500);
        //下拉刷新还是释放刷新  默认下拉刷新default is false
        ptrFrameLayout.setPullToRefresh(false);
        // default is true 刷新时是否保持头部
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        //刷新回调
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //模拟刷新操作
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                        //刷新完成调用
                        ptrFrameLayout.refreshComplete();
                    }
                }, 3000);
            }
        });


//        ViewPager滑动冲突: disableWhenHorizontalMove()
//
//        长按LongPressed, setInterceptEventWhileWorking()
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //有焦点之后自动刷新
        if (hasFocus) {
            ptrFrameLayout.autoRefresh();
        }
    }
}
