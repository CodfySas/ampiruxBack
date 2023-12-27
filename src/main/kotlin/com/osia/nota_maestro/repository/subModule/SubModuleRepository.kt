package com.osia.nota_maestro.repository.subModule

import com.osia.nota_maestro.model.SubModule
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("subModule.crud_repository")
interface SubModuleRepository :
    JpaRepository<SubModule, UUID>,
    JpaSpecificationExecutor<SubModule>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM sub_modules", nativeQuery = true)
    override fun count(increment: Int, schoolUuid: UUID): Long

    fun findAllByUuidInOrderByOrdered(list: List<UUID>): List<SubModule>
}
