package com.osia.ampirux.util

import org.springframework.data.jpa.domain.Specification
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class CreateSpec<T> {

    fun createSpec(
        where: String,
        barberShopUuid: UUID? = null,
        globalFields: List<String> = emptyList()
    ): Specification<T> {
        var finalSpec = Specification<T> { root, _, _ ->
            root.get<Boolean>("deleted").`in`(false)
        }

        if (barberShopUuid != null) {
            finalSpec = finalSpec.and { root, _, cb ->
                cb.equal(root.get<String>("barbershopUuid"), barberShopUuid)
            }
        }

        if (where.isNotEmpty() && where != "-") {
            where.split(",").forEach {
                val (field, value) = it.split(":")
                val upperValue = "%${value.uppercase()}%"

                if (field == "global") {
                    if (globalFields.isNotEmpty()) {
                        finalSpec = finalSpec.and { root, _, cb ->
                            val predicates = globalFields.map { f ->
                                cb.like(cb.upper(root.get<String>(f)), upperValue)
                            }
                            cb.or(*predicates.toTypedArray())
                        }
                    }
                } else {
                    finalSpec = finalSpec.and { root, _, cb ->
                        cb.like(cb.upper(root.get(field)), upperValue)
                    }
                }
            }
        }

        return finalSpec
    }

}
