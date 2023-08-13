package com.osia.osia_erp.repository.module

import com.osia.osia_erp.model.Module
import com.osia.osia_erp.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("module.crud_repository")
interface ModuleRepository :
    JpaRepository<Module, UUID>,
    JpaSpecificationExecutor<Module>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM modules", nativeQuery = true)
    override fun count(increment: Int): Long

    fun findAllByUuidInOrderByOrder(list: List<UUID>): List<Module>
}
