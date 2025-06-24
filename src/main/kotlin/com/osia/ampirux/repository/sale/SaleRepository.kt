package com.osia.ampirux.repository.sale
import com.osia.ampirux.model.Sale
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository("sale.crud_repository")
interface SaleRepository : CommonRepository<Sale> {
    @Query("SELECT COUNT(*) FROM sale", nativeQuery = true)
    override fun count(): Long

    fun findAllByCreatedAtBetweenAndBarberUuidIn(initDate: LocalDateTime, finishDate: LocalDateTime, barbers: List<UUID>): List<Sale>
}
