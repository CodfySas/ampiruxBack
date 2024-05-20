package com.osia.nota_maestro.dto.studentPosition.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.StudentPosition
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
interface StudentPositionMapper : BaseMapper<StudentPositionRequest, StudentPosition, StudentPositionDto> {
    @Mappings
    override fun toModel(r: StudentPositionRequest): StudentPosition

    @Mappings
    override fun toDto(m: StudentPosition): StudentPositionDto

    @Mappings
    override fun toRequest(d: StudentPositionDto): StudentPositionRequest

    override fun update(r: StudentPositionRequest, @MappingTarget m: StudentPosition)
}
