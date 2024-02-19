package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.director.DirectorListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "directors",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DirectorListener::class
    ]
)
@Where(clause = "deleted = false")
data class Director(
    var uuidClassroom: UUID? = null,
    var uuidTeacher: UUID? = null
) : BaseModel()
