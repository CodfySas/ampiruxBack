package com.osia.nota_maestro.model.listener.notification

import com.osia.nota_maestro.model.Notification
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.notification.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class NotificationListener : CodeSetter() {

    companion object {
        private lateinit var notificationRepository: NotificationRepository
    }

    @Autowired
    fun setProducer(_notificationRepository: NotificationRepository) {
        notificationRepository = _notificationRepository
    }

    @PrePersist
    fun prePersist(notification: Notification) {
        this.setCode(notificationRepository, notification)
    }
}
