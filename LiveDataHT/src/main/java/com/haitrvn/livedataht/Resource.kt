package com.haitrvn.htlivedata

sealed class Resource<T>
class ResourceSuccess<T>(val data: T?) : Resource<T>()
class ResourceLoading<T>(val isLoading: Boolean, val progress: Int) : Resource<T>()
class ResourceFailed<T>(val errorCode: Int) : Resource<T>()