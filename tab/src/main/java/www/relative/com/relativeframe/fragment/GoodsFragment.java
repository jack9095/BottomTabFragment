package www.relative.com.relativeframe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.relative.com.relativeframe.R;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class GoodsFragment extends BaseFragment{

    private TextView tv;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==23){
                tv.setText("加载完毕");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_goods, null);

        tv = (TextView) view.findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    handler.sendEmptyMessage(23);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        return view;
    }
}
