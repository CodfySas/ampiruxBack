package com.osia.ampirux.repository.judgment

import com.osia.ampirux.model.Judgment
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("judgment.crud_repository")
interface JudgmentRepository : CommonRepository<Judgment>
