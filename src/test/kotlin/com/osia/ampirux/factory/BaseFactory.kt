package com.osia.template.factory

abstract class BaseFactory<T> {
    abstract fun create(): T
}
