package com.osia.ampirux.repository.saleservice
import com.osia.ampirux.model.SaleService
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("sale_service.crud_repository")
interface SaleServiceRepository : CommonRepository<SaleService>
