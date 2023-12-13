package com.osia.nota_maestro.dto.classroomSubject.v1

import com.osia.nota_maestro.dto.BaseDto

class ClassroomSubjectClassDto : BaseDto() {
    // classroom name
    var name: String = ""
    var subjects: List<ClassroomSubjectByTeacherDto> = mutableListOf()
}
