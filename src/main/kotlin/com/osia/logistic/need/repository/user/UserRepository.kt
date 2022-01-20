package com.osia.logistic.need.repository.user

import com.osia.logistic.need.model.User
import com.osia.logistic.need.repository.baseRepository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("user.crud_repository")
interface UserRepository : JpaRepository<User, UUID>, JpaSpecificationExecutor<User>, BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM user", nativeQuery = true)
    override fun countAll(): Long

    fun findByUuidIn(list: List<UUID>): List<User>
}
