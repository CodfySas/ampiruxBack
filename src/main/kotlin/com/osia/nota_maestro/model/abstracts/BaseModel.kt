package com.osia.nota_maestro.model.abstracts

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass

@EntityListeners(
    value = [
        AuditingEntityListener::class
    ]
)
abstract class BaseModel {

    @Id
    var uuid: UUID ? = UUID.randomUUID()

    var code: String? = null

    var version: Long? = null

    @Column(name = "created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @Column(name = "last_modified_at",)
    @LastModifiedDate
    var lastModifiedAt: LocalDateTime? = LocalDateTime.now()

    @Column(name = "deleted_at",)
    var deletedAt: LocalDateTime? = null

    var deleted: Boolean = false

    fun getCode(counter: Long): String {
        return this.javaClass.simpleName.substring(0..1).uppercase(Locale.getDefault()) + "-" + String.format(String.format("%%0%dd", 7), counter)
    }
}
