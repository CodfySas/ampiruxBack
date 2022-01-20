package com.osia.logistic.need.dto.user.v1

import javax.validation.constraints.NotNull

open class UserRequest {
    @NotNull
    var name: String = ""
    @NotNull
    var email: String = ""
    @NotNull
    var password: String = ""
}
