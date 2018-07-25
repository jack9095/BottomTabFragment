package www.relative.com.relativeframe.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class BaseFragment extends Fragment {

    private Activity mActivity;

    @Override
    /**
     * 当Fragment与Activity发生关联时调用
     *
     * 在Fragment基类里设置一个Activity mActivity的全局变量，在onAttach(Activity activity)里赋值，
     * 使用mActivity代替getActivity()，保证Fragment即使在onDetach后，仍持有Activity的引用
     */
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

}
