package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.accompaniment.AccompanimentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "accompaniments",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AccompanimentListener::class
    ]
)
@Where(clause = "deleted = false")
data class Accompaniment(
    var uuidClassroom: UUID? = null,
    var uuidTeacher: UUID? = null
) : BaseModel()
