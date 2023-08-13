package com.osia.osia_erp.factory

abstract class BaseFactory<T> {
    abstract fun create(): T
}
