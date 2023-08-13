package com.osia.osia_erp.repository.client

import com.osia.osia_erp.model.Client
import com.osia.osia_erp.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("client.crud_repository")
interface ClientRepository :
    JpaRepository<Client, UUID>,
    JpaSpecificationExecutor<Client>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM clients", nativeQuery = true)
    override fun count(increment: Int): Long
}
