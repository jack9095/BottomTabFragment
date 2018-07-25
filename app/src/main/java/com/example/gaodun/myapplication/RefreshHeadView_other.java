package com.example.gaodun.myapplication;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class RefreshHeadView_other extends FrameLayout implements PtrUIHandler {
    private Context context;
    private RelativeLayout rl;
    private TextView tv;

    public RefreshHeadView_other(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.head_view, this);
        rl = (RelativeLayout) findViewById(R.id.progress_bar);
        tv = (TextView) findViewById(R.id.tv_ptr);
    }

    public RefreshHeadView_other(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeadView_other(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        //onUIRefreshComplete之后调用，用于复位
//        rl.setVisibility(View.GONE);
//        tv.setVisibility(View.VISIBLE);
//        setText("为您推送了15条新内容");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //开始下拉的时候调用一次
//        rl.setVisibility(View.VISIBLE);
//        tv.setVisibility(View.GONE);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        //正在刷新的时候调用，开始帧动画
//        rl.setVisibility(View.VISIBLE);
//        tv.setVisibility(View.GONE);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        //刷新完成的时候调用
//        rl.setVisibility(View.GONE);
//        tv.setVisibility(View.VISIBLE);
//        setText("为您推送了15条新内容");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //触发刷新的高度  默认是头部高度的1.2倍
        final int offsetToRefresh = ptrIndicator.getOffsetToRefresh();
        //当前下拉的高度
        final int currentPos = ptrIndicator.getCurrentPosY();
        //计算下拉的百分比
        int persent = (int) (((double) currentPos / offsetToRefresh) * 100);
        //只有在prepare状态的时候才执行下面的替换图片
        if (status!=PtrFrameLayout.PTR_STATUS_PREPARE) {
            return;
        }
        //根据不同的比例替换不同的图片和文字
//        if (persent < 50) {
//            setImageAndText(R.mipmap.ptr_loading_1, "下拉刷新");
//        } else if (persent < 60) {
//            setImageAndText(R.mipmap.ptr_loading_2, "下拉刷新");
//        } else if (persent < 70) {
//            setImageAndText(R.mipmap.ptr_loading_3, "下拉刷新");
//        } else if (persent < 80) {
//            setImageAndText(R.mipmap.ptr_loading_4, "下拉刷新");
//        } else if (persent < 100) {
//            setImageAndText(R.mipmap.ptr_loading_5, "下拉刷新");
//        } else {
//            setImageAndText(R.mipmap.ptr_loading_6, "松开松开~");
//        }
    }

    public void setText(String text) {
        tv.setText(text);
    }

    public void setShow(boolean b){
        if (b) {
            rl.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }else{
            rl.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }

    }
}
