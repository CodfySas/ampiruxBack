package com.osia.ampirux.service.saleservice

import com.osia.ampirux.dto.saleservice.v1.SaleServiceDto
import com.osia.ampirux.dto.saleservice.v1.SaleServiceRequest
import com.osia.ampirux.model.SaleService
import com.osia.ampirux.service.common.CommonService

interface SaleServiceService : CommonService<SaleService, SaleServiceDto, SaleServiceRequest>
