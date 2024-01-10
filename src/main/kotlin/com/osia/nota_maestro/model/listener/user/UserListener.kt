package com.osia.nota_maestro.model.listener.user

import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.user.UserRepository
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
        this.setCode(userRepository, user, user.uuidSchool)
    }
}
