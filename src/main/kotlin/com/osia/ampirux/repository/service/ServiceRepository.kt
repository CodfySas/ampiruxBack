package com.osia.ampirux.repository.service
import com.osia.ampirux.model.Service
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("services.crud_repository")
interface ServiceRepository : CommonRepository<Service>
