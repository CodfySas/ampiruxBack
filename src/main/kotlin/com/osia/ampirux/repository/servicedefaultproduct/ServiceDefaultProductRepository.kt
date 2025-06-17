package com.osia.ampirux.repository.servicedefaultproduct
import com.osia.ampirux.model.ServiceDefaultProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("service_default_products.crud_repository")
interface ServiceDefaultProductRepository : CommonRepository<ServiceDefaultProduct>
