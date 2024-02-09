package com.osia.nota_maestro.repository.studentSubject

import com.osia.nota_maestro.model.StudentSubject
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository("studentSubject.crud_repository")
interface StudentSubjectRepository :
    JpaRepository<StudentSubject, UUID>,
    JpaSpecificationExecutor<StudentSubject>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM student_subjects", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    fun findAllByUuidClassroomStudentInAndUuidSubjectIn(uuids: List<UUID>, uuidSubjects: List<UUID>): List<StudentSubject>

    fun findAllByUuidClassroomStudentAndUuidSubjectIn(uuid: UUID, uuidSubjects: List<UUID>): List<StudentSubject>

    fun findAllByUuidClassroomStudentInAndUuidSubject(uuids: List<UUID>, uuidSubject: UUID): List<StudentSubject>

    fun findAllByUuidClassroomStudentInAndUuidSubjectAndPeriod(classroomStudents: List<UUID>, uuidSubject: UUID, period: Int): List<StudentSubject>

    fun findAllByUuidClassroomStudentInAndUuidSubjectAndPeriodIn(classroomStudents: List<UUID>, uuidSubject: UUID, periods: List<Int>): List<StudentSubject>

    fun findAllByUuidClassroomStudentIn(classroomStudents: List<UUID>): List<StudentSubject>

    @Modifying
    @Transactional
    @Query("UPDATE StudentSubject SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
