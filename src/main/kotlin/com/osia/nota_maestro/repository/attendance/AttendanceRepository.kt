package com.osia.nota_maestro.repository.attendance

import com.osia.nota_maestro.model.Attendance
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("attendance.crud_repository")
interface AttendanceRepository :
    JpaRepository<Attendance, UUID>,
    JpaSpecificationExecutor<Attendance>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM attendances", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM attendances where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Attendance>

    @Modifying
    @Transactional
    @Query("UPDATE Attendance SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomAndUuidSubjectAndMonth(classroom: UUID, subject: UUID, month: Int): List<Attendance>

    fun getAllByUuidClassroomAndMonthAndUuidSubjectIsNull(classroom: UUID, month: Int): List<Attendance>
}
