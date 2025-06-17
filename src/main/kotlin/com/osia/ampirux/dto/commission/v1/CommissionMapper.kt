package com.osia.ampirux.dto.commission.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Commission
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
interface CommissionMapper : BaseMapper<CommissionRequest, Commission, CommissionDto> {
    @Mappings
    override fun toModel(r: CommissionRequest): Commission

    @Mappings
    override fun toDto(m: Commission): CommissionDto

    @Mappings
    override fun toRequest(d: CommissionDto): CommissionRequest

    override fun update(r: CommissionRequest, @MappingTarget m: Commission)
}
