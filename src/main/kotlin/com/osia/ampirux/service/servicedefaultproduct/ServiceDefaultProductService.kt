package com.osia.ampirux.service.servicedefaultproduct

import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductDto
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductRequest
import com.osia.ampirux.model.ServiceDefaultProduct
import com.osia.ampirux.service.common.CommonService

interface ServiceDefaultProductService : CommonService<ServiceDefaultProduct, ServiceDefaultProductDto, ServiceDefaultProductRequest>
