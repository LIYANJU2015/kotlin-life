 package org.cuieney.videolife.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;

import org.cuieney.videolife.App;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.di.component.DaggerFragmentComponent;
import org.cuieney.videolife.di.component.FragmentComponent;
import org.cuieney.videolife.di.module.FragmentModule;
import org.cuieney.videolife.ui.act.MainActivity;
import org.cuieney.videolife.ui.fragment.video.VideoFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

 /**
  * Created by cuieney on 2016/8/2.
  * MVP Fragment基类
  */
 public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment implements BaseView{
     @Inject
     protected T mPresenter;
     protected View mView;
     protected Activity mActivity;
     protected Context mContext;
     private Unbinder mUnBinder;
     protected boolean isInited = false;

     @Override
     public void onAttach(Context context) {
         mActivity = (Activity) context;
         mContext = context;
         super.onAttach(context);
     }


     protected FragmentComponent getFragmentComponent(){
         return DaggerFragmentComponent.builder()
                 .appComponent(App.getInstance().getAppComponent())
                 .fragmentModule(getFragmentModule())
                 .build();
     }

     protected FragmentModule getFragmentModule(){
         return new FragmentModule(this);
     }

     @Nullable
     @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(getLayoutId(), null);
         initInject();
         return mView;
     }

     @Override
     public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         mPresenter.attachView(this);
         mUnBinder = ButterKnife.bind(this, view);
         if (savedInstanceState == null) {
             if (!isHidden()) {
                 isInited = true;
                 initEventAndData();
             }
         } else {
             if (!isHidden()) {
                 isInited = true;
                 initEventAndData();
             }
         }
     }

     @Override
     public void onHiddenChanged(boolean hidden) {
         super.onHiddenChanged(hidden);
         LogUtil.d("onHiddenChanged hidden " + hidden);
         if (!isInited && !hidden) {
             isInited = true;
             initEventAndData();
         } else {
             StatusBarUtil.setColor(getActivity(), MainActivity.sCurrentStatusColor);
         }
     }

     @Override
     public void onDestroyView() {
         super.onDestroyView();
         mUnBinder.unbind();
     }

     @Override
     public void onDestroy() {
         super.onDestroy();
         if (mPresenter != null) mPresenter.detachView();
     }

     protected abstract void initInject();
     protected abstract int getLayoutId();
     protected abstract void initEventAndData();
 }
