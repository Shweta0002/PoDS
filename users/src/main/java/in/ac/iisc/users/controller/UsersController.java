package in.ac.iisc.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import in.ac.iisc.users.model.Users;
import in.ac.iisc.users.repository.UsersRepository;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable Integer user_id) {
        try {
            if (user_id == null) {
                return ResponseEntity.badRequest().body("Please provide the User ID !!");
            }
            Users u = usersRepository.findByUserId(user_id);

            if (u != null) {
                return ResponseEntity.ok(u);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Error occurred during user search:" + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        try {
            List<Users> existingUsers = usersRepository.findByEmail(user.getEmail());
            if (existingUsers == null || existingUsers.size() != 0) {
                return ResponseEntity.badRequest()
                        .body("Email ID already exists. Please provide another email ID !!");
            }

            Users newUser = usersRepository.save(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error occurred during user add:" + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer user_id) {
        try {
            Users u = usersRepository.findByUserId(user_id);
            if (u == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            try {
                restTemplate.delete("http://localhost:8081/bookings/users/{user_id}");
            } catch (Exception e) {
                System.out.println("Booking of this user didnot exist !!: " + e);
            }
            try {
                restTemplate.delete("http://localhost:8082/wallets/{user_id}");
            } catch (Exception e) {
                System.out.println("Wallet of this user did not exist !!: " + e);
            }

            usersRepository.deleteById(user_id);
            return ResponseEntity.ok("User successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during user delete: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllUsers() {
        try {
            try {
                restTemplate.delete("http://localhost:8081/bookings");
            } catch (Exception e) {
                System.out.println("No bookings exist !!: " + e);
            }
            restTemplate.delete("http://localhost:8082/wallets");

            usersRepository.deleteAll();
            return ResponseEntity.ok("All users successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during all wallets delete: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}