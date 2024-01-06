package com.osia.nota_maestro.repository.studentSubject

import com.osia.nota_maestro.model.StudentNote
import com.osia.nota_maestro.model.StudentSubject
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("studentSubject.crud_repository")
interface StudentSubjectRepository :
    JpaRepository<StudentSubject, UUID>,
    JpaSpecificationExecutor<StudentSubject>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM student_subjects", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    fun findAllByUuidClassroomStudentInAndUuidSubjectIn(uuids: List<UUID>, uuidSubjects: List<UUID>): List<StudentSubject>


    @Modifying
    @Transactional
    @Query("UPDATE StudentSubject SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
