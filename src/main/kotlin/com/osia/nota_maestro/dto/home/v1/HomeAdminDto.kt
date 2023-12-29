package com.osia.nota_maestro.dto.home.v1

import com.osia.nota_maestro.dto.BaseDto

class HomeAdminDto : BaseDto() {
    var studentsByGrade: List<ChartDto> = mutableListOf()
    var performanceByCourses: List<ChartSeriesDto> = mutableListOf(ChartSeriesDto())
    var performanceBasics: List<ChartDto> = mutableListOf(ChartDto())
    var polarStudent: List<ChartDto> = mutableListOf(ChartDto())
}
