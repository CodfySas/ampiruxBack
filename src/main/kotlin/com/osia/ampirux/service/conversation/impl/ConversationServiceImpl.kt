package com.osia.ampirux.service.conversation.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.controller.message.v1.MessageWSController
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.conversation.v1.ConversationDto
import com.osia.ampirux.dto.conversation.v1.ConversationRequest
import com.osia.ampirux.dto.message.v1.MessageDto
import com.osia.ampirux.dto.message.v1.MessageMapper
import com.osia.ampirux.dto.message.v1.MessageRequest
import com.osia.ampirux.dto.user.v1.UserMinDto
import com.osia.ampirux.model.Conversation
import com.osia.ampirux.repository.conversation.ConversationRepository
import com.osia.ampirux.repository.message.MessageRepository
import com.osia.ampirux.service.conversation.ConversationService
import com.osia.ampirux.service.message.MessageService
import com.osia.ampirux.service.user.UserService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class ConversationServiceImpl(
    private val repository: ConversationRepository,
    private val mapper: BaseMapper<ConversationRequest, Conversation, ConversationDto>,
    private val objectMapper: ObjectMapper,
    private val userService: UserService,
    private val messageRepository: MessageRepository,
    private val messageMapper: MessageMapper,
    private val messageService: MessageService,
    private val messageWSController: MessageWSController
) : ConversationService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Conversation {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<ConversationDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ConversationDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<Conversation>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<ConversationDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<Conversation>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: ConversationRequest, replace: Boolean): ConversationDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun saveMultiple(requestList: List<ConversationRequest>): List<ConversationDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: ConversationRequest, includeDelete: Boolean): ConversationDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun updateMultiple(dtoList: List<ConversationDto>): List<ConversationDto> {
        log.trace("updateMultiple -> dtoList: ${objectMapper.writeValueAsString(dtoList)}")
        val ids = dtoList.mapNotNull { it.uuid }
        val entities = repository.findAllById(ids)
        entities.forEach { entity ->
            val dto = dtoList.first { it.uuid == entity.uuid }
            mapper.update(mapper.toRequest(dto), entity)
        }
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun delete(id: UUID) {
        log.trace("delete -> id: $id")
        val entity = getById(id)
        entity.deleted = true
        entity.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        repository.save(entity)
    }

    @Transactional
    override fun deleteMultiple(idList: List<UUID>) {
        log.trace("deleteMultiple -> idList: $idList")
        val entities = repository.findAllById(idList)
        entities.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        }
        repository.saveAll(entities)
    }

    override fun getAllByUser(user: UUID, page: Int, size: Int): Page<ConversationDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val conversations = repository.findAllByUserOneUuidOrUserTwoUuid(user, user, pageable)
        val combinedUsers = (conversations.mapNotNull { it.userOneUuid } + conversations.mapNotNull { it.userTwoUuid }).distinct()
        val users = userService.findByMultiple(combinedUsers).associateBy { it.uuid }
        val pageable2 = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))
        val messagesPage = messageRepository.findAllByReceiverUuidInOrSenderUuidIn(combinedUsers, combinedUsers, pageable2)
        val messages = messagesPage.content.map(messageMapper::toDto)

        val messagesByConversation = messages.groupBy { it.conversationUuid }

        return conversations.map { conversation ->

            val conversationMessages = messagesByConversation[conversation.uuid]?.map { msg ->
                MessageDto().apply {
                    this.message = msg.message
                    this.senderUuid = msg.senderUuid
                    this.receiverUuid = msg.receiverUuid
                    this.sender = users[msg.senderUuid]
                    this.read = msg.read
                    this.createdAt = msg.createdAt
                }
            } ?: emptyList()

            ConversationDto().apply {
                this.uuid = conversation.uuid
                this.userOneUuid = conversation.userOneUuid
                this.userTwoUuid = conversation.userTwoUuid
                this.messages = PageImpl(conversationMessages, pageable2, conversationMessages.size.toLong())

                val userO = users[conversation.userOneUuid]
                val userT = users[conversation.userTwoUuid]
                this.userOne = UserMinDto().apply {
                    this.uuid = userO?.uuid
                    this.image = userO?.image
                    this.name = userO?.name
                    this.lastname = userO?.lastname
                    this.username = userO?.username
                }
                this.userTwo = UserMinDto().apply {
                    this.uuid = userT?.uuid
                    this.image = userT?.image
                    this.name = userT?.name
                    this.lastname = userT?.lastname
                    this.username = userT?.username
                }
            }
        }
    }

    override fun getUserConversation(user: UUID, to: String, page: Int, size: Int): ConversationDto {
        val userF = userService.getByUsername(to)
        val conversation = repository.findFirstByUserOneUuidAndUserTwoUuidOrUserOneUuidAndUserTwoUuid(user, userF.uuid!!, userF.uuid!!, user)
        return if (conversation.isPresent) {
            val conver = conversation.get()
            val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
            val messages = messageRepository.findAllByConversationUuid(conver.uuid!!, pageable).map(messageMapper::toDto)
            return mapper.toDto(conver).apply {
                this.messages = messages
            }
        } else {
            ConversationDto().apply {
                this.userOneUuid = user
                this.userTwoUuid = userF.uuid
                this.messages = Page.empty()
            }
        }
    }

    override fun sendMessage(user: UUID, to: UUID, message: String): Boolean {
        val conversation = repository.findFirstByUserOneUuidAndUserTwoUuidOrUserOneUuidAndUserTwoUuid(user, to, to, user)
        val newMessage = if (conversation.isPresent) {
            this.messageService.save(
                MessageRequest().apply {
                    this.message = message
                    this.conversationUuid = conversation.get().uuid
                    this.read = false
                    this.senderUuid = user
                    this.receiverUuid = to
                }
            )
        } else {
            val conversationCreated = save(
                ConversationRequest().apply {
                    this.userOneUuid = user
                    this.userTwoUuid = to
                }
            )
            this.messageService.save(
                MessageRequest().apply {
                    this.message = message
                    this.conversationUuid = conversationCreated.uuid
                    this.read = false
                    this.senderUuid = user
                    this.receiverUuid = to
                }
            )
        }
        return true
    }

    @Transactional
    override fun readMessages(user: UUID, to: UUID): Boolean {
        val conversation = repository.findFirstByUserOneUuidAndUserTwoUuidOrUserOneUuidAndUserTwoUuid(user, to, to, user)
        if (conversation.isPresent) {
            val messages = messageRepository.getAllByConversationUuidAndReceiverUuid(conversation.get().uuid!!, user)
            messages.forEach { msg ->
                msg.read = true
            }
            messageRepository.saveAll(messages)
        }
        return true
    }
}
