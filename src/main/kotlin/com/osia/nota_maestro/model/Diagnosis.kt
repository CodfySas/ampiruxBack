package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.diagnosis.DiagnosisListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "diagnoses",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DiagnosisListener::class
    ]
)
@Where(clause = "deleted = false")
data class Diagnosis(
    var name: String? = null,
    var lastname: String? = null,
    var age: Int? = null,
    var uuidStudent: UUID? = null,
    var grade: String? = null,
    var diagnosis: String? = null,
    var uuidSchool: UUID? = null
) : BaseModel()
