package com.osia.ampirux.service.cash

import com.osia.ampirux.dto.cash.v1.CashDto
import com.osia.ampirux.dto.cash.v1.CashRequest
import com.osia.ampirux.model.Cash
import com.osia.ampirux.service.common.CommonService

interface CashService : CommonService<Cash, CashDto, CashRequest>
