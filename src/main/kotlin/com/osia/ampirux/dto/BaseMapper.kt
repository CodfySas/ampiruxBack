package com.osia.template.dto

interface BaseMapper<R, M, D> {
    fun toModel(r: R): M
    fun toDto(m: M): D
    fun toRequest(d: D): R
    fun update(r: R, m: M)
}
