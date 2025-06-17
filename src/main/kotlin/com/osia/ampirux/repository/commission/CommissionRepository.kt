package com.osia.ampirux.repository.commission
import com.osia.ampirux.model.Commission
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("commission.crud_repository")
interface CommissionRepository : CommonRepository<Commission>
