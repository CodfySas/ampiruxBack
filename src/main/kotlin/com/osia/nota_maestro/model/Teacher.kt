package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.teacher.TeacherListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "teachers",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        TeacherListener::class
    ]
)
@Where(clause = "deleted = false")
data class Teacher(
    var name: String? = null,
    var dni: String? = null,
    var documentType: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var address: String? = null,
    @NotNull
    var uuidSchool: UUID? = null
) : BaseModel()
