package com.osia.ampirux.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import java.util.Optional
import java.util.UUID

@NoRepositoryBean
interface CommonRepository <T> :
    JpaRepository<T, UUID>,
    JpaSpecificationExecutor<T>,
    BaseRepository {

    @Query("SELECT COUNT(*) FROM #{#entityName}", nativeQuery = true)
    override fun count(): Long

    @Query("SELECT * FROM #{#entityName} WHERE uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<T>
}
