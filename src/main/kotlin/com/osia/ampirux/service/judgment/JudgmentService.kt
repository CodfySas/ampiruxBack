package com.osia.ampirux.service.judgment

import com.osia.ampirux.dto.judgment.v1.JudgmentDto
import com.osia.ampirux.dto.judgment.v1.JudgmentRequest
import com.osia.ampirux.model.Judgment
import com.osia.ampirux.service.common.CommonService

interface JudgmentService : CommonService<Judgment, JudgmentDto, JudgmentRequest>
