package com.osia.ampirux.service.commission

import com.osia.ampirux.dto.commission.v1.CommissionDto
import com.osia.ampirux.dto.commission.v1.CommissionRequest
import com.osia.ampirux.model.Commission
import com.osia.ampirux.service.common.CommonService

interface CommissionService : CommonService<Commission, CommissionDto, CommissionRequest>
