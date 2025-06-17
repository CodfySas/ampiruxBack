package com.osia.template.service.judgment

import com.osia.template.dto.judgment.v1.JudgmentDto
import com.osia.template.dto.judgment.v1.JudgmentRequest
import com.osia.template.model.Judgment
import com.osia.template.service.common.CommonService

interface JudgmentService : CommonService<Judgment, JudgmentDto, JudgmentRequest>