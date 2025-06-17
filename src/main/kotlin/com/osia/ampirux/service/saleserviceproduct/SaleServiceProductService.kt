package com.osia.ampirux.service.saleserviceproduct

import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductDto
import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductRequest
import com.osia.ampirux.model.SaleServiceProduct
import com.osia.ampirux.service.common.CommonService

interface SaleServiceProductService : CommonService<SaleServiceProduct, SaleServiceProductDto, SaleServiceProductRequest>
