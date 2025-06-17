package com.osia.ampirux.service.discountproduct

import com.osia.ampirux.dto.discountproduct.v1.DiscountProductDto
import com.osia.ampirux.dto.discountproduct.v1.DiscountProductRequest
import com.osia.ampirux.model.DiscountProduct
import com.osia.ampirux.service.common.CommonService

interface DiscountProductService : CommonService<DiscountProduct, DiscountProductDto, DiscountProductRequest>
