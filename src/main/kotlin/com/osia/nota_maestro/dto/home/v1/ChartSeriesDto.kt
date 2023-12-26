package com.osia.nota_maestro.dto.home.v1

import com.osia.nota_maestro.dto.BaseDto

class ChartSeriesDto : BaseDto() {
    var name: String = ""
    var series: List<ChartDto> = mutableListOf(ChartDto())
}
