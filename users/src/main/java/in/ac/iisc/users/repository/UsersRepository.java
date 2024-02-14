package in.ac.iisc.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.ac.iisc.users.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query(value = "SELECT * FROM Users u WHERE u.id = :id", nativeQuery = true)
    Users findByUserId(Integer id);

    List<Users> findByEmail(String email);

    void deleteById(Integer id);

    void deleteAll();
}
