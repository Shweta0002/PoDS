package in.ac.iisc.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.ac.iisc.users.model.Users;
import jakarta.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query(value = "SELECT * FROM Users u WHERE u.id = :id", nativeQuery = true)
    Users findByUserId(Integer id);

    List<Users> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Users u WHERE u.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Integer id);
}
