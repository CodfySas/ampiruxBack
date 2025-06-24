package com.osia.ampirux.repository.saleserviceproduct
import com.osia.ampirux.model.SaleService
import com.osia.ampirux.model.SaleServiceProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("sale_service_product.crud_repository")
interface SaleServiceProductRepository : CommonRepository<SaleServiceProduct> {
    @Query("SELECT COUNT(*) FROM sale_service_product", nativeQuery = true)
    override fun count(): Long

    fun findAllBySaleServiceUuid(sale: UUID): List<SaleServiceProduct>

    fun findAllBySaleServiceUuidIn(sales: List<UUID>): List<SaleServiceProduct>
}
