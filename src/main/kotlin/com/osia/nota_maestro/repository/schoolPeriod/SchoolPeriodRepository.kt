package com.osia.nota_maestro.repository.schoolPeriod

import com.osia.nota_maestro.model.SchoolPeriod
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("schoolPeriod.crud_repository")
interface SchoolPeriodRepository :
    JpaRepository<SchoolPeriod, UUID>,
    JpaSpecificationExecutor<SchoolPeriod>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM school_periods", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM school_periods where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<SchoolPeriod>

    fun findAllByUuidSchool(uuid: UUID): List<SchoolPeriod>

    fun findAllByUuidSchoolAndActualYear(uuid: UUID, year: Int): List<SchoolPeriod>

    @Modifying
    @Transactional
    @Query("UPDATE SchoolPeriod SET deleted = true WHERE uuidSchool = :uuid AND actualYear = :year1 ")
    fun deleteByUuidSchoolAndActualYear(uuid: UUID, year1: Int)
}
