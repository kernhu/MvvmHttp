package com.xcion.mvvmokhttp.base;

import androidx.databinding.ViewDataBinding;


public abstract class BaseViewModelFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<V> {

    protected VM viewModel;

    /**
     * 初始化ViewModel
     */
    @Override
    public void initViewModel() {
        viewModel = getViewModel();
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
    }

    public abstract VM getViewModel();
}
