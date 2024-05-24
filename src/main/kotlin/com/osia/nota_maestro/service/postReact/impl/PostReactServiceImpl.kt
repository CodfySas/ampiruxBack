package com.osia.nota_maestro.service.postReact.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.postReact.v1.PostReactComplete
import com.osia.nota_maestro.dto.postReact.v1.PostReactDto
import com.osia.nota_maestro.dto.postReact.v1.PostReactMapper
import com.osia.nota_maestro.dto.postReact.v1.PostReactRequest
import com.osia.nota_maestro.model.PostReact
import com.osia.nota_maestro.repository.post.PostRepository
import com.osia.nota_maestro.repository.postComment.PostCommentRepository
import com.osia.nota_maestro.repository.postReact.PostReactRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.post.PostService
import com.osia.nota_maestro.service.postComment.PostCommentService
import com.osia.nota_maestro.service.postReact.PostReactService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Service("postReact.crud_service")
@Transactional
class PostReactServiceImpl(
    private val postReactRepository: PostReactRepository,
    private val postReactMapper: PostReactMapper,
    private val objectMapper: ObjectMapper,
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postCommentService: PostCommentService,
    private val postCommentRepository: PostCommentRepository
) : PostReactService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("postReact count -> increment: $increment")
        return postReactRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): PostReact {
        return postReactRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "PostReact $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<PostReactDto> {
        log.trace("postReact findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return postReactRepository.findAllById(uuidList).map(postReactMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<PostReactDto> {
        log.trace("postReact findAll -> pageable: $pageable")
        return postReactRepository.findAll(Specification.where(CreateSpec<PostReact>().createSpec("", school)), pageable).map(postReactMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PostReactDto> {
        log.trace("postReact findAllByFilter -> pageable: $pageable, where: $where")
        return postReactRepository.findAll(Specification.where(CreateSpec<PostReact>().createSpec(where, school)), pageable).map(postReactMapper::toDto)
    }

    @Transactional
    override fun save(postReactRequest: PostReactRequest, replace: Boolean): PostReactDto {
        log.trace("postReact save -> request: $postReactRequest")
        val savedPostReact = postReactMapper.toModel(postReactRequest)
        return postReactMapper.toDto(postReactRepository.save(savedPostReact))
    }

    @Transactional
    override fun saveMultiple(postReactRequestList: List<PostReactRequest>): List<PostReactDto> {
        log.trace("postReact saveMultiple -> requestList: ${objectMapper.writeValueAsString(postReactRequestList)}")
        val postReacts = postReactRequestList.map(postReactMapper::toModel)
        return postReactRepository.saveAll(postReacts).map(postReactMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, postReactRequest: PostReactRequest, includeDelete: Boolean): PostReactDto {
        log.trace("postReact update -> uuid: $uuid, request: $postReactRequest")
        val postReact = if (!includeDelete) {
            getById(uuid)
        } else {
            postReactRepository.getByUuid(uuid).get()
        }
        postReactMapper.update(postReactRequest, postReact)
        return postReactMapper.toDto(postReactRepository.save(postReact))
    }

    @Transactional
    override fun updateMultiple(postReactDtoList: List<PostReactDto>): List<PostReactDto> {
        log.trace("postReact updateMultiple -> postReactDtoList: ${objectMapper.writeValueAsString(postReactDtoList)}")
        val postReacts = postReactRepository.findAllById(postReactDtoList.mapNotNull { it.uuid })
        postReacts.forEach { postReact ->
            postReactMapper.update(postReactMapper.toRequest(postReactDtoList.first { it.uuid == postReact.uuid }), postReact)
        }
        return postReactRepository.saveAll(postReacts).map(postReactMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("postReact delete -> uuid: $uuid")
        val postReact = getById(uuid)
        postReact.deleted = true
        postReact.deletedAt = LocalDateTime.now()
        postReactRepository.save(postReact)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("postReact deleteMultiple -> uuid: $uuidList")
        val postReacts = postReactRepository.findAllById(uuidList)
        postReacts.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        postReactRepository.saveAll(postReacts)
    }

    @Transactional
    @Async
    override fun reactClick(post: UUID, react: Int, user: UUID): PostReactDto {
        val postF = postService.getById(post)
        val postReact = postReactRepository.findFirstByUuidPostAndUuidUserAndUuidCommentIsNull(post, user)
        val new = if(postReact.isPresent){
            val actualReact = postReact.get().react
            if(actualReact != 0){
                postF.reacts--;
                when (actualReact) {
                    1 -> { postF.likes--; }
                    2 -> { postF.loved--; }
                    3 -> { postF.interesting--; }
                    4 -> { postF.wows--; }
                    5 -> { postF.dislikes--; }
                }
            }
            if(react != 0){
                postF.reacts++;
                when (react) {
                    1 -> { postF.likes++; }
                    2 -> { postF.loved++; }
                    3 -> { postF.interesting++; }
                    4 -> { postF.wows++; }
                    5 -> { postF.dislikes++; }
                }
            }
            postReact.get().react = react
            postF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postRepository.save(postF)
            postReactMapper.toDto(postReactRepository.save(postReact.get()))
        }else{
            if(react != 0){
                postF.reacts++;
                when (react) {
                    1 -> { postF.likes++; }
                    2 -> { postF.loved++; }
                    3 -> { postF.interesting++; }
                    4 -> { postF.wows++; }
                    5 -> { postF.dislikes++; }
                }
            }
            postF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postRepository.save(postF)
            save(PostReactRequest().apply {
                this.react = react
                this.uuidPost = post
                this.uuidUser = user
            })
        }
        return new
    }

    @Transactional
    @Async
    override fun reactComment(comment: UUID, post: UUID, react: Int, user: UUID): PostReactDto {
        val postF = postCommentService.getById(comment)
        val postReact = postReactRepository.findFirstByUuidCommentAndUuidUser(comment, user)
        val new = if(postReact.isPresent){
            val actualReact = postReact.get().react
            if(actualReact != 0){
                postF.reacts--;
                when (actualReact) {
                    1 -> { postF.likes--; }
                    2 -> { postF.loved--; }
                    3 -> { postF.interesting--; }
                    4 -> { postF.wows--; }
                    5 -> { postF.dislikes--; }
                }
            }
            if(react != 0){
                postF.reacts++;
                when (react) {
                    1 -> { postF.likes++; }
                    2 -> { postF.loved++; }
                    3 -> { postF.interesting++; }
                    4 -> { postF.wows++; }
                    5 -> { postF.dislikes++; }
                }
            }
            postReact.get().react = react
            postF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postCommentRepository.save(postF)
            postReactMapper.toDto(postReactRepository.save(postReact.get()))
        }else{
            if(react != 0){
                postF.reacts++;
                when (react) {
                    1 -> { postF.likes++; }
                    2 -> { postF.loved++; }
                    3 -> { postF.interesting++; }
                    4 -> { postF.wows++; }
                    5 -> { postF.dislikes++; }
                }
            }
            postF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postCommentRepository.save(postF)
            save(PostReactRequest().apply {
                this.react = react
                this.uuidComment = comment
                this.uuidUser = user
                this.uuidPost = post
            })
        }
        return new
    }

    @Transactional
    @Async
    override fun reactResponse(response: UUID, post: UUID, comment: UUID, react: Int, user: UUID): PostReactDto {
        val responseF = postCommentService.getById(response)
        val postReact = postReactRepository.findFirstByUuidCommentAndUuidUser(response, user)
        val new = if(postReact.isPresent){
            val actualReact = postReact.get().react
            if(actualReact != 0){
                responseF.reacts--;
                when (actualReact) {
                    1 -> { responseF.likes--; }
                    2 -> { responseF.loved--; }
                    3 -> { responseF.interesting--; }
                    4 -> { responseF.wows--; }
                    5 -> { responseF.dislikes--; }
                }
            }
            if(react != 0){
                responseF.reacts++;
                when (react) {
                    1 -> { responseF.likes++; }
                    2 -> { responseF.loved++; }
                    3 -> { responseF.interesting++; }
                    4 -> { responseF.wows++; }
                    5 -> { responseF.dislikes++; }
                }
            }
            postReact.get().react = react
            responseF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postCommentRepository.save(responseF)
            postReactMapper.toDto(postReactRepository.save(postReact.get()))
        }else{
            if(react != 0){
                responseF.reacts++;
                when (react) {
                    1 -> { responseF.likes++; }
                    2 -> { responseF.loved++; }
                    3 -> { responseF.interesting++; }
                    4 -> { responseF.wows++; }
                    5 -> { responseF.dislikes++; }
                }
            }
            responseF.lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
            postCommentRepository.save(responseF)
            save(PostReactRequest().apply {
                this.react = react
                this.uuidComment = response
                this.uuidUser = user
                this.uuidPost = post
            })
        }
        return new
    }

    override fun getReacts(uuid: UUID, type: String): List<PostReactComplete> {
        val final = mutableListOf<PostReactComplete>()
        val reacts = when(type){
            "post"-> postReactRepository.findAllByUuidPostAndReactNotAndUuidCommentIsNull(uuid, 0).map(postReactMapper::toDto)
            "comment" -> postReactRepository.findAllByUuidCommentAndReactNot(uuid, 0).map(postReactMapper::toDto)
            else -> mutableListOf()
        }
        val users = userRepository.findAllById(reacts.mapNotNull { it.uuidUser })
        reacts.forEach { r->
            val myu = users.firstOrNull { u-> u.uuid == r.uuidUser }
            r.userName = "${myu?.name} ${myu?.lastname}"
            if(myu?.role == "admin"){
                r.userRole = "Administrador"
            }
            if(myu?.role == "teacher"){
                r.userRole = "Docente"
            }
            if(myu?.role == "student"){
                r.userRole = "Estudiante"
            }
        }
        final.add(PostReactComplete().apply {
            this.reacts = reacts
            this.count = reacts.size
            this.react = "Todas"
        })
        val likes = reacts.filter { it.react == 1 }
        val loves = reacts.filter { it.react == 2 }
        val ideas = reacts.filter { it.react == 3 }
        val wows = reacts.filter { it.react == 4 }
        val dislikes = reacts.filter { it.react == 5 }
        final.add(PostReactComplete().apply {
            this.reacts = likes
            this.count = likes.size
            this.react = "Me gusta"
            this.img = "like"
        })
        final.add(PostReactComplete().apply {
            this.reacts = loves
            this.count = loves.size
            this.react = "Me Encanta"
            this.img = "heart"
        })
        final.add(PostReactComplete().apply {
            this.reacts = ideas
            this.count = ideas.size
            this.react = "Interesante"
            this.img = "idea"
        })
        final.add(PostReactComplete().apply {
            this.reacts = wows
            this.count = wows.size
            this.react = "Me Impresiona"
            this.img = "wow"

        })
        final.add(PostReactComplete().apply {
            this.reacts = dislikes
            this.count = dislikes.size
            this.react = "Poco Útil"
            this.img = "dislike"
        })

        return final
    }
}
