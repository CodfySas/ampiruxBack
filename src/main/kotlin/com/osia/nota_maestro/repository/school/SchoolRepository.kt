package com.osia.nota_maestro.repository.school

import com.osia.nota_maestro.model.School
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("school.crud_repository")
interface SchoolRepository :
    JpaRepository<School, UUID>,
    JpaSpecificationExecutor<School>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM schools", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long
}
