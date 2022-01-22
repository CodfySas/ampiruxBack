package com.osia.logistic.need.repository.client

import com.osia.logistic.need.model.Client
import com.osia.logistic.need.repository.baseRepository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("client.crud_repository")
interface ClientRepository : JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client>, BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM client", nativeQuery = true)
    override fun countAll(): Long

    fun findByUuidIn(list: List<UUID>): List<Client>
}
