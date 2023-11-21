package com.osia.nota_maestro.dto.user.v1

class SavedMultipleUserDto  {
    var savedUsers: List<UserDto>? = null
    var notSavedUsers: List<NotSavedUserDto>? = emptyList()
}
