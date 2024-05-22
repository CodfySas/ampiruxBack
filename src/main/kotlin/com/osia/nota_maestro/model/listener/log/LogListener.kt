package com.osia.nota_maestro.model.listener.log

import com.osia.nota_maestro.model.Log
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.log.LogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class LogListener : CodeSetter() {

    companion object {
        private lateinit var logRepository: LogRepository
    }

    @Autowired
    fun setProducer(_logRepository: LogRepository) {
        logRepository = _logRepository
    }

    @PrePersist
    fun prePersist(log: Log) {
        this.setCode(logRepository, log)
    }
}
