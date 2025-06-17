package com.osia.ampirux.service.service

import com.osia.ampirux.dto.service.v1.ServiceDto
import com.osia.ampirux.dto.service.v1.ServiceRequest
import com.osia.ampirux.model.Service
import com.osia.ampirux.service.common.CommonService

interface ServiceService : CommonService<Service, ServiceDto, ServiceRequest>
