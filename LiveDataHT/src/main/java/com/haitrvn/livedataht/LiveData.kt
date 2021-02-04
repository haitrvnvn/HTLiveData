package com.haitrvn.htlivedata

import androidx.lifecycle.*

/**
 * Created by haitrvn on 1/28/21.
 */
fun <T, R> LiveData<T>.map(transformation: (T) -> R): LiveData<R> {
    return Transformations.map(this, transformation)
}

fun <A, B, C> LiveData<A>.zipWith(other: LiveData<B>, zipFunc: (A, B) -> C): LiveData<C> {
    return ZippedLiveData<A, B, C>(this, other, zipFunc)
}

/**
 * Not sure why using the direct method doesn't work...
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, f: (T?) -> Unit) {
    this.observe(owner, Observer { t -> f(t) })
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, f: (T) -> Unit) {
    this.observe(owner, object : Observer<T> {
        override fun onChanged(t: T?) {
            t?.let(f)
        }

    })
}

class ZippedLiveData<A, B, C>(
    private val ldA: LiveData<A>,
    private val ldB: LiveData<B>,
    private val zipFunc: (A, B) -> C
) : MediatorLiveData<C>() {

    private var lastValueA: A? = null
    private var lastValueB: B? = null

    init {
        addSource(ldA) {
            lastValueA = it
            emitZipped()
        }
        addSource(ldB) {
            lastValueB = it
            emitZipped()
        }
    }

    private fun emitZipped() {
        val valueA = lastValueA
        val valueB = lastValueB
        if (valueA != null && valueB != null) {
            value = zipFunc(valueA, valueB)
        }
    }
}