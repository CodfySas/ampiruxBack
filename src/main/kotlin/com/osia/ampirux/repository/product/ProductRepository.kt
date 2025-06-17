package com.osia.ampirux.repository.product
import com.osia.ampirux.model.Product
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("product.crud_repository")
interface ProductRepository : CommonRepository<Product>
