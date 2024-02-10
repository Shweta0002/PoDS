package in.ac.iisc.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ac.iisc.wallet.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findByUserId(Integer userId);

    void deleteById(Integer userID);

    void deleteAll();
}
