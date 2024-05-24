package com.osia.nota_maestro.model.listener.postComment

import com.osia.nota_maestro.model.PostComment
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.postComment.PostCommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class PostCommentListener : CodeSetter() {

    companion object {
        private lateinit var postCommentRepository: PostCommentRepository
    }

    @Autowired
    fun setProducer(_postCommentRepository: PostCommentRepository) {
        postCommentRepository = _postCommentRepository
    }

    @PrePersist
    fun prePersist(postComment: PostComment) {
        this.setCode(postCommentRepository, postComment)
    }
}
