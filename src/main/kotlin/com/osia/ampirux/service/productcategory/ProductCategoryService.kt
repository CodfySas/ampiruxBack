package com.osia.ampirux.service.productcategory

import com.osia.ampirux.dto.productcategory.v1.ProductCategoryDto
import com.osia.ampirux.dto.productcategory.v1.ProductCategoryRequest
import com.osia.ampirux.model.ProductCategory
import com.osia.ampirux.service.common.CommonService

interface ProductCategoryService : CommonService<ProductCategory, ProductCategoryDto, ProductCategoryRequest>
