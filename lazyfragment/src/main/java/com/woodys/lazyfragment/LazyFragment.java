package com.woodys.lazyfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * <h1>懒加载Fragment</h1> 只有创建并显示的时候才会调用onCreateViewLazy方法<br>
 * <br>
 * <p/>
 * 懒加载的原理onCreateView的时候Fragment有可能没有显示出来。<br>
 * 但是调用到setUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true的时候就说明有显示出来<br>
 * 但是要考虑onCreateView和setUserVisibleHint的先后问题所以才有了下面的代码
 * <p/>
 * 注意：<br>
 * 《1》原先的Fragment的回调方法名字后面要加个Lazy，比如Fragment的onCreateView方法， 就写成onCreateViewLazy <br>
 * 《2》使用该LazyFragment会导致多一层布局深度
 *
 * @author woodys
 */
public abstract class LazyFragment extends BaseFragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private boolean isInit = false;//真正要显示的View是否已经被初始化（正常加载）
    private Bundle savedInstanceState;
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
    private boolean isSetVisibleToUser = false;//判断是否设置过setUserVisibleHint()的值。
    private boolean isVisibleToUser = false;
    private boolean isLazyLoad = false;
    private FrameLayout layout;
    private boolean isStart = false;//是否处于可见状态，in the screen

    @Override
    protected final void onCreateView(Bundle savedInstanceState) {
        if (DEBUG) Log.d(TAG, "onCreateView() : " + "isVisibleToUser:" + getVisibleHint());
        super.onCreateView(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        }
        //判断是否懒加载
        if (isLazyLoad) {
            //一旦isVisibleToUser==true即可对真正需要的显示内容进行加载(注意：在FragmentStatePagerAdapter里面getUserVisibleHint()获取会出问题)
            if (getVisibleHint() && !isInit) {
                this.savedInstanceState = savedInstanceState;
                onCreateViewLazy(savedInstanceState);
                isInit = true;
            } else {
                //进行懒加载
                layout = new FrameLayout(getApplicationContext());
                layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_lazy_loading, null,false);
                layout.addView(onCreateLazyoadingView());
                super.setContentView(layout);
            }
        } else {
            //不需要懒加载，开门见山，调用onCreateViewLazy正常加载显示内容即可
            onCreateViewLazy(savedInstanceState);
            isInit = true;
        }
    }
    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 获取懒加载等待界面
     * @return
     */
    protected abstract View onCreateLazyoadingView();

    //当Fragment被滑到可见的位置时，调用
    protected void onFragmentStartLazy() {
        if (DEBUG) Log.d(TAG, "onFragmentStartLazy() called with: " + "");
    }

    //当Fragment被滑到不可见的位置，offScreen时，调用
    protected void onFragmentStopLazy() {
        if (DEBUG) Log.d(TAG, "onFragmentStopLazy() called with: " + "");
    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {
        if (DEBUG) Log.d(TAG, "onCreateViewLazy() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    protected void onResumeLazy() {
        if (DEBUG) Log.d(TAG, "onResumeLazy() called with: " + "");
    }

    protected void onPauseLazy() {
        if (DEBUG) Log.d(TAG, "onPauseLazy() called with: " + "");
    }

    protected void onDestroyViewLazy() {}

    /**
     * 判断当前fragment的显示状态
     * @return
     */
    private boolean getVisibleHint(){
        return isSetVisibleToUser?isVisibleToUser:getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (DEBUG) Log.d(TAG, "setUserVisibleHint() called with: " + "isVisibleToUser = [" + isVisibleToUser + "]");
        this.isSetVisibleToUser = true;
        //一旦isVisibleToUser==true即可进行对真正需要的显示内容的加载
        this.isVisibleToUser = isVisibleToUser;
        //可见，但还没被初始化
        if (isVisibleToUser && !isInit && getContentView() != null) {
            onCreateViewLazy(savedInstanceState);
            isInit = true;
            onResumeLazy();
        }
        //已经被初始化（正常加载）过了
        if (isInit && getContentView() != null) {
            if (isVisibleToUser) {
                isStart = true;
                onFragmentStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        //判断若isLazyLoad==true,移除所有lazy view，加载真正要显示的view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            View view = inflater.inflate(layoutResID, layout, false);
            layout.addView(view);
        }
        //否则，开门见山，直接加载
        else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        //判断若isLazyLoad==true,移除所有lazy view，加载真正要显示的view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            layout.addView(view);
        }
        //否则，开门见山，直接加载
        else {
            super.setContentView(view);
        }
    }

    @Deprecated
    @Override
    public final void onStart() {
        if (DEBUG) Log.d(TAG, "onStart() : " + "getUserVisibleHint():" + getVisibleHint());
        super.onStart();
        if (isInit && !isStart && getVisibleHint()) {
            isStart = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        if (DEBUG) Log.d(TAG, "onStop() called: " + "getUserVisibleHint():" + getVisibleHint());
        if (isInit && isStart && getVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    @Override
    @Deprecated
    public final void onResume() {
        if (DEBUG) Log.d(TAG, "onResume() : " + "getUserVisibleHint():" + getVisibleHint());
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        if (DEBUG) Log.d(TAG, "onPause() : " + "getUserVisibleHint():" + getVisibleHint());
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        if (DEBUG) Log.d(TAG, "onDestroyView() : " + "getUserVisibleHint():" + getVisibleHint());
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
        isVisibleToUser = false;
        isSetVisibleToUser = false;
    }
}