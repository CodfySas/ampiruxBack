package com.osia.ampirux.model.abstracts

import com.osia.ampirux.repository.BaseRepository

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel) {
        var counter = repository.count()
        baseModel.code = baseModel.getCode(++counter)
    }
}
