package com.osia.nota_maestro.dto.classroomResource.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ClassroomResource
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
interface ClassroomResourceMapper : BaseMapper<ClassroomResourceRequest, ClassroomResource, ClassroomResourceDto> {
    @Mappings
    override fun toModel(r: ClassroomResourceRequest): ClassroomResource

    @Mappings
    override fun toDto(m: ClassroomResource): ClassroomResourceDto

    @Mappings
    override fun toRequest(d: ClassroomResourceDto): ClassroomResourceRequest

    override fun update(r: ClassroomResourceRequest, @MappingTarget m: ClassroomResource)
}
