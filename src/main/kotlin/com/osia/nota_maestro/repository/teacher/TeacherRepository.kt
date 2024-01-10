package com.osia.nota_maestro.repository.teacher

import com.osia.nota_maestro.model.Teacher
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("teacher.crud_repository")
interface TeacherRepository :
    JpaRepository<Teacher, UUID>,
    JpaSpecificationExecutor<Teacher>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM teachers", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    fun findFirstByDni(dni: String): Optional<Teacher>

    @Query(value = "SELECT * FROM teachers where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Teacher>
}
