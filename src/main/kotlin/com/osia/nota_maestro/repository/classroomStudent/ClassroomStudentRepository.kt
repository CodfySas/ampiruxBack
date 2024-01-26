package com.osia.nota_maestro.repository.classroomStudent

import com.osia.nota_maestro.model.ClassroomStudent
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("classroomStudent.crud_repository")
interface ClassroomStudentRepository :
    JpaRepository<ClassroomStudent, UUID>,
    JpaSpecificationExecutor<ClassroomStudent>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classroom_students", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM classroom_students where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ClassroomStudent>

    fun getAllByUuidClassroomIn(classrooms: List<UUID>): List<ClassroomStudent>

    fun findAllByUuidStudentInAndUuidClassroomIn(uuidStudents: List<UUID>, classrooms: List<UUID>): List<ClassroomStudent>

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomStudent SET deleted = true, deletedAt = now() WHERE uuidStudent IN :uuids AND uuidClassroom IN :classrooms")
    fun deleteByUuidStudentsAndClassRooms(uuids: List<UUID>, classrooms: List<UUID>)

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomStudent SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomStudent SET deleted = true, deletedAt = now() WHERE uuidClassroom IN :uuids")
    fun deleteByUuidClassroom(uuids: List<UUID>)

    fun findAllByUuidStudent(uuid: UUID): List<ClassroomStudent>

    fun findAllByUuidClassroom(uuid: UUID): List<ClassroomStudent>

    fun findAllByUuidClassroomIn(uuids: List<UUID>): List<ClassroomStudent>

    fun findFirstByUuidClassroomAndUuidStudent(uuidClassroom: UUID, uuidStudent: UUID): Optional<ClassroomStudent>
}
