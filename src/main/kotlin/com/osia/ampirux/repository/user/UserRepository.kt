package com.osia.ampirux.repository.user

import com.osia.ampirux.model.User
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository("user.crud_repository")
interface UserRepository : CommonRepository<User> {
    fun getFirstByUsernameIgnoreCaseOrEmailIgnoreCase(username: String, email: String): Optional<User>
    fun getFistByUsernameIgnoreCase(username: String): Optional<User>
    fun getFistByEmailIgnoreCase(username: String): Optional<User>
}
