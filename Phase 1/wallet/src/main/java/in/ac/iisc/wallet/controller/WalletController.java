package in.ac.iisc.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import in.ac.iisc.wallet.VO.Users;
import in.ac.iisc.wallet.model.Wallet;
import in.ac.iisc.wallet.model.WalletTransaction;
import in.ac.iisc.wallet.repository.WalletRepository;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserWallet(@PathVariable Integer user_id) {
        try {
            // Ensure user ID is not null
            if (user_id == null) {
                return ResponseEntity.badRequest().body("Please provide the User ID !!");
            }
            Wallet w = walletRepository.findByUserId(user_id);

            // When no users exists with given user ID
            if (w != null) {
                return new ResponseEntity<>(w, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during wallet search:" + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<?> updateBalance(@PathVariable Integer user_id, @RequestBody WalletTransaction transaction) {
        try {
            try {
                restTemplate.getForEntity("http://host.docker.internal:8080/users/" + user_id,
                        Users.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("User doesnot exist !!");
            }

            Wallet w = walletRepository.findByUserId(user_id);

            // If user wallet doesnot exists, initialize balance to 0
            Integer balance = (w == null) ? 0 : w.getBalance();
            String action = transaction.getAction();
            Integer amount = transaction.getAmount();

            if (action.equals("debit")) {
                // If debit amount is greater than wallet balance dont perform any transaction
                if (balance < amount) {
                    walletRepository.save(new Wallet(user_id, balance));
                    return ResponseEntity.badRequest().body("Insufficient Balance !!");
                } else {
                    // Debiting wallet balance
                    balance -= amount;
                }
            } else if (action.equals("credit")) {
                // Crediting wallet balance
                balance += amount;
            }

            Wallet updatedWallet = walletRepository.save(new Wallet(user_id, balance));
            return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during wallet update:" + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteWallet(@PathVariable Integer user_id) {
        try {
            // Ensure an user with user ID exists
            Wallet w = walletRepository.findByUserId(user_id);

            // If user with user ID doesnot exist
            if (w == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            walletRepository.deleteByUserId(user_id);
            return ResponseEntity.ok("Wallet successfully deleted !!");
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during wallet delete: " + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllWallets() {
        try {
            // Deleting all wallets
            walletRepository.deleteAll();

            return ResponseEntity.ok("All wallets successfully deleted !!");
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during all wallets delere: " + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}