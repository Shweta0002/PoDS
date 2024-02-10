package in.ac.iisc.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ac.iisc.users.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUserId(Integer userId);

    List<Users> findByEmail(String email);

    void deleteById(Integer userID);

    void deleteAll();
}
