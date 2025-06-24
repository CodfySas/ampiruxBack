package com.osia.ampirux.repository.commission
import com.osia.ampirux.model.Commission
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("commission.crud_repository")
interface CommissionRepository : CommonRepository<Commission> {
    @Query("SELECT COUNT(*) FROM commission", nativeQuery = true)
    override fun count(): Long

    fun findAllByBarberUuidInAndStatus(barbers: List<UUID>, status: String): List<Commission>

    fun findAllByBarberUuid(pageable: Pageable, barber: UUID): Page<Commission>
}
