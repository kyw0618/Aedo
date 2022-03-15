package com.example.my_heaven.util.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_heaven.util.network.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLottieLoading = MutableLiveData(false)
    val isLottieLoading: LiveData<Boolean> get() = _isLottieLoading
    private var _backClick = SingleLiveEvent<Unit>()


    val backClick: LiveData<Unit> get() = _backClick

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    fun showLottieProgress() {
        _isLottieLoading.value = true
    }

    fun hideLottieProgress() {
        _isLottieLoading.value = false
    }

    fun onBackClick() {
        _backClick.call()
    }

}