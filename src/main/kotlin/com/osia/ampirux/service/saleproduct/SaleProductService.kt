package com.osia.ampirux.service.saleproduct

import com.osia.ampirux.dto.saleproduct.v1.SaleProductDto
import com.osia.ampirux.dto.saleproduct.v1.SaleProductRequest
import com.osia.ampirux.model.SaleProduct
import com.osia.ampirux.service.common.CommonService

interface SaleProductService : CommonService<SaleProduct, SaleProductDto, SaleProductRequest>
