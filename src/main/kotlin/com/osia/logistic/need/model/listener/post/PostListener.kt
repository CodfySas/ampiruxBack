package com.osia.logistic.need.model.listener.post

import com.osia.logistic.need.model.Post
import com.osia.logistic.need.model.abstracts.CodeSetter
import com.osia.logistic.need.repository.post.PostRepository
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
