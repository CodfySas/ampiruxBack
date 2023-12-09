package com.osia.nota_maestro.repository.user

import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository("user.crud_repository")
interface UserRepository :
    JpaRepository<User, UUID>,
    JpaSpecificationExecutor<User>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    override fun count(increment: Int): Long

    fun getFirstByUsernameAndPassword(username: String, password: String): Optional<User>

    fun getFirstByUsernameOrDni(username: String, dni: String): Optional<User>

    @Query(value = "SELECT * FROM users where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<User>

    fun getAllByUuidIn(uuids: List<UUID>): List<User>

    @Query(
        value = "select * from users u where role = 'student' and deleted != true " +
            "AND uuid_school = ?1 AND uuid NOT IN (\n" +
            "    SELECT uuid_student\n" +
            "    FROM classroom_students\n" +
            "    WHERE uuid_student IS NOT null and deleted is not true and uuid_classroom != '00000000-0000-0000-0000-000000000000'\n" +
            "  );",
        nativeQuery = true
    )
    fun getStudentsWithoutClassroom(school: UUID): List<User>

    fun findAllByUuidSchool(school: UUID): List<User>
}
