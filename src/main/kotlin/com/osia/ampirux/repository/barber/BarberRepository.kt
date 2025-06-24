package com.osia.ampirux.repository.barber
import com.osia.ampirux.model.Barber
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("barber.crud_repository")
interface BarberRepository : CommonRepository<Barber> {

}
