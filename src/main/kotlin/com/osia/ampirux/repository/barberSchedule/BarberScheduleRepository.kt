package com.osia.ampirux.repository.barberSchedule
import com.osia.ampirux.model.BarberSchedule
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("barber_schedule.crud_repository")
interface BarberScheduleRepository : CommonRepository<BarberSchedule>
