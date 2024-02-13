package in.ac.iisc.wallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Wallet {
    @Id
    private Integer user_id;
    private Integer balance;

    public Wallet() {
    }

    public Wallet(Integer user_id, Integer balance) {
        this.user_id = user_id;
        this.balance = balance;
    }

    public Integer getUser_id() {
        return this.user_id;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
