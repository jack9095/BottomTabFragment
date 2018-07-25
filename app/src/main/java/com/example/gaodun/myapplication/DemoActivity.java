package com.example.gaodun.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class DemoActivity extends AppCompatActivity {

    private PtrFrameLayout ptrFrameLayout;
    private RefreshHeadView_other headview;
    private TextView tv_ptr_a;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptrFrameLayout);
        tv_ptr_a = (TextView) findViewById(R.id.tv_ptr_a);
        headview = new RefreshHeadView_other(this);
        //设置headview
        ptrFrameLayout.setHeaderView(headview);
        //设置uihandler回调  因为headview实现了PtrUIHandler接口，所以这里还是headview
        ptrFrameLayout.addPtrUIHandler(headview);

        //阻尼系数 默认1.7f 越大下拉越吃力
        ptrFrameLayout.setResistance(2.7f);
        //触发刷新时移动的位置比例 默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        //回弹延时  默认 200ms，回弹到刷新高度所用时间
        ptrFrameLayout.setDurationToClose(200);
        //头部回弹时间 默认1000ms
        ptrFrameLayout.setDurationToCloseHeader(1000);
        //下拉刷新还是释放刷新  默认下拉刷新default is false
        ptrFrameLayout.setPullToRefresh(false);
        // default is true 刷新时是否保持头部
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        //刷新回调
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                //改变刷新头部
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                headview.setText("为您推送了105条新内容");
                                ptrFrameLayout.requestDisallowInterceptTouchEvent(false);
                            }
                        });
                        headview.setShow(false);
                    }
                }, 1000);

                //模拟刷新操作
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                        //刷新完成调用
                        ptrFrameLayout.refreshComplete();
                    }
                }, 3000);
                ptrFrameLayout.requestDisallowInterceptTouchEvent(true);

                //改变刷新头部
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        headview.setShow(true);
                    }
                }, 3500);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mData.addAll(mockNewData(10));
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //有焦点之后自动刷新
        if (hasFocus) {
            ptrFrameLayout.autoRefresh();
        }
    }

    private RecyclerView.Adapter<ItemViewHolder> mAdapter = new RecyclerView.Adapter<ItemViewHolder>() {
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(DemoActivity.this);
            return new ItemViewHolder(inflater.inflate(R.layout.item_recycler_view, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.update(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    };
    private final List<String> mData = new ArrayList<>();

    private List<String> mockNewData(int count) {
        final List<String> data = new ArrayList<>(count);
        for (int i = mData.size() + 1, max = i + count; i < max; i++) {
            data.add("第" + i + "项");
        }
        return data;
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }

        void update(String text) {
            mTextView.setText(text);
        }
    }
}
