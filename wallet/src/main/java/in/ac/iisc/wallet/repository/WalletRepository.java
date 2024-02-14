package in.ac.iisc.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import in.ac.iisc.wallet.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query(value = "SELECT * FROM Wallet w WHERE w.user_id = :user_id", nativeQuery = true)
    Wallet findByUserId(Integer user_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Wallet w WHERE w.user_id = :user_id", nativeQuery = true)
    void deleteById(@Param("user_id") Integer user_id);
}
