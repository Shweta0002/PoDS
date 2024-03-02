package in.ac.iisc.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;

import in.ac.iisc.users.model.Users;
import in.ac.iisc.users.repository.UsersRepository;

@Transactional
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
            // Ensure user ID is not null
            if (user_id == null) {
                return ResponseEntity.badRequest().body("Please provide the User ID !!");
            }
            Users u = usersRepository.findByUserId(user_id);

            // When no users exists with given user ID
            if (u != null) {
                return ResponseEntity.ok(u);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during user search:" + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        try {
            // Getting existing users having same email id
            List<Users> existingUsers = usersRepository.findByEmail(user.getEmail());

            // Ensure no existing user with the same email id
            if (existingUsers == null || existingUsers.size() != 0) {
                return ResponseEntity.badRequest()
                        .body("Email ID already exists. Please provide another email ID !!");
            }

            // Saving user if email id in request payload doesnot already exist
            Users newUser = usersRepository.save(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during user add:" + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer user_id) {
        try {
            // Ensure an user with user ID exists
            Users u = usersRepository.findByUserId(user_id);

            // If user with user ID doesnot exist
            if (u == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Deleting all bookings of user using users microservice
            try {
                restTemplate.delete("http://booking-microservice:8081/bookings/users/" + user_id);
            } catch (Exception e) {
                System.out.println("Booking of this user didnot exist !!: " + e);
            }

            // Deleting user wallet from wallets service
            try {
                restTemplate.delete("http://wallet-microservice:8082/wallets/" + user_id);
            } catch (Exception e) {
                System.out.println("Wallet of this user did not exist !!: " + e);
            }

            usersRepository.deleteById(user_id);
            return ResponseEntity.ok("User successfully deleted !!");
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during user delete: " + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllUsers() {
        try {
            // Deleting all bookings of all users
            try {
                restTemplate.delete("http://booking-microservice:8081/bookings");
            } catch (Exception e) {
                System.out.println("No bookings exist !!: " + e);
            }

            // Deleting all user wallets
            restTemplate.delete("http://wallet-microservice:8082/wallets");

            usersRepository.deleteAll();
            return ResponseEntity.ok("All users successfully deleted !!");
        } catch (Exception e) {
            // Log the exception for debugging
            System.out.println("Error occurred during all wallets delete: " + e);

            // Return a generic error response to the user
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}