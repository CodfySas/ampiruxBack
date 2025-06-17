package com.osia.ampirux.repository.discount
import com.osia.ampirux.model.Discount
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("discounts.crud_repository")
interface DiscountRepository : CommonRepository<Discount>
