package com.osia.nota_maestro.model.listener.post

import com.osia.nota_maestro.model.Post
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.post.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class PostListener : CodeSetter() {

    companion object {
        private lateinit var postRepository: PostRepository
    }

    @Autowired
    fun setProducer(_postRepository: PostRepository) {

        postRepository = _postRepository
    }

    @PrePersist
    fun prePersist(post: Post) {
        this.setCode(postRepository, post)
    }
}
