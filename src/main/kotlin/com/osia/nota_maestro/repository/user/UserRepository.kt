package com.osia.nota_maestro.repository.user

import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("user.crud_repository")
interface UserRepository :
    JpaRepository<User, UUID>,
    JpaSpecificationExecutor<User>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    override fun count(increment: Int): Long

    fun getFirstByUsernameAndPassword(username: String, password: String): Optional<User>

    fun getFirstByUsernameOrDni(username: String, dni: String): Optional<User>
}
