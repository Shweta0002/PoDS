package in.ac.iisc.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import in.ac.iisc.wallet.model.Wallet;
import in.ac.iisc.wallet.model.WalletTransaction;
import in.ac.iisc.wallet.repository.WalletRepository;

@RestController
@RequestMapping("/api")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/wallets/{userId}")
    public ResponseEntity<?> getUserWallet(@PathVariable Integer userId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().body("Please provide the User ID !!");
            }
            Wallet w = walletRepository.findByUserId(userId);

            if (w != null) {
                return ResponseEntity.ok(w.getBalance());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Error occurred during wallet search:" + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @PutMapping("/wallets/{userId}")
    public ResponseEntity<?> updateBalance(@PathVariable Integer userId, @RequestBody WalletTransaction transaction) {
        try {
            Wallet w = walletRepository.findByUserId(userId);
            Integer balance = (w == null) ? 0 : w.getBalance();
            String action = transaction.getAction();
            Integer amount = transaction.getAmount();

            if (action.equals("debit")) {
                if (balance < amount) {
                    walletRepository.save(new Wallet(userId, balance));
                    return ResponseEntity.badRequest().body("Insufficient Balance !!");
                } else {
                    balance -= amount;
                }
            } else if (action.equals("credit")) {
                balance += amount;
            }

            Wallet updatedWallet = walletRepository.save(new Wallet(userId, balance));
            return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error occurred during wallet update:" + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/wallets/{userId}")
    public ResponseEntity<?> deleteWallet(@PathVariable Integer userId) {
        try {
            Wallet w = walletRepository.findByUserId(userId);
            if (w == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            walletRepository.deleteById(userId);
            return ResponseEntity.ok("Wallet successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during wallet delete: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/wallets")
    public ResponseEntity<?> deleteAllWallets() {
        try {
            walletRepository.deleteAll();
            return ResponseEntity.ok("All wallets successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during all wallets delere: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}