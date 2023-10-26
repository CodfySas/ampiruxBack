package com.osia.nota_maestro.repository.company

import com.osia.nota_maestro.model.Company
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("company.crud_repository")
interface CompanyRepository :
    JpaRepository<Company, UUID>,
    JpaSpecificationExecutor<Company>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM companies", nativeQuery = true)
    override fun count(increment: Int): Long
}
