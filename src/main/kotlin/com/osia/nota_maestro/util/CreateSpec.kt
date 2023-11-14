package com.osia.nota_maestro.util

import org.springframework.data.jpa.domain.Specification
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class CreateSpec<T> {

    fun createSpec(where: String, school: UUID? = null): Specification<T> {
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
        school?.let {
            finalSpec = finalSpec.and { root: Root<T>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
                root.get<UUID>("uuidSchool").`in`(it)
            }
        }
        return finalSpec
    }
}
