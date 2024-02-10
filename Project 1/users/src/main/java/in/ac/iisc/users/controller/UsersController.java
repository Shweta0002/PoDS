package in.ac.iisc.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.ac.iisc.users.model.Users;
import in.ac.iisc.users.repository.UsersRepository;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().body("Please provide the User ID !!");
            }
            Users u = usersRepository.findByUserId(userId);

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

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        try {
            List<Users> existingUsers = usersRepository.findByEmail(user.getEmail());

            if (existingUsers.size() != 0) {
                return ResponseEntity.badRequest()
                        .body("Email ID already exists. Please provide another email ID !!");
            }

            Users newUser = usersRepository.save(user);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error occurred during user add:" + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        try {
            // TODO: is this even required
            Users u = usersRepository.findByUserId(userId);
            if (u == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            usersRepository.deleteById(userId);
            return ResponseEntity.ok("User successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during user delete: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteAllUsers() {
        try {
            usersRepository.deleteAll();
            return ResponseEntity.ok("All users successfully deleted !!");
        } catch (Exception e) {
            System.out.println("Error occurred during all wallets delete: " + e);
            return ResponseEntity.internalServerError().body("An error occurred while processing the request");
        }
    }
}