package com.osia.osia_erp.repository.subModuleUser

import com.osia.osia_erp.model.SubModuleUser
import com.osia.osia_erp.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("subModuleUser.crud_repository")
interface SubModuleUserRepository :
    JpaRepository<SubModuleUser, UUID>,
    JpaSpecificationExecutor<SubModuleUser>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM sub_module_users", nativeQuery = true)
    override fun count(increment: Int): Long

    fun findAllByUuidUser(uuid: UUID): List<SubModuleUser>
}
