package com.osia.ampirux.repository.productcategory
import com.osia.ampirux.model.ProductCategory
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository("product_category.crud_repository")
interface ProductCategoryRepository : CommonRepository<ProductCategory> {
    @Query("SELECT COUNT(*) FROM product_category", nativeQuery = true)
    override fun count(): Long
}
