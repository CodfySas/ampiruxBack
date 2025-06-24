package com.osia.ampirux.repository.saleservice
import com.osia.ampirux.model.SaleService
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("sale_service.crud_repository")
interface SaleServiceRepository : CommonRepository<SaleService> {
    @Query("SELECT COUNT(*) FROM sale_service", nativeQuery = true)
    override fun count(): Long

    fun findAllBySaleUuid(sale: UUID): List<SaleService>

    fun findAllBySaleUuidIn(sales: List<UUID>): List<SaleService>
}
