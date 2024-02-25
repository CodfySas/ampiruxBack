package com.osia.nota_maestro.repository.schedule

import com.osia.nota_maestro.model.Schedule
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("schedule.crud_repository")
interface ScheduleRepository :
    JpaRepository<Schedule, UUID>,
    JpaSpecificationExecutor<Schedule>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM schedules", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM schedules where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Schedule>

    @Modifying
    @Transactional
    @Query("UPDATE Schedule SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidSchoolAndUuidClassroom(uuid: UUID, classroom: UUID): List<Schedule>
}
