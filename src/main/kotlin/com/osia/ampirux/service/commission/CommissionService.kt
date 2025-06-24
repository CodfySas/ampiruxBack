package com.osia.ampirux.service.commission

import com.osia.ampirux.dto.commission.v1.CommissionDto
import com.osia.ampirux.dto.commission.v1.CommissionRequest
import com.osia.ampirux.model.Commission
import com.osia.ampirux.service.common.CommonService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CommissionService : CommonService<Commission, CommissionDto, CommissionRequest> {
    fun paidBarber(barber: UUID): List<CommissionDto>;
    fun getHistoryByBarber(pageable: Pageable, barber: UUID): Page<CommissionDto>
}
