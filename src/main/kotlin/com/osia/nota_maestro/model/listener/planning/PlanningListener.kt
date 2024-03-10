package com.osia.nota_maestro.model.listener.planning

import com.osia.nota_maestro.model.Planning
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.planning.PlanningRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class PlanningListener : CodeSetter() {

    companion object {
        private lateinit var planningRepository: PlanningRepository
    }

    @Autowired
    fun setProducer(_planningRepository: PlanningRepository) {
        planningRepository = _planningRepository
    }

    @PrePersist
    fun prePersist(planning: Planning) {
        this.setCode(planningRepository, planning)
    }
}
