package com.osia.nota_maestro.dto.notification.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Notification
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
)
interface NotificationMapper : BaseMapper<NotificationRequest, Notification, NotificationDto> {
    @Mappings
    override fun toModel(r: NotificationRequest): Notification

    @Mappings
    override fun toDto(m: Notification): NotificationDto

    @Mappings
    override fun toRequest(d: NotificationDto): NotificationRequest

    override fun update(r: NotificationRequest, @MappingTarget m: Notification)
}
