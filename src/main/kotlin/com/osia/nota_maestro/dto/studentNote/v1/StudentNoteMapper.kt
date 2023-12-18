package com.osia.nota_maestro.dto.studentNote.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.StudentNote
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
interface StudentNoteMapper : BaseMapper<StudentNoteRequest, StudentNote, StudentNoteDto> {
    @Mappings
    override fun toModel(r: StudentNoteRequest): StudentNote

    @Mappings
    override fun toDto(m: StudentNote): StudentNoteDto

    @Mappings
    override fun toRequest(d: StudentNoteDto): StudentNoteRequest

    override fun update(r: StudentNoteRequest, @MappingTarget m: StudentNote)
}
