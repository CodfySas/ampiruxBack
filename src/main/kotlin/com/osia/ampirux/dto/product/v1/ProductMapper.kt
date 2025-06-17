package com.osia.ampirux.dto.product.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.Product
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
interface ProductMapper : BaseMapper<ProductRequest, Product, ProductDto> {
    @Mappings
    override fun toModel(r: ProductRequest): Product

    @Mappings
    override fun toDto(m: Product): ProductDto

    @Mappings
    override fun toRequest(d: ProductDto): ProductRequest

    override fun update(r: ProductRequest, @MappingTarget m: Product)
}
