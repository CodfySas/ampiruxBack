package com.osia.nota_maestro.repository.classroom

import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("classroom.crud_repository")
interface ClassroomRepository :
    JpaRepository<Classroom, UUID>,
    JpaSpecificationExecutor<Classroom>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classrooms", nativeQuery = true)
    override fun count(increment: Int): Long

    @Query(value = "SELECT * FROM classrooms where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Classroom>

    fun findAllByUuidGradeInAndYear(classrooms: List<UUID>, year: Int): List<Classroom>
}
