package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.enums.TaskTypeEnum
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

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
    @NotNull
    var uuidSchool: UUID? = null,
    var scheduleInit: LocalDateTime? = null,
    var hour: String? = null,
    var scheduleFinish: LocalDateTime? = null,
    var taskType: TaskTypeEnum? = null,
    var description: String? = null,
    // UserUuid
    var assignedTo: UUID? = null,
    var uuidClient: UUID? = null
) : BaseModel()
