package com.osia.ampirux.model.listener.user

import com.osia.ampirux.model.User
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class UserListener : CodeSetter() {

    companion object {
        private lateinit var userRepository: UserRepository
    }

    @Autowired
    fun setProducer(_userRepository: UserRepository) {
        userRepository = _userRepository
    }

    @PrePersist
    fun prePersist(user: User) {
        this.setCode(userRepository, user)
    }
}
