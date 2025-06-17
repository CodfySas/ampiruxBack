package com.osia.ampirux.service.product

import com.osia.ampirux.dto.product.v1.ProductDto
import com.osia.ampirux.dto.product.v1.ProductRequest
import com.osia.ampirux.model.Product
import com.osia.ampirux.service.common.CommonService

interface ProductService : CommonService<Product, ProductDto, ProductRequest>
