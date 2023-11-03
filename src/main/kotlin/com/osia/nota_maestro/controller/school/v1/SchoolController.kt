package com.osia.nota_maestro.controller.school.v1

import com.osia.nota_maestro.dto.school.v1.SchoolMapper
import com.osia.nota_maestro.service.school.SchoolService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("school.v1.crud")
@RequestMapping("v1/schools")
@Validated
class SchoolController(
    private val schoolService: SchoolService,
    private val schoolMapper: SchoolMapper
)
