package com.osia.osia_erp.dto.client.v1

import com.osia.osia_erp.dto.BaseMapper
import com.osia.osia_erp.model.Client
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
interface ClientMapper : BaseMapper<ClientRequest, Client, ClientDto> {
    @Mappings
    override fun toModel(r: ClientRequest): Client

    @Mappings
    override fun toDto(m: Client): ClientDto

    @Mappings
    override fun toRequest(d: ClientDto): ClientRequest

    override fun update(r: ClientRequest, @MappingTarget m: Client)
}
