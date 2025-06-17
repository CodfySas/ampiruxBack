package com.osia.ampirux.repository.saleserviceproduct
import com.osia.ampirux.model.SaleServiceProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("sale_service_product.crud_repository")
interface SaleServiceProductRepository : CommonRepository<SaleServiceProduct>
