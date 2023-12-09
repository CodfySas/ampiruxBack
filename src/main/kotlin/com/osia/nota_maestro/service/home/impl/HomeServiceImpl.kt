package com.osia.nota_maestro.service.home.impl

import com.osia.nota_maestro.dto.home.v1.ChartDto
import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.home.HomeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service("home.crud_service")
@Transactional
class HomeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val userRepository: UserRepository,
) : HomeService {

    override fun getByAdmin(school: UUID): HomeAdminDto {
        val users = userRepository.findAll()
        val chartDto = mutableListOf<ChartDto>()
        val grades = gradeRepository.findAllById(users.mapNotNull { it.actualGrade }.distinct())

        grades.forEach {
            chartDto.add(
                (
                    ChartDto().apply {
                        this.name = it.name
                        this.value = users.filter { u -> u.actualGrade == it.uuid }.size
                    }
                    )
            )
        }
        return HomeAdminDto().apply {
            this.studentsByGrade = chartDto
        }
    }
}
