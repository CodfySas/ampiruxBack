package com.osia.ampirux.model.listener.productcategory

import com.osia.ampirux.model.ProductCategory
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.productcategory.ProductCategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ProductCategoryListener : CodeSetter() {

    companion object {
        private lateinit var productcategoryRepository: ProductCategoryRepository
    }

    @Autowired
    fun setProducer(_productcategoryRepository: ProductCategoryRepository) {
        productcategoryRepository = _productcategoryRepository
    }

    @PrePersist
    fun prePersist(productcategory: ProductCategory) {
        this.setCode(productcategoryRepository, productcategory)
    }
}
