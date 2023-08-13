package com.osia.osia_erp.model.listener.post

import com.osia.osia_erp.model.Post
import com.osia.osia_erp.model.abstracts.CodeSetter
import com.osia.osia_erp.repository.post.PostRepository
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
