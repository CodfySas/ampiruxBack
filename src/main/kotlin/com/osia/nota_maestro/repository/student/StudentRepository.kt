package com.osia.nota_maestro.repository.student

import com.osia.nota_maestro.model.Student
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("student.crud_repository")
interface StudentRepository :
    JpaRepository<Student, UUID>,
    JpaSpecificationExecutor<Student>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    fun findFirstByDni(dni: String): Optional<Student>

    @Query(value = "SELECT * FROM students where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Student>
}
