package com.osia.ampirux.model.listener.product

import com.osia.ampirux.model.Product
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.product.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ProductListener : CodeSetter() {

    companion object {
        private lateinit var productRepository: ProductRepository
    }

    @Autowired
    fun setProducer(_productRepository: ProductRepository) {
        productRepository = _productRepository
    }

    @PrePersist
    fun prePersist(product: Product) {
        this.setCode(productRepository, product)
    }
}
