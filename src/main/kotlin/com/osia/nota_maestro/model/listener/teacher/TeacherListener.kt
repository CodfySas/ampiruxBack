package com.osia.nota_maestro.model.listener.teacher

import com.osia.nota_maestro.model.Teacher
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class TeacherListener : CodeSetter() {

    companion object {
        private lateinit var teacherRepository: TeacherRepository
    }

    @Autowired
    fun setProducer(_teacherRepository: TeacherRepository) {
        teacherRepository = _teacherRepository
    }

    @PrePersist
    fun prePersist(teacher: Teacher) {
        this.setCode(teacherRepository, teacher)
    }
}
