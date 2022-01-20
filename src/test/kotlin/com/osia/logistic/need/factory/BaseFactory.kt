package com.osia.logistic.need.factory

abstract class BaseFactory<T> {
    abstract fun create(): T
}
