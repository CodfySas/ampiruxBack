package com.osia.ampirux.repository.servicedefaultproduct
import com.osia.ampirux.model.ServiceDefaultProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("service_default_products.crud_repository")
interface ServiceDefaultProductRepository : CommonRepository<ServiceDefaultProduct> {
    fun getAllByServiceUuidIn(uuids: List<UUID>): List<ServiceDefaultProduct>

    @Query("SELECT COUNT(*) FROM service_default_product", nativeQuery = true)
    override fun count(): Long

    fun findAllByServiceUuid(uuids: UUID): List<ServiceDefaultProduct>
}
