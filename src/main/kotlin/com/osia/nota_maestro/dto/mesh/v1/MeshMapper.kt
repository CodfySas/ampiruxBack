package com.osia.nota_maestro.dto.mesh.v1

import com.osia.nota_maestro.dto.BaseMapper
import com.osia.nota_maestro.model.Mesh
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
interface MeshMapper : BaseMapper<MeshRequest, Mesh, MeshDto> {
    @Mappings
    override fun toModel(r: MeshRequest): Mesh

    @Mappings
    override fun toDto(m: Mesh): MeshDto

    @Mappings
    override fun toRequest(d: MeshDto): MeshRequest

    override fun update(r: MeshRequest, @MappingTarget m: Mesh)
}
