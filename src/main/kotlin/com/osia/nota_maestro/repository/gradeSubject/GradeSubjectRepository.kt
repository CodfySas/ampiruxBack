package com.osia.nota_maestro.repository.gradeSubject

import com.osia.nota_maestro.model.GradeSubject
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("gradeSubject.crud_repository")
interface GradeSubjectRepository :
    JpaRepository<GradeSubject, UUID>,
    JpaSpecificationExecutor<GradeSubject>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM grade_subjects", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    @Query(value = "SELECT * FROM grade_subjects where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<GradeSubject>

    fun findAllByUuidGradeIn(grades: List<UUID>): List<GradeSubject>

    fun findAllByUuidSubjectIn(subjects: List<UUID>): List<GradeSubject>

    @Modifying
    @Transactional
    @Query("UPDATE GradeSubject SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
