package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.student.StudentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "students",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        StudentListener::class
    ]
)
@Where(clause = "deleted = false")
data class Student(
    var name: String? = null,
    var lastname: String? = null,
    var dni: String? = null,
    var documentType: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var address: String? = null,
    @NotNull
    var uuidSchool: UUID? = null
) : BaseModel()
