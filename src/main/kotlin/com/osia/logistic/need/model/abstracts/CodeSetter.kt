package com.osia.logistic.need.model.abstracts

import com.osia.logistic.need.repository.baseRepository.BaseRepository

abstract class CodeSetter {
    fun setCode(repository: BaseRepository, baseModel: BaseModel) {
        var counter = repository.countAll()
        baseModel.code = baseModel.getCode(++counter)
    }
}
