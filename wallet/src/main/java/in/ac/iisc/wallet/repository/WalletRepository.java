package in.ac.iisc.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.ac.iisc.wallet.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query(value = "SELECT * FROM Wallet w WHERE w.user_id = :user_id", nativeQuery = true)
    Wallet findByUserId(Integer user_id);

    void deleteById(Integer user_id);

    void deleteAll();
}
