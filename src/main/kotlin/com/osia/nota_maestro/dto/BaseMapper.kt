package com.osia.nota_maestro.dto

interface BaseMapper<R, M, D> {
    fun toModel(r: R): M
    fun toDto(m: M): D
    fun toRequest(d: D): R
    fun update(r: R, m: M)
}
