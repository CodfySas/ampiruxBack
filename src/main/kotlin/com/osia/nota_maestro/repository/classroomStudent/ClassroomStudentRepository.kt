package com.osia.nota_maestro.repository.classroomStudent

import com.osia.nota_maestro.model.ClassroomStudent
import com.osia.nota_maestro.model.Student
import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("classroomStudent.crud_repository")
interface ClassroomStudentRepository :
    JpaRepository<ClassroomStudent, UUID>,
    JpaSpecificationExecutor<ClassroomStudent>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classroom_students", nativeQuery = true)
    override fun count(increment: Int): Long

    @Query(value = "SELECT * FROM classroom_students where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ClassroomStudent>

    fun getAllByUuidClassroomIn(classrooms: List<UUID>): List<ClassroomStudent>
}
