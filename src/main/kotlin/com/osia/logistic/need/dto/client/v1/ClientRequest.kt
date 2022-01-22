package com.osia.logistic.need.dto.client.v1

import javax.validation.constraints.NotNull

open class ClientRequest {
    @NotNull
    var name: String = ""
    @NotNull
    var email: String = ""
    @NotNull
    var password: String = ""
}
