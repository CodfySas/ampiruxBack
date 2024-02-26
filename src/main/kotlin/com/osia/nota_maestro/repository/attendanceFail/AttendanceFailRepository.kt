package com.osia.nota_maestro.repository.attendanceFail

import com.osia.nota_maestro.model.AttendanceFail
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("attendanceFail.crud_repository")
interface AttendanceFailRepository :
    JpaRepository<AttendanceFail, UUID>,
    JpaSpecificationExecutor<AttendanceFail>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM attendance_fails", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM attendance_fails where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<AttendanceFail>

    @Modifying
    @Transactional
    @Query("UPDATE AttendanceFail SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidAttendanceIn(attendances: List<UUID>): List<AttendanceFail>
}
