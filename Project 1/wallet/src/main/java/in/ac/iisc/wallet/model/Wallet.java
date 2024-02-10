package in.ac.iisc.wallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Wallet {
    @Id
    private Integer userId;
    private Integer balance;

    public Wallet() {
    }

    public Wallet(Integer userId, Integer balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getBalance() {
        return this.balance;
    }
}
