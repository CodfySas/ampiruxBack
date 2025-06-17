package com.osia.ampirux.repository.notification

import com.osia.ampirux.model.Notification
import com.osia.ampirux.model.enums.NotificationEnum
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("notification.crud_repository")
interface NotificationRepository : CommonRepository<Notification> {
    fun findAllByUserUuid(uuid: UUID, pageable: Pageable): Page<Notification>
    fun getAllByUserUuid(uuid: UUID): List<Notification>
    fun getFirstByPostUuidAndType(post: UUID, type: NotificationEnum): Optional<Notification>
}
