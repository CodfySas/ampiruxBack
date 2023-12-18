package com.osia.nota_maestro.model.listener.studentNote

import com.osia.nota_maestro.model.StudentNote
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class StudentNoteListener : CodeSetter() {

    companion object {
        private lateinit var studentNoteRepository: StudentNoteRepository
    }

    @Autowired
    fun setProducer(_studentNoteRepository: StudentNoteRepository) {
        studentNoteRepository = _studentNoteRepository
    }

    @PrePersist
    fun prePersist(studentNote: StudentNote) {
        this.setCode(studentNoteRepository, studentNote)
    }
}
