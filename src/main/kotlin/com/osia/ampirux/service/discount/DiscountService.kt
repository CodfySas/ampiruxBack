package com.osia.ampirux.service.discount

import com.osia.ampirux.dto.discount.v1.DiscountDto
import com.osia.ampirux.dto.discount.v1.DiscountRequest
import com.osia.ampirux.model.Discount
import com.osia.ampirux.service.common.CommonService

interface DiscountService : CommonService<Discount, DiscountDto, DiscountRequest>
