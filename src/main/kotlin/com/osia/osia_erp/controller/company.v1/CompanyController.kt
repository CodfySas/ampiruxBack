package com.osia.osia_erp.controller.company.v1

import com.osia.osia_erp.dto.company.v1.CompanyMapper
import com.osia.osia_erp.service.company.CompanyService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("company.v1.crud")
@RequestMapping("v1/companies")
@Validated
class CompanyController(
    private val companyService: CompanyService,
    private val companyMapper: CompanyMapper
)
