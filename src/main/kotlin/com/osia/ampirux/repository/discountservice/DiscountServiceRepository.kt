package com.osia.ampirux.repository.discountservice
import com.osia.ampirux.model.DiscountService
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("discount_service.crud_repository")
interface DiscountServiceRepository : CommonRepository<DiscountService>
