package com.osia.nota_maestro.repository

import java.util.UUID

interface BaseRepository {
    fun count(increment: Int, schoolUuid: UUID): Long
}
