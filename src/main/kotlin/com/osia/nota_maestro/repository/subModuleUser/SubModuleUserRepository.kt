package com.osia.nota_maestro.repository.subModuleUser

import com.osia.nota_maestro.model.SubModuleUser
import com.osia.nota_maestro.repository.BaseRepository
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
    override fun count(schoolUuid: UUID): Long

    fun findAllByUuidUser(uuid: UUID): List<SubModuleUser>
}
