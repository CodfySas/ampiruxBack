package com.osia.nota_maestro.dto.home.v1

import com.osia.nota_maestro.dto.BaseDto
import com.osia.nota_maestro.dto.note.v1.NoteSubjectsDto

class HomeAdminDto : BaseDto() {
    var studentsByGrade: List<ChartDto> = mutableListOf()
    var performanceByCourses: List<ChartSeriesDto> = mutableListOf(ChartSeriesDto())
    var performanceBasics: List<ChartDto> = mutableListOf(ChartDto())
    var polarStudent: List<ChartDto> = mutableListOf(ChartDto())
    var myNotes: List<NoteSubjectsDto> = mutableListOf(NoteSubjectsDto())
    var promPerGrade: List<ChartDto> = mutableListOf(ChartDto())
    var horizontalTeachers: List<ChartDto> = mutableListOf(ChartDto())
}
