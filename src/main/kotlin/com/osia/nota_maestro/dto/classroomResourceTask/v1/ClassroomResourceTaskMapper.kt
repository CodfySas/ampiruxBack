package com.osia.nota_maestro.dto.classroomResourceTask.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.ClassroomResourceTask
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
interface ClassroomResourceTaskMapper : BaseMapper<ClassroomResourceTaskRequest, ClassroomResourceTask, ClassroomResourceTaskDto> {
    @Mappings
    override fun toModel(r: ClassroomResourceTaskRequest): ClassroomResourceTask

    @Mappings
    override fun toDto(m: ClassroomResourceTask): ClassroomResourceTaskDto

    @Mappings
    override fun toRequest(d: ClassroomResourceTaskDto): ClassroomResourceTaskRequest

    override fun update(r: ClassroomResourceTaskRequest, @MappingTarget m: ClassroomResourceTask)
}
