package com.osia.nota_maestro.dto.worker.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Worker
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
interface WorkerMapper : BaseMapper<WorkerRequest, Worker, WorkerDto> {
    @Mappings
    override fun toModel(r: WorkerRequest): Worker

    @Mappings
    override fun toDto(m: Worker): WorkerDto

    @Mappings
    override fun toRequest(d: WorkerDto): WorkerRequest

    override fun update(r: WorkerRequest, @MappingTarget m: Worker)
}
