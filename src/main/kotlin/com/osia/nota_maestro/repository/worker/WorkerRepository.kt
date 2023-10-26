package com.osia.nota_maestro.repository.worker

import com.osia.nota_maestro.model.Worker
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("worker.crud_repository")
interface WorkerRepository :
    JpaRepository<Worker, UUID>,
    JpaSpecificationExecutor<Worker>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM workers", nativeQuery = true)
    override fun count(increment: Int): Long
}
