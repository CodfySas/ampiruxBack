package com.osia.nota_maestro.model.listener.worker

import com.osia.nota_maestro.model.Worker
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.worker.WorkerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class WorkerListener : CodeSetter() {

    companion object {
        private lateinit var workerRepository: WorkerRepository
    }

    @Autowired
    fun setProducer(_workerRepository: WorkerRepository) {
        workerRepository = _workerRepository
    }

    @PrePersist
    fun prePersist(worker: Worker) {
        this.setCode(workerRepository, worker)
    }
}
