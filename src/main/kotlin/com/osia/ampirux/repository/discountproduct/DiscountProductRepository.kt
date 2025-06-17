package com.osia.ampirux.repository.discountproduct
import com.osia.ampirux.model.DiscountProduct
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("discount_product.crud_repository")
interface DiscountProductRepository : CommonRepository<DiscountProduct>
