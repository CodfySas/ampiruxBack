package com.osia.nota_maestro.factory

abstract class BaseFactory<T> {
    abstract fun create(): T
}
