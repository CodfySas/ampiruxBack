package com.osia.ampirux.repository.barbershop
import com.osia.ampirux.model.BarberShop
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository("barbershop.crud_repository")
interface BarberShopRepository : CommonRepository<BarberShop> {
    fun getFirstByCode(code: String): Optional<BarberShop>
}

