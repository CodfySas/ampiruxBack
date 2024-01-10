package com.osia.nota_maestro.model.abstracts

import com.osia.nota_maestro.repository.BaseRepository
import java.util.UUID

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel, uuidSchool: UUID? = null) {
        var counter = repository.count(uuidSchool)
        baseModel.code = baseModel.getCode(++counter)
    }
}
