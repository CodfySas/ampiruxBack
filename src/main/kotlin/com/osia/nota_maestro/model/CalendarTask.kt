package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.enums.TaskTypeEnum
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(
    name = "calendar_tasks",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AuditingEntityListener::class,
    ]
)
@Where(clause = "deleted = false")
data class CalendarTask(
    var uuidSchool: UUID? = null,
    var day: LocalDate? = null,
    var hourInit: String? = null,
    var hourFinish: String? = null,
    @Enumerated(EnumType.STRING)
    var taskType: TaskTypeEnum? = null,
    var description: String? = null,
    var assignedTo: UUID? = null,
    var uuidResource: UUID? = null
) : BaseModel()
