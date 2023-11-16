package com.osia.nota_maestro.model.listener.student

import com.osia.nota_maestro.model.Student
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.student.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class StudentListener : CodeSetter() {

    companion object {
        private lateinit var studentRepository: StudentRepository
    }

    @Autowired
    fun setProducer(_studentRepository: StudentRepository) {
        studentRepository = _studentRepository
    }

    @PrePersist
    fun prePersist(student: Student) {
        this.setCode(studentRepository, student)
    }
}
