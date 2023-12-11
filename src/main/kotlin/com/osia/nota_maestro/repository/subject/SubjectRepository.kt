package com.osia.nota_maestro.repository.subject

import com.osia.nota_maestro.model.Subject
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("subject.crud_repository")
interface SubjectRepository :
    JpaRepository<Subject, UUID>,
    JpaSpecificationExecutor<Subject>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM subjects", nativeQuery = true)
    override fun count(increment: Int): Long

    @Query(value = "SELECT * FROM subjects where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Subject>
}
