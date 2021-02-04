package com.haitrvn.livedataht

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleStateLiveData<T> : MutableLiveData<Resource<T>>() {
    private val mPending: AtomicBoolean = AtomicBoolean(false)

    fun postSuccess(t: T?) {
        super.postValue(ResourceSuccess(t))
    }

    fun postFailed(errorCode: Int) {
        super.postValue(ResourceFailed(errorCode))
    }

    fun postLoading(isLoading: Boolean, process: Int = 0) {
        super.postValue(ResourceLoading(isLoading, process))
    }

    @MainThread
    override fun setValue(value: Resource<T>?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun postValue(value: Resource<T>?) {
        mPending.set(true)
        super.postValue(value)
    }

    @MainThread
    fun observeSuccess(
        owner: LifecycleOwner,
        observer: Observer<in T?>
    ): Pair<SingleStateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceSuccess<*>) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(resource.data as T?)
                }
            }
        }
        return Pair(this, owner)
    }

    @MainThread
    fun observeFailed(
        owner: LifecycleOwner,
        observer: Observer<in Int?>
    ): Pair<SingleStateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceFailed<*>) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(resource.errorCode)
                }
            }
        }
        return Pair(this, owner)
    }

    @MainThread
    fun observeLoading(
        owner: LifecycleOwner,
        observer: (isLoading: Boolean, progress: Int) -> Unit
    ): Pair<SingleStateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceLoading<*>) {
                if (mPending.compareAndSet(true, false)) {
                    observer(resource.isLoading, resource.progress)
                }
            }
        }
        return Pair(this, owner)
    }
}

fun <T> Pair<SingleStateLiveData<T>, LifecycleOwner>.observeSuccess(observer: Observer<in T?>): Pair<SingleStateLiveData<T>, LifecycleOwner> {
    first.observeSuccess(second, observer)
    return this
}

fun <T> Pair<SingleStateLiveData<T>, LifecycleOwner>.observeLoading(observer: (isLoading: Boolean, progress: Int) -> Unit): Pair<SingleStateLiveData<T>, LifecycleOwner> {
    first.observeLoading(second, observer)
    return this
}

fun <T> Pair<SingleStateLiveData<T>, LifecycleOwner>.observeFailed(observer: Observer<in Int?>): Pair<SingleStateLiveData<T>, LifecycleOwner> {
    first.observeFailed(second, observer)
    return this
}