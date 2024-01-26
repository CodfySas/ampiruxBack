package com.osia.nota_maestro.repository.classroomSubject

import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("classroomSubject.crud_repository")
interface ClassroomSubjectRepository :
    JpaRepository<ClassroomSubject, UUID>,
    JpaSpecificationExecutor<ClassroomSubject>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classroom_subjects", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM classroom_subjects where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ClassroomSubject>

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomSubject SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomIn(classrooms: List<UUID>): List<ClassroomSubject>

    fun getAllByUuidClassroom(classroom: UUID): List<ClassroomSubject>

    fun getAllByUuidTeacher(uuidTeacher: UUID): List<ClassroomSubject>
}
