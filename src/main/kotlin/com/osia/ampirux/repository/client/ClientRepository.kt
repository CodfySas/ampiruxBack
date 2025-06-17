package com.osia.ampirux.repository.client
import com.osia.ampirux.model.Client
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("client.crud_repository")
interface ClientRepository : CommonRepository<Client>
