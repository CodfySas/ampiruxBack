package com.osia.ampirux.repository.saleproduct
import com.osia.ampirux.model.SaleProduct
import com.osia.ampirux.model.SaleService
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("sale_product.crud_repository")
interface SaleProductRepository : CommonRepository<SaleProduct> {
    @Query("SELECT COUNT(*) FROM sale_product", nativeQuery = true)
    override fun count(): Long

    fun findAllBySaleUuid(sale: UUID): List<SaleProduct>

    fun findAllBySaleUuidIn(sale: List<UUID>): List<SaleProduct>
}
