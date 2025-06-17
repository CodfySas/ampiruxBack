package com.osia.ampirux.dto.productcategory.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.ProductCategory
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
interface ProductCategoryMapper : BaseMapper<ProductCategoryRequest, ProductCategory, ProductCategoryDto> {
    @Mappings
    override fun toModel(r: ProductCategoryRequest): ProductCategory

    @Mappings
    override fun toDto(m: ProductCategory): ProductCategoryDto

    @Mappings
    override fun toRequest(d: ProductCategoryDto): ProductCategoryRequest

    override fun update(r: ProductCategoryRequest, @MappingTarget m: ProductCategory)
}
