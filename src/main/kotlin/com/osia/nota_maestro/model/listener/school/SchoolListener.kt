package com.osia.nota_maestro.model.listener.school

import com.osia.nota_maestro.model.School
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.school.SchoolRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.PostPersist
import javax.persistence.PostUpdate
import javax.persistence.PrePersist

@Component
class SchoolListener : CodeSetter() {
    companion object {
        lateinit var schoolRepository: SchoolRepository
    }

    @Autowired
    fun setCompanion(

        schoolRepository1: SchoolRepository
    ) {
        schoolRepository = schoolRepository1
    }

    private val log = LoggerFactory.getLogger(javaClass)

    @PrePersist
    fun prePersist(school: School) {
        this.setCode(schoolRepository, school)
        school.expireDate = LocalDateTime.now().plusMonths(1)
        log.trace("prePersist -> school: $school")
    }

    @PostPersist
    fun postPersist(school: School) {
        log.trace("postPersist -> school: $school")
    }

    @PostUpdate
    fun postUpdate(school: School) {
        log.trace("postUpdate -> school: $school")
    }
}
