package com.osia.ampirux.util

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class CreateSpec<T> {

    fun createSpec(where: String): Specification<T> {
        var finalSpec = Specification { root: Root<T>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        if (where != "") {
            where.split(",").forEach {
                finalSpec = finalSpec.and { root: Root<T>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
                    cb.like(cb.upper(root.get(it.split(":")[0])), "%" + it.split(":")[1].uppercase() + "%")
                }
            }
        }
        return finalSpec
    }
}
