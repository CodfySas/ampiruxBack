package com.osia.ampirux.model.listener.barbershop

import com.osia.ampirux.model.BarberShop
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.barbershop.BarberShopRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class BarberShopListener : CodeSetter() {

    companion object {
        private lateinit var barbershopRepository: BarberShopRepository
    }

    @Autowired
    fun setProducer(_barbershopRepository: BarberShopRepository) {
        barbershopRepository = _barbershopRepository
    }

    @PrePersist
    fun prePersist(barbershop: BarberShop) {
        this.setCode(barbershopRepository, barbershop)
    }
}

