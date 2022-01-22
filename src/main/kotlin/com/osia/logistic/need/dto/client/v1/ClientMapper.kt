package com.osia.logistic.need.dto.client.v1

import com.osia.logistic.need.model.Client
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface ClientMapper {

    fun toDto(client: Client): ClientDto

    fun toModel(clientRequest: ClientRequest): Client

    fun updateModel(clientRequest: ClientRequest, @MappingTarget client: Client)
}
