package com.osia.osia_erp.model.abstracts

import com.osia.osia_erp.repository.BaseRepository

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel) {
        var counter = repository.count(0)
        baseModel.code = baseModel.getCode(++counter)
    }
}
