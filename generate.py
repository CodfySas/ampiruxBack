import os

TEMPLATES = {
 "controller": '''package com.osia.ampirux.controller.{{name}}.v1
import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Dto
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Mapper
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Request
import com.osia.ampirux.service.{{name}}.{{Name}}Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("{{name}}.v1.crud")
@CrossOrigin
@RequestMapping("v1/{{name}}s")
@Validated
class {{Name}}Controller(
private val service:{{Name}}Service,
private val mapper:{{Name}}Mapper
)  {
    @GetMapping
    fun findAll(pageable: Pageable): Page< {{Name}}Dto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String,
        @RequestHeader("barbershop_uuid") barbershopUuid: String): Page< {{Name}}Dto> {
        return service.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity< {{Name}}Dto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List< {{Name}}Dto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody request: {{Name}}Request): ResponseEntity<{{Name}}Dto > {
        request.barbershopUuid = barbershopUuid;
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody requestList: List< {{Name}}Request>): ResponseEntity<List<{{Name}}Dto > > {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: {{Name}}Request): ResponseEntity<{{Name}}Dto > {
        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List< {{Name}}Dto>): ResponseEntity<List<{{Name}}Dto > > {
        return ResponseEntity.ok(service.updateMultiple(dtoList))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        service.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(@RequestBody uuidList: List<UUID>): ResponseEntity<HttpStatus> {
        service.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}

''',
 "dto": '''package com.osia.ampirux.dto.{{name}}.v1

import com.osia.ampirux.dto.BaseDto
import java.util.UUID

class {{Name}}Dto : BaseDto() {
   var name: String? = null
}

''',

 "mapper": '''package com.osia.ampirux.dto.{{name}}.v1

import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.model.{{Name}}
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
)
interface {{Name}}Mapper : BaseMapper<{{Name}}Request, {{Name}}, {{Name}}Dto> {
    @Mappings
    override fun toModel(r: {{Name}}Request): {{Name}}

    @Mappings
    override fun toDto(m: {{Name}}): {{Name}}Dto

    @Mappings
    override fun toRequest(d: {{Name}}Dto): {{Name}}Request

    override fun update(r: {{Name}}Request, @MappingTarget m: {{Name}})
}

''',

 "request": '''package com.osia.ampirux.dto.{{name}}.v1

import java.util.UUID

class {{Name}}Request {
    var name: String? = null
}


''',

 "listener": '''package com.osia.ampirux.model.listener.{{name}}

import com.osia.ampirux.model.{{Name}}
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.{{name}}.{{Name}}Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class {{Name}}Listener : CodeSetter() {

    companion object {
        private lateinit var {{name}}Repository: {{Name}}Repository
    }

    @Autowired
    fun setProducer(_{{name}}Repository: {{Name}}Repository) {
        {{name}}Repository = _{{name}}Repository
    }

    @PrePersist
    fun prePersist({{name}}: {{Name}}) {
        this.setCode({{name}}Repository, {{name}})
    }
}

''',

 "model": '''package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.{{name}}.{{Name}}Listener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "{{name}}s",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        {{Name}}Listener::class
    ]
)
@Where(clause = "deleted = false")
data class {{Name}}(
    var name: String? = null,
) : BaseModel()

''',

 "service": '''package com.osia.ampirux.service.{{name}}

import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Dto
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Request
import com.osia.ampirux.model.{{Name}}
import com.osia.ampirux.service.common.CommonService

interface {{Name}}Service : CommonService<{{Name}}, {{Name}}Dto, {{Name}}Request>
''',

 "impl": '''package com.osia.ampirux.service.{{name}}.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Dto
import com.osia.ampirux.dto.{{name}}.v1.{{Name}}Request
import com.osia.ampirux.model.{{Name}}
import com.osia.ampirux.repository.{{name}}.{{Name}}Repository
import com.osia.ampirux.service.{{name}}.{{Name}}Service
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
class {{Name}}ServiceImpl(
    private val repository: {{Name}}Repository,
    private val mapper: BaseMapper<{{Name}}Request, {{Name}}, {{Name}}Dto>,
    private val objectMapper: ObjectMapper
) : {{Name}}Service {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): {{Name}} {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<{{Name}}Dto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<{{Name}}Dto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<{{Name}}>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<{{Name}}Dto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<{{Name}}>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: {{Name}}Request, replace: Boolean): {{Name}}Dto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun saveMultiple(requestList: List<{{Name}}Request>): List<{{Name}}Dto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: {{Name}}Request, includeDelete: Boolean): {{Name}}Dto {
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
    override fun updateMultiple(dtoList: List<{{Name}}Dto>): List<{{Name}}Dto> {
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
}

''',

 "repository": '''package com.osia.ampirux.repository.{{name}}
import com.osia.ampirux.model.{{Name}}
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("{{name}}.crud_repository")
interface {{Name}}Repository : CommonRepository<{{Name}}>

'''
}

def render(template: str, context: dict):
    for key, value in context.items():
        template = template.replace(f"{{{{{key}}}}}", value)
    return template

def create_files(name: str, base_path: str = "./src/main/kotlin/com/osia/ampirux"):
    Name = name[0].upper() + name[1:]
    name_lower = name.lower()
    context = {"Name": Name, "name": name_lower}

    files = {
      f"controller/{name_lower}/v1/{Name}Controller.kt": TEMPLATES["controller"],
      f"dto/{name_lower}/v1/{Name}Dto.kt": TEMPLATES["dto"],
      f"dto/{name_lower}/v1/{Name}Mapper.kt": TEMPLATES["mapper"],
      f"dto/{name_lower}/v1/{Name}Request.kt": TEMPLATES["request"],
      f"model/listener/{name_lower}/{Name}Listener.kt": TEMPLATES["listener"],
      f"model/{Name}.kt": TEMPLATES["model"],
      f"service/{name_lower}/{Name}Service.kt": TEMPLATES["service"],
      f"service/{name_lower}/impl/{Name}ServiceImpl.kt": TEMPLATES["impl"],
      f"repository/{name_lower}/{Name}Repository.kt": TEMPLATES["repository"],
     }

    for rel_path, template in files.items():
        full_path = os.path.join(base_path, rel_path)
        os.makedirs(os.path.dirname(full_path), exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(render(template, context))
        print(f"✅ Created: {full_path}")

if __name__ == "__main__":
    import sys
    if len(sys.argv) < 2:
        print("❌ Usage: python generate.py <EntityName>")
    else:
        create_files(sys.argv[1])