package com.osia.nota_maestro.repository.studentNote

import com.osia.nota_maestro.model.StudentNote
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("studentNote.crud_repository")
interface StudentNoteRepository :
    JpaRepository<StudentNote, UUID>,
    JpaSpecificationExecutor<StudentNote>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM student_notes", nativeQuery = true)
    override fun count(increment: Int): Long

    @Query(value = "SELECT * FROM student_notes where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<StudentNote>

    fun findAllByUuidClassroomStudentIn(uuids: List<UUID>): List<StudentNote>

    fun findAllByUuidClassroomStudentInAndUuidSubjectIn(uuids: List<UUID>, uuidSubjects: List<UUID>): List<StudentNote>

    @Modifying
    @Transactional
    @Query("UPDATE StudentNote SET deleted = true, deletedAt = now() WHERE uuid = :uuid")
    fun deleteByUuid(uuid: UUID)

    @Modifying
    @Transactional
    @Query("UPDATE StudentNote SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    @Query("SELECT MAX(cnt) AS max_count " +
            "FROM (" +
            "    SELECT uuid_classroom_student, COUNT(*) AS cnt " +
            "    FROM student_notes " +
            "    WHERE uuid_classroom_student IN ?1 " +
            "      AND period = ?2 " +
            "      AND uuid_subject = ?3 " +
            "      AND deleted = false " +
            "    GROUP BY uuid_classroom_student" +
            ") AS counts;", nativeQuery = true)
    fun getNoteMAx(uuids: List<UUID>, period: Int, uuidSubject: UUID): Int
}
