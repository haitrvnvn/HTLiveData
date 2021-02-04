package com.haitrvn.livedataht

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class StateLiveData<T> : MutableLiveData<Resource<T>>() {
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
    fun observeSuccess(
        owner: LifecycleOwner,
        observer: Observer<in T?>
    ): Pair<StateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceSuccess<*>) {
                observer.onChanged(resource.data as T?)
            }
        }
        return Pair(this, owner)
    }

    @MainThread
    fun observeFailed(
        owner: LifecycleOwner,
        observer: Observer<in Int?>
    ): Pair<StateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceFailed<*>) {
                observer.onChanged(resource.errorCode)
            }
        }
        return Pair(this, owner)
    }

    @MainThread
    fun observeLoading(
        owner: LifecycleOwner,
        observer: (isLoading: Boolean, progress: Int) -> Unit
    ): Pair<StateLiveData<T>, LifecycleOwner> {
        super.observe(owner) { resource ->
            if (resource is ResourceLoading<*>) {
                observer(resource.isLoading, resource.progress)
            }
        }
        return Pair(this, owner)
    }
}

fun <T> Pair<StateLiveData<T>, LifecycleOwner>.observeSuccess(observer: Observer<in T?>): Pair<StateLiveData<T>, LifecycleOwner> {
    first.observeSuccess(second, observer)
    return this
}

fun <T> Pair<StateLiveData<T>, LifecycleOwner>.observeLoading(observer: (isLoading: Boolean, progress: Int) -> Unit): Pair<StateLiveData<T>, LifecycleOwner> {
    first.observeLoading(second, observer)
    return this
}

fun <T> Pair<StateLiveData<T>, LifecycleOwner>.observeFailed(observer: Observer<in Int?>): Pair<StateLiveData<T>, LifecycleOwner> {
    first.observeFailed(second, observer)
    return this
}