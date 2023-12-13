package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto

class ClassroomSubjectCompleteDto : BaseDto() {
    // grade name
    var name: String = ""
    var classrooms: List<ClassroomSubjectClassDto> = mutableListOf()
}
