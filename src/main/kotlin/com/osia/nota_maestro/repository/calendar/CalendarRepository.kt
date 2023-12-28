package com.osia.nota_maestro.repository.calendar

import com.osia.nota_maestro.model.CalendarTask
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository("calendar.crud_repository")
interface CalendarRepository : JpaRepository<CalendarTask, UUID>, JpaSpecificationExecutor<CalendarTask>, BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM calendar_tasks", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    fun findAllByScheduleInitAfterAndScheduleFinishBeforeOrderByScheduleInitAsc(initTime: LocalDateTime, finishTime: LocalDateTime): List<CalendarTask>
}
