package com.osia.ampirux.service.sale

import com.osia.ampirux.dto.sale.v1.SaleDto
import com.osia.ampirux.dto.sale.v1.SaleRequest
import com.osia.ampirux.model.Sale
import com.osia.ampirux.service.common.CommonService
import java.util.UUID

interface SaleService : CommonService<Sale, SaleDto, SaleRequest> {
    fun removeStockProduct(map: MutableMap<UUID, Double>, mapP: MutableMap<UUID, Double>)
}
