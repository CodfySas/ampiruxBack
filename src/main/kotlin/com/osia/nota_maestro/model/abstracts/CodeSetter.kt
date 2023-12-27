package com.osia.nota_maestro.model.abstracts

import com.osia.nota_maestro.repository.BaseRepository

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel) {
        var counter = repository.count(0, baseModel.uuid!!)
        baseModel.code = baseModel.getCode(++counter)
    }
}
