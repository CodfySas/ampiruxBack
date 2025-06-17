package com.osia.template.repository.user

import com.osia.template.model.User
import com.osia.template.repository.CommonRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("user.crud_repository")
interface UserRepository : CommonRepository<User> {
    fun getFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username: String, email: String): Optional<User>
    fun getFistByUsernameIgnoreCase(username: String): Optional<User>
    fun getFistByEmailIgnoreCase(username: String): Optional<User>
}
