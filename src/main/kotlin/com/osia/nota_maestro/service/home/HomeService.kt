package com.osia.nota_maestro.service.home

import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import java.util.UUID

interface HomeService {
    fun getByAdmin(school: UUID): HomeAdminDto
    fun getByTeacher(teacher: UUID): HomeAdminDto
    fun getByStudent(student: UUID): HomeAdminDto
}
