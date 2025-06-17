package com.osia.ampirux.repository.sale
import com.osia.ampirux.model.Sale
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("sale.crud_repository")
interface SaleRepository : CommonRepository<Sale>
