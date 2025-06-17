package com.osia.template.repository.judgment

import com.osia.template.model.Judgment
import com.osia.template.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("judgment.crud_repository")
interface JudgmentRepository : CommonRepository<Judgment>
