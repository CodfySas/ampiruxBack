package com.osia.ampirux.model.listener.barber

import com.osia.ampirux.model.Barber
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.barber.BarberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class BarberListener : CodeSetter() {

    companion object {
        private lateinit var barberRepository: BarberRepository
    }

    @Autowired
    fun setProducer(_barberRepository: BarberRepository) {
        barberRepository = _barberRepository
    }

    @PrePersist
    fun prePersist(barber: Barber) {
        this.setCode(barberRepository, barber)
    }
}
