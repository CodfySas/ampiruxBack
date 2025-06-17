package com.osia.ampirux.repository.cash
import com.osia.ampirux.model.Cash
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("cash.crud_repository")
interface CashRepository : CommonRepository<Cash>
