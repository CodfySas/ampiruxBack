package com.osia.ampirux.factory

abstract class BaseFactory<T> {
    abstract fun create(): T
}
