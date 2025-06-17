package com.osia.template.model.abstracts

import com.osia.template.repository.BaseRepository

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel) {
        var counter = repository.count()
        baseModel.code = baseModel.getCode(++counter)
    }
}
