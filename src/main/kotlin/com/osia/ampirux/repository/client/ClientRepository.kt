package com.osia.ampirux.repository.client
import com.osia.ampirux.model.Client
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("client.crud_repository")
interface ClientRepository : CommonRepository<Client> {
    @Query(
        """
    SELECT 
        CAST(c.uuid AS TEXT) AS client_uuid,
        MAX(s.created_at) AS last_visit,
        COUNT(s.uuid) AS visit_count,
        COALESCE(SUM(s.total), 0) AS total_pay,
        (
            SELECT s2.total
            FROM sale s2
            WHERE s2.client_uuid = c.uuid
            AND s2.status = 'PAID'
            ORDER BY s2.date DESC
            LIMIT 1
        ) AS last_pay
    FROM clients c
    LEFT JOIN sale s ON s.client_uuid = c.uuid
    WHERE c.uuid IN (:clientUuids) AND s.status = 'PAID'
    GROUP BY c.uuid
    """,
        nativeQuery = true
    )
    fun findClientVisitStats(@Param("clientUuids") clientUuids: List<UUID>): List<Array<Any>>

}