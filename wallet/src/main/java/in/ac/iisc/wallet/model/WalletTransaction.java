package in.ac.iisc.wallet.model;

// Java class for Request payload
public class WalletTransaction {
    String action;
    Integer amount;

    public String getAction() {
        return this.action;
    }

    public Integer getAmount() {
        return this.amount;
    }
}
