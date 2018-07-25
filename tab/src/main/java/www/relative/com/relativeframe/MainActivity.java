package www.relative.com.relativeframe;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import www.relative.com.relativeframe.fragment.GoodsFragment;
import www.relative.com.relativeframe.fragment.HomeFragment;
import www.relative.com.relativeframe.fragment.ShoppingFragment;
import www.relative.com.relativeframe.fragment.UserFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RadioButton btnHome, btnGoods, btnShopCart, btnUserVip;
    private RelativeLayout homePage, goodsPage, shoppingPage, usePage;

    private FragmentManager fragmentManager;

    private List<Fragment> fragmentList = new ArrayList<>();

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager = getSupportFragmentManager();
        initView();



        // 显示选中的 Fragment
        showFragment();

    }

    //初始化view和fragment
    private void initView() {

        fragmentList.add(new HomeFragment());
        fragmentList.add(new GoodsFragment());
        fragmentList.add(new ShoppingFragment());
        fragmentList.add(new UserFragment());

        homePage = (RelativeLayout) findViewById(R.id.homepage);
        goodsPage = (RelativeLayout) findViewById(R.id.goods_page);
        shoppingPage = (RelativeLayout) findViewById(R.id.shopping_cart_page);
        usePage = (RelativeLayout) findViewById(R.id.user_page);
        btnHome = (RadioButton) findViewById(R.id.home_button);
        btnGoods = (RadioButton) findViewById(R.id.goods_button);
        btnShopCart = (RadioButton) findViewById(R.id.shopping_cart_button);
        btnUserVip = (RadioButton) findViewById(R.id.user_button);
        homePage.setOnClickListener(this);
        goodsPage.setOnClickListener(this);
        shoppingPage.setOnClickListener(this);
        usePage.setOnClickListener(this);
    }

    @Override
    //RadioButton点击事件
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.homepage://首页
                currentIndex = 0;
                changeState(true, false, false, false);
                break;

            case R.id.goods_page://商品
                currentIndex = 1;
                changeState(false, true, false, false);
                break;

            case R.id.shopping_cart_page://进货单
                currentIndex = 2;
                changeState(false, false, true, false);
                break;

            case R.id.user_page://会员
                currentIndex = 3;
                changeState(false, false, false, true);
                break;

        }

        showFragment();

    }

    //改变RadioButton的状态
    private void changeState(boolean homeB, boolean goodsB, boolean shopB, boolean userB) {
        btnHome.setChecked(homeB);
        btnGoods.setChecked(goodsB);
        btnShopCart.setChecked(shopB);
        btnUserVip.setChecked(userB);
    }



    // 显示选中的 Fragment
    private void showFragment() {

        Fragment currentFragment = fragmentList.get(currentIndex);

        FragmentTransaction ft = fragmentManager.beginTransaction();

        //如果当前的fragment没有添加，添加当前的fragment
        if(!currentFragment.isAdded()){
            /**
             * 把fragment添加到MainActivity中
             * 第一个参数：fragment添加的位置；（必须是布局容器，一般是一个framelayout）
             * 第二个参数：要添加的fragment对象
             */
            ft.add(R.id.fragment_container, currentFragment);
        }

        for(int x=0;x<fragmentList.size();x++){
            if(fragmentList.get(x)!=currentFragment){
                //隐藏不需要显示的Fragment，仅仅是设为不可见，并不会销毁
                ft.hide(fragmentList.get(x));
            }else {
                ft.show(fragmentList.get(x));
            }
        }
        ft.commit();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
