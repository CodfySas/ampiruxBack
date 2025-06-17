package com.osia.ampirux.repository.saleproduct
import com.osia.ampirux.model.SaleProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("sale_product.crud_repository")
interface SaleProductRepository : CommonRepository<SaleProduct>
