package com.osia.nota_maestro.repository.log

import com.osia.nota_maestro.model.Log
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@Repository("log.crud_repository")
interface LogRepository :
    JpaRepository<Log, UUID>,
    JpaSpecificationExecutor<Log>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM logs", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM logs where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Log>

    @Modifying
    @Transactional
    @Query("UPDATE Log SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByDayBetweenAndUuidSchool(day1: LocalDate, day2: LocalDate, uuid: UUID): List<Log>

    fun findAllByDayAndUuidSchool(day: LocalDate, uuid: UUID): List<Log>
}
