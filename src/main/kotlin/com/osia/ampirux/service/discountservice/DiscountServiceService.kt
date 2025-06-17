package com.osia.ampirux.service.discountservice

import com.osia.ampirux.dto.discountservice.v1.DiscountServiceDto
import com.osia.ampirux.dto.discountservice.v1.DiscountServiceRequest
import com.osia.ampirux.model.DiscountService
import com.osia.ampirux.service.common.CommonService

interface DiscountServiceService : CommonService<DiscountService, DiscountServiceDto, DiscountServiceRequest>
