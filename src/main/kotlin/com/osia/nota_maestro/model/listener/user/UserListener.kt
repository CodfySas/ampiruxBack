package com.osia.nota_maestro.model.listener.user

import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.model.abstracts.BaseModel
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
        var counter = userRepository.countByUser(user.uuidSchool, user.role)
        when(user.role){
            "admin"->{
                user.code = user.getCode(++counter, "US")
            }
            "teacher"->{
                user.code = user.getCode(++counter, "TH")
            }
            "student"->{
                user.code = user.getCode(++counter, "ES")
            }
            else->{
                user.code = user.getCode(++counter)
            }
        }
    }
}
