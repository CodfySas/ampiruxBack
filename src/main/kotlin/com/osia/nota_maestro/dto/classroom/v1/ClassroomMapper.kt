package com.osia.nota_maestro.dto.classroom.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Classroom
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
interface ClassroomMapper : BaseMapper<ClassroomRequest, Classroom, ClassroomDto> {
    @Mappings
    override fun toModel(r: ClassroomRequest): Classroom

    @Mappings
    override fun toDto(m: Classroom): ClassroomDto

    @Mappings
    fun toComplete(m: Classroom): ClassroomCompleteDto

    @Mappings
    override fun toRequest(d: ClassroomDto): ClassroomRequest

    override fun update(r: ClassroomRequest, @MappingTarget m: Classroom)
}
