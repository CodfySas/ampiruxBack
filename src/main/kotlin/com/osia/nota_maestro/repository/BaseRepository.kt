package com.osia.nota_maestro.repository

interface BaseRepository {
    fun count(increment: Int): Long
}
