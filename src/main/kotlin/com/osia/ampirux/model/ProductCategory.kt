package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.productcategory.ProductCategoryListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "product_category",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ProductCategoryListener::class
    ]
)
@Where(clause = "deleted = false")
data class ProductCategory(
    var name: String? = null,
    var barbershopUuid: UUID? = null
) : BaseModel()
