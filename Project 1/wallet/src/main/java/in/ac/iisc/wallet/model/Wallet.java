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

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}
}
