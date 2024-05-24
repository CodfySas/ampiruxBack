package com.osia.nota_maestro.model.listener.postReact

import com.osia.nota_maestro.model.PostReact
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.postReact.PostReactRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class PostReactListener : CodeSetter() {

    companion object {
        private lateinit var postReactRepository: PostReactRepository
    }

    @Autowired
    fun setProducer(_postReactRepository: PostReactRepository) {
        postReactRepository = _postReactRepository
    }

    @PrePersist
    fun prePersist(postReact: PostReact) {
        this.setCode(postReactRepository, postReact)
    }
}
